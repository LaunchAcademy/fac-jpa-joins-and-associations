# JPA Joins and Associations

---

## We're Going to Model a Meetup Website

- `Event` Entity
- `Category` Entity

---

## Create Our Database

```bash
createdb java_meetups_development
```

---

## Let's take a look at our Flyway Migrations...

<small>`src/main/resources/db/migration/V1__create_events.sql`</small>

```sql
CREATE TABLE events (
  id SERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL
);
```

---

## Our Second Flyway Migration

<small>`src/main/resources/db/migration/V2__create_categories.sql`</small>

```sql
CREATE TABLE categories (
  id SERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL
);

CREATE UNIQUE INDEX categories_name_index ON categories(name);
```

---

## Run Our Flyway Migrations

---

## Write Our Entities - Event

```java
@Entity
@Table(name="events")
public class Event {
  @Id
  @SequenceGenerator(name="event_generator", sequenceName="events_id_seq", allocationSize = 1)
  @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="event_generator")
  @Column(name="id", nullable=false, unique=true)
  private Long id;

  @Column(name="name", nullable=false)
  private String name;

  //...
}
```

---

## Write Our Entities - Category

```java
@Entity
@Table(name="categories")
public class Category {
  @Id
  @SequenceGenerator(name="category_generator", sequenceName="categories_id_seq", allocationSize = 1)
  @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="category_generator")
  @Column(name="id", nullable=false, unique=true)
  private Long id;

  @Column(name="name", nullable=false)
  private String name;

  //...
}
```

---

## Seed Our Categories

```java
public class EventSeeder {
  private static String[] CATEGORY_NAMES = {
    "Tech",
    "Networking",
    "Social"
  };

  public static void main(String[] args) {
    EntityManagerFactory emf =
      Persistence.
        createEntityManagerFactory("com.launchacademy.meetups");

    EntityManager em = emf.createEntityManager();

    em.getTransaction().begin();
    //seeder code here
    em.getTransaction().commit();
  }
}
```

---

## Loop Through Our List

```java
em.getTransaction().begin();
//seeder code here
for(String categoryName : CATEGORY_NAMES) {
  Category category = new Category();
  category.setName(categoryName);
  em.persist(category);
}
em.getTransaction().commit();
```

---

## Run the seeder multiple times

```java
for(String categoryName : CATEGORY_NAMES) {
  String queryString = "SELECT c FROM Category c WHERE name = :name";
  Query query = em.createQuery(queryString);
  query.setParameter("name", categoryName);
  query.setMaxResults(1);
  List<Category> categories = query.getResultList();

  if(events.size() == 0) {
    Category category = new Category();
    category.setName(categoryName);
    em.persist(category);
  }
}
```

---

## Seeding Should Be Re-Runnable

- We want to run our seed scripts multiple times
- They should be "fault tolerant"
- We don't want duplicate data

---

## Ok, so how do we associate events with categories?

First let's create a migration:

<small>`src/main/resources/db/migration/V3__add_category_id.sql`</small>

```sql
ALTER TABLE events
  ADD category_id BIGINT;

CREATE INDEX event_category_id_index ON events(category_id);
```

---

## Remember? We're adding a foreign key

- The foreign key points to the primary key in the `categories` table
- This is how we normalize data in relational data models

---

## How do we connect these two entities with the JPA?

- We use **associations**
- Associations can be configured using annotations

---

## Defining Our Association

<small>`jpa-joins-and-associations/src/main/java/Event.java`</small>

```java
@ManyToOne
@JoinColumn(name="category_id", nullable=false)
private Category category;

public Category getCategory() {
  return category;
}

public void setCategory(Category category) {
  this.category = category;
}
```

---

### Use the Association in Our Seeder - Retrieve Category

```java
//retrieve the networking category
String networkingQ = "SELECT c FROM Category c WHERE name = :name";
Query networkingQuery = em.createQuery(networkingQ);
networkingQuery.setParameter("name", "Networking");
networkingQuery.setMaxResults(1);
Category networkingCategory = (Category)networkingQuery.getResultList().get(0);
```

---

### Associate a Category in Our Seeder - Assign Category

```java
//create a new event
Event event = new Event();
event.setCategory(networkingCategory);
event.setName("Networkapolooza");
em.persist(event);
```

---

## The Magic of the JPA

- If we look at our `events` table, we'll have a record persisted
- The record will have a `category_id` of 2 pointing at the right `Category` record
- Associations are **powerful** - ORM gives us the ability to express relationships in human-friendly ways

---

## Generally, you want bi-directional associations

In our `Category` entity:

```java
@OneToMany(mappedBy = "category")
private List<Event> events;

public List<Event> getEvents() {
  return events;
}

public void setEvents(List<Event> events) {
  this.events = events;
}
```

---

## We can use this in our seeder for fun

```java
em.getTransaction().begin();
em.refresh(networkingCategory);
for(Event networkingEvent : networkingCategory.getEvents()) {
  System.out.println(networkingEvent.getName() + " is in the networking category");
}
em.getTransaction().commit();
```

---

## The `refresh` method

- We use it to retrieve state from the database
- It "resets" the object to what's found in our Postgres database

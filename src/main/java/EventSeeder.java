import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class EventSeeder {
  private static String[] CATEGORY_NAMES = {"Tech", "Networking", "Social"};

  public static void main(String[] args) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("com.launchacademy.meetups");
    EntityManager em = emf.createEntityManager();

    em.getTransaction().begin();
    for(String categoryName : CATEGORY_NAMES) {
      String queryString = "SELECT c FROM Category c WHERE name = :name";
      Query query = em.createQuery(queryString);
      query.setParameter("name", categoryName);
      query.setMaxResults(1);
      List<Category> categories = query.getResultList();

      if(categories.size() == 0) {
        Category category = new Category();
        category.setName(categoryName);
        em.persist(category);
      }
    }
    em.getTransaction().commit();

    em.getTransaction().begin();

    //retrieve the networking category
    String networkingQ = "SELECT c FROM Category c WHERE name = :name";
    Query networkingQuery = em.createQuery(networkingQ);
    networkingQuery.setParameter("name", "Networking");
    networkingQuery.setMaxResults(1);
    Category networkingCategory = (Category)networkingQuery.getResultList().get(0);

    //create a new event
    Event event = new Event();
    event.setCategory(networkingCategory);
    event.setName("Jim's Networking Event");
    em.persist(event);

    em.getTransaction().commit();

    em.getTransaction().begin();
    em.refresh(networkingCategory);
    for(Event networkingEvent : networkingCategory.getEvents()) {
      System.out.println(networkingEvent.getName() + " is in the networking category");
    }
    em.getTransaction().commit();

    em.close();
    emf.close();
  }
}

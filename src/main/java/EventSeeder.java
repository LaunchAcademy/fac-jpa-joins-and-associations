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
/*
create transactions to create categories in the Category table using CATEGORY_NAMES
write code to prevent duplication
*/

/*
create a query to retrieve a category
use that query to create a Category variable to be used when creating an Event
create a transation to create an event.
ensure that transaction sets the category by leveraging associations
*/
    em.getTransaction().begin();

    //retrieve the networking category

    //create a new event
  }
}

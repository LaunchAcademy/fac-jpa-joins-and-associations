import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
/* create an entity for category with
id - primary key
name - cannot be null, must be unique
events should be associated to this table
A category can be used by any number of events
Ensure you create a migration to create your table
*/

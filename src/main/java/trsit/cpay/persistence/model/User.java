/**
 * 
 */
package trsit.cpay.persistence.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author black
 */
@Entity
public class User {
    @Id
    @GeneratedValue
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

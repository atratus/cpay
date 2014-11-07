/**
 * 
 */
package trsit.cpay.persistence.model;

import javax.persistence.Entity;

/**
 * @author black
 */
@Entity
public class User extends Persistent {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

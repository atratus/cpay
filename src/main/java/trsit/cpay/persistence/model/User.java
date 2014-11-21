/**
 * 
 */
package trsit.cpay.persistence.model;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author black
 */
@Entity
@Getter
@Setter
public class User extends Persistent {

    private String name;
}

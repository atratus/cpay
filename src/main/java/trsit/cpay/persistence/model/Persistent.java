/**
 * 
 */
package trsit.cpay.persistence.model;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.Data;

/**
 * @author black
 *
 */
@MappedSuperclass
@Data
public abstract class Persistent {
    @Id
    @GeneratedValue
    private Long id;

}

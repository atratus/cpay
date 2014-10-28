/**
 * 
 */
package trsit.cpay.persistence.dao;

import java.util.List;

import trsit.cpay.persistence.model.User;


/**
 * @author black
 *
 */
public interface UserDAO {
    List<User> getAllUsers();
}

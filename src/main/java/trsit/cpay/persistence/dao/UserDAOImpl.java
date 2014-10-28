/**
 * 
 */
package trsit.cpay.persistence.dao;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import trsit.cpay.persistence.model.User;




/**
 * @author black
 *
 */
@Service
public class UserDAOImpl implements UserDAO {

    /**
     * @see trsit.cpay.persistence.dao.UserDAO#getAllUsers()
     */
    @Override
    public List<User> getAllUsers() {
        User user = new User();
        user.setName("user1");
        return Collections.singletonList(user);
    }

}

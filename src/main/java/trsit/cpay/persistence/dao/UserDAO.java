/**
 * 
 */
package trsit.cpay.persistence.dao;

import javax.inject.Inject;

import org.springframework.stereotype.Repository;

import trsit.cpay.data.ItemsSet;
import trsit.cpay.persistence.model.QUser;
import trsit.cpay.persistence.model.User;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.hibernate.HibernateQuery;


/**
 * @author black
 *
 */
@Repository
public class UserDAO {
    @Inject
    private PersistentSetsFactory persistentSetsFactory;

    /**
     * @see trsit.cpay.persistence.dao.UserDAO#getUsers()
     */
    public ItemsSet<User> getUsers() {
        JPQLQuery query = new HibernateQuery().from(QUser.user);
        return persistentSetsFactory.buildSet(query, QUser.user);
    }

}

/**
 *
 */
package trsit.cpay.persistence.dao;

import javax.inject.Inject;

import org.hibernate.Session;
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
public class UserDAO  extends AbstractDAO {
    @Inject
    private PersistentSetsFactory persistentSetsFactory;

    private static class UsersQueryProvider implements QueryProvider {
        private static final long serialVersionUID = 1L;

        @Override
        public JPQLQuery getQuery(final Session session) {
            return new HibernateQuery(session).from(QUser.user);
        }

    }
    /**
     * @see trsit.cpay.persistence.dao.UserDAO#getUsers()
     */
    public ItemsSet<User> getUsers() {

        return persistentSetsFactory.buildSet(QUser.user, new UsersQueryProvider());
    }

}

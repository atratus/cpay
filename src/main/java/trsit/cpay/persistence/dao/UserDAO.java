/**
 *
 */
package trsit.cpay.persistence.dao;

import org.springframework.stereotype.Repository;

import trsit.cpay.data.ItemsSet;
import trsit.cpay.persistence.model.QUser;
import trsit.cpay.persistence.model.User;

import com.mysema.query.jpa.JPQLQuery;


/**
 * @author black
 *
 */
@Repository
public class UserDAO  extends AbstractDAO {

    private static class UsersQueryProvider extends SimpleCountQueryProvider  {
        private static final long serialVersionUID = 1L;

        @Override
        public JPQLQuery getQuery(final JPQLQuery query) {
            return query.from(QUser.user);
        }
    }

    /**
     * @see trsit.cpay.persistence.dao.UserDAO#getUsers()
     */
    public ItemsSet<User> getUsers() {
        return buildSet(QUser.user, new UsersQueryProvider());
    }

}

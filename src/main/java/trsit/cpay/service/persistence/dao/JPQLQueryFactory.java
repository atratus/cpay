/**
 *
 */
package trsit.cpay.service.persistence.dao;

import javax.inject.Inject;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.hibernate.HibernateQuery;

/**
 * @author black
 *
 */
@Service
public class JPQLQueryFactory {
    @Inject
    private SessionFactory sessionFactory;

    public JPQLQuery createBaseQuery() {
        return new HibernateQuery(sessionFactory.getCurrentSession());
    }
}

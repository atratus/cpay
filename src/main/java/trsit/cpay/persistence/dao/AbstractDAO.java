/**
 * 
 */
package trsit.cpay.persistence.dao;

import javax.inject.Inject;

import org.hibernate.SessionFactory;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.hibernate.HibernateQuery;

/**
 * @author black
 *
 */
public class AbstractDAO {
    
    @Inject
    private SessionFactory sessionFactory;
    
    protected JPQLQuery attachedQuery() {
        return new HibernateQuery(sessionFactory.getCurrentSession());
    }
    
    protected JPQLQuery detachedQuery() {
        return new HibernateQuery();
    }
}

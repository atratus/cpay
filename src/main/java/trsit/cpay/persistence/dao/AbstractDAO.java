/**
 *
 */
package trsit.cpay.persistence.dao;

import javax.inject.Inject;

import org.hibernate.Session;
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

    protected JPQLQuery query() {
        return new HibernateQuery(sessionFactory.getCurrentSession());
    }

    @SuppressWarnings("unchecked")
    protected <T> T attach(T entity) {
        return (T) sessionFactory.getCurrentSession().merge(entity);
    }

    protected void saveOrUpdate(Object entity) {
        sessionFactory.getCurrentSession().saveOrUpdate(entity);
    }

    protected void remove(Class<?> entityClass, Long id) {
        Session session = sessionFactory.getCurrentSession();
        Object entity  = session.load(entityClass, id);
        session.delete(entity);

    }

}

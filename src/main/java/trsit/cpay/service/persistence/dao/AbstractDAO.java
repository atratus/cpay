/**
 *
 */
package trsit.cpay.service.persistence.dao;

import javax.inject.Inject;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import trsit.cpay.data.ItemsSet;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.hibernate.HibernateQuery;
import com.mysema.query.types.Expression;

/**
 * @author black
 *
 */
public class AbstractDAO {

    @Inject
    private PersistentSetsFactory persistentSetsFactory;

    @Inject
    private SessionFactory sessionFactory;

    protected JPQLQuery attachedQuery() {
        return new HibernateQuery(sessionFactory.getCurrentSession());
    }

    @SuppressWarnings("unchecked")
    protected <T> T attach(final T entity) {
        return (T) sessionFactory.getCurrentSession().merge(entity);
    }

    protected void saveOrUpdate(final Object entity) {
        sessionFactory.getCurrentSession().saveOrUpdate(entity);
    }

    protected void remove(final Class<?> entityClass, final Long id) {
        final Session session = sessionFactory.getCurrentSession();
        final Object entity  = session.load(entityClass, id);
        session.delete(entity);

    }


    protected <T> ItemsSet<T> buildSet(final Expression<T> resultExpression, final QueryProvider queryProvider) {
        return persistentSetsFactory.buildSet(resultExpression, queryProvider);
    }

}

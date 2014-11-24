/**
 * 
 */
package trsit.cpay.persistence.dao;

import java.io.Serializable;

import org.hibernate.SessionFactory;
import org.springframework.transaction.support.TransactionTemplate;

import trsit.cpay.data.ItemsSet;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.hibernate.HibernateQuery;
import com.mysema.query.types.Expression;

/**
 * @author black
 *
 */
public class HibernatePersistentItemsSet<T> extends AbstractPersistentItemsSet<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    private final JPQLQuery query;
    private SessionFactory sessionFactory;
    private final long from;
    private final long to;
    private final TransactionTemplate transactionTemplate;
    private Expression<T> resultExpression;

    private HibernatePersistentItemsSet(SessionFactory sessionFactory,
            TransactionTemplate transactionTemplate, long from, long to, JPQLQuery query,
            Expression<T> resultExpression) {
        super(transactionTemplate, from, to, resultExpression);
        this.query = query;
        this.sessionFactory = sessionFactory;
        this.transactionTemplate = transactionTemplate;
        this.from = from;
        this.to = to;
        this.resultExpression = resultExpression;
    }
    public HibernatePersistentItemsSet(SessionFactory sessionFactory,
            TransactionTemplate transactionTemplate, JPQLQuery query,
            Expression<T> resultExpression) {
        this(sessionFactory,
                 transactionTemplate, -0, -1, query,
                 resultExpression);
    }

    @Override
    public ItemsSet<T> subset(long startIncl, long endExcl) {
        long newFrom = this.from + startIncl;
        long newTo = this.to == -1 ? -1 : Math.min(this.to, newFrom + endExcl);
        return new HibernatePersistentItemsSet<T>(sessionFactory, transactionTemplate,
                newFrom, newTo, query, resultExpression);
    }

    protected JPQLQuery getAtachedQuery() {
        return ((HibernateQuery) query).clone(sessionFactory
                .getCurrentSession());
    }

}

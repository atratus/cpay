/**
 * 
 */
package trsit.cpay.persistence.dao;

import java.util.Iterator;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import trsit.cpay.data.ItemsSet;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.hibernate.HibernateQuery;
import com.mysema.query.types.Expression;

/**
 * @author black
 *
 */
public class PersistentItemsSet<T> implements ItemsSet<T> {
    private final long from;
    private final long to;
    private final Expression<T> resultExpression;

    private final TransactionTemplate transactionTemplate;
    private final JPQLQuery query;
    private final SessionFactory sessionFactory;

    public PersistentItemsSet(SessionFactory sessionFactory,
            TransactionTemplate transactionTemplate, JPQLQuery query,
            Expression<T> resultExpression) {
        this(sessionFactory, transactionTemplate, query, 0, -1,
                resultExpression);
    }

    private PersistentItemsSet(SessionFactory sessionFactory,
            TransactionTemplate transactionTemplate, JPQLQuery query,
            long from, long to, Expression<T> resultTuple) {
        this.from = from;
        this.to = to;

        this.sessionFactory = sessionFactory;

        this.query = query;

        this.transactionTemplate = transactionTemplate;
        this.resultExpression = resultTuple;
    }

    @Override
    public Iterator<T> iterator() {
        return transactionTemplate
                .execute(new TransactionCallback<Iterator<T>>() {

                    @Override
                    public Iterator<T> doInTransaction(TransactionStatus status) {
                        JPQLQuery query = getQuery();
                        query.offset((int) from);
                        if (to > from) {
                            query.limit((int) (to - from));
                        }

                        List<T> items = query.list(resultExpression);
                        return items.iterator();
                    }
                });
    }

    @Override
    @Transactional(readOnly = true)
    public int getSize() {
        return transactionTemplate.execute(new TransactionCallback<Integer>() {

            @Override
            public Integer doInTransaction(TransactionStatus status) {
                JPQLQuery query = getQuery();

                return (int) query.count();
            }
        });
    }

    private JPQLQuery getQuery() {
        return ((HibernateQuery) query).clone(sessionFactory
                .getCurrentSession());
    }

    @Override
    public ItemsSet<T> subset(long startIncl, long endExcl) {
        long newFrom = this.from + startIncl;
        long newTo = this.to == -1 ? -1 : Math.min(this.to, newFrom + endExcl);
        return new PersistentItemsSet<T>(sessionFactory, transactionTemplate,
                query, newFrom, newTo, resultExpression);
    }

}

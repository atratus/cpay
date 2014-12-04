/**
 *
 */
package trsit.cpay.persistence.dao;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import trsit.cpay.SpringContextHolder;
import trsit.cpay.data.ItemsSet;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.types.Expression;

/**
 * @author black
 *
 */
public class PersistentItemsSet<T> implements ItemsSet<T>, Serializable {
    private static final long serialVersionUID = 1L;
    private final long from;
    private final long to;
    private final Expression<T> resultExpression;
    private final QueryProvider queryProvider;

    private final TransactionTemplate transactionTemplate;

    public PersistentItemsSet(
            TransactionTemplate transactionTemplate,
            QueryProvider queryProvider, Expression<T> resultTuple) {
        this(transactionTemplate,
                queryProvider,
                0, -1, resultTuple);
    }

    protected PersistentItemsSet(
            TransactionTemplate transactionTemplate,
            QueryProvider queryProvider,
            long from, long to, Expression<T> resultTuple) {
        this.from = from;
        this.to = to;
        this.queryProvider = queryProvider;

        this.transactionTemplate = transactionTemplate;
        this.resultExpression = resultTuple;
    }

    @Override
    public Iterator<T> iterator() {
        return transactionTemplate
                .execute(new TransactionCallback<Iterator<T>>() {

                    @Override
                    public Iterator<T> doInTransaction(TransactionStatus status) {
                        JPQLQuery query = query();
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
    public ItemsSet<T> subset(long startIncl, long endExcl) {
        long newFrom = this.from + startIncl;
        long newTo = this.to == -1 ? -1 : Math.min(this.to, newFrom + endExcl);
        return new PersistentItemsSet<T>(transactionTemplate, queryProvider,
                newFrom, newTo,  resultExpression);
    }

    @Override
    @Transactional(readOnly = true)
    public int getSize() {
        return transactionTemplate.execute(new TransactionCallback<Integer>() {

            @Override
            public Integer doInTransaction(TransactionStatus status) {

                return (int) query().count();
            }
        });
    }


    protected JPQLQuery query() {
        SessionFactory sessionFactory = SpringContextHolder.getCtx().getBean(SessionFactory.class);
        return queryProvider.getQuery(sessionFactory.getCurrentSession());
    }

}

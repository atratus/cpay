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
public abstract class AbstractPersistentItemsSet<T> implements ItemsSet<T> {
    private final long from;
    private final long to;
    private final Expression<T> resultExpression;

    private final TransactionTemplate transactionTemplate;


    protected AbstractPersistentItemsSet(
            TransactionTemplate transactionTemplate,
            long from, long to, Expression<T> resultTuple) {
        this.from = from;
        this.to = to;


        this.transactionTemplate = transactionTemplate;
        this.resultExpression = resultTuple;
    }

    @Override
    public Iterator<T> iterator() {
        return transactionTemplate
                .execute(new TransactionCallback<Iterator<T>>() {

                    @Override
                    public Iterator<T> doInTransaction(TransactionStatus status) {
                        JPQLQuery query = getAtachedQuery();
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
                JPQLQuery query = getAtachedQuery();

                return (int) query.count();
            }
        });
    }

    protected abstract JPQLQuery getAtachedQuery();


}

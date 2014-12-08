/**
 *
 */
package trsit.cpay.persistence.dao;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

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
            final TransactionTemplate transactionTemplate,
            final QueryProvider queryProvider, final Expression<T> resultExpression) {
        this(transactionTemplate,
                queryProvider,
                0, -1, resultExpression);
    }

    protected PersistentItemsSet(
            final TransactionTemplate transactionTemplate,
            final QueryProvider queryProvider,
            final long from, final long to, final Expression<T> resultExpression) {
        this.from = from;
        this.to = to;
        this.queryProvider = queryProvider;

        this.transactionTemplate = transactionTemplate;
        this.resultExpression = resultExpression;
    }

    @Override
    public Iterator<T> iterator() {
        return transactionTemplate
                .execute(new TransactionCallback<Iterator<T>>() {

                    @Override
                    public Iterator<T> doInTransaction(final TransactionStatus status) {
                        final JPQLQuery query = queryProvider.getQuery(baseQuery());
                        query.offset((int) from);
                        if (to > from) {
                            query.limit((int) (to - from));
                        }

                        final List<T> items = query.list(resultExpression);
                        return items.iterator();
                    }
                });
    }

    @Override
    public ItemsSet<T> subset(final long startIncl, final long endExcl) {
        final long newFrom = this.from + startIncl;
        final long newTo = this.to == -1 ? -1 : Math.min(this.to, newFrom + endExcl);
        return new PersistentItemsSet<T>(transactionTemplate, queryProvider,
                newFrom, newTo,  resultExpression);
    }

    @Override
    @Transactional(readOnly = true)
    public int getSize() {
        return transactionTemplate.execute(new TransactionCallback<Integer>() {
            @Override
            public Integer doInTransaction(final TransactionStatus status) {
                return (int) queryProvider.getCount(baseQuery());
            }
        });
    }


    protected JPQLQuery baseQuery() {
        return SpringContextHolder.getCtx().getBean(JPQLQueryFactory.class).createBaseQuery();
    }

}

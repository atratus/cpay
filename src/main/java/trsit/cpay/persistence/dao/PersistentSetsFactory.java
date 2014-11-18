/**
 * 
 */
package trsit.cpay.persistence.dao;

import javax.inject.Inject;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import trsit.cpay.data.ItemsSet;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.types.Expression;

/**
 * @author black
 *
 */
@Service
public class PersistentSetsFactory {

    @Inject
    private SessionFactory sessionFactory;

    @Inject
    private TransactionTemplate transactionTemplate;

    public <T> ItemsSet<T> buildSet(JPQLQuery query,
            Expression<T> resultExpression) {
        return new PersistentItemsSet<T>(sessionFactory,
                transactionTemplate, query, resultExpression);
    }

}

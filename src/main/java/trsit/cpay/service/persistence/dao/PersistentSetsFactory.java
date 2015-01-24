/**
 *
 */
package trsit.cpay.service.persistence.dao;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import trsit.cpay.data.ItemsSet;

import com.mysema.query.types.Expression;

/**
 * @author black
 *
 */
@Service
public class PersistentSetsFactory {

    @Inject
    private TransactionTemplate transactionTemplate;

    public <T> ItemsSet<T> buildSet(Expression<T> resultExpression, QueryProvider queryProvider) {
        return new PersistentItemsSet<T>(transactionTemplate, queryProvider, resultExpression);
    }

}

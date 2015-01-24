/**
 *
 */
package trsit.cpay.service.persistence.dao;

import com.mysema.query.jpa.JPQLQuery;

/**
 * @author black
 *
 */
public abstract class SimpleCountQueryProvider implements QueryProvider {

    private static final long serialVersionUID = 1L;

    /**
     * {@inheritDoc}
     */
    @Override
    public long getCount(final JPQLQuery query) {

        return getQuery(query).count();
    }

}

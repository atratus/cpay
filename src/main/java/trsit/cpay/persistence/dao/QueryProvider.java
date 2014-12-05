/**
 *
 */
package trsit.cpay.persistence.dao;

import java.io.Serializable;

import com.mysema.query.jpa.JPQLQuery;


/**
 * @author black
 *
 */
public interface QueryProvider extends Serializable {

    JPQLQuery getQuery(final JPQLQuery query);
}

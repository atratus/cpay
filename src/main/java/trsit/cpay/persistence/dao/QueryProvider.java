/**
 *
 */
package trsit.cpay.persistence.dao;

import java.io.Serializable;

import org.hibernate.Session;

import com.mysema.query.jpa.JPQLQuery;


/**
 * @author black
 *
 */
public interface QueryProvider extends Serializable {

    JPQLQuery getQuery(final Session session);
}

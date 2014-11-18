/**
 * 
 */
package trsit.cpay.persistence.dao;

import javax.inject.Inject;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import trsit.cpay.data.ItemsSet;
import trsit.cpay.persistence.model.PaymentEvent;
import trsit.cpay.persistence.model.QPaymentEvent;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.hibernate.HibernateQuery;

/**
 * @author black
 */
@Repository
public class EventsDAO {

    @Inject
    private PersistentSetsFactory persistentSetsFactory;

    @Transactional(readOnly = true)
    public ItemsSet<PaymentEvent> getEvents() {
        JPQLQuery query = new HibernateQuery().from(QPaymentEvent.paymentEvent);

        return persistentSetsFactory.buildSet(query, QPaymentEvent.paymentEvent);
    }
}

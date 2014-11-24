/**
 * 
 */
package trsit.cpay.persistence.dao;

import javax.inject.Inject;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import trsit.cpay.data.ItemsSet;
import trsit.cpay.persistence.model.PaymentEvent;
import trsit.cpay.persistence.model.QPaymentEvent;

import com.mysema.query.jpa.JPQLQuery;

/**
 * @author black
 */
@Repository
public class EventsDAO extends AbstractDAO {


    @Inject
    private PersistentSetsFactory persistentSetsFactory;

    public ItemsSet<PaymentEvent> getEvents() {
        JPQLQuery query = detachedQuery().from(QPaymentEvent.paymentEvent).orderBy(QPaymentEvent.paymentEvent.creationTimestamp.desc());

        return persistentSetsFactory.buildSet(query, QPaymentEvent.paymentEvent);
    }

    @Transactional(readOnly = true)
    public PaymentEvent loadEvent(Long eventId) {
        PaymentEvent event = attachedQuery()
                .from(QPaymentEvent.paymentEvent)
                .where(QPaymentEvent.paymentEvent.id.eq(eventId)).uniqueResult(QPaymentEvent.paymentEvent);
        if(event == null) {
            throw new RuntimeException("Event #" + eventId + " is not found");
        }
        Hibernate.initialize(event.getPayments());
        return event;
    }

    @Transactional
    public void save(PaymentEvent paymentEvent) {
        saveOrUpdate(paymentEvent);
    }

    @Transactional
    public void removeEvent(Long id) {
        remove(PaymentEvent.class, id);
    }

}

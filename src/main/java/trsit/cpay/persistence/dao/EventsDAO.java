/**
 *
 */
package trsit.cpay.persistence.dao;

import javax.inject.Inject;

import org.hibernate.Hibernate;
import org.hibernate.Session;
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
public class EventsDAO extends AbstractDAO {

    @Inject
    private PersistentSetsFactory persistentSetsFactory;

    private static class EventsQueryProvider implements QueryProvider {
        private static final long serialVersionUID = 1L;

        @Override
        public JPQLQuery getQuery(final Session session) {
            return new HibernateQuery(session).from(QPaymentEvent.paymentEvent).orderBy(QPaymentEvent.paymentEvent.creationTimestamp.desc());
        }
    }

    public ItemsSet<PaymentEvent> getEvents() {
        return persistentSetsFactory.buildSet(QPaymentEvent.paymentEvent, new EventsQueryProvider());
    }

    @Transactional(readOnly = true)
    public PaymentEvent loadEvent(final Long eventId) {
        final PaymentEvent event = attachedQuery()
                .from(QPaymentEvent.paymentEvent)
                .where(QPaymentEvent.paymentEvent.id.eq(eventId)).uniqueResult(QPaymentEvent.paymentEvent);
        if(event == null) {
            throw new RuntimeException("Event #" + eventId + " is not found");
        }
        Hibernate.initialize(event.getPayments());
        return event;
    }

    @Transactional
    public void save(final PaymentEvent paymentEvent) {
        saveOrUpdate(paymentEvent);
    }

    @Transactional
    public void removeEvent(final Long id) {
        remove(PaymentEvent.class, id);
    }

}

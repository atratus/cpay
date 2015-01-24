/**
 *
 */
package trsit.cpay.service.persistence.dao;

import static trsit.cpay.persistence.model.QPaymentEvent.paymentEvent;

import java.util.List;

import javax.inject.Inject;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import trsit.cpay.data.ItemsSet;
import trsit.cpay.persistence.model.QPaymentEvent;
import trsit.cpay.service.persistence.model.PaymentEvent;

import com.mysema.query.jpa.JPQLQuery;

/**
 * @author black
 */
@Repository
public class EventsDAO extends AbstractDAO {

    @Inject
    private PersistentSetsFactory persistentSetsFactory;
    @Inject
    private JPQLQueryFactory jpqlQueryFactory;

    private static class EventsQueryProvider extends SimpleCountQueryProvider {
        private static final long serialVersionUID = 1L;

        @Override
        public JPQLQuery getQuery(final JPQLQuery query) {
            return query.from(QPaymentEvent.paymentEvent).orderBy(QPaymentEvent.paymentEvent.creationTimestamp.desc());
        }
    }

    public ItemsSet<PaymentEvent> getEvents() {
        return persistentSetsFactory.buildSet(QPaymentEvent.paymentEvent, new EventsQueryProvider());
    }

    @Transactional(readOnly = true)
    public PaymentEvent loadEvent(final Long eventId) {
        final PaymentEvent event =
                attachedQuery().from(QPaymentEvent.paymentEvent).where(QPaymentEvent.paymentEvent.id.eq(eventId))
                .uniqueResult(QPaymentEvent.paymentEvent);
        if (event == null) {
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

    @Transactional(readOnly = true)
    public List<String> findTypes(final String input) {
        return jpqlQueryFactory.createBaseQuery().from(paymentEvent)
                .where(paymentEvent.eventType.like("%" + input + "%")).distinct().list(paymentEvent.eventType);
    }

}

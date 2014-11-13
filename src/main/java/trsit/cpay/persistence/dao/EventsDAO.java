/**
 * 
 */
package trsit.cpay.persistence.dao;

import javax.inject.Inject;

import org.hibernate.SessionFactory;
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
    private SessionFactory sessionFactory;
    
    @Transactional(readOnly = true)
    public ItemsSet<PaymentEvent> getEvents() {
        JPQLQuery query = new HibernateQuery(sessionFactory.getCurrentSession()).from(QPaymentEvent.paymentEvent);
        
        return new PersistentItemsSet<PaymentEvent>(query, QPaymentEvent.paymentEvent);
 
        /*
        List<PaymentEvent> events = new ArrayList<PaymentEvent>();
        events.add(PaymentEvent.builder().title("Payment1").creationTimestamp(new Date()).build());
        events.add(PaymentEvent.builder().title("Payment2").creationTimestamp(new Date()).build());
        events.add(PaymentEvent.builder().title("Payment3").creationTimestamp(new Date()).build());
        return new ListItemsSet<PaymentEvent>(events);
        */
    }
}

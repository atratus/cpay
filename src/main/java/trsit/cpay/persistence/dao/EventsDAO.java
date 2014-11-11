/**
 * 
 */
package trsit.cpay.persistence.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import trsit.cpay.data.ItemsSet;
import trsit.cpay.data.ListItemsSet;
import trsit.cpay.persistence.model.PaymentEvent;

/**
 * @author black
 *
 */
@Repository
public class EventsDAO {

    public ItemsSet<PaymentEvent> getEvents() {
        List<PaymentEvent> events = new ArrayList<PaymentEvent>();
        events.add(PaymentEvent.builder().title("Payment1").creationTimestamp(new Date()).build());
        events.add(PaymentEvent.builder().title("Payment2").creationTimestamp(new Date()).build());
        events.add(PaymentEvent.builder().title("Payment3").creationTimestamp(new Date()).build());
        return new ListItemsSet<PaymentEvent>(events);
    }
}

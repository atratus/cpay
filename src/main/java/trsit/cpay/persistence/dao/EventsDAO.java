/**
 * 
 */
package trsit.cpay.persistence.dao;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import trsit.cpay.persistence.model.PaymentEvent;

/**
 * @author black
 *
 */
@Repository
public class EventsDAO {

    public List<PaymentEvent> getEvents() {
        return Collections.singletonList(PaymentEvent.builder().title("Payment1").creationTimestamp(new Date()).build());
    }
}

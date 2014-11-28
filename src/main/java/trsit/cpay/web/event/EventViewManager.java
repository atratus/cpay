/**
 * 
 */
package trsit.cpay.web.event;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.collections4.ListUtils;
import org.springframework.stereotype.Service;

import trsit.cpay.persistence.dao.EventsDAO;
import trsit.cpay.persistence.model.Payment;
import trsit.cpay.persistence.model.PaymentEvent;
import trsit.cpay.persistence.model.User;
import trsit.cpay.web.edit.EventMemberItem;
import trsit.cpay.web.edit.UserViewBeanBuilder;

/**
 * @author black
 */
@Service
public class EventViewManager {
    @Inject
    private EventsDAO eventsDAO;
    
    /**
     * Saves eventView corresponding to eventView
     */
    public void saveEvent(EventView eventView) {
        PaymentEvent persistablePaymentEvent = eventView.getEventId() == null ? new PaymentEvent() : eventsDAO.loadEvent(eventView.getEventId());
        
        // Set Title
        persistablePaymentEvent.setTitle(eventView.getEventTitle());
        
        // Set Payments
        List<Payment> payments = persistablePaymentEvent.getPayments();
        if(payments != null) {
            payments.clear(); //Important for delete-orphans behavior!
        } else {
            persistablePaymentEvent.setPayments(payments = new ArrayList<Payment>());
        }
        payments.addAll(buildPersistablePayments(persistablePaymentEvent, eventView.getEventItems()));
        
        eventsDAO.save(persistablePaymentEvent);
    }

    
    /**
     * Loads persistent event and represents it as a view.
     */
    public EventView loadEvent(Long eventId) {
        EventView eventView = new EventView();
        List<EventMemberItem> items = eventView.getEventItems();
        if(eventId != null) {
            //Load existing event for editing
            PaymentEvent paymentEvent = eventsDAO.loadEvent(eventId);
            
            // Calculate total event's avg
            List<Payment> persistedInvestments = ListUtils.emptyIfNull(paymentEvent.getPayments());
            BigDecimal avg = persistedInvestments.isEmpty() ?
                    BigDecimal.ZERO :
                        paymentEvent.getTotalValue().divide(
                    BigDecimal.valueOf(persistedInvestments.size()), RoundingMode.HALF_UP);

            
            for(Payment payment:persistedInvestments) {
                items.add(EventMemberItem.builder()
                        .user(UserViewBeanBuilder.from(payment.getUser()))
                        .paymentValue(payment.getValue().add(avg))
                        .build());
            }
            eventView.setEventTitle(paymentEvent.getTitle());
            eventView.setEventId(paymentEvent.getId());
        }
        if(items.isEmpty()) {
            items.add(EventMemberItem.builder()
                    .user(null)
                    .build());

        }
        return eventView;
    }

    
    private List<Payment> buildPersistablePayments(
            PaymentEvent paymentEvent, List<EventMemberItem> eventItems) {
        List<Payment> payments = new ArrayList<>();
        
        // Calculate total event's avg
        BigDecimal total = calculateTotalValue(eventItems);
        paymentEvent.setTotalValue(total);
        BigDecimal avg = total.divide(BigDecimal.valueOf(eventItems.size()), RoundingMode.HALF_UP);
     


        for(EventMemberItem item:eventItems) {
            BigDecimal investedValue = item.getPaymentValue();
            investedValue = (investedValue == null ? BigDecimal.ZERO : investedValue)
                    .subtract(avg);
            payments.add(Payment.builder()
                    .user(User.identity(item.getUser().getUserId()))
                    .value(investedValue)
                    .paymentEvent(paymentEvent)
                    .build());
            
        }
        return payments;
    }


    /**
     * Calculates average positive value.
     */
    private BigDecimal calculateTotalValue(List<EventMemberItem> eventItems) {
        if(eventItems.isEmpty()) {
            return BigDecimal.ZERO;
        }
        BigDecimal total = BigDecimal.ZERO;
        for(EventMemberItem item:eventItems) {
            if(item.getPaymentValue() != null) {
                total = total.add(item.getPaymentValue());
            }
        }
        return total;
    }

}

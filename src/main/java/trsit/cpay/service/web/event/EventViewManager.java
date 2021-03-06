/**
 *
 */
package trsit.cpay.service.web.event;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.collections4.ListUtils;
import org.springframework.stereotype.Service;

import trsit.cpay.service.persistence.dao.EventsDAO;
import trsit.cpay.service.persistence.model.Payment;
import trsit.cpay.service.persistence.model.PaymentEvent;
import trsit.cpay.service.persistence.model.User;
import trsit.cpay.web.edit.EventMemberItem;
import trsit.cpay.web.edit.UserViewBeanBuilder;
import trsit.cpay.web.event.EventView;

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
    public void saveEvent(final EventView eventView) {
        final PaymentEvent persistablePaymentEvent = eventView.getEventId() == null ? new PaymentEvent() : eventsDAO.loadEvent(eventView.getEventId());

        // Set Title
        persistablePaymentEvent.setTitle(eventView.getEventTitle());
        persistablePaymentEvent.setEventType(eventView.getEventType());

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
    public EventView loadEvent(final Long eventId) {
        final EventView eventView = new EventView();
        final List<EventMemberItem> items = eventView.getEventItems();
        if(eventId != null) {
            //Load existing event for editing
            final PaymentEvent paymentEvent = eventsDAO.loadEvent(eventId);

            // Calculate total event's avg
            final List<Payment> persistedInvestments = ListUtils.emptyIfNull(paymentEvent.getPayments());

            for(final Payment payment:persistedInvestments) {
                items.add(EventMemberItem.builder()
                        .user(UserViewBeanBuilder.from(payment.getUser()))
                        .paymentValue(payment.getPaymentValue())
                        .debt(payment.getDebt())
                        .build());
            }
            eventView.setEventTitle(paymentEvent.getTitle());
            eventView.setEventId(paymentEvent.getId());
            eventView.setEventType(paymentEvent.getEventType());
        }
        if(items.isEmpty()) {
            items.add(EventMemberItem.builder()
                    .user(null)
                    .build());

        }
        return eventView;
    }

    private List<Payment> buildPersistablePayments(
            final PaymentEvent paymentEvent, final List<EventMemberItem> eventItems) {
        final List<Payment> payments = new ArrayList<>();

        // Calculate total event's avg
        final BigDecimal total = calculateTotalValue(eventItems);
        paymentEvent.setTotalValue(total); //TODO: seems not needed

        for(final EventMemberItem item:eventItems) {
            BigDecimal paymentValue = item.getPaymentValue();
            paymentValue = (paymentValue == null ? BigDecimal.ZERO : paymentValue);
            payments.add(Payment.builder()
                    .user(User.identity(item.getUser().getUserId()))
                    .paymentValue(paymentValue)
                    .debt(paymentValue.subtract(
                            total.divide(BigDecimal.valueOf(eventItems.size()), RoundingMode.HALF_UP)))
                            .paymentEvent(paymentEvent)
                            .build());

        }
        return payments;
    }


    /**
     * Calculates average positive value.
     */
    private BigDecimal calculateTotalValue(final List<EventMemberItem> eventItems) {
        if(eventItems.isEmpty()) {
            return BigDecimal.ZERO;
        }
        BigDecimal total = BigDecimal.ZERO;
        for(final EventMemberItem item:eventItems) {
            if(item.getPaymentValue() != null) {
                total = total.add(item.getPaymentValue());
            }
        }
        return total;
    }

}

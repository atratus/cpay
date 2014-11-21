/**
 * 
 */
package trsit.cpay.web.edit;

import trsit.cpay.persistence.model.Payment;

/**
 * @author black
 *
 */
public class EventMemberItemBuilder {

    public static EventMemberItem from(Payment payment) {
        if(payment == null) {
            throw new NullPointerException("Payment is null");
        }

        return EventMemberItem.builder()
                .user(UserViewBeanBuilder.from(payment.getUser()))
                .paymentValue(payment.getValue())
                .build();
    }

}

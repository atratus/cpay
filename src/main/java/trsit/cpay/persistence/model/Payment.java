/**
 * 
 */
package trsit.cpay.persistence.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * @author black
 *
 */
@Entity
public class Payment extends Persistent {

    public final static String FIELD_PAYMENT_EVENT = "paymentEvent";
    
    private User user;
    private BigDecimal value;
    private PaymentEvent paymentEvent;
    
    @ManyToOne
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public BigDecimal getValue() {
        return value;
    }
    public void setValue(BigDecimal value) {
        this.value = value;
    }

    @ManyToOne
    public PaymentEvent getPaymentEvent() {
        return paymentEvent;
    }

    public void setPaymentEvent(PaymentEvent paymentEvent) {
        this.paymentEvent = paymentEvent;
    }
    
    
}

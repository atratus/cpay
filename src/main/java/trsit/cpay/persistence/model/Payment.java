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
public class Payment {

    public final static String FIELD_PAYMENT_EVENT = "paymentEvent";
    
    private User user;
    private BigDecimal value;
    private PaymentEvent paymentEvent;
    
    public User getUser() {
        return user;
    }
    
    @ManyToOne
    public void setUser(User user) {
        this.user = user;
    }
    public BigDecimal getValue() {
        return value;
    }
    public void setValue(BigDecimal value) {
        this.value = value;
    }
    
    
}

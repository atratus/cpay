/**
 * 
 */
package trsit.cpay.persistence.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

/**
 * @author black
 *
 */
@Entity
@Getter
@Setter
public class Payment extends Persistent {

    public final static String FIELD_PAYMENT_EVENT = "paymentEvent";
    
    @ManyToOne
    private User user;
    
    private BigDecimal value;
    
    @ManyToOne
    private PaymentEvent paymentEvent;
    
}

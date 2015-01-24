/**
 *
 */
package trsit.cpay.service.persistence.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Builder;

/**
 * @author black
 *
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
public class Payment extends Persistent {

    public final static String FIELD_PAYMENT_EVENT = "paymentEvent";

    @ManyToOne
    private User user;

    @Column(nullable = false)
    private BigDecimal paymentValue;

    @Column(nullable = false)
    private BigDecimal debt;

    @ManyToOne
    private PaymentEvent paymentEvent;

}

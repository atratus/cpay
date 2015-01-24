/**
 *
 */
package trsit.cpay.service.persistence.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Builder;



/**
 * @author black
 */
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentEvent extends Persistent {
    private String title;

    @Column(nullable = false)
    private String eventType;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTimestamp = new Date();

    @OneToMany(mappedBy = Payment.FIELD_PAYMENT_EVENT, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Payment> payments;

    @Column(nullable = false)
    private BigDecimal totalValue;
}

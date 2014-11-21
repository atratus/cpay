/**
 * 
 */
package trsit.cpay.persistence.model;

import java.util.Date;
import java.util.Set;

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
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTimestamp;

    @OneToMany(mappedBy = Payment.FIELD_PAYMENT_EVENT)
    private Set<Payment> payments;
    
}

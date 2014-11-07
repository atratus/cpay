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
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

/**
 * @author black
 *
 */
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEvent extends Persistent {
    private String title;
    private Date creationTimestamp;

    private Set<Payment> payments;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(Date creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    @OneToMany(mappedBy = Payment.FIELD_PAYMENT_EVENT)
    public Set<Payment> getPayments() {
        return payments;
    }

    public void setPayments(Set<Payment> payments) {
        this.payments = payments;
    }

    
}

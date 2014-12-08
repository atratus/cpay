/**
 *
 */
package trsit.cpay.persistence.dao;

import java.math.BigDecimal;

import lombok.Data;
import trsit.cpay.persistence.model.User;

/**
 * @author black
 *
 */
@Data
public class UserPayment {
    private User user;
    private BigDecimal paymentValue;
    private BigDecimal debt;
}

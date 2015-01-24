/**
 *
 */
package trsit.cpay.service.persistence.dao;

import java.math.BigDecimal;

import lombok.Data;
import trsit.cpay.service.persistence.model.User;

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

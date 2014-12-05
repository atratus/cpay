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

public class UserDebt {
    private User user;
    private BigDecimal debt;
}

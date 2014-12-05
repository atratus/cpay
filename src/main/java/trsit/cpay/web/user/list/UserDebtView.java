/**
 *
 */
package trsit.cpay.web.user.list;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Value;
import lombok.experimental.Builder;

/**
 * @author black
 *
 */
@Value
@Builder
public class UserDebtView implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userName;
    private BigDecimal debt;
}

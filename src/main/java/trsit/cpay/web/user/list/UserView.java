/**
 *
 */
package trsit.cpay.web.user.list;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;
import lombok.experimental.Builder;

/**
 * @author black
 */
@Data
@Builder
public class UserView implements Serializable {
    private static final long serialVersionUID = 1L;
    private long id;
    private String name;
    private BigDecimal debt;
}

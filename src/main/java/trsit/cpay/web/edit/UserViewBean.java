/**
 * 
 */
package trsit.cpay.web.edit;

import java.io.Serializable;

import lombok.Data;
import lombok.experimental.Builder;

/**
 * @author black
 *
 */
@Data
@Builder
public class UserViewBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private Long userId;
}

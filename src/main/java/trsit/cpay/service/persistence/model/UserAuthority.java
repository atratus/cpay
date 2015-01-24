/**
 *
 */
package trsit.cpay.service.persistence.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

/**
 * @author black
 *
 */
@Entity
@Getter
@Setter
public class UserAuthority {

    public static final String USER = "USER";

    @Id
    private String authority;

    public static UserAuthority identity(final String authority_id) {
        final UserAuthority auth = new UserAuthority();
        auth.setAuthority(authority_id);
        return auth;
    }

}

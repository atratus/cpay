/**
 *
 */
package trsit.cpay.service.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Builder;

/**
 * @author black
 */
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends Persistent {

    @Column(nullable = false, unique=true)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean enabled;

    @ManyToOne(optional = false)
    @JoinColumn(name="authority")
    private UserAuthority authority;

    public static User identity(final Long userId) {
        final User identity = new User();
        identity.setId(userId);
        return identity;
    }
}

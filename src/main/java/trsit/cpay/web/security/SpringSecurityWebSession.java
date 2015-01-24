/**
 *
 */
package trsit.cpay.web.security;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author black
 *
 */
public class SpringSecurityWebSession extends AuthenticatedWebSession {
    public SpringSecurityWebSession(final Request request) {
        super(request);
    }

    private static final long serialVersionUID = 1L;

    @Override
    public final boolean authenticate(final String username, final String password) {
        throw new UnsupportedOperationException("You are supposed to use Spring-Security!!");
    }

    @Override
    public final Roles getRoles() {
        final Roles roles = new Roles();
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        for (final GrantedAuthority authority : authentication.getAuthorities()) {
            roles.add(authority.getAuthority());
        }
        return roles;
    }

};


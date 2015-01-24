/**
 *
 */
package trsit.cpay.web;

import org.apache.wicket.Page;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AnnotationsRoleAuthorizationStrategy;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.springframework.context.ApplicationContext;

import trsit.cpay.web.main.MainPage;
import trsit.cpay.web.security.RegisterPage;
import trsit.cpay.web.security.SignInPage;
import trsit.cpay.web.security.SpringSecurityWebSession;

/**
 * @author black
 *
 */
public class CPayApplication extends AuthenticatedWebApplication {


    public static ApplicationContext SPRING_CTX;

    /**
     * @see org.apache.wicket.Application#getHomePage()
     */
    @Override
    public Class<? extends Page> getHomePage() {
        return MainPage.class;
    }



    @Override
    public void init() {
        getComponentInstantiationListeners().add(new SpringComponentInjector(this,
                SPRING_CTX));
        getSecuritySettings().setAuthorizationStrategy(new AnnotationsRoleAuthorizationStrategy(this));
        mountPage("/login", SignInPage.class);
        mountPage("/register", RegisterPage.class);
    }


    @Override
    protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionClass() {
        return SpringSecurityWebSession.class;
    }


    @Override
    protected Class<? extends WebPage> getSignInPageClass() {
        return SignInPage.class;
    }
}

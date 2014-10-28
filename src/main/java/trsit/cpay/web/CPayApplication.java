/**
 * 
 */
package trsit.cpay.web;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.springframework.context.ApplicationContext;

import trsit.cpay.web.page.MainPage;

/**
 * @author black
 *
 */
public class CPayApplication extends WebApplication {
    

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
    }
}

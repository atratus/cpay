/**
 *
 */
package trsit.cpay.web.security;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import trsit.cpay.web.page.Layout;

/**
 * @author black
 *
 */
public class SignInPage extends Layout {

    private static final long serialVersionUID = 1L;


    public SignInPage() {
    }


    @Override
    protected IModel<String> getPageTitle() {
        return new Model<String>("Please sign in");
    }

}

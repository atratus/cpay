/**
 *
 */
package trsit.cpay.web.page;

import org.apache.wicket.markup.head.CssReferenceHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.CssResourceReference;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.googlecode.wicket.jquery.ui.resource.JQueryUIResourceReference;
import com.googlecode.wicket.jquery.ui.theme.Initializer;

/**
 * @author black
 *
 */
public abstract class Layout extends WebPage {
    private static final long serialVersionUID = 1L;


    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new Label("pageTitle", getPageTitle()));
        add(new Label("greeting", getUserName()));
    }


    private String getUserName() {
        final SecurityContext ctx = SecurityContextHolder.getContext();
        if(ctx != null) {
            final Authentication auth = ctx.getAuthentication();
            if(auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
                return "Hi " + auth.getName() + "!";
            }
        }
        return "";
    }


    protected abstract IModel<String> getPageTitle();


    @Override
    public void renderHead(final IHeaderResponse response) {
        response.render(JavaScriptHeaderItem.forReference(JQueryUIResourceReference.get()));
        response.render(CssReferenceHeaderItem.forReference(
                new CssResourceReference(Initializer.class, "jquery-ui.css")));

    }

}

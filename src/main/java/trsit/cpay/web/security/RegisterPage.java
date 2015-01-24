/**
 *
 */
package trsit.cpay.web.security;

import java.util.Objects;

import lombok.Data;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import trsit.cpay.service.persistence.dao.UserDAO;
import trsit.cpay.service.persistence.model.User;
import trsit.cpay.service.persistence.model.UserAuthority;
import trsit.cpay.web.page.Layout;

/**
 * @author black
 *
 */
public class RegisterPage extends Layout {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private UserDAO userDAO;

    @Data
    private static class RegistrationData {
        private String username;
        private String password;
        private String repeatPassword;
    }
    private final RegistrationData registrationData = new RegistrationData();
    public RegisterPage() {

        final Form<Void> registrationForm = new Form<Void>("registrationForm") {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit() {
                if(!Objects.equals(registrationData.getPassword(), registrationData.getRepeatPassword())) {
                    error("Passwords do not match");
                } else {
                    userDAO.save(User.builder() //
                            .authority(UserAuthority.identity(UserAuthority.USER)) //
                            .name(registrationData.getUsername()) //
                            .password(registrationData.getPassword()) //
                            .enabled(true) //
                            .build());
                    setResponsePage(SignInPage.class);
                }
            }

        };
        setDefaultModel(new CompoundPropertyModel<RegistrationData>(registrationData));
        registrationForm.add(new TextField<String>("username"));
        registrationForm.add(new PasswordTextField("password"));
        registrationForm.add(new PasswordTextField("repeatPassword"));
        add(registrationForm);

        add(new FeedbackPanel("feedback"));
    }


    /**
     * @see trsit.cpay.web.page.Layout#getPageTitle()
     */
    @Override
    protected IModel<String> getPageTitle() {

        return new Model<String>("Register");
    }

}

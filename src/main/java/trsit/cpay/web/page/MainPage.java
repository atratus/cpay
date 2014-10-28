/**
 * 
 */
package trsit.cpay.web.page;

import java.util.List;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.spring.injection.annot.SpringBean;

import trsit.cpay.persistence.dao.UserDAO;
import trsit.cpay.persistence.model.User;

/**
 * @author black
 *
 */
public class MainPage extends WebPage {
    private static final long serialVersionUID = 1L;
    
    @SpringBean
    private UserDAO userDAO;

    public MainPage() {
        List<User> users = userDAO.getAllUsers();
        add(new Label("helloMessage", "Hello " + users.get(0).getName()));
    }

    
}

/**
 *
 */
package trsit.cpay.web.user.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import trsit.cpay.persistence.dao.UserDAO;
import trsit.cpay.persistence.model.User;

/**
 * @author black
 *
 */
public class UsersProvider  extends SortableDataProvider<UserItem, String>  {

    private final UserDAO userDAO;

    public UsersProvider(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    private static final long serialVersionUID = 1L;

    @Override
    public Iterator<? extends UserItem> iterator(long first, long count) {
        Collection<UserItem> userViews = new ArrayList<>();
        for(User persistedUser: userDAO.getUsers().subset(first, first + count)) {
            userViews.add(UserItem.builder() //
                    .name(persistedUser.getName()) //
                    .build());
        }
        return userViews.iterator();
    }

    @Override
    public long size() {
        return userDAO.getUsers().getSize();
    }

    @Override
    public IModel<UserItem> model(UserItem object) {
        return new Model<UserItem>(object);
    }

}

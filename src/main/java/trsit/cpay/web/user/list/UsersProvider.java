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

import trsit.cpay.data.ItemsSet;
import trsit.cpay.persistence.model.User;

/**
 * @author black
 *
 */
public class UsersProvider  extends SortableDataProvider<UserItem, String>  {

    private final ItemsSet<User> users;

    public UsersProvider(final ItemsSet<User> users) {
        this.users = users;
    }

    private static final long serialVersionUID = 1L;

    @Override
    public Iterator<? extends UserItem> iterator(final long first, final long count) {
        final Collection<UserItem> userViews = new ArrayList<>();
        for(final User persistedUser: users.subset(first, first + count)) {
            userViews.add(UserItem.builder() //
                    .name(persistedUser.getName()) //
                    .build());
        }
        return userViews.iterator();
    }

    @Override
    public long size() {
        return users.getSize();
    }

    @Override
    public IModel<UserItem> model(final UserItem object) {
        return new Model<UserItem>(object);
    }

}

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
import trsit.cpay.persistence.dao.UserPayment;

/**
 * @author black
 *
 */
public class UsersDebtsProvider  extends SortableDataProvider<UserDebtView, String>  {

    private final ItemsSet<UserPayment> debts;

    public UsersDebtsProvider(final ItemsSet<UserPayment> debts) {
        this.debts = debts;
    }

    private static final long serialVersionUID = 1L;

    @Override
    public Iterator<? extends UserDebtView> iterator(final long first, final long count) {
        final Collection<UserDebtView> userDebtViews = new ArrayList<>();
        for(final UserPayment persistedDebt: debts.subset(first, first + count)) {
            userDebtViews.add(UserDebtView.builder() //
                    .userName(persistedDebt.getUser().getName()) //
                    .payment(persistedDebt.getPaymentValue()) //
                    .debt(persistedDebt.getDebt()) //
                    .build());
        }
        return userDebtViews.iterator();
    }

    @Override
    public long size() {
        return debts.getSize();
    }

    @Override
    public IModel<UserDebtView> model(final UserDebtView object) {
        return new Model<>(object);
    }

}

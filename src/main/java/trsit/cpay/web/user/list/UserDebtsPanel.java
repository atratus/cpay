/**
 *
 */
package trsit.cpay.web.user.list;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import trsit.cpay.data.ItemsSet;
import trsit.cpay.service.persistence.dao.EventsDAO;
import trsit.cpay.service.persistence.dao.PaymentDAO;
import trsit.cpay.service.persistence.dao.UserPayment;

/**
 * @author black
 *
 */
public class UserDebtsPanel extends Panel {

    private static final long serialVersionUID = 1L;
    private static final int ROWS_PER_PAGE = 15;

    @SpringBean
    private PaymentDAO paymentDAO;

    @SpringBean
    private EventsDAO eventsDAO;

    private String eventType;

    public UserDebtsPanel(final String id) {
        super(id);
        setDefaultModel(new CompoundPropertyModel<UserDebtsPanel>(this));

        final Component table = createUsersTable("users");
        table.setOutputMarkupId(true);
        add(table);

        final DropDownChoice<String> eventFilter = new DropDownChoice<String>("eventType", new LoadableDetachableModel<List<String>>() {
            private static final long serialVersionUID = 1L;
            @Override
            protected List<String> load() {
                return eventsDAO.findTypes("");
            }
        });
        eventFilter.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(final AjaxRequestTarget target) {
                target.add(table);

            }
        });
        add(eventFilter);


    }

    private Component createUsersTable(final String tableComponentName) {

        final List<IColumn<UserDebtView, String>> columns = new ArrayList<>();

        columns.add(new AbstractColumn<UserDebtView, String>(new Model<String>("Name")) {
            private static final long serialVersionUID = 1L;

            @Override
            public void populateItem(
                    final Item<ICellPopulator<UserDebtView>> cellItem,
                    final String componentId,
                    final IModel<UserDebtView> rowModel) {
                final UserDebtView data = rowModel.getObject();
                cellItem.add(new Label(componentId, data.getUserName()));
            }
        });
        columns.add(new AbstractColumn<UserDebtView, String>(new Model<String>("Payment")) {
            private static final long serialVersionUID = 1L;

            @Override
            public void populateItem(
                    final Item<ICellPopulator<UserDebtView>> cellItem,
                    final String componentId,
                    final IModel<UserDebtView> rowModel) {
                final UserDebtView data = rowModel.getObject();
                cellItem.add(new Label(componentId, data.getPayment()));
            }

        });
        columns.add(new AbstractColumn<UserDebtView, String>(new Model<String>("Debt")) {
            private static final long serialVersionUID = 1L;

            @Override
            public void populateItem(
                    final Item<ICellPopulator<UserDebtView>> cellItem,
                    final String componentId,
                    final IModel<UserDebtView> rowModel) {
                final UserDebtView data = rowModel.getObject();
                cellItem.add(new Label(componentId, data.getDebt()));
            }
        });

        final DefaultDataTable<UserDebtView, String> usersTable =
                new DefaultDataTable<UserDebtView, String>(tableComponentName, columns, new UsersDebtsProvider() {

                    @Override
                    protected ItemsSet<UserPayment> getDebts() {
                        return paymentDAO.getUserDebts(eventType);
                    }

                }, ROWS_PER_PAGE);
        usersTable.setOutputMarkupId(true);
        return usersTable;

    }

}

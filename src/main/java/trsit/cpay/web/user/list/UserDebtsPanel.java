/**
 *
 */
package trsit.cpay.web.user.list;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import trsit.cpay.persistence.dao.PaymentDAO;

/**
 * @author black
 *
 */
public class UserDebtsPanel extends Panel {

    private static final long serialVersionUID = 1L;
    private static final int ROWS_PER_PAGE = 15;

    @SpringBean
    private PaymentDAO paymentDAO;

    public UserDebtsPanel(final String id) {
        super(id);
        add(createUsersTable("users"));

    }

    @Override
    public void onEvent(final IEvent<?> event) {
        if(event.getPayload() instanceof AjaxRequestTarget) {
            ((AjaxRequestTarget)event.getPayload()).appendJavaScript("init();");
        }
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
                new DefaultDataTable<UserDebtView, String>(tableComponentName, columns, new UsersDebtsProvider(
                        paymentDAO.getUserDebts()), ROWS_PER_PAGE);
        usersTable.setOutputMarkupId(true);
        return usersTable;

    }

}

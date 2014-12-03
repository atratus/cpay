/**
 *
 */
package trsit.cpay.web.event.list;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import trsit.cpay.persistence.dao.EventsDAO;
import trsit.cpay.web.edit.EditEvent;

/**
 * @author black
 */
public class EventsListPanel extends Panel {

    private static final int ROWS_PER_PAGE = 15;
    private static final String EVENT_TITLE_CLASS = "title-cell";
    private static final long serialVersionUID = 1L;

    @SpringBean
    private EventsDAO eventsDAO;

    private DefaultDataTable<EventItem, String> eventsTable;

    @Deprecated
    private abstract static class CustomStyledColumn<T, S> extends AbstractColumn<T, S> {

        public CustomStyledColumn(IModel<String> displayModel) {
            super(displayModel);
        }

    }

    /**
     * Constructor.
     */
    public EventsListPanel(String id) {
        super(id);

        add(createEventsTable("events"));

    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(JavaScriptHeaderItem.forScript("init()", "init"));

    }

    @Override
    public void onEvent(IEvent<?> event) {
        if(event.getPayload() instanceof AjaxRequestTarget) {
            ((AjaxRequestTarget)event.getPayload()).appendJavaScript("init();");
        }
    }

    private Component createEventsTable(String tableComponentName) {
        List<IColumn<EventItem, String>> columns = new ArrayList<>();

        columns.add(new CustomStyledColumn<EventItem, String>(new Model<String>("Title")) {
            private static final long serialVersionUID = 1L;

            @Override
            public void populateItem(
                    Item<ICellPopulator<EventItem>> cellItem,
                    String componentId,
                    IModel<EventItem> rowModel) {
                EventItem data = rowModel.getObject();
                cellItem.add(new Label(componentId, data.getTitle()));
            }

            @Override
            public String getCssClass() {
                return EVENT_TITLE_CLASS;
            }

        });
        columns.add(new CustomStyledColumn<EventItem, String>(new Model<String>("Date")) {
            private static final long serialVersionUID = 1L;

            @Override
            public void populateItem(
                    Item<ICellPopulator<EventItem>> cellItem,
                    String componentId,
                    IModel<EventItem> rowModel) {
                EventItem data = rowModel.getObject();
                cellItem.add(new Label(componentId, new SimpleDateFormat("dd-MM-yyyy hh:mm").format(data
                        .getCreationTimestamp())));

            }

        });
        columns.add(new CustomStyledColumn<EventItem, String>(new Model<String>("")) {
            private static final long serialVersionUID = 1L;

            @Override
            public void populateItem(
                    final Item<ICellPopulator<EventItem>> cellItem,
                    String componentId,
                    final IModel<EventItem> rowModel) {
                cellItem.add(new EventActionsPanel(componentId) {
                    private static final long serialVersionUID = 1L;

                    @Override
                    protected void onRemoveTriggered(AjaxRequestTarget target) {
                        removeEvent(rowModel.getObject().getId());
                        target.add(eventsTable);
                        target.appendJavaScript("init();");
                    }

                    @Override
                    protected void onEditTriggered(AjaxRequestTarget target) {
                        setResponsePage(EditEvent.class,
                                new PageParameters().add(EditEvent.EVENT_ID, rowModel.getObject().getId()));

                    }
                });

            }
        });

        eventsTable =
                new DefaultDataTable<EventItem, String>(tableComponentName, columns, new EventsProvider(
                        eventsDAO), ROWS_PER_PAGE);
        eventsTable.setOutputMarkupId(true);
        return eventsTable;
    }

    private void removeEvent(Long id) {
        eventsDAO.removeEvent(id);
    }

}

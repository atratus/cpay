/**
 *
 */
package trsit.cpay.web.event.list;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import trsit.cpay.service.persistence.dao.EventsDAO;
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

    private DefaultDataTable<EventListView, String> eventsTable;

    /**
     * Constructor.
     */
    public EventsListPanel(final String id) {
        super(id);

        add(createEventsTable("events"));

    }

    @Override
    public void renderHead(final IHeaderResponse response) {
        super.renderHead(response);
    }


    private Component createEventsTable(final String tableComponentName) {
        final List<IColumn<EventListView, String>> columns = new ArrayList<>();

        columns.add(new AbstractColumn<EventListView, String>(new Model<String>("Title")) {
            private static final long serialVersionUID = 1L;

            @Override
            public void populateItem(
                    final Item<ICellPopulator<EventListView>> cellItem,
                    final String componentId,
                    final IModel<EventListView> rowModel) {
                final EventListView data = rowModel.getObject();
                cellItem.add(new Label(componentId, data.getTitle()));
            }

            @Override
            public String getCssClass() {
                return EVENT_TITLE_CLASS;
            }

        });
        columns.add(new AbstractColumn<EventListView, String>(new Model<String>("Date")) {
            private static final long serialVersionUID = 1L;

            @Override
            public void populateItem(
                    final Item<ICellPopulator<EventListView>> cellItem,
                    final String componentId,
                    final IModel<EventListView> rowModel) {
                final EventListView data = rowModel.getObject();
                cellItem.add(new Label(componentId, new SimpleDateFormat("dd-MM-yyyy hh:mm").format(data
                        .getCreationTimestamp())));

            }

        });
        columns.add(new AbstractColumn<EventListView, String>(new Model<String>("Event Type")) {
            private static final long serialVersionUID = 1L;

            @Override
            public void populateItem(
                    final Item<ICellPopulator<EventListView>> cellItem,
                    final String componentId,
                    final IModel<EventListView> rowModel) {
                final EventListView data = rowModel.getObject();
                cellItem.add(new Label(componentId, data
                        .getEventType()));

            }

        });
        columns.add(new AbstractColumn<EventListView, String>(new Model<String>("")) {
            private static final long serialVersionUID = 1L;

            @Override
            public void populateItem(
                    final Item<ICellPopulator<EventListView>> cellItem,
                    final String componentId,
                    final IModel<EventListView> rowModel) {
                cellItem.add(new EventActionsPanel(componentId) {
                    private static final long serialVersionUID = 1L;

                    @Override
                    protected void onRemoveTriggered(final AjaxRequestTarget target) {
                        removeEvent(rowModel.getObject().getId());
                        target.add(eventsTable);
                    }

                    @Override
                    protected void onEditTriggered(final AjaxRequestTarget target) {
                        setResponsePage(EditEvent.class,
                                new PageParameters().add(EditEvent.EVENT_ID, rowModel.getObject().getId()));

                    }
                });

            }
        });

        eventsTable =
                new DefaultDataTable<EventListView, String>(tableComponentName, columns, new EventsProvider(
                        eventsDAO.getEvents()), ROWS_PER_PAGE);
        eventsTable.setOutputMarkupId(true);
        return eventsTable;
    }

    private void removeEvent(final Long id) {
        eventsDAO.removeEvent(id);
    }

}

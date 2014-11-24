/**
 * 
 */
package trsit.cpay.web.main;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import trsit.cpay.persistence.dao.EventsDAO;
import trsit.cpay.persistence.dao.UserDAO;
import trsit.cpay.web.edit.EditEvent;
import trsit.cpay.web.page.Layout;

/**
 * @author black
 */
public class MainPage extends Layout {
    private static final long serialVersionUID = 1L;

    private static final int ROWS_PER_PAGE = 15;

    protected static final String EVENT_TITLE_CLASS = "title-cell";
    
    @SpringBean
    private UserDAO userDAO;
    
    @SpringBean
    private EventsDAO eventsDAO;

    private DefaultDataTable<EventItem, String> eventsTable;
    
    public MainPage() {

        add(createEventsTable("events"));
        
        add(new Link<Void>("add") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                /*    setResponsePage(EditEvent.class,
                new PageParameters().add(EditEvent.EVENT_ID, newEventId));*/
                setResponsePage(EditEvent.class);
                
            }
        });

    }

    private Component createEventsTable(String tableComponentName) {
        List<IColumn<EventItem, String>> columns = new ArrayList<IColumn<EventItem, String>>();

        columns.add(new AbstractColumn<EventItem, String>(new Model<String>("Title"))
        {
            private static final long serialVersionUID = 1L;

            @Override
            public void populateItem(Item<ICellPopulator<EventItem>> cellItem,
                    String componentId, IModel<EventItem> rowModel) {
                EventItem data = rowModel.getObject();
                cellItem.add(new Label(componentId, data.getTitle()));
            }

            @Override
            public String getCssClass() {
                return EVENT_TITLE_CLASS;
            }

        });
        columns.add(new AbstractColumn<EventItem, String>(new Model<String>("Date"))
        {
            private static final long serialVersionUID = 1L;

            @Override
            public void populateItem(Item<ICellPopulator<EventItem>> cellItem,
                    String componentId, IModel<EventItem> rowModel) {
                EventItem data = rowModel.getObject();
                cellItem.add(new Label(componentId, new SimpleDateFormat("dd-MM-yyyy hh:mm").format(data.getCreationTimestamp())));
                
            }
            
            
        });
        columns.add(new AbstractColumn<EventItem, String>(new Model<String>(""))
        {
            private static final long serialVersionUID = 1L;

            @Override
            public void populateItem(final Item<ICellPopulator<EventItem>> cellItem,
                    String componentId, final IModel<EventItem> rowModel) {
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
                        setResponsePage(EditEvent.class, new PageParameters()
                            .add(EditEvent.EVENT_ID, rowModel.getObject().getId()));
                        
                    }
                });
                
            }
        });

        eventsTable = new DefaultDataTable<>(
                tableComponentName, columns, new EventsProvider(eventsDAO.getEvents()), ROWS_PER_PAGE);
        eventsTable.setOutputMarkupId(true);
        return eventsTable;
    }
    
    private void removeEvent(Long id) {
        eventsDAO.removeEvent(id);
    }
}

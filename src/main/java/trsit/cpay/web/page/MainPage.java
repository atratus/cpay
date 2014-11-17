/**
 * 
 */
package trsit.cpay.web.page;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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
import trsit.cpay.web.events.EventItem;
import trsit.cpay.web.events.EventsProvider;

/**
 * @author black
 *
 */
public class MainPage extends Layout {
    private static final long serialVersionUID = 1L;

    private static final int ROWS_PER_PAGE = 15;
    
    @SpringBean
    private UserDAO userDAO;
    
    @SpringBean
    private EventsDAO eventsDAO;
    
    public MainPage() {
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
        add(new DefaultDataTable<EventItem, String>(
                "events", columns, new EventsProvider(eventsDAO.getEvents()), ROWS_PER_PAGE));
        
        add(new Link<Void>("add") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                Long newEventId = 1l;
                setResponsePage(EditEvent.class,
                        new PageParameters().add(EditEvent.EVENT_ID, newEventId));
                
            }
        });

    }
    
    
}

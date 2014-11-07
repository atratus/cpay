/**
 * 
 */
package trsit.cpay.web.page;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import lombok.Data;
import lombok.experimental.Builder;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.CssReferenceHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;

import trsit.cpay.persistence.dao.EventsDAO;
import trsit.cpay.persistence.dao.UserDAO;
import trsit.cpay.persistence.model.PaymentEvent;

/**
 * @author black
 *
 */
public class MainPage extends WebPage {
    private static final long serialVersionUID = 1L;
    
    @SpringBean
    private UserDAO userDAO;
    
    @SpringBean
    private EventsDAO eventsDAO;
    
    @Data
    @Builder
    private static class EventItem implements Serializable {
        private static final long serialVersionUID = 1L;
        private String title;
        private Date creationTimestamp;
    }
    
    private class EventsProvider extends SortableDataProvider<EventItem, String> {

        private static final long serialVersionUID = 1L;

        @Override
        public Iterator<? extends EventItem> iterator(long first, long count) {
            Collection<EventItem> items = new ArrayList<EventItem>();
            for(PaymentEvent event: eventsDAO.getEvents()) {
                items.add(EventItem.builder().title(event.getTitle()).creationTimestamp(event.getCreationTimestamp()).build());
            }
            return items.iterator();
        }

        @Override
        public long size() {
            return eventsDAO.getEvents().size();
        }

        @Override
        public IModel<EventItem> model(EventItem object) {
            return new Model<EventItem>(object);
        }
      
    }

    public MainPage() {
        //List<User> users = userDAO.getAllUsers();
        add(new DataView<EventItem>("events", new EventsProvider()) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(Item<EventItem> item) {
                EventItem data = item.getModel().getObject();
                item.add(new Label("title", data.getTitle()));
                item.add(new Label("date", new SimpleDateFormat("dd-MM-yyyy hh:mm").format(data.getCreationTimestamp())));
                
            }
        });
    }

    @Override
    public void renderHead(IHeaderResponse response) {
       response.render(CssReferenceHeaderItem.forReference(
               new CssResourceReference(MainPage.class, "/com/googlecode/wicket/jquery/ui/theme/jquery-ui.css")));
    }    
}

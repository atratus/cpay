package trsit.cpay.web.event.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import trsit.cpay.persistence.dao.EventsDAO;
import trsit.cpay.persistence.model.PaymentEvent;

public class EventsProvider extends SortableDataProvider<EventItem, String> {
    private static final long serialVersionUID = 1L;

    private EventsDAO eventDAO;

    /**
     * @param mainPage
     */
    public EventsProvider(EventsDAO eventDAO) {
        this.eventDAO = eventDAO;
    }

    @Override
    public Iterator<? extends EventItem> iterator(long first, long count) {
        Collection<EventItem> items = new ArrayList<>();
        for(PaymentEvent event: eventDAO.getEvents().subset(first, first + count)) {
            items.add(EventItem.builder()
                    .title(event.getTitle())
                    .creationTimestamp(event.getCreationTimestamp())
                    .id(event.getId())
                    .build());
        }
        return items.iterator();
    }

    @Override
    public long size() {
        return eventDAO.getEvents().getSize();
    }

    @Override
    public IModel<EventItem> model(EventItem object) {
        return new Model<EventItem>(object);
    }

}
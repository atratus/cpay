package trsit.cpay.web.event.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import trsit.cpay.data.ItemsSet;
import trsit.cpay.persistence.model.PaymentEvent;

public class EventsProvider extends SortableDataProvider<EventItem, String> {
    private static final long serialVersionUID = 1L;

    private ItemsSet<PaymentEvent> events;

    /**
     * @param mainPage
     */
    public EventsProvider(ItemsSet<PaymentEvent> events) {
        this.events = events;
    }

    @Override
    public Iterator<? extends EventItem> iterator(long first, long count) {
        Collection<EventItem> items = new ArrayList<>();
        for(PaymentEvent event: events.subset(first, first + count)) {
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
        return events.getSize();
    }

    @Override
    public IModel<EventItem> model(EventItem object) {
        return new Model<EventItem>(object);
    }

}
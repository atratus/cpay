package trsit.cpay.web.event.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import trsit.cpay.data.ItemsSet;
import trsit.cpay.persistence.model.PaymentEvent;

public class EventsProvider extends SortableDataProvider<EventListView, String> {
    private static final long serialVersionUID = 1L;

    private final ItemsSet<PaymentEvent> events;

    /**
     * @param mainPage
     */
    public EventsProvider(final ItemsSet<PaymentEvent> events) {
        this.events = events;
    }

    @Override
    public Iterator<? extends EventListView> iterator(final long first, final long count) {
        final Collection<EventListView> items = new ArrayList<>();
        for(final PaymentEvent event: events.subset(first, first + count)) {
            items.add(EventListView.builder()
                    .title(event.getTitle())
                    .creationTimestamp(event.getCreationTimestamp())
                    .eventType(event.getEventType())
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
    public IModel<EventListView> model(final EventListView object) {
        return new Model<EventListView>(object);
    }

}
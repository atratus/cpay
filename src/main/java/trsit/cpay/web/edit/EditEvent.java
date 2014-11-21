/**
 * 
 */
package trsit.cpay.web.edit;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.SetUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import trsit.cpay.persistence.dao.EventsDAO;
import trsit.cpay.persistence.model.Payment;
import trsit.cpay.persistence.model.PaymentEvent;
import trsit.cpay.web.page.Layout;


/**
 * @author black
 */
public class EditEvent extends Layout {
    private static final long serialVersionUID = 1L;
    public static final String EVENT_ID = "eventId";
    
    private ListView<EventMemberItem> eventItemsControl;
    
    private final List<EventMemberItem> eventItems;
    
    @SpringBean
    private EventsDAO eventsDAO;

    public EditEvent(PageParameters pageParameters) {

        eventItems = loadEvent(pageParameters.get(EVENT_ID).toOptionalLong());

        setDefaultModel(new CompoundPropertyModel<EditEvent>(this));

        // Events list view's container (for Ajax updates)
        final WebMarkupContainer eventItemsContainer = new WebMarkupContainer("eventItemsContainer");
        eventItemsContainer.setOutputMarkupId(true);
        add(eventItemsContainer);

        // Events list view
        eventItemsControl = new ListView<EventMemberItem>(
                "eventItems") {
            private static final long serialVersionUID = 1L;
            @Override
            protected void populateItem(final ListItem<EventMemberItem> item) {
                item.add(new EventMemberComponent("eventMemberItem", item.getModel(), eventItems.size() > 1) {

                    private static final long serialVersionUID = 1L;

                    @Override
                    protected void onAddEventItem(AjaxRequestTarget target) {
                        eventItems.add(EventMemberItem.builder()
                                .user(null)
                                .build());
                        target.add(eventItemsContainer);
                    }

                    @Override
                    protected void onDeleteEventItem(AjaxRequestTarget target) {
                        eventItems.remove(item.getIndex());
                        target.add(eventItemsContainer);                      
                    }
                    
                });
                
            }
            
        };
        eventItemsContainer.add(eventItemsControl);
       
        // Event submission button
        add(new AjaxLink<Void>("submit") {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                saveEvent();
            }
            
        });
    }

    private List<EventMemberItem> loadEvent(Long eventId) {
        List<EventMemberItem> items = new ArrayList<EventMemberItem>();
        if(eventId != null) {
            //Load existing event for editing
            PaymentEvent paymentEvent = eventsDAO.loadEvent(eventId);
            for(Payment payment:SetUtils.emptyIfNull(paymentEvent.getPayments())) {
                items.add(EventMemberItemBuilder.from(payment));
            }
        }
        if(items.isEmpty()) {
            items.add(EventMemberItem.builder()
                    .user(null)
                    .build());

        }
        return items;
    }
    
    private void saveEvent() {
        //eventsDAO.save();
    }


}

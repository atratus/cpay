/**
 * 
 */
package trsit.cpay.web.edit;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

import org.apache.commons.collections4.ListUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import trsit.cpay.persistence.dao.EventsDAO;
import trsit.cpay.persistence.model.Payment;
import trsit.cpay.persistence.model.PaymentEvent;
import trsit.cpay.persistence.model.User;
import trsit.cpay.web.main.MainPage;
import trsit.cpay.web.page.Layout;


/**
 * @author black
 */
public class EditEvent extends Layout {
    private static final long serialVersionUID = 1L;
    public static final String EVENT_ID = "eventId";
    
    private ListView<EventMemberItem> eventItemsControl;
    
    @Data
    private static class EventView implements Serializable {
        private static final long serialVersionUID = 1L;
        private Long eventId;
        private final List<EventMemberItem> eventItems = new ArrayList<>();
        private String eventTitle;
        
    }
    
    private final EventView eventView;
    
    @SpringBean
    private EventsDAO eventsDAO;

    public EditEvent(PageParameters pageParameters) {

        eventView = loadEvent(pageParameters.get(EVENT_ID).toOptionalLong());
        

        setDefaultModel(new CompoundPropertyModel<EventView>(eventView));

        // Event's title:
        TextField<String> eventTitle = new TextField<String>("eventTitle");
        eventTitle.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            private static final long serialVersionUID = 1L;

            protected void onUpdate(AjaxRequestTarget target) {

            }
        });

        add(eventTitle);
        
        // Events list view's container (for Ajax updates)
        final WebMarkupContainer eventItemsContainer = new WebMarkupContainer("eventItemsContainer");
        eventItemsContainer.setOutputMarkupId(true);
        add(eventItemsContainer);

        // Events list view
        final List<EventMemberItem> eventItems = eventView.getEventItems();
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

    private EventView loadEvent(Long eventId) {
        EventView eventView = new EventView();
        List<EventMemberItem> items = eventView.getEventItems();
        if(eventId != null) {
            //Load existing event for editing
            PaymentEvent paymentEvent = eventsDAO.loadEvent(eventId);
            for(Payment payment:ListUtils.emptyIfNull(paymentEvent.getPayments())) {
                items.add(EventMemberItemBuilder.from(payment));
            }
            eventView.setEventTitle(paymentEvent.getTitle());
            eventView.setEventId(paymentEvent.getId());
        }
        if(items.isEmpty()) {
            items.add(EventMemberItem.builder()
                    .user(null)
                    .build());

        }
        return eventView;
    }
    
    private void saveEvent() {
        PaymentEvent persistablePaymentEvent = eventView.getEventId() == null ? new PaymentEvent() : eventsDAO.loadEvent(eventView.getEventId());
        
        // Set Title
        persistablePaymentEvent.setTitle(eventView.getEventTitle());
        
        // Set Payments
        List<Payment> payments = persistablePaymentEvent.getPayments();
        if(payments != null) {
            payments.clear(); //Important for delete-orphans behavior!
        } else {
            persistablePaymentEvent.setPayments(payments = new ArrayList<Payment>());
        }
        payments.addAll(buildPersistablePayments(persistablePaymentEvent, eventView.getEventItems()));
        
        eventsDAO.save(persistablePaymentEvent);
        
        setResponsePage(MainPage.class);
    }

    private List<Payment> buildPersistablePayments(
            PaymentEvent paymentEvent, List<EventMemberItem> eventItems) {
        List<Payment> payments = new ArrayList<>();
        for(EventMemberItem item:eventItems) {
            payments.add(Payment.builder()
                    .user(User.identity(item.getUser().getUserId()))
                    .value(item.getPaymentValue())
                    .paymentEvent(paymentEvent)
                    .build());
            
        }
        return payments;
    }


}
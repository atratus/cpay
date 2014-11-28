/**
 * 
 */
package trsit.cpay.web.edit;


import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

import trsit.cpay.web.event.EventView;
import trsit.cpay.web.event.EventViewManager;
import trsit.cpay.web.main.MainPage;
import trsit.cpay.web.page.Layout;


/**
 * @author black
 */
public class EditEvent extends Layout {
    private static final long serialVersionUID = 1L;
    public static final String EVENT_ID = "eventId";
    
    private ListView<EventMemberItem> eventItemsControl;
    
    private final EventView eventView;
    
    @SpringBean
    private EventViewManager eventManager;

    public EditEvent(PageParameters pageParameters) {

        eventView = eventManager.loadEvent(pageParameters.get(EVENT_ID).toOptionalLong());
        

        //setDefaultModel(new CompoundPropertyModel<EventView>(eventView));
        
        Form<EventView> eventForm = new Form<EventView>(
                "eventForm", new CompoundPropertyModel<EventView>(eventView)) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onError() {
                visitFormComponents(new IVisitor<FormComponent<?>, Void>() {

                    @Override
                    public void component(FormComponent<?> object, IVisit<Void> visit) {
                        // TODO Auto-generated method stub
                        
                    }
                });
            }
            
            
            
        };
       // eventForm.setDefaultModel(new CompoundPropertyModel<EventView>(eventView));
        
        add(eventForm);

        // Event's title:
        TextField<String> eventTitle = new TextField<String>("eventTitle");
        eventTitle.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            private static final long serialVersionUID = 1L;

            protected void onUpdate(AjaxRequestTarget target) {

            }
        });

        eventForm.add(eventTitle);
        
        // Events list view's container (for Ajax updates)
        final WebMarkupContainer eventItemsContainer = new WebMarkupContainer("eventItemsContainer");
        eventItemsContainer.setOutputMarkupId(true);
        eventForm.add(eventItemsContainer);

        // Events list view

        eventItemsControl = new ListView<EventMemberItem>(
                "eventItems") {
            private static final long serialVersionUID = 1L;
            @Override
            protected void populateItem(final ListItem<EventMemberItem> item) {
                /**/System.out.print("");
                item.add(new EventMemberComponent(
                        "eventMemberItem", item.getModel(), eventView.getEventItems().size() > 1) {

                    private static final long serialVersionUID = 1L;

                    @Override
                    protected void onAddEventItem(AjaxRequestTarget target) {
                        eventView.getEventItems().add(EventMemberItem.builder()
                                .user(null)
                                .build());
                        target.add(eventItemsContainer);
                    }

                    @Override
                    protected void onDeleteEventItem(AjaxRequestTarget target) {
                        eventView.getEventItems().remove(item.getIndex());
                        target.add(eventItemsContainer);                      
                    }
                    
                });
                
            }
            
        };
        eventItemsControl.setReuseItems(true);
        eventItemsContainer.add(eventItemsControl);
       
        // Event submission button
        eventForm.add(new AjaxSubmitLink("submit") {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                eventManager.saveEvent(eventView);
                setResponsePage(MainPage.class);
            }
            
        });
    }


}

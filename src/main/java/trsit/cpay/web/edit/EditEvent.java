/**
 * 
 */
package trsit.cpay.web.edit;


import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import trsit.cpay.web.page.Layout;


/**
 * @author black
 */
public class EditEvent extends Layout {
    private static final long serialVersionUID = 1L;
    public static final String EVENT_ID = "eventId";
    
    private final List<EventMemberItem> eventItems;

    public EditEvent(PageParameters pageParameters) {

        eventItems = loadEvent(pageParameters.get(EVENT_ID).toLong());

        setDefaultModel(new CompoundPropertyModel<EditEvent>(this));
 
        ListView<EventMemberItem> eventItemsControl = new ListView<EventMemberItem>(
                "eventItems") {
            private static final long serialVersionUID = 1L;
            @Override
            protected void populateItem(final ListItem<EventMemberItem> item) {
                item.add(new EventMemberComponent("eventMemberItem", item.getModel()));
                
            }
            
        };
        eventItemsControl.setReuseItems(true);
        eventItemsControl.setOutputMarkupId(true);
        add(eventItemsControl);
       
        add(new AjaxLink<Void>("submit") {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                saveEvent();
            }
            
        });
    }

    private List<EventMemberItem> loadEvent(long long1) {
        List<EventMemberItem> items = new ArrayList<EventMemberItem>();
        items.add(EventMemberItem.builder()
                .user(null)
                .build());
        items.add(EventMemberItem.builder()
                .user(null)
                .build());
        return items;
    }
    
    private void saveEvent() {
        // TODO: implement
        
    }


}

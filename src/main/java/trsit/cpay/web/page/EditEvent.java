/**
 * 
 */
package trsit.cpay.web.page;


import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.ListModel;


/**
 * @author black
 */
public class EditEvent extends Layout {
    private static final long serialVersionUID = 1L;
    protected static final String EVENT_ID = "eventId";

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class EventMemberItem {
        private String userName;
    }
    public EditEvent() {
        Form<Void> form = new Form<>("eventForm");
        add(form);
        
        ListView<EventMemberItem> membersList = new ListView<EventMemberItem>("event-members", getEventMembers()) {
            private static final long serialVersionUID = 1L;
            @Override
            protected void populateItem(ListItem<EventMemberItem> item) {
                item.add(new DropDownChoice<String>("users"));
                
            }
            
        };
        membersList.setReuseItems(true);
        add(membersList);
        
        form.add(membersList);
    }
    private IModel<? extends List<? extends EventMemberItem>> getEventMembers() {
        
        List<EventMemberItem> items = new ArrayList<EventMemberItem>();
        items.add(EventMemberItem.builder().userName("User1").build());
        items.add(EventMemberItem.builder().userName("User2").build());
        return new ListModel<EventMemberItem>(items);
    }
}

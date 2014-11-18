/**
 * 
 */
package trsit.cpay.web.edit;


import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;

import trsit.cpay.web.page.Layout;


/**
 * @author black
 */
public class EditEvent extends Layout {
    private static final long serialVersionUID = 1L;
    public static final String EVENT_ID = "eventId";
    

    public EditEvent() {

       // final List<DropDownChoice<UserViewBean>> selectedUserModels = new ArrayList<>();

        Form<Void> form = new Form<Void>("eventForm") {
            private static final long serialVersionUID = 1L;
            private List<EventMemberItem> items;

            @Override
            protected void onSubmit() {
                super.onSubmit();
                
            }
            
        };
        form.setDefaultModel(new CompoundPropertyModel<Form<Void>>(form));

        add(form);
        

        ListView<EventMemberItem> membersList = new ListView<EventMemberItem>("eventMembers", getEventMembers()) {
            private static final long serialVersionUID = 1L;
            @Override
            protected void populateItem(final ListItem<EventMemberItem> item) {
                item.add(new EventMemberComponent("eventMemberItem", new Model<EventMemberItem>() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void setObject(EventMemberItem object) {
                        item.setDefaultModelObject(object);
                    }
                    
                }));
                
            }
            
        };
        membersList.setReuseItems(true);
        membersList.setOutputMarkupId(true);
        
        
        form.add(membersList);
    }

    private IModel<? extends List<? extends EventMemberItem>> getEventMembers() {
        List<EventMemberItem> items = new ArrayList<EventMemberItem>();
        items.add(EventMemberItem.builder().userName("User1").build());
        items.add(EventMemberItem.builder().userName("User2").build());
        return new ListModel<EventMemberItem>(items);
    }
}

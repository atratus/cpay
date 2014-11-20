/**
 * 
 */
package trsit.cpay.web.edit;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import trsit.cpay.persistence.dao.UserDAO;
import trsit.cpay.persistence.model.User;

/**
 * @author black
 *
 */
public class EventMemberComponent extends FormComponentPanel<EventMemberItem> {
    private static final long serialVersionUID = 1L;

    @SpringBean
    private UserDAO userDAO;
    

    private static class UserViewBeanRenderer implements IChoiceRenderer<UserViewBean> {
        private static final long serialVersionUID = 1L;

        @Override
        public Object getDisplayValue(UserViewBean object) {
            return object.getName();
        }

        @Override
        public String getIdValue(UserViewBean object, int index) {
            return String.valueOf(object.getUserId());
        }
    }

    public EventMemberComponent(String id, IModel<EventMemberItem> model) {
        super(id, model);

        setDefaultModel(new CompoundPropertyModel<EventMemberItem>(model));
        final DropDownChoice<UserViewBean> userSelector =  new DropDownChoice<UserViewBean>(
                "user", getUserViews(), new UserViewBeanRenderer()) {

                    private static final long serialVersionUID = 1L;
/*
                    protected boolean wantOnSelectionChangedNotifications() { return true; }
                    
                    @Override
                    protected void onSelectionChanged(UserViewBean newSelection) {
                        super.onSelectionChanged(newSelection);
                    }
*/
        };
        userSelector.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            private static final long serialVersionUID = 1L;

            protected void onUpdate(AjaxRequestTarget target) {

            }
        });

        userSelector.setOutputMarkupId(true);
        add(userSelector);

        NumberTextField<Integer> paymentInput = new NumberTextField<Integer>("paymentValue");
        add(paymentInput);
    }


    private IModel<List<UserViewBean>> getUserViews() {
        return new LoadableDetachableModel<List<UserViewBean>>() {
            private static final long serialVersionUID = 1L;

            @Override
            protected List<UserViewBean> load() {
                List<UserViewBean> userViews = new ArrayList<UserViewBean>();
                for(User user: userDAO.getUsers()) {//TODO: use [declarative] per-thread cache 
                    userViews.add(UserViewBean.builder()
                            .name(user.getName())
                            .userId(user.getId())
                            .build());
                }
                return userViews;
            }
            
        };
    }
}

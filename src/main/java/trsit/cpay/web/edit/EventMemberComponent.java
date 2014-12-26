/**
 *
 */
package trsit.cpay.web.edit;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import trsit.cpay.persistence.dao.UserDAO;
import trsit.cpay.persistence.model.User;

/**
 * @author black
 *
 */
public abstract class EventMemberComponent extends FormComponentPanel<EventMemberItem> {
    private static final long serialVersionUID = 1L;

    private static class DummyAjaxComponentBehavior extends AjaxFormComponentUpdatingBehavior {
        public DummyAjaxComponentBehavior(final String event) {
            super(event);
        }
        private static final long serialVersionUID = 1L;
        @Override
        protected void onUpdate(final AjaxRequestTarget target) {

        }
    };

    @SpringBean
    private UserDAO userDAO;
    private final NumberTextField<Integer> paymentInput;

    private static class UserViewBeanRenderer implements IChoiceRenderer<UserViewBean> {
        private static final long serialVersionUID = 1L;

        @Override
        public Object getDisplayValue(final UserViewBean object) {
            return object.getName();
        }

        @Override
        public String getIdValue(final UserViewBean object, final int index) {
            return String.valueOf(object.getUserId());
        }
    }

    public EventMemberComponent(final String id, final IModel<EventMemberItem> model, final boolean removeControlEnabled) {
        super(id, model);

        setDefaultModel(new CompoundPropertyModel<EventMemberItem>(model));

        // User selector
        final DropDownChoice<UserViewBean> userSelector =  new DropDownChoice<UserViewBean>(
                "user", getUserViews(), new UserViewBeanRenderer());
        userSelector.add(new DummyAjaxComponentBehavior("onchange"));
        userSelector.setOutputMarkupId(true);
        add(userSelector);

        // Payment input
        paymentInput = new NumberTextField<Integer>("paymentValue");
        paymentInput.add(new DummyAjaxComponentBehavior("onchange"));
        add(paymentInput);

        // 'Add' control
        add(new AjaxLink<Void>("add") {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(final AjaxRequestTarget target) {
                onAddEventItem(target);
            }
        });

        // 'Delete' control
        final AjaxLink<Void> deleteLink = new AjaxLink<Void>("delete") {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(final AjaxRequestTarget target) {
                onDeleteEventItem(target);
            }
        };
        if(!removeControlEnabled) {
            deleteLink.add(new AttributeAppender("style", "display:none;"));
        }
        add(deleteLink);

    }
    protected abstract void onDeleteEventItem(final AjaxRequestTarget target);

    protected abstract void onAddEventItem(final AjaxRequestTarget target);

    @Override
    protected void convertInput() {
        final EventMemberItem member = getModelObject();
        setConvertedInput(member);
        if(member.getUser() == null) {
            error("User is not specified");
        }
    }

    private IModel<List<UserViewBean>> getUserViews() {
        return new LoadableDetachableModel<List<UserViewBean>>() {
            private static final long serialVersionUID = 1L;

            @Override
            protected List<UserViewBean> load() {
                final List<UserViewBean> userViews = new ArrayList<UserViewBean>();
                for(final User user: userDAO.getUsers()) {//TODO: use [declarative] per-thread cache
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

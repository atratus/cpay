/**
 *
 */
package trsit.cpay.web.edit;

import java.math.BigDecimal;
import java.util.Iterator;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.apache.wicket.validation.validator.StringValidator;

import trsit.cpay.persistence.dao.EventsDAO;
import trsit.cpay.web.event.EventView;
import trsit.cpay.web.event.EventViewManager;
import trsit.cpay.web.main.MainPage;
import trsit.cpay.web.page.Layout;
import trsit.cpay.web.validation.NotNullValidator;

/**
 * @author black
 */
public class EditEvent extends Layout {
    private static final long serialVersionUID = 1L;
    public static final String EVENT_ID = "eventId";
    private static final int MIN_EVENT_LEN = 3;
    private static final int MIN_TYPE_LEN = 3;

    private final EventView eventView;

    @SpringBean
    private EventViewManager eventManager;

    @SpringBean
    private EventsDAO eventsDAO;
    private final Form<EventView> eventForm;
    private final WebMarkupContainer members;

    public EditEvent(final PageParameters pageParameters) {

        eventView = eventManager.loadEvent(pageParameters.get(EVENT_ID).toOptionalLong());

        eventForm =
                new Form<EventView>("eventForm", new CompoundPropertyModel<EventView>(eventView));

        eventForm.setOutputMarkupId(true);
        add(eventForm);

        // Event's title:
        eventForm.add(getEventTitle("eventTitle"));

        // Event's type:
        eventForm.add(getEventType("eventType"));

        // Members
        members = getMemberItems("eventItemsContainer");
        eventForm.add(members);

        // Event submission button
        eventForm.add(getSubmissionButton("submit", members, eventForm));

        eventForm.add(getCancelLink("cancel"));
    }


    private Component getCancelLink(final String id) {
        return new Link<Void>(id) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                setResponsePage(MainPage.class);
            }
        };
    }

    private Component getSubmissionButton(final String id, final WebMarkupContainer members, final Form<EventView> eventForm) {
        return new AjaxSubmitLink(id) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(final AjaxRequestTarget target, final Form<?> form) {
                boolean hasPayment = false;
                for(final EventMemberItem item: eventView.getEventItems()) {
                    if(item.getPaymentValue() != null && item.getPaymentValue().compareTo(BigDecimal.ZERO) > 0) {
                        hasPayment = true;
                    }
                }
                if(!hasPayment) {
                    target.appendJavaScript(
                            "$('#"+members.getMarkupId() + "').addClass('error');");
                    members.add(new AttributeModifier("title", "No payment specified"));
                } else {
                    eventManager.saveEvent(eventView);
                    setResponsePage(MainPage.class);
                }

            }

            @Override
            protected void onError(final AjaxRequestTarget target, final Form<?> form) {
                form.visitFormComponents(new IVisitor<FormComponent<?>, Void>() {

                    @Override
                    public void component(final FormComponent<?> formComponent, final IVisit<Void> visit) {
                        if(formComponent.hasErrorMessage()) {
                            target.add(eventForm);
                            target.appendJavaScript(
                                    "$('#"+formComponent.getMarkupId() + "').addClass('error');");
                            final FeedbackMessage message = formComponent.getFeedbackMessages().first(FeedbackMessage.ERROR);
                            formComponent.add(new AttributeModifier("title", message.getMessage()));
                        } else {
                            formComponent.add(new AttributeModifier("title", ""));
                        }

                    }
                });
            }

        };
    }


    private WebMarkupContainer getMemberItems(final String id) {
        final WebMarkupContainer eventItemsContainer = new WebMarkupContainer(id);
        eventItemsContainer.setOutputMarkupId(true);

        // Members list view

        final ListView<EventMemberItem> eventItemsControl = new ListView<EventMemberItem>("eventItems") {
            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(final ListItem<EventMemberItem> item) {
                final EventMemberComponent c = new EventMemberComponent("eventMemberItem", item.getModel(),
                        eventView.getEventItems().size() > 1) {

                    private static final long serialVersionUID = 1L;

                    @Override
                    protected void onAddEventItem(final AjaxRequestTarget target) {
                        eventView.getEventItems().add(EventMemberItem.builder().user(null).build());
                        target.add(eventItemsContainer);
                    }

                    @Override
                    protected void onDeleteEventItem(final AjaxRequestTarget target) {
                        eventView.getEventItems().remove(item.getIndex());
                        target.add(eventItemsContainer);
                    }

                };
                c.setOutputMarkupId(true);
                item.add(c);

            }

        };
        eventItemsControl.setReuseItems(true);
        eventItemsContainer.setOutputMarkupId(true);
        eventItemsContainer.add(eventItemsControl);
        return eventItemsContainer;
    }

    private AutoCompleteTextField<String> getEventType(final String id) {
        final AutoCompleteTextField<String> eventType = new AutoCompleteTextField<String>(id) {

            private static final long serialVersionUID = 1L;

            @Override
            protected Iterator<String> getChoices(final String input) {

                return eventsDAO.findTypes(input).iterator();
            }
        };
        eventType.add(StringValidator.minimumLength(MIN_TYPE_LEN));
        eventType.add(NotNullValidator.instance());
        eventType.setOutputMarkupId(true);
        return eventType;
    }

    private TextField<String> getEventTitle(final String id) {
        final TextField<String> eventTitle = new TextField<String>(id);
        /*eventTitle.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(final AjaxRequestTarget target) {
            }
        });*/
        eventTitle.add(StringValidator.minimumLength(MIN_EVENT_LEN));
        eventTitle.add(NotNullValidator.instance());
        eventTitle.setOutputMarkupId(true);
        return eventTitle;
    }

    @Override
    protected IModel<String> getPageTitle() {

        return new Model<String>("Edit Event");//TODO
    }

}

/**
 *
 */
package trsit.cpay.web.main;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import trsit.cpay.web.edit.EditEvent;
import trsit.cpay.web.event.list.EventsListPanel;
import trsit.cpay.web.page.Layout;
import trsit.cpay.web.user.list.UserDebtsPanel;

/**
 * @author black
 */
@AuthorizeInstantiation("USER")
public class MainPage extends Layout {
    private static final long serialVersionUID = 1L;

    public MainPage() {
        add(createTabbedPanel("tabs"));

        add(new Link<Void>("add") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                setResponsePage(EditEvent.class);
            }
        });

    }

    private Component createTabbedPanel(final String tabbedPanelId) {
        final List<ITab> tabs = new ArrayList<ITab>();
        tabs.add(new AbstractTab(new Model<String>("Events"))
        {
            private static final long serialVersionUID = 1L;

            @Override
            public Panel getPanel(final String panelId) {
                return new EventsListPanel(panelId);
            }
        });
        tabs.add(new AbstractTab(new Model<String>("Users"))
        {
            private static final long serialVersionUID = 1L;

            @Override
            public Panel getPanel(final String panelId) {
                return new UserDebtsPanel(panelId);
            }
        });

        return new AjaxTabbedPanel<ITab>(tabbedPanelId, tabs);
    }
    @Override
    protected IModel<String> getPageTitle() {

        return new Model<String>("Community Events");//TODO
    }

}

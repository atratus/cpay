/**
 * 
 */
package trsit.cpay.web.event.list;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * @author black
 *
 */
public abstract class EventActionsPanel extends Panel {
    private static final long serialVersionUID = 1L;

    public EventActionsPanel(String id) {
        super(id);
        
        add(new AjaxLink<Void>("editLink") {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                onEditTriggered(target);
                
            }
            
        });
        add(new AjaxLink<Void>("removeLink") {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                onRemoveTriggered(target);
                
            }
            
        });
    }

    protected abstract void onEditTriggered(AjaxRequestTarget target);

    protected abstract void onRemoveTriggered(AjaxRequestTarget target);


}

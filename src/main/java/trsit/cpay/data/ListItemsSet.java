/**
 * 
 */
package trsit.cpay.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author black
 *
 */
public class ListItemsSet<T> implements ItemsSet<T> {
    private final List<T> items;
    
    public ListItemsSet(List<T> items) {
        this.items = new ArrayList<T>(items);
    }
    protected ListItemsSet(List<T> items, int startInc, int endExcl) {
        if(startInc < 0){
            startInc = 0;
        }
        if (endExcl > items.size()) {
            endExcl = items.size();
        }
        this.items = items.subList(startInc, endExcl);
    }

    @Override
    public Iterator<T> iterator() {
        return items.iterator();
    }

    @Override
    public int getSize() {
         return items.size();
    }

    @Override
    public ItemsSet<T> subset(long startInc, long endExcl) {
        //TODO: check indexes bounds (should be in range of int)
        return new ListItemsSet<T>(items, (int)startInc, (int)endExcl);
    }

}

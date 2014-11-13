/**
 * 
 */
package trsit.cpay.persistence.dao;

import java.util.Iterator;
import java.util.List;

import trsit.cpay.data.ItemsSet;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.types.Expression;

/**
 * @author black
 *
 */
public class PersistentItemsSet<T> implements ItemsSet<T> {
    private long from;
    private long to = -1;
    private JPQLQuery query;
    private Expression<T> resultTuple;
    
   
    public PersistentItemsSet(JPQLQuery query, Expression<T> resultTuple) {
        this.query = query;
        this.resultTuple = resultTuple;
    }

    private PersistentItemsSet(long from, long to, JPQLQuery query) {
        this.from = from;
        this.to = to;
        this.query = query;
    }

    @Override
    public Iterator<T> iterator() {
        query.offset((int) from);
        if(to > from) {
            query.limit((int) (to-from));
        }
        
        List<T> items = query.list(resultTuple);
        return items.iterator();
    }

    @Override
    public int getSize() {
        return (int) query.count();
    }

    @Override
    public ItemsSet<T> subset(long startIncl, long endExcl) {
        long newFrom = this.from + startIncl;
        long newTo = this.to == -1 ? -1 : Math.min(this.to, newFrom+endExcl);
        return new PersistentItemsSet<T>(newFrom, newTo, query);
    }

}

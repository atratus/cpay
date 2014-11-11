/**
 * 
 */
package trsit.cpay.data;

/**
 * @author black
 *
 */
public interface ItemsSet<T> extends Iterable<T> {
    int getSize();
    ItemsSet<T> subset(long startIncl, long endExcl);
}

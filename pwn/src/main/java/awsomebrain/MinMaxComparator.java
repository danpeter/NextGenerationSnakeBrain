/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package awsomebrain;

import java.util.Comparator;

/**
 *
 * @author Dan
 */
public class MinMaxComparator implements Comparator<MinMaxPosition> {

    public int compare(MinMaxPosition t, MinMaxPosition t1) {
        if(t.score == t1.score) {
            return 0;
        }
        return t.score > t1.score ? 1 : -1;
    }
    
}

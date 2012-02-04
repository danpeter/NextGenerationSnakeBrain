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
public class NodeComparator implements Comparator<AStarNode> {

    public int compare(AStarNode nodeA, AStarNode nodeB) {
        
        if(nodeA.estimatedCostF == nodeB.estimatedCostF) {
            return 0;
        } 
        return nodeA.estimatedCostF > nodeB.estimatedCostF ? 1 : -1;
    }
    
}

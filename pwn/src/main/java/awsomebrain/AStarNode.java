/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package awsomebrain;

import se.citerus.crazysnake.Position;

/**
 *
 * @author Dan
 */
public class AStarNode extends Position { 

    int heuristicH;
    int fixedCostG;
    int estimatedCostF;
    AStarNode parent;

    public AStarNode(Position position, Position destination, int fixedCostG, AStarNode parent) {
        super(position.getX(), position.getY());
        this.parent = parent;
        this.heuristicH = calculateManhattanDistance(position, destination);
        this.fixedCostG = fixedCostG;
        this.estimatedCostF = this.heuristicH + this.fixedCostG;
    }
    
    public void setFixedCost(int fixedCost) {
        this.fixedCostG = fixedCost;
        this.estimatedCostF = this.heuristicH + this.fixedCostG;
    }
    
    private int calculateManhattanDistance(Position start, Position finish) {
        return Math.abs(finish.getX() - start.getX()) + Math.abs(finish.getY() - start.getY());
    }
    
    
    
}

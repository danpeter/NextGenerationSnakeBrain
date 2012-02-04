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

    private int heuristicH;
    int fixedCostG;
    int estimatedCostF;
    int moveCost = 1;
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
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + getX();
        result = prime * result + getY();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Position))
        	return false;
        Position other = (Position) obj;
        if (getX() != other.getX())
            return false;
        if (getY() != other.getY())
            return false;
        return true;
    }
    
}

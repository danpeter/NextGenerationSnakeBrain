
package awsomebrain;

import java.util.LinkedList;
import java.util.List;
import se.citerus.crazysnake.*;

import static se.citerus.crazysnake.Direction.*;
import static se.citerus.crazysnake.Movement.FORWARD;
import static se.citerus.crazysnake.Movement.LEFT;
import static se.citerus.crazysnake.Movement.RIGHT;

/**
 * Base brain for one track-minds.
 */
public class pwnBrain extends BaseBrain {

	private static final int LOOK_AHEAD = 3;
	private StringBuffer memory = new StringBuffer();


	public Movement getDodgeProblemsAheadDirection() {
        return RIGHT;
    }

    @Override
    public Movement getNextMove(HeatState state) {
    	
    	return calculateNextMove(state);
    }

    private Movement calculateNextMove(HeatState state) {
       
        Snake me = state.getSnake(new BrainId(this));
        Position headPosition = me.getHeadPosition();
        List<Position> fruits = state.getFruitPositions();
        Direction direction = me.getDirection();
        
        AStar algo = new AStar(state);
        List<Position> bestPath = new LinkedList<Position>();
        for(Position fruit : fruits) {
            List<Position> path = algo.getBestPath(headPosition, fruit);
            if(path.size() < bestPath.size()) {
                bestPath = path;
            }
        }
        
        
        Position nextMove = bestPath.get(0);
        Direction newDirection = calculateDirection(headPosition, nextMove);     
        return Direction.newMovement(direction, direction);
    }
    
    public Direction calculateDirection(Position position1, Position position2) {
        if(position2.getX() > position1.getX()) {
            return EAST;
        }
        if(position2.getX() < position1.getX()) {
            return WEST;
        }
        if(position2.getY() > position1.getY()) {
            return NORTH;
        }
        if(position2.getY() < position1.getY()) {
            return SOUTH;
        }
        return NORTH;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" + "memory=" + memory + '}';
    }
}

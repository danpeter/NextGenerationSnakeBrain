
package awsomebrain;

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
        try {
            Snake me = state.getSnake(new BrainId(this));

            Position headPosition = me.getHeadPosition();
            Direction direction = me.getDirection();

            Square[] lookAheadArr = new Square[LOOK_AHEAD];
            int x = headPosition.getX();
            int y = headPosition.getY();

            if (direction == EAST) {
                for (int i = 1; i <= LOOK_AHEAD; i++)
                    lookAheadArr[i - 1] = state.getSquare(x + i, y);
            } else if (direction == WEST) {
                for (int i = 1; i <= LOOK_AHEAD; i++)
                    lookAheadArr[i - 1] = state.getSquare(x - i, y);
            } else if (direction == NORTH) {
                for (int i = 1; i <= LOOK_AHEAD; i++)
                    lookAheadArr[i - 1] = state.getSquare(x, y - i);
            } else {
                for (int i = 1; i <= LOOK_AHEAD; i++)
                    lookAheadArr[i - 1] = state.getSquare(x, y + i);
            }

            return anyIsOccupied(lookAheadArr) ? getDodgeProblemsAheadDirection() : FORWARD;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean anyIsOccupied(Square[] arr) {
        for (Square square : arr) {
            if (!square.isUnoccupied()) return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" + "memory=" + memory + '}';
    }
}

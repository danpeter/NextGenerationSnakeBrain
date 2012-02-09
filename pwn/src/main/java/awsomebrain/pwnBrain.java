package awsomebrain;

import com.sun.corba.se.spi.protocol.ForwardException;
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
	public Movement getNextMove(GameState state) {
		try {
			long start = System.nanoTime();
			Movement mov = calculateNextMove(state);
			long total = System.nanoTime() - start;
			// System.out.println("Total time: " + total / 1000000);
			return mov;
		} catch (Throwable t) {
			t.printStackTrace();
			throw new RuntimeException(t);
		}
	}

	private Movement calculateNextMove(GameState state) {

		Snake me = state.getSnake(getName());
		Position headPosition = me.getHeadPosition();
		List<Position> fruits = state.getFruitPositions();
		Direction direction = me.getDirection();

		participants.remove(getName());
                Snake opponent = null;
                for(String snake : participants) {
                    opponent = state.getSnake(snake);
                }
                if (fruits.isEmpty()) {
			return FORWARD;
		}
                MinMaxApples miniMaxi = new MinMaxApples(fruits, me, opponent, state);
		//AStar algo = new AStar(state, participants, me);
		/*List<Position> bestPath = new LinkedList<Position>();
		
		if (fruits.isEmpty()) {
			bestPath = algo.getBestPath(headPosition, new Position(25, 25));
		} else {
			for (Position fruit : fruits) {
				List<Position> path = algo.getBestPath(headPosition, fruit);
				if (bestPath.isEmpty()
						|| (!path.isEmpty() && path.size() < bestPath.size())) {
					bestPath = path;
				}
			}
		}

		if (bestPath.isEmpty()) {
			System.err.println("No path to apple");
			return FORWARD;
		}
		Position nextPosition = bestPath.get(0);*/
                List<Position> path = miniMaxi.getOptimalApple();
                Position nextPosition = path.get(0);
		// System.out.println("current position: " + headPosition);
		// System.out.println("next position: " + nextPosition);
		Direction newDirection = calculateDirection(headPosition, nextPosition);
		// System.out.println("next direction: " + newDirection);
		return Direction.newMovement(direction, newDirection);
	}

	public static Direction calculateDirection(Position position1,
			Position position2) {
		if (position2.getX() > position1.getX()) {
			return EAST;
		}
		if (position2.getX() < position1.getX()) {
			return WEST;
		}
		if (position2.getY() > position1.getY()) {
			return SOUTH;
		}
		if (position2.getY() < position1.getY()) {
			return NORTH;
		}
		throw new RuntimeException("error");
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "{" + "memory=" + memory + '}';
	}
}

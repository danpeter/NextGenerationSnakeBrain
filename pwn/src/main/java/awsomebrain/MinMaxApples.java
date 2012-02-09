/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package awsomebrain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import se.citerus.crazysnake.GameState;
import se.citerus.crazysnake.Position;
import se.citerus.crazysnake.Snake;

/**
 *
 * @author Dan
 */
public class MinMaxApples {

    private final List<Position> fruits;
    private final Position me;
    private final Position opponent;
    AStar algo;

    public MinMaxApples(List<Position> fruits, Snake me, Snake opponent, GameState state) {
        this.fruits = fruits;
        this.me = me.getHeadPosition();
        this.opponent = opponent.getHeadPosition();
        Set<String> opponents = new HashSet<String>();
        opponents.add(opponent.getName());
        algo = new AStar(state, opponents, me);
    }

    public List<Position> getOptimalApple() {
        Position positionToGoFor = minMax(fruits, me, opponent);
        return algo.getBestPath(me, positionToGoFor);
    }
    /*
     * The returned in is a value for how good the path is. 
     * 
     */

    private MinMaxPosition minMax(List<Position> fruits, Position me, Position opponent) {
        return minMax(fruits, me, opponent, 0, 0);
    }

    private MinMaxPosition minMax(List<Position> fruits, Position me, Position opponent, int applesMe, int applesOpponent) {
        if (fruits.isEmpty()) {
            if (applesMe == 0 && applesOpponent == 0) {
                return new MinMaxPosition(25, 25, 0);
            }
            return new MinMaxPosition(25, 25, applesMe / (applesOpponent + applesMe));
        }

        Map<Position, List<Position>> opponentDistances = calculateFruitDistances(fruits, opponent);
        Map<Position, List<Position>> myDistances = calculateFruitDistances(fruits, me);
        Position opponentBestFruit = null;
        int opponentBestFruitLength = 0;
        for (Map.Entry<Position, List<Position>> entry : opponentDistances.entrySet()) {
            if (opponentBestFruitLength == 0 || (entry.getValue().size() < opponentBestFruitLength)) {
                opponentBestFruitLength = entry.getValue().size();
                opponentBestFruit = entry.getKey();
            }
        }
        //should se if my snake is closet to this fruit, then he should change

        SortedSet<MinMaxPosition> scores = new TreeSet<MinMaxPosition>(new MinMaxComparator());
        for (Position fruit : fruits) {
            int lengthToMyFruit = myDistances.get(fruit).size();
            List<Position> subsetfruits = new LinkedList<Position>(fruits);
            Position opponentNewPosition = null;
            Position myNewPosition = null;
            if (lengthToMyFruit == opponentBestFruitLength) {
                if(fruit.equals(opponentBestFruit)) {
                    //We will crash in each other, should calculate who has most points;
                    return new MinMaxPosition(25, 25, 0);
                }
                subsetfruits.remove(fruit);
                subsetfruits.remove(opponentBestFruit);
                myNewPosition = fruit;
                opponentNewPosition = opponentBestFruit;
                applesOpponent++;
                applesMe++;
            } else if (lengthToMyFruit > opponentBestFruitLength) {
                subsetfruits.remove(opponentBestFruit);
                opponentNewPosition = opponentBestFruit;
                myNewPosition = myDistances.get(fruit).get(opponentBestFruitLength - 1);
                applesOpponent++;
            } else {
                subsetfruits.remove(fruit);
                opponentNewPosition = opponentDistances.get(opponentBestFruit).get(lengthToMyFruit - 1);
                myNewPosition = fruit;
                applesMe++;
            }
            MinMaxPosition moveHeuristic = minMax(subsetfruits, myNewPosition, opponentNewPosition, applesMe, applesOpponent);
            scores.add(new MinMaxPosition(fruit.getX(), fruit.getY(), moveHeuristic.score));
        }
        return scores.last();
    }

    private Map<Position, List<Position>> calculateFruitDistances(List<Position> fruits, Position snakeHead) {
        Map<Position, List<Position>> result = new HashMap<Position, List<Position>>();
        for (Position fruit : fruits) {
            result.put(fruit, algo.getBestPath(snakeHead, fruit));
        }
        return result;
    }
}

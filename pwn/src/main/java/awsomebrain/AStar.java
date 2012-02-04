/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package awsomebrain;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import se.citerus.crazysnake.HeatState;
import se.citerus.crazysnake.Position;

/**
 *
 * @author Dan
 */
public class AStar {

    HeatState state;
    Map<Position, AStarNode> objectPool;
    Position destinationPosition;

    public AStar(HeatState state) {
        this.state = state;
    }

    public List<Position> getBestPath(Position start, Position destination) {
        SortedSet<AStarNode> openList = new TreeSet<AStarNode>(new NodeComparator());
        Set<AStarNode> closedSet = new HashSet<AStarNode>();
        objectPool = new HashMap<Position, AStarNode>();


        AStarNode startNode = new AStarNode(start, destination, 0, null);
        AStarNode destinationNode = new AStarNode(destination, destination, 0, null);
        openList.add(startNode);
        objectPool.put(startNode, startNode);
        destinationPosition = destinationNode;

        while (!openList.isEmpty()) {
            AStarNode current = openList.first();
            if (current.equals(destinationNode)) {
                return createPath(current);
            } else {
                closedSet.add(current);
                openList.remove(current);
                List<AStarNode> neighbours = getNeighbours(current);
                for (AStarNode neighbour : neighbours) {
                    if (closedSet.contains(neighbour) && neighbour.fixedCostG > current.fixedCostG + 1) {
                        neighbour.setFixedCost(current.fixedCostG + 1);
                        neighbour.parent = current;
                    } else if (openList.contains(neighbour) && neighbour.fixedCostG > current.fixedCostG + 1) {
                        neighbour.setFixedCost(current.fixedCostG + 1);
                        neighbour.parent = current;
                        openList.remove(neighbour); //remove and re-add to update sorting on F
                        openList.add(neighbour);
                    } else {
                        neighbour.setFixedCost(current.fixedCostG + 1);
                        openList.add(neighbour);
                    }
                }
            }
        }
        return Collections.EMPTY_LIST;
    }

    private List<AStarNode> getNeighbours(AStarNode current) {
        List<AStarNode> neighbours = new LinkedList<AStarNode>();
        int x = current.getX();
        int y = current.getY();

        if (x - 1 >= 0) {
            if (!state.getSquare(x - 1, y).hasSolidContent()) {
                AStarNode existingNode = objectPool.get(new Position(x - 1, y));
                if (existingNode == null) {
                    AStarNode newNode = new AStarNode(new Position(x-1,y), destinationPosition, current.fixedCostG+1, current);
                    neighbours.add(newNode);
                    objectPool.put(newNode, newNode);
                } else {
                    neighbours.add(existingNode);
                }
            }
        }
        if (x + 1 >= state.getNoColumns()) {
            if (!state.getSquare(x + 1, y).hasSolidContent()) {
                AStarNode existingNode = objectPool.get(new Position(x + 1, y));
                if (existingNode == null) {
                    AStarNode newNode = new AStarNode(new Position(x+1,y), destinationPosition, current.fixedCostG+1, current);
                    neighbours.add(newNode);
                    objectPool.put(newNode, newNode);
                } else {
                    neighbours.add(existingNode);
                }
            }
        }
        if (y - 1 >= 0) {
            if (!state.getSquare(y - 1, y).hasSolidContent()) {
                AStarNode existingNode = objectPool.get(new Position(x, y-1));
                if (existingNode == null) {
                    AStarNode newNode = new AStarNode(new Position(x,y-1), destinationPosition, current.fixedCostG+1, current);
                    neighbours.add(newNode);
                    objectPool.put(newNode, newNode);
                } else {
                    neighbours.add(existingNode);
                }
            }
        }
        if (y + 1 >= state.getNoRows()) {
            if (!state.getSquare(x, y+1).hasSolidContent()) {
                AStarNode existingNode = objectPool.get(new Position(x, y+1));
                if (existingNode == null) {
                    AStarNode newNode = new AStarNode(new Position(x,y+1), destinationPosition, current.fixedCostG+1, current);
                    neighbours.add(newNode);
                    objectPool.put(newNode, newNode);
                } else {
                    neighbours.add(existingNode);
                }
            }
        }
        return neighbours;
    }

    private List<Position> createPath(AStarNode current) {
        List<Position> path = new LinkedList<Position>();   
        if(current.parent == null) {
            path.add(current);
            return path;
        } else {
            path.addAll(createPath(current.parent));  
            path.add(current);
            return path;
        }                           
    }
}

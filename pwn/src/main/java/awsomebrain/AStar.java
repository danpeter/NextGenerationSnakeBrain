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

import se.citerus.crazysnake.GameState;
import se.citerus.crazysnake.Position;
import se.citerus.crazysnake.Snake;

/**
 * 
 * @author Dan
 */
public class AStar {

	GameState state;
	Map<Position, AStarNode> objectPool;
	Position destinationPosition;
	private Set<String> opponents;
	private Set<Position> opponentThreatPositions;
	private boolean checkThreatPositions;

	public AStar(GameState state, Set<String> opponents) {
		this.state = state;
		this.opponents = opponents;
	}

	public List<Position> getBestPath(Position start, Position destination) {

		calculateOpponentThreatPositions();

		SortedSet<AStarNode> openList = new TreeSet<AStarNode>(
				new NodeComparator());
		Set<AStarNode> closedSet = new HashSet<AStarNode>();
		objectPool = new HashMap<Position, AStarNode>();

		AStarNode startNode = new AStarNode(start, destination, 0, null);
		AStarNode destinationNode = new AStarNode(destination, destination, 0,
				null);
		openList.add(startNode);
		objectPool.put(startNode, startNode);
		objectPool.put(destinationNode, destinationNode);
		destinationPosition = destinationNode;
		checkThreatPositions = true;

		while (!openList.isEmpty()) {
			AStarNode current = openList.first();
			if (current.equals(destinationNode)) {
				// System.out.println("openList size: " + openList.size());
				// System.out.println("closedList size: " + closedSet.size());
				return createPath(current, new LinkedList<Position>());
			} else {
				closedSet.add(current);
				openList.remove(current);
				List<AStarNode> neighbours = getNeighbours(current);
				checkThreatPositions = false;
				for (AStarNode neighbour : neighbours) {
					if (closedSet.contains(neighbour)
							&& neighbour.fixedCostG > current.fixedCostG + neighbour.moveCost) {
						neighbour.setFixedCost(current.fixedCostG + neighbour.moveCost);
						neighbour.parent = current;
					} else if (openList.contains(neighbour)
							&& neighbour.fixedCostG > current.fixedCostG + neighbour.moveCost) {
						neighbour.setFixedCost(current.fixedCostG + neighbour.moveCost);
						neighbour.parent = current;
						openList.remove(neighbour); // remove and re-add to
						// update sorting on F
						openList.add(neighbour);
					} else if (!closedSet.contains(neighbour)
							&& !openList.contains(neighbour)) {
						neighbour.setFixedCost(current.fixedCostG + neighbour.moveCost);
						openList.add(neighbour);
					}
				}
			}
		}

		return Collections.EMPTY_LIST;
	}

	private void calculateOpponentThreatPositions() {
		Set<Position> opponentsSet = new HashSet<Position>();
		for (String opp : opponents) {
			Snake snake = state.getSnake(opp);
			if (snake != null) {
				opponentsSet.add(snake.getHeadPosition());
			}
		}
		Set<Position> neighbours = new HashSet<Position>();
		for (Position pos : opponentsSet) {
			neighbours.addAll(getNeighbours(pos));
		}

		this.opponentThreatPositions = neighbours;
	}

	private List<Position> getNeighbours(Position position) {
		List<Position> neighbours = new LinkedList<Position>();

		int x = position.getX();
		int y = position.getY();

		neighbours.add(new Position(x - 1, y));
		neighbours.add(new Position(x + 1, y));
		neighbours.add(new Position(x, y - 1));
		neighbours.add(new Position(x, y + 1));

		return neighbours;
	}

	private List<AStarNode> getNeighbours(AStarNode current) {
		List<AStarNode> neighbours = new LinkedList<AStarNode>();
		int x = current.getX();
		int y = current.getY();

		if (x - 1 >= 0) {
			addNeighbour(new Position(x - 1, y), current, neighbours);
		}
		if (x + 1 < state.getNoColumns()) {
			addNeighbour(new Position(x + 1, y), current, neighbours);
		}
		if (y - 1 >= 0) {
			addNeighbour(new Position(x, y - 1), current, neighbours);
		}
		if (y + 1 < state.getNoRows()) {
			addNeighbour(new Position(x, y + 1), current, neighbours);
		}
		return neighbours;
	}

	private void addNeighbour(Position position, AStarNode current,
			List<AStarNode> neighbours) {
		if (state.getSquare(position).isUnoccupied()) {
			AStarNode existingNode = objectPool.get(position);
			if (existingNode == null) {
				int moveCost = 1;
				if (checkThreatPositions
						&& opponentThreatPositions.contains(position)) {
					moveCost = 1000;
					System.err.println("Found threat position: " + position);
				}
				AStarNode newNode = new AStarNode(position,
						destinationPosition, current.fixedCostG + moveCost,
						current);
				newNode.moveCost = moveCost;
				
				neighbours.add(newNode);
				objectPool.put(newNode, newNode);
			} else {
				neighbours.add(existingNode);
			}
		}
	}

	private List<Position> createPath(AStarNode current,
			List<Position> aggregator) {
		if (current.parent == null) {
			return aggregator;
		} else {
			createPath(current.parent, aggregator);
			aggregator.add(current);
			return aggregator;
		}
	}
}

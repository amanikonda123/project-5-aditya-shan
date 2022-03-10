import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AStarPathingStrategy
        implements PathingStrategy
{
    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {

        PriorityQueue<WorldNode> openList = new PriorityQueue<>(Comparator.comparingInt(WorldNode::getF));
        WorldNode startNode = new WorldNode(start, 0, calculateManhattanDistance(start, end), null);

        openList.add(startNode);

        HashSet<Point> closedList = new HashSet<>();
        closedList.add(startNode.getPosition());

        WorldNode endNode;
        List<Point> path = new ArrayList<>();

        if (withinReach.test(start, end)) {
            return path;
        }

        if (start.getX() == end.getX() && start.getY() == end.getY()) {
            return path;
        }

        while (openList.size() != 0) {
            WorldNode current = openList.remove();
            if (withinReach.test(current.getPosition(), end)) {
                endNode = current;
                while(endNode != null) {
                    path.add(0, endNode.getPosition());
                    endNode = endNode.getPrev();
                }
                path.remove(0);
                return path;
            }
            potentialNeighbors.apply(current.getPosition())
                    .filter(canPassThrough)
                    .map(n -> new WorldNode(n, current.getActualDistanceFromStart() + 1,
                            calculateManhattanDistance(n, end), current))
                    .filter(n -> !closedList.contains(n.getPosition()))
                    .forEach(n -> {
                        updateActualDistanceFromStart(openList, n);
                    });
            closedList.add(current.getPosition());
        }
        return path;
    }

    public int calculateManhattanDistance(Point p1, Point p2) {
        int xDist = Math.abs(p1.getX() - p2.getX());
        int yDist = Math.abs(p1.getY() - p2.getY());
        return xDist + yDist;
    }

    public void updateActualDistanceFromStart(PriorityQueue<WorldNode> openList, WorldNode n) {
        HashSet<WorldNode> openList2 = new HashSet<>(openList);
        // could update openList2 whenever openList gets updated instead -> would be more efficient
        if (openList.contains(n)) {
            for (WorldNode node : openList2) {
                if (node.getPosition().equals(n.getPosition()) &&
                        node.getActualDistanceFromStart() > n.getActualDistanceFromStart()) {
                    openList.remove(node);
                    openList.add(n);
                }
            }
        }
        else {
            openList.add(n);
        }
    }
}
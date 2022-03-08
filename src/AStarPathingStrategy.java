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
        PriorityQueue<WorldNode> openList =
                new PriorityQueue<>(Collections.reverseOrder(Comparator.comparingInt(WorldNode::getF)));

        WorldNode startNode = new WorldNode(start, 0,
                calculateManhattanDistance(start, end), null);
        openList.add(startNode);

        HashSet<Integer> closedList = new HashSet<>();
        WorldNode endNode = null;
        List<Point> path = new ArrayList<>();
        while (!openList.isEmpty()) {
            System.out.println(closedList.toString());
            WorldNode current = openList.remove();
            if (withinReach.test(current.getPosition(), end)) {
                endNode = current;
                System.out.println("goal found");
                break;
            }
            potentialNeighbors.apply(current.getPosition())
                .filter(canPassThrough)
                .map(n -> new WorldNode(n, current.getActualDistanceFromStart() + 1,
                     calculateManhattanDistance(n, end), current))
                .filter(n -> !closedList.contains(n.hashCode()))
                .forEach(n -> {
                    updateActualDistanceFromStart(openList, n);
                });
            closedList.add(current.hashCode());
        }

        //System.out.println(endNode);

        while(endNode != null) {
            path.add(0, endNode.getPosition());
            endNode = endNode.getPrev();
        }
        //System.out.println(path);
        return path;
    }

    public int calculateManhattanDistance(Point p1, Point p2) {
        int xDist = Math.abs(p1.getX() - p2.getX());
        int yDist = Math.abs(p1.getY() - p2.getY());
        return xDist + yDist;
    }

    public void updateActualDistanceFromStart(PriorityQueue<WorldNode> openList, WorldNode n) {
        if (openList.contains(n)) {
            for (WorldNode node : openList) {
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
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
        HashMap<Integer, WorldNode> openList2 = new HashMap<>();
        WorldNode startNode = new WorldNode(start, 0, calculateManhattanDistance(start, end), null);

        openList.add(startNode);
        openList2.put(startNode.hashCode(), startNode);

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
            Point CURRENT_POINT = current.getPosition();
            //openList2.remove(current.hashCode());
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
                        //closedList.add(n.getPosition());
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


//    public void updateActualDistanceFromStart2(PriorityQueue<WorldNode> openList, HashMap<Integer, WorldNode> openList2, WorldNode n) {
//        if (openList.contains(n)) {
//            WorldNode existingNode = openList2.get(n.hashCode());
//            if (existingNode.getActualDistanceFromStart() > n.getActualDistanceFromStart()) {
//                openList.remove(existingNode);
//                openList2.remove(existingNode.hashCode());
//                openList.add(n);
//                openList2.put(n.hashCode(), n);
//            }
//        }
//        else {
//            openList.add(n);
//            openList2.put(n.hashCode(), n);
//        }
//    }
}
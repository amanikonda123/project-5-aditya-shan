import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PathingTest {

    @Test
    public void testAStarNoObstacles() {
        boolean [][] grid = {
                {true, true, true},
                {true, true, true},
                {true, true, true}
        };

        Point start = new Point(0,0);
        Point end = new Point(2,2);
        PathingStrategy ps = new AStarPathingStrategy();
        List<Point> path = ps.computePath(
                start,
                end,
                p -> withinBounds(p, grid) && grid[p.getY()][p.getX()],
                Functions::adjacent,
                PathingStrategy.CARDINAL_NEIGHBORS);

        assertEquals(List.of(new Point(0, 1)), path);
    }

    public static boolean withinBounds(Point pos, boolean[][] grid) {
        return pos.getY() >= 0 && pos.getY() < grid.length && pos.getX() >= 0
                && pos.getX() < grid[0].length;
    }
}

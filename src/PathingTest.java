import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class PathingTest {

    @Test
    public void testAStarNoObstacles01() {
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

        assertTrue(isValidPath(path));
        assertEquals(List.of(new Point(0, 1), new Point(1, 1), new Point(2, 1)), path);
    }

    @Test
    public void testAStarOneObstacle01() {
        boolean [][] grid = {
                {true, false, true},
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

        assertTrue(isValidPath(path));
        assertEquals(List.of(new Point(0, 1), new Point(0, 2), new Point(1, 2)), path);
    }

    @Test
    public void testAStarOneObstacle02() {
        boolean [][] grid = {
                {true, true, true},
                {true, false, true},
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

        assertTrue(isValidPath(path));
        assertEquals(List.of(new Point(0, 1), new Point(0, 2), new Point(1, 2)), path);
    }

    @Test
    public void testAStarOneObstacle03() {
        boolean [][] grid = {
                {true, true, true},
                {true, true, true},
                {true, false, true}
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

        assertTrue(isValidPath(path));
        assertEquals(List.of(new Point(0, 1), new Point(1, 1), new Point(2, 1)), path);
    }

    @Test
    public void testAStarTwoObstacles01() {
        boolean [][] grid = {
                {true, true, true},
                {true, false, true},
                {true, false, true}
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

        assertTrue(isValidPath(path));
        assertEquals(List.of(new Point(1, 0), new Point(2, 0), new Point(2, 1)), path);
    }

    @Test
    public void testAStarTwoObstacles02() {
        boolean [][] grid = {
                {true, false, true},
                {false, true, true},
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

        assertFalse(isValidPath(path));
        assertEquals(List.of(), path);
    }

    @Test
    public void testAStarTwoObstacles03() {
        boolean [][] grid = {
                {true, true, true},
                {true, false, true},
                {true, false, true}
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

        assertTrue(isValidPath(path));
        assertEquals(List.of(new Point(1,0), new Point(2,0), new Point(2,1)), path);
    }

    @Test
    public void testAStarTwoObstacles04() {
        boolean [][] grid = {
                {true, true, true},
                {true, true, false},
                {true, false, true}
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

        assertFalse(isValidPath(path));
        assertEquals(List.of(), path);
    }

    @Test
    public void testAStarTwoObstacles05() {
        boolean [][] grid = {
                {true, false, true},
                {true, true, true},
                {true, false, true}
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

        assertTrue(isValidPath(path));
        assertEquals(List.of(new Point(0,1), new Point(1,1), new Point(2,1)), path);
    }

    @Test
    public void testAStarThreeObstacles01() {
        boolean [][] grid = {
                {true, false, true},
                {true, true, false},
                {true, false, true}
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

        assertFalse(isValidPath(path));
        assertEquals(List.of(), path);
    }

    @Test
    public void testAStarThreeObstacles02() {
        boolean [][] grid = {
                {true, false, true},
                {true, false, true},
                {true, false, true}
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

        assertFalse(isValidPath(path));
        assertEquals(List.of(), path);
    }

    @Test
    public void testAStarThreeObstacles03() {
        boolean [][] grid = {
                {true, true, false},
                {false, true, true},
                {true, false, true}
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

        assertTrue(isValidPath(path));
        assertEquals(List.of(new Point(1,0), new Point(1,1), new Point(2,1)), path);
    }

    @Test
    public void testAStarDifferentStartEnd01() {
        boolean [][] grid = {
                {true, true, true},
                {true, false, true},
                {true, true, true}
        };

        Point start = new Point(1,0);
        Point end = new Point(1,2);
        PathingStrategy ps = new AStarPathingStrategy();
        List<Point> path = ps.computePath(
                start,
                end,
                p -> withinBounds(p, grid) && grid[p.getY()][p.getX()],
                Functions::adjacent,
                PathingStrategy.CARDINAL_NEIGHBORS);

        assertTrue(isValidPath(path));
        assertEquals(List.of(new Point(0,0), new Point(0,1), new Point(0,2)), path);
    }

    @Test
    public void testAStarDifferentStartEnd02() {
        boolean [][] grid = {
                {true, true, true},
                {false, false, true},
                {true, true, true}
        };

        Point start = new Point(0,0);
        Point end = new Point(0,2);
        PathingStrategy ps = new AStarPathingStrategy();
        List<Point> path = ps.computePath(
                start,
                end,
                p -> withinBounds(p, grid) && grid[p.getY()][p.getX()],
                Functions::adjacent,
                PathingStrategy.CARDINAL_NEIGHBORS);

        // assertTrue(isValidPath(path));
        assertEquals(List.of(new Point(1,0), new Point(2,0), new Point(2,1),
                new Point(2,2), new Point(1,2)), path);
    }

    @Test
    public void testAStarDifferentStartEnd03() {
        boolean [][] grid = {
                {true, true, true},
                {true, true, true},
                {true, false, true}
        };

        Point start = new Point(1,1);
        Point end = new Point(2,2);
        PathingStrategy ps = new AStarPathingStrategy();
        List<Point> path = ps.computePath(
                start,
                end,
                p -> withinBounds(p, grid) && grid[p.getY()][p.getX()],
                Functions::adjacent,
                PathingStrategy.CARDINAL_NEIGHBORS);

        // assertTrue(isValidPath(path));
        assertEquals(List.of(new Point(2,1)), path);
    }

    @Test
    public void testAStarDifferentStartEnd4() {
        boolean [][] grid = {
                {true, true, true},
                {true, true, false},
                {true, true, true}
        };

        Point start = new Point(1,1);
        Point end = new Point(2,2);
        PathingStrategy ps = new AStarPathingStrategy();
        List<Point> path = ps.computePath(
                start,
                end,
                p -> withinBounds(p, grid) && grid[p.getY()][p.getX()],
                Functions::adjacent,
                PathingStrategy.CARDINAL_NEIGHBORS);

        // assertTrue(isValidPath(path));
        assertEquals(List.of(new Point(1,2)), path);
    }

    public static boolean withinBounds(Point pos, boolean[][] grid) {
        return pos.getY() >= 0 && pos.getY() < grid.length && pos.getX() >= 0
                && pos.getX() < grid[0].length;
    }

    public boolean isValidPath(List<Point> path) {
        if (!(path.size() == 3)) {
            return false;
        }
        for (int i = 0; i < path.size(); i++) {
            if (path.get(i).equals(path.get(0))) {
                continue;
            }
            if (!Functions.adjacent(path.get(i), path.get(i-1))) {
                return false;
            }
        }
        return true;
    }
}

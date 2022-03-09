import java.util.Objects;

public class WorldNode {

    private Point position;
    private final int actualDistanceFromStart;
    private final int estimatedDistanceToEnd;
    private final WorldNode prev;

    public WorldNode(Point position, int actualDistanceFromStart, int estimatedDistanceToEnd, WorldNode prev) {
        this.position = position;
        this.actualDistanceFromStart = actualDistanceFromStart;
        this.estimatedDistanceToEnd = estimatedDistanceToEnd;
        this.prev = prev;
    }

    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (this == other) {
            return true;
        }
        if (!(other instanceof WorldNode)) {
            return false;
        }

        WorldNode otherNode = (WorldNode) other;
        return this.position != null && this.position.equals(otherNode.getPosition());
        //this.getF() == otherNode.getF() &&
    }

    public int hashCode() {
        //return Objects.hash(position.getX(), position.getY(), estimatedDistanceToEnd);
        int result = 1;
        result = result * 37 + ((this.position == null) ? 0 : this.position.hashCode());
        result = result * 37 + actualDistanceFromStart;
        result = result * 37 + estimatedDistanceToEnd;
        result = result * 37 + ((this.prev == null) ? 0 : this.prev.hashCode());
        return result;
    }

    public Point getPosition() {
        return position;
    }

    public int getActualDistanceFromStart() {
        return actualDistanceFromStart;
    }

    public WorldNode getPrev() {
        return prev;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public int getF() {
        return this.actualDistanceFromStart + this.estimatedDistanceToEnd;
    }
}

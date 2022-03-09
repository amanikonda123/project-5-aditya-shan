import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public abstract class MovableEntity extends ExecutableEntity {

    public MovableEntity(String id,
                            Point position,
                            List<PImage> images,
                            int imageIndex,
                            int animationPeriod,
                            int actionPeriod)
    {
        super(id, position, images, imageIndex, animationPeriod, actionPeriod);
    }

    public boolean moveTo(
            WorldModel world,
            Entity target,
            EventScheduler scheduler) {
        if (Functions.adjacent(this.getPosition(), target.getPosition())) {
            return _moveToHelper(world, target, scheduler);
        } else {
            Point nextPos = this.nextPosition(world, target.getPosition());

            if (!this.getPosition().equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                occupant.ifPresent(scheduler::unscheduleAllEvents);

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }

    protected abstract boolean _moveToHelper(WorldModel world,
                                     Entity target,
                                     EventScheduler scheduler);

    public Point nextPosition(
            WorldModel world, Point destPos)
    {
//        int horiz = Integer.signum(destPos.getX() - this.getPosition().getX());
//        Point newPos = new Point(this.getPosition().getX() + horiz, this.getPosition().getY());
//
//        if (_nextPositionHelper(world, newPos, horiz)) {
//            int vert = Integer.signum(destPos.getY() - this.getPosition().getY());
//            newPos = new Point(this.getPosition().getX(), this.getPosition().getY() + vert);
//
//            if (_nextPositionHelper(world, newPos, vert)) {
//                newPos = this.getPosition();
//            }
//        }
//        return newPos;

        PathingStrategy ps = new AStarPathingStrategy();
        List<Point> path = ps.computePath(this.getPosition(),
                destPos,
                p -> world.withinBounds(p) && !world.isOccupied(p),
                Functions::adjacent,
                PathingStrategy.CARDINAL_NEIGHBORS);

        if (path.size() > 0) {
            return path.get(0);
        }
        return this.getPosition();
    }

    //protected abstract boolean _nextPositionHelper(WorldModel world, Point newPos, int dimension);
}

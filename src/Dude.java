import processing.core.PImage;

import java.awt.*;
import java.util.List;

public abstract class Dude extends MovableEntity implements TransformableEntity {

    private final int resourceLimit;

    public Dude(String id,
                 Point position,
                 List<PImage> images,
                 int imageIndex,
                 int animationPeriod,
                 int actionPeriod,
                int resourceLimit)
    {
        super(id, position, images, imageIndex, animationPeriod, actionPeriod);
        this.resourceLimit = resourceLimit;
    }

//    protected boolean _nextPositionHelper(WorldModel world, Point newPos, int dimension) {
//        return dimension == 0 || world.isOccupied(newPos) && world.getOccupancyCell(newPos).getClass() != Stump.class;
//    }

    public boolean transform(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore) {
        Entity dude = _transformHelper(imageStore);
        if (dude != null) {
            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(dude);
            ((Dude) dude).scheduleActions(scheduler, world, imageStore);
            return true;
        }

        return false;
    }

    protected abstract Entity _transformHelper(ImageStore imageStore);

    protected int getResourceLimit() {
        return this.resourceLimit;
    }
}
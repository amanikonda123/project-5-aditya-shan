import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Fairy extends MovableEntity {

    public Fairy(
            String id,
            Point position,
            List<PImage> images,
            int imageIndex,
            int animationPeriod,
            int actionPeriod)
    {
        super(id, position, images, imageIndex, animationPeriod, actionPeriod);
    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> fairyTarget =
                world.findNearest(this.getPosition(), new ArrayList<>(Arrays.asList(Stump.class)));

        if (fairyTarget.isPresent()) {
            Point tgtPos = fairyTarget.get().getPosition();

            if (this.moveTo(world, fairyTarget.get(), scheduler)) {
                Entity sapling = Functions.createSapling("sapling_" + this.getId(), tgtPos,
                        imageStore.getImageList(Functions.SAPLING_KEY));

                world.addEntity(sapling);
                ((Sapling) sapling).scheduleActions(scheduler, world, imageStore);
            }
        }

        super.executeActivity(world, imageStore, scheduler);
    }

    protected boolean _moveToHelper(WorldModel world,
                                             Entity target,
                                             EventScheduler scheduler)
    {
        world.removeEntity(target);
        scheduler.unscheduleAllEvents(target);
        return true;
    }

//    protected boolean _nextPositionHelper(WorldModel world, Point newPos, int dimension) {
//        return dimension == 0 || world.isOccupied(newPos);
//    }
}

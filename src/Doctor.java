import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Doctor extends MovableEntity {

    public Doctor(String id, Point position, List<PImage> images, int imageIndex, int animationPeriod, int actionPeriod) {
        super(id, position, images, imageIndex, animationPeriod, actionPeriod);
    }

    @Override
    protected boolean _moveToHelper(WorldModel world, Entity target, EventScheduler scheduler) {
        return false;
    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> target =
                world.findNearest(this.getPosition(), new ArrayList<>(Arrays.asList(PoisonedDude.class)));

        if (target.isPresent()){
            Point tgtPos = target.get().getPosition();

            this.moveTo(world, target.get(), scheduler);
            //((PoisonedDude)target.get()).transformIll(world, imageStore, tgtPos, scheduler);


        }
        super.executeActivity(world, imageStore, scheduler);
    }

}

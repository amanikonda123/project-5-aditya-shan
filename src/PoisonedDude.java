import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class PoisonedDude extends Dude {

    public PoisonedDude(
            String id,
            Point position,
            List<PImage> images,
            int imageIndex,
            int animationPeriod,
            int actionPeriod)
    {
        super(id, position, images, imageIndex, animationPeriod, actionPeriod, 0);
    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> target =
                world.findNearest(this.getPosition(), new ArrayList<>(Arrays.asList(Fairy.class)));

        if (target.isPresent() && this.moveTo(world,
                target.get(), scheduler))
        {
            this.transform(world, scheduler, imageStore);
            world.removeEntity(target.get());
        }
        else {
            if (!target.isPresent()) {
                Optional<Entity> house =
                        world.findNearest(this.getPosition(), new ArrayList<>(Arrays.asList(Hospital.class)));
                this.moveTo(world, house.get(), scheduler);
            }
            super.executeActivity(world, imageStore, scheduler);
        }
    }

    protected boolean _moveToHelper(WorldModel world,
                                    Entity target,
                                    EventScheduler scheduler)
    {
        if (target instanceof Hospital){
            return true;
        }else {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
    }

    protected Entity _transformHelper(ImageStore imageStore) {
        return new PoisonedDude(this.getId(),
                this.getPosition(),
                imageStore.getImageList(Functions.POISONED_DUDE_KEY),
                0,
                Functions.POISONED_DUDE_ANIMATION_PERIOD,
                Functions.POISONED_DUDE_ACTION_PERIOD);
    }
}
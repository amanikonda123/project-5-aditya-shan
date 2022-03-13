import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DudeFull extends Dude {

    public DudeFull(
            String id,
            Point position,
            List<PImage> images,
            int imageIndex,
            int animationPeriod,
            int actionPeriod,
            int resourceLimit)
    {
        super(id, position, images, imageIndex, animationPeriod, actionPeriod, resourceLimit);
    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> fullTarget =
                world.findNearest(this.getPosition(), new ArrayList<>(Arrays.asList(House.class)));

        if (fullTarget.isPresent() && this.moveTo(world,
                fullTarget.get(), scheduler))
        {
            this.transform(world, scheduler, imageStore);
        }
        else {
            super.executeActivity(world, imageStore, scheduler);
        }
    }

    protected boolean _moveToHelper(WorldModel world,
                                    Entity target,
                                    EventScheduler scheduler)
    {
        return true;
    }

    protected Entity _transformHelper(ImageStore imageStore) {
        return Functions.createDudeNotFull(this.getId(),
                this.getPosition(), this.getActionPeriod(),
                this.getAnimationPeriod(),
                this.getResourceLimit(),
                this.getImages());
    }


}
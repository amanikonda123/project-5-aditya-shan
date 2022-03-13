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
//        return Functions.createDudeNotFull(this.getId(),
//                this.getPosition(), this.getActionPeriod(),
//                this.getAnimationPeriod(),
//                this.getResourceLimit(),
//                this.getImages());

        return new PoisonedDude("poisoned_dude_" + this.getId(),
                this.getPosition(),
                imageStore.getImageList("poisoned_dude"),
                0,
                4,
                4);
    }

    public void transformIll(WorldModel world, ImageStore imageStore, Point pos, EventScheduler scheduler){
        world.removeEntity(this);
        DudeNotFull dudeNotFull = new DudeNotFull(this.getId(), pos,
                imageStore.getImageList("dude"), 0, 5,
                6, 4, 0
        );
        world.addEntity(dudeNotFull);
        dudeNotFull.scheduleActions(scheduler, world, imageStore);
    }
}
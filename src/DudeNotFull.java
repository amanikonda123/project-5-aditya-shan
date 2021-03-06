import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DudeNotFull extends Dude {

    private int resourceCount;

    public DudeNotFull(
            String id,
            Point position,
            List<PImage> images,
            int imageIndex,
            int animationPeriod,
            int actionPeriod,
            int resourceLimit,
            int resourceCount)
    {
        super(id, position, images, imageIndex, animationPeriod, actionPeriod, resourceLimit);
        this.resourceCount = resourceCount;
    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> target =
                world.findNearest(this.getPosition(), new ArrayList<>(Arrays.asList(Tree.class, Sapling.class)));

        if (!target.isPresent() || !this.moveTo(world,
                target.get(),
                scheduler)
                || !this.transform(world, scheduler, imageStore))
        {
            super.executeActivity(world, imageStore, scheduler);
        }
    }


    protected boolean _moveToHelper(WorldModel world,
                                    Entity target,
                                    EventScheduler scheduler)
    {
        if (target instanceof Plant) {
            this.resourceCount++;
            ((Plant) target).setHealth(((Plant) target).getHealth() - 1);
        }
        return true;
    }

    protected Entity _transformHelper(ImageStore imageStore) {
        Entity dudeFull = null;
        if (this.resourceCount >= this.getResourceLimit()) {
            dudeFull = Functions.createDudeFull(this.getId(),
                    this.getPosition(), this.getActionPeriod(),
                    this.getAnimationPeriod(),
                    this.getResourceLimit(),
                    this.getImages());
        }

        return dudeFull;
    }

    /*public void transformIll(WorldModel world, ImageStore imageStore, Point pos, EventScheduler scheduler){
        world.removeEntity(this);
        DudeNotFull dudeNotFull = new DudeNotFull(this.getId(), pos,
                imageStore.getImageList("dude"), 0, 5,
                6, 4, 0
        );
        world.addEntity(dudeNotFull);
        dudeNotFull.scheduleActions(scheduler, world, imageStore);
    }*/
}

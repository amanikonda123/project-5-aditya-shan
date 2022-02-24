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


    public boolean moveTo(
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (Functions.adjacent(this.getPosition(), target.getPosition())) {
            if (target instanceof Plant) {
                this.resourceCount++;
                ((Plant) target).setHealth(((Plant) target).getHealth() - 1);
            }
            return true;
        }
        else {
            return super.moveTo(world, target, scheduler);
        }
    }

    protected Entity _transformHelper() {
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
}

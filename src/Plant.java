import processing.core.PImage;

import java.util.List;

public abstract class Plant extends ExecutableEntity implements TransformableEntity {

    private final String STUMP_KEY = "stump";
    private int health;

    public Plant(String id,
                            Point position,
                            List<PImage> images,
                            int imageIndex,
                            int animationPeriod,
                            int actionPeriod,
                            int health)
    {
        super(id, position, images, imageIndex, animationPeriod, actionPeriod);
        this.health = health;
    }

    public void executeActivity(WorldModel world,
                                ImageStore imageStore,
                                EventScheduler scheduler)
    {
        if (!this.transform(world, scheduler, imageStore))
        {
            super.executeActivity(world, imageStore, scheduler);
        }
    }

    public boolean transform(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        if (this.health <= 0) {
            Entity stump = Functions.createStump(this.getId(),
                    this.getPosition(),
                    imageStore.getImageList(STUMP_KEY));

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(stump);
            //stump.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        else {
            return _transformHelper(world, scheduler, imageStore);
        }
    }

    protected abstract boolean _transformHelper(WorldModel world,
                                             EventScheduler scheduler,
                                             ImageStore imageStore);

    protected int getHealth() {
        return this.health;
    }

    protected void setHealth(int health) {
        this.health = health;
    }
}

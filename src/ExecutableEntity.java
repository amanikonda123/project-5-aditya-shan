import processing.core.PImage;

import java.util.List;

public abstract class ExecutableEntity extends AnimatedEntity {

    private final int actionPeriod;

    public ExecutableEntity(String id,
                          Point position,
                          List<PImage> images,
                          int imageIndex,
                          int animationPeriod,
                            int actionPeriod)
    {
        super(id, position, images, imageIndex, animationPeriod);
        this.actionPeriod = actionPeriod;
    }

    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                this.createActivityAction(world, imageStore),
                this.actionPeriod);
        super.scheduleActions(scheduler, world, imageStore);
    }

    public void executeActivity(WorldModel world,
                                ImageStore imageStore,
                                EventScheduler scheduler)
    {
        scheduler.scheduleEvent(this,
                this.createActivityAction(world, imageStore),
                this.getActionPeriod());
    }

    protected int getActionPeriod() {
        return this.actionPeriod;
    }
}

import processing.core.PImage;

import java.util.List;

public class Sapling extends Plant {

    private static final int TREE_ANIMATION_MAX = 600;
    private static final int TREE_ANIMATION_MIN = 50;
    private static final int TREE_ACTION_MAX = 1400;
    private static final int TREE_ACTION_MIN = 1000;
    private static final int TREE_HEALTH_MAX = 3;
    private static final int TREE_HEALTH_MIN = 1;

    private final int healthLimit;

    public Sapling(
            String id,
            Point position,
            List<PImage> images,
            int imageIndex,
            int animationPeriod,
            int actionPeriod,
            int health,
            int healthLimit)
    {
        super(id, position, images, imageIndex, animationPeriod, actionPeriod, health);
        this.healthLimit = healthLimit;
    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        this.setHealth(this.getHealth() + 1);
        super.executeActivity(world, imageStore, scheduler);
    }

    protected boolean _transformHelper(WorldModel world,
                                    EventScheduler scheduler,
                                    ImageStore imageStore)
    {
        if (this.getHealth() >= this.healthLimit)
        {
            Entity tree = Functions.createTree("tree_" + this.getId(),
                    this.getPosition(),
                    Functions.getNumFromRange(TREE_ACTION_MAX, TREE_ACTION_MIN),
                    Functions.getNumFromRange(TREE_ANIMATION_MAX, TREE_ANIMATION_MIN),
                    Functions.getNumFromRange(TREE_HEALTH_MAX, TREE_HEALTH_MIN),
                    imageStore.getImageList(Functions.TREE_KEY));

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(tree);
            ((Tree) tree).scheduleActions(scheduler, world, imageStore);

            return true;
        }
        return false;
    }
}

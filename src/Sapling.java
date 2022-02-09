import processing.core.PImage;

import java.util.List;

public class Sapling implements Plant, TransformableEntity {

    private static final int TREE_ANIMATION_MAX = 600;
    private static final int TREE_ANIMATION_MIN = 50;
    private static final int TREE_ACTION_MAX = 1400;
    private static final int TREE_ACTION_MIN = 1000;
    private static final int TREE_HEALTH_MAX = 3;
    private static final int TREE_HEALTH_MIN = 1;

    private String id;
    private Point position;
    private int actionPeriod;
    private int animationPeriod;
    private int health;
    private int healthLimit;
    private List<PImage> images;
    private int imageIndex;

    public Sapling(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            int health,
            int healthLimit,
            List<PImage> images,
            int imageIndex)
    {
        this.id = id;
        this.position = position;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
        this.health = health;
        this.healthLimit = healthLimit;
        this.images = images;
        this.imageIndex = imageIndex;
    }

    public PImage getCurrentImage() {
        return this.images.get(this.imageIndex);
    }

    public String getId() {
        return this.id;
    }

    public Point getPosition() {
        return this.position;
    }

    public void setPosition(Point p) {
        this.position = p;
    }

    public int getAnimationPeriod() {
        return this.animationPeriod;
    }

    public void nextImage() {
        this.imageIndex = (this.imageIndex + 1) % this.images.size();
    }

    public Action createAnimationAction(int repeatCount) {
        return new Animation(this, repeatCount);
    }

    public Action createActivityAction(
            WorldModel world, ImageStore imageStore) {
        return new Activity(this, world, imageStore);
    }

    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                this.createActivityAction(world, imageStore),
                this.actionPeriod);
        scheduler.scheduleEvent(this,
                this.createAnimationAction(0),
                this.getAnimationPeriod());
    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        this.health++;
        if (!this.transform(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    this.createActivityAction(world, imageStore),
                    this.actionPeriod);
        }
    }

    public boolean transform(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        if (this.health <= 0) {
            Entity stump = Functions.createStump(this.id,
                    this.position,
                    imageStore.getImageList(STUMP_KEY));

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(stump);
            //stump.scheduleActions(scheduler, world, imageStore);

            return true;
        }
        else if (this.health >= this.healthLimit)
        {
            Entity tree = Functions.createTree("tree_" + this.id,
                    this.position,
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

    public int getHealth() {
        return this.health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}

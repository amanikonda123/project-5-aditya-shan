import processing.core.PImage;

import java.util.List;

public class Tree implements Plant, TransformableEntity {

    private String id;
    private Point position;
    private int actionPeriod;
    private int animationPeriod;
    private int health;
    private List<PImage> images;
    private int imageIndex;

    public Tree(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            int health,
            List<PImage> images,
            int imageIndex)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
        this.health = health;
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
            WorldModel world, ImageStore imageStore)
    {
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

        if (!this.transform(world, scheduler, imageStore)) {

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

        return false;
    }

    public int getHealth() {
        return this.health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}

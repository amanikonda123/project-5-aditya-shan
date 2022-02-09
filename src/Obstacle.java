import processing.core.PImage;

import java.util.List;

public class Obstacle implements AnimatedEntity {

    private String id;
    private Point position;
    private int animationPeriod;
    private List<PImage> images;
    private int imageIndex;

    public Obstacle(
            String id,
            Point position,
            int animationPeriod,
            List<PImage> images,
            int imageIndex) {
        this.id = id;
        this.position = position;
        this.animationPeriod = animationPeriod;
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
        return new Action(ActionKind.ANIMATION, this, null, null,
                repeatCount);
    }

    public Action createActivityAction(
            WorldModel world, ImageStore imageStore) {
        return new Action(ActionKind.ACTIVITY, this, world, imageStore, 0);
    }

    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                this.createAnimationAction(0),
                this.getAnimationPeriod());
    }
}

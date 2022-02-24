import processing.core.PImage;

import java.util.List;

public abstract class AnimatedEntity extends Entity {

    private final int animationPeriod;

    public AnimatedEntity(String id,
                          Point position,
                          List<PImage> images,
                          int imageIndex,
                          int animationPeriod)
    {
        super(id, position, images, imageIndex);
        this.animationPeriod = animationPeriod;
    }

    protected int getAnimationPeriod() {
        return this.animationPeriod;
    }

    public void nextImage() {
        setImageIndex((getImageIndex() + 1) % getImages().size());
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
                this.createAnimationAction(0),
                this.getAnimationPeriod());
    }

}

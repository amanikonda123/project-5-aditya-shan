public interface AnimatedEntity extends Entity {
    int getAnimationPeriod();
    void nextImage();
    Action createAnimationAction(int repeatCount);
    Action createActivityAction(WorldModel world, ImageStore imageStore);
    void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore);

}

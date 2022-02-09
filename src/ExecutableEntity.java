public interface ExecutableEntity extends AnimatedEntity {
    void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);
}

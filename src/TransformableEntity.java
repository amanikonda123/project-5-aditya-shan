public interface TransformableEntity extends Entity {
    boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore);
}

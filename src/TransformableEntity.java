public interface TransformableEntity {
    boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore);
}
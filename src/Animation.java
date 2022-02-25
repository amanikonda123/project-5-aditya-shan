public class Animation implements Action {
    private final Entity entity;
    private final int repeatCount;

    public Animation(Entity entity, int repeatCount) {
        this.entity = entity;
        this.repeatCount = repeatCount;
    }

    public void executeAction(
            EventScheduler scheduler) {

        if (this.entity instanceof AnimatedEntity) {
            AnimatedEntity entity = (AnimatedEntity) this.entity;
            entity.nextImage();

            if (this.repeatCount != 1) {
                scheduler.scheduleEvent(this.entity,
                        (entity.createAnimationAction(Math.max(this.repeatCount - 1, 0))),
                        (entity.getAnimationPeriod()));
            }
        }
    }
}
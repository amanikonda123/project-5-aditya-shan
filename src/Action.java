/**
 * An action that can be taken by an entity
 */
public final class Action
{
    private ActionKind kind;
    private Entity entity;
    private WorldModel world;
    private ImageStore imageStore;
    private int repeatCount;

    public Action(
            ActionKind kind,
            Entity entity,
            WorldModel world,
            ImageStore imageStore,
            int repeatCount)
    {
        this.kind = kind;
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = repeatCount;
    }

    public void executeAction(EventScheduler scheduler) {
        switch (this.kind) {
            case ACTIVITY:
                executeActivityAction(scheduler);
                break;

            case ANIMATION:
                executeAnimationAction(scheduler);
                break;
        }
    }

    public void executeAnimationAction(
            EventScheduler scheduler)
    {
        ((AnimatedEntity) this.entity).nextImage();

        if (this.repeatCount != 1) {
            scheduler.scheduleEvent(this.entity,
                    ((AnimatedEntity) this.entity).createAnimationAction(
                            Math.max(this.repeatCount - 1,
                                    0)),
                    ((AnimatedEntity) this.entity).getAnimationPeriod());
        }
    }

    public void executeActivityAction(
            EventScheduler scheduler)
    {
        if (Sapling.class.equals(this.entity.getClass())) {
            ((Sapling) this.entity).executeActivity(this.world,
                    this.imageStore, scheduler);
        } else if (Tree.class.equals(this.entity.getClass())) {
            ((Tree) this.entity).executeActivity(this.world,
                    this.imageStore, scheduler);
        } else if (Fairy.class.equals(this.entity.getClass())) {
            ((Fairy) this.entity).executeActivity(this.world,
                    this.imageStore, scheduler);
        } else if (DudeNotFull.class.equals(this.entity.getClass())) {
            ((DudeNotFull) this.entity).executeActivity(this.world,
                    this.imageStore, scheduler);
        } else if (DudeFull.class.equals(this.entity.getClass())) {
            ((DudeFull) this.entity).executeActivity(this.world,
                    this.imageStore, scheduler);
        } else {
            throw new UnsupportedOperationException(String.format(
                    "executeActivityAction not supported for %s",
                    this.entity.getClass()));
        }
    }

}

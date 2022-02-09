public class Activity implements Action {
    private Entity entity;
    private WorldModel world;
    private ImageStore imageStore;

    public Activity(Entity entity, WorldModel world, ImageStore imageStore) {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
    }

    public void executeAction(
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

import java.util.*;

import processing.core.PImage;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public final class Entity
{
    public EntityKind kind;
    public String id;
    public Point position;
    public List<PImage> images;
    public int imageIndex;
    public int resourceLimit;
    public int resourceCount;
    public int actionPeriod;
    public int animationPeriod;
    public int health;
    public int healthLimit;

    public Entity(
            EntityKind kind,
            String id,
            Point position,
            List<PImage> images,
            int resourceLimit,
            int resourceCount,
            int actionPeriod,
            int animationPeriod,
            int health,
            int healthLimit)
    {
        this.kind = kind;
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
        this.health = health;
        this.healthLimit = healthLimit;
    }

    public int getAnimationPeriod() {
        switch (this.kind) {
            case DUDE_FULL:
            case DUDE_NOT_FULL:
            case OBSTACLE:
            case FAIRY:
            case SAPLING:
            case TREE:
                return this.animationPeriod;
            default:
                throw new UnsupportedOperationException(
                        String.format("getAnimationPeriod not supported for %s",
                                this.kind));
        }
    }

    public void nextImage() {
        this.imageIndex = (this.imageIndex + 1) % this.images.size();
    }

    public PImage getCurrentImage() {
        return this.images.get(this.imageIndex);
    }

    public void executeSaplingActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        this.health++;
        if (!Functions.transformPlant(this, world, scheduler, imageStore))
        {
            Functions.scheduleEvent(scheduler, this,
                    Functions.createActivityAction(this, world, imageStore),
                    this.actionPeriod);
        }
    }

    public void executeTreeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {

        if (!Functions.transformPlant(this, world, scheduler, imageStore)) {

            Functions.scheduleEvent(scheduler, this,
                    Functions.createActivityAction(this, world, imageStore),
                    this.actionPeriod);
        }
    }

    public void executeFairyActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> fairyTarget =
                Functions.findNearest(world, this.position, new ArrayList<>(Arrays.asList(EntityKind.STUMP)));

        if (fairyTarget.isPresent()) {
            Point tgtPos = fairyTarget.get().position;

            if (Functions.moveToFairy(this, world, fairyTarget.get(), scheduler)) {
                Entity sapling = Functions.createSapling("sapling_" + this.id, tgtPos,
                        Functions.getImageList(imageStore, Functions.SAPLING_KEY));

                Functions.addEntity(world, sapling);
                sapling.scheduleActions(scheduler, world, imageStore);
            }
        }

        Functions.scheduleEvent(scheduler, this,
                Functions.createActivityAction(this, world, imageStore),
                this.actionPeriod);
    }

    public void executeDudeNotFullActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> target =
                Functions.findNearest(world, this.position, new ArrayList<>(Arrays.asList(EntityKind.TREE, EntityKind.SAPLING)));

        if (!target.isPresent() || !Functions.moveToNotFull(this, world,
                target.get(),
                scheduler)
                || !Functions.transformNotFull(this, world, scheduler, imageStore))
        {
            Functions.scheduleEvent(scheduler, this,
                    Functions.createActivityAction(this, world, imageStore),
                    this.actionPeriod);
        }
    }

    public void executeDudeFullActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> fullTarget =
                Functions.findNearest(world, this.position, new ArrayList<>(Arrays.asList(EntityKind.HOUSE)));

        if (fullTarget.isPresent() && Functions.moveToFull(this, world,
                fullTarget.get(), scheduler))
        {
            Functions.transformFull(this, world, scheduler, imageStore);
        }
        else {
            Functions.scheduleEvent(scheduler, this,
                    Functions.createActivityAction(this, world, imageStore),
                    this.actionPeriod);
        }
    }

    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore)
    {
        switch (this.kind) {
            case DUDE_FULL:
                Functions.scheduleEvent(scheduler, this,
                        Functions.createActivityAction(this, world, imageStore),
                        this.actionPeriod);
                Functions.scheduleEvent(scheduler, this,
                        Functions.createAnimationAction(this, 0),
                        this.getAnimationPeriod());
                break;

            case DUDE_NOT_FULL:
                Functions.scheduleEvent(scheduler, this,
                        Functions.createActivityAction(this, world, imageStore),
                        this.actionPeriod);
                Functions.scheduleEvent(scheduler, this,
                        Functions.createAnimationAction(this, 0),
                        this.getAnimationPeriod());
                break;

            case OBSTACLE:
                Functions.scheduleEvent(scheduler, this,
                        Functions.createAnimationAction(this, 0),
                        this.getAnimationPeriod());
                break;

            case FAIRY:
                Functions.scheduleEvent(scheduler, this,
                        Functions.createActivityAction(this, world, imageStore),
                        this.actionPeriod);
                Functions.scheduleEvent(scheduler, this,
                        Functions.createAnimationAction(this, 0),
                        this.getAnimationPeriod());
                break;

            case SAPLING:
                Functions.scheduleEvent(scheduler, this,
                        Functions.createActivityAction(this, world, imageStore),
                        this.actionPeriod);
                Functions.scheduleEvent(scheduler, this,
                        Functions.createAnimationAction(this, 0),
                        this.getAnimationPeriod());
                break;

            case TREE:
                Functions.scheduleEvent(scheduler, this,
                        Functions.createActivityAction(this, world, imageStore),
                        this.actionPeriod);
                Functions.scheduleEvent(scheduler, this,
                        Functions.createAnimationAction(this, 0),
                        this.getAnimationPeriod());
                break;

            default:
        }
    }
}

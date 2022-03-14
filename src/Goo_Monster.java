import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Goo_Monster extends MovableEntity {

    public Goo_Monster(
            String id,
            Point position,
            List<PImage> images,
            int imageIndex,
            int animationPeriod,
            int actionPeriod)
    {
        super(id, position, images, imageIndex, animationPeriod, actionPeriod);
    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> monsterTarget =
                world.findNearest(this.getPosition(), new ArrayList<>(Arrays.asList(DudeFull.class, DudeNotFull.class)));

        if (monsterTarget.isPresent()) {
            Point tgtPos = monsterTarget.get().getPosition();

            if (this.moveTo(world, monsterTarget.get(), scheduler)) {
                Entity poisonedDude = new PoisonedDude("poisoned_dude_" + this.getId(),
                        tgtPos,
                        imageStore.getImageList(Functions.POISONED_DUDE_KEY),
                        0,
                        Functions.POISONED_DUDE_ANIMATION_PERIOD,
                        Functions.POISONED_DUDE_ACTION_PERIOD);

                world.addEntity(poisonedDude);
                ((Dude) poisonedDude).scheduleActions(scheduler, world, imageStore);
                world.removeEntity(this);
                scheduler.unscheduleAllEvents(this);

            }
        }

        super.executeActivity(world, imageStore, scheduler);
    }

    protected boolean _moveToHelper(WorldModel world,
                                    Entity target,
                                    EventScheduler scheduler)
    {
        world.removeEntity(target);
        scheduler.unscheduleAllEvents(target);
        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);
        return true;
    }

//    protected boolean _nextPositionHelper(WorldModel world, Point newPos, int dimension) {
//        return dimension == 0 || world.isOccupied(newPos);
//    }
}

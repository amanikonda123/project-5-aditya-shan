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

//    public void executeActivity(
//            WorldModel world,
//            ImageStore imageStore,
//            EventScheduler scheduler)
//    {
//        Optional<Entity> fullTarget =
//                world.findNearest(this.getPosition(), new ArrayList<>(Arrays.asList(DudeFull.class, DudeNotFull.class)));
//
//        if (fullTarget.isPresent() && this.moveTo(world,
//                fullTarget.get(), scheduler))
//        {
//            super.executeActivity(world, imageStore, scheduler);
//            //this.transform(world, scheduler, imageStore);
//        }
//        else {
//            super.executeActivity(world, imageStore, scheduler);
//        }
//    }

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
                        imageStore.getImageList("poisoned_dude"),
                        0,
                        300,
                        600);

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
        /*PoisonedDude poisonedDude = new PoisonedDude("poisoned_dude_" + target.getPosition().getX() + "-" + target.getPosition().getY(),
                target.getPosition(),
                this.getImages(),
                0,
                5,
                5);*/
        world.removeEntity(target);
        scheduler.unscheduleAllEvents(target);
        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);
        /*world.addEntity(poisonedDude);*/
        return true;
    }

//    protected boolean _nextPositionHelper(WorldModel world, Point newPos, int dimension) {
//        return dimension == 0 || world.isOccupied(newPos);
//    }
}

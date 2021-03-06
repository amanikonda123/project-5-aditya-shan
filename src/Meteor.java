import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Meteor extends ExecutableEntity {

    public Meteor(String id, Point position, List<PImage> images, int imageIndex, int animationPeriod, int actionPeriod) {
        super(id, position, images, imageIndex, animationPeriod, actionPeriod);
    }


    public void executeActivity (
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> monsterTarget =
                world.findNearest(this.getPosition(), new ArrayList<>(Arrays.asList(PoisonedDude.class, Goo_Monster.class)));

        if (!monsterTarget.isPresent()) {
            Goo_Monster monster = new Goo_Monster("goo_monster_" + this.getPosition().getX() + "_" + this.getPosition().getY(),
                    this.getPosition(),
                    imageStore.getImageList(VirtualWorld.GOO_MONSTER_KEY),
                    0,
                    VirtualWorld.GOO_MONSTER_ANIMATION_PERIOD,
                    VirtualWorld.GOO_MONSTER_ACTION_PERIOD);
            world.addEntity(monster);
            monster.scheduleActions(scheduler, world, imageStore);
        }

        super.executeActivity(world, imageStore, scheduler);
    }




}

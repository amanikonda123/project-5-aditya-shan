import processing.core.PImage;

import java.util.List;

public class Tree extends Plant {


    public Tree(
            String id,
            Point position,
            List<PImage> images,
            int imageIndex,
            int animationPeriod,
            int actionPeriod,
            int health)
    {
        super(id, position, images, imageIndex, animationPeriod, actionPeriod, health);
    }

    protected boolean _transformHelper(WorldModel world,
                                       EventScheduler scheduler,
                                       ImageStore imageStore)
    {
        return false;
    }
}

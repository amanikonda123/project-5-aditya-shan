import processing.core.PImage;

import java.util.List;

public class Fire extends Obstacle {

    private static final int FIRE_ANIMATION_PERIOD = 3;

    public Fire(String id, Point position, List<PImage> images, int imageIndex) {
        super(id, position, images, imageIndex, FIRE_ANIMATION_PERIOD);
        //blah
    }


}

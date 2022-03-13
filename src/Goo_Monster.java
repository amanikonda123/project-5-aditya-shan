import processing.core.PImage;

import java.util.List;

public class Goo_Monster extends AnimatedEntity {

    private static final int FIRE_ANIMATION_PERIOD = 3;

    public Goo_Monster(String id, Point position, List<PImage> images, int imageIndex) {
        super(id, position, images, imageIndex, FIRE_ANIMATION_PERIOD);
    }


}
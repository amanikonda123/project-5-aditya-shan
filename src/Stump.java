import processing.core.PImage;

import java.util.List;

public class Stump extends Entity {

    public Stump(
            String id,
            Point position,
            List<PImage> images,
            int imageIndex)
    {
        super(id, position, images, imageIndex);
    }
}


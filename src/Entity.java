import java.util.*;

import processing.core.PImage;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public abstract class Entity {
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;

    public Entity(
            String id,
            Point position,
            List<PImage> images,
            int imageIndex)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = imageIndex;
    }

    protected PImage getCurrentImage() {
        return this.images.get(this.imageIndex);
    }

    protected String getId() {
        return this.id;
    }

    protected Point getPosition() {
        return this.position;
    }

    protected void setPosition(Point p) {
        this.position = p;
    }

    protected List<PImage> getImages() {
        return this.images;
    }

    protected int getImageIndex() {
        return this.imageIndex;
    }

    protected void setImageIndex(int imageIndex) {
        this.imageIndex = imageIndex;
    }
}

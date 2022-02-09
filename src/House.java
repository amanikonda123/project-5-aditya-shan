import processing.core.PImage;

import java.util.List;

public class House implements Entity {

    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;

    public House(
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

    public PImage getCurrentImage() {
        return this.images.get(this.imageIndex);
    }

    public String getId() {
        return this.id;
    }

    public Point getPosition() {
        return this.position;
    }

    public void setPosition(Point p) {
        this.position = p;
    }


}

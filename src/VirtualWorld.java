import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.Optional;

import processing.core.*;

public final class VirtualWorld extends PApplet
{
    private static final int TIMER_ACTION_PERIOD = 100;

    private static final int VIEW_WIDTH = 640;
    private static final int VIEW_HEIGHT = 480;
    private static final int TILE_WIDTH = 32;
    private static final int TILE_HEIGHT = 32;
    private static final int WORLD_WIDTH_SCALE = 2;
    private static final int WORLD_HEIGHT_SCALE = 2;

    private static final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
    private static final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;
    private static final int WORLD_COLS = VIEW_COLS * WORLD_WIDTH_SCALE;
    private static final int WORLD_ROWS = VIEW_ROWS * WORLD_HEIGHT_SCALE;

    private static final String IMAGE_LIST_FILE_NAME = "imagelist";
    private static final String DEFAULT_IMAGE_NAME = "background_default";
    private static final int DEFAULT_IMAGE_COLOR = 0x808080;

    private static String LOAD_FILE_NAME = "world.sav";

    private static final String FAST_FLAG = "-fast";
    private static final String FASTER_FLAG = "-faster";
    private static final String FASTEST_FLAG = "-fastest";
    private static final double FAST_SCALE = 0.5;
    private static final double FASTER_SCALE = 0.25;
    private static final double FASTEST_SCALE = 0.10;

    private static double timeScale = 1.0;

    private ImageStore imageStore;
    private WorldModel world;
    private WorldView view;
    private EventScheduler scheduler;

    private long nextTime;

    public void settings() {
        size(VIEW_WIDTH, VIEW_HEIGHT);
    }

    /*
       Processing entry point for "sketch" setup.
    */
    public void setup() {
        this.imageStore = new ImageStore(
                createImageColored(TILE_WIDTH, TILE_HEIGHT,
                                   DEFAULT_IMAGE_COLOR));
        this.world = new WorldModel(WORLD_ROWS, WORLD_COLS,
                                    createDefaultBackground(imageStore));
        this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world, TILE_WIDTH,
                                  TILE_HEIGHT);
        this.scheduler = new EventScheduler(timeScale);

        loadImages(IMAGE_LIST_FILE_NAME, imageStore, this);
        loadWorld(world, LOAD_FILE_NAME, imageStore);

        scheduleActions(world, scheduler, imageStore);

        nextTime = System.currentTimeMillis() + TIMER_ACTION_PERIOD;
    }

    public void draw() {
        long time = System.currentTimeMillis();
        if (time >= nextTime) {
            this.scheduler.updateOnTime(time);
            nextTime = time + TIMER_ACTION_PERIOD;
        }

        view.drawViewport();
    }

    // Just for debugging and for P5
    // This should be refactored as appropriate
    public void mousePressed() {
        Point pressed = mouseToPoint(mouseX, mouseY);
        System.out.println("CLICK! " + pressed.getX() + ", " + pressed.getY());

        Optional<Entity> entityOptional = world.getOccupant(pressed);
        if (entityOptional.isPresent())
        {
            Entity entity = entityOptional.get();
            System.out.println(entity.getId());
            //System.out.println(entity.getId() + ": " + entity.getKind() + " : " + entity.getHealth());
        } else {
            setCrackedGroundBackground(pressed);
            //addFireEntities(pressed);
            Meteor meteor = new Meteor(
                    "meteor (" + pressed.getX() + " " + pressed.getY() + ")",
                    pressed,
                    this.imageStore.getImageList("meteor"),
                    0,
                    50
            );
            world.addEntity(meteor);
            scheduleActions(this.world, this.scheduler, this.imageStore);
        }
    }

    private void setCrackedGroundBackground(Point pressed) {

        Background cracked_bottom = new Background("cracked_ground-" + pressed.getX() + "-" + pressed.getY(),
                imageStore.getImageList("cracked-ground-bottom"));
        Background cracked_right = new Background("cracked_ground-" + pressed.getX() + "-" + pressed.getY(),
                imageStore.getImageList("cracked-ground-right"));
        Background cracked_left = new Background("cracked_ground-" + pressed.getX() + "-" + pressed.getY(),
                imageStore.getImageList("cracked-ground-left"));
        Background cracked_top = new Background("cracked_ground-" + pressed.getX() + "-" + pressed.getY(),
                imageStore.getImageList("cracked-ground-top"));

        Background cracked_lb = new Background("cracked_ground-" + pressed.getX() + "-" + pressed.getY(),
                imageStore.getImageList("cracked-ground-lb"));
        Background cracked_rb = new Background("cracked_ground-" + pressed.getX() + "-" + pressed.getY(),
                imageStore.getImageList("cracked-ground-rb"));
        Background cracked_lt = new Background("cracked_ground-" + pressed.getX() + "-" + pressed.getY(),
                imageStore.getImageList("cracked-ground-lt"));
        Background cracked_rt = new Background("cracked_ground-" + pressed.getX() + "-" + pressed.getY(),
                imageStore.getImageList("cracked-ground-rt"));
        world.setBackground(new Point(pressed.getX() + 1, pressed.getY()), cracked_right);
        world.setBackground(new Point(pressed.getX() - 1, pressed.getY()), cracked_left);
        world.setBackground(new Point(pressed.getX(), pressed.getY() + 1), cracked_bottom);
        world.setBackground(new Point(pressed.getX(), pressed.getY() - 1), cracked_top);
        //world.setBackground(new Point(pressed.getX() + 2, pressed.getY()), cracked_ground);
        //world.setBackground(new Point(pressed.getX() - 2, pressed.getY()), cracked_ground);
        //world.setBackground(new Point(pressed.getX(), pressed.getY() + 2), cracked_ground);
        //world.setBackground(new Point(pressed.getX(), pressed.getY() - 2), cracked_ground);

        world.setBackground(new Point(pressed.getX() + 1, pressed.getY()  + 1), cracked_rb);
        world.setBackground(new Point(pressed.getX() + 1, pressed.getY() - 1), cracked_rt);
        world.setBackground(new Point(pressed.getX() - 1, pressed.getY() + 1), cracked_lb);
        world.setBackground(new Point(pressed.getX() - 1, pressed.getY() - 1), cracked_lt);
    }

    private void addGooMonsterEntities(Point pressed) {
        _addGooMonsterEntitiesHelper(new Point(pressed.getX() + 1, pressed.getY() + 1));
        _addGooMonsterEntitiesHelper(new Point(pressed.getX() + 1, pressed.getY() - 1));
        _addGooMonsterEntitiesHelper(new Point(pressed.getX() - 1, pressed.getY() + 1));
        _addGooMonsterEntitiesHelper(new Point(pressed.getX() - 1, pressed.getY() - 1));
    }

    private void _addGooMonsterEntitiesHelper(Point curPos) {
        if (!world.isOccupied(curPos)) {
            world.addEntity(new Goo_Monster("goo_monster-" + curPos.getX() + "-" + curPos.getY(),
                    curPos,
                    this.imageStore.getImageList("goo_monster"),
                    0));
        }
}

    private Point mouseToPoint(int x, int y)
    {
        return view.getViewport().viewportToWorld(mouseX/TILE_WIDTH, mouseY/TILE_HEIGHT);
    }
    public void keyPressed() {
        if (key == CODED) {
            int dx = 0;
            int dy = 0;

            switch (keyCode) {
                case UP:
                    dy = -1;
                    break;
                case DOWN:
                    dy = 1;
                    break;
                case LEFT:
                    dx = -1;
                    break;
                case RIGHT:
                    dx = 1;
                    break;
            }
            view.shiftView(dx, dy);
        }
    }

    public static Background createDefaultBackground(ImageStore imageStore) {
        return new Background(DEFAULT_IMAGE_NAME,
                              imageStore.getImageList(DEFAULT_IMAGE_NAME));
    }

    public static PImage createImageColored(int width, int height, int color) {
        PImage img = new PImage(width, height, RGB);
        img.loadPixels();
        for (int i = 0; i < img.pixels.length; i++) {
            img.pixels[i] = color;
        }
        img.updatePixels();
        return img;
    }

    static void loadImages(
            String filename, ImageStore imageStore, PApplet screen)
    {
        try {
            Scanner in = new Scanner(new File(filename));
            Functions.loadImages(in, imageStore, screen);
        }
        catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void loadWorld(
            WorldModel world, String filename, ImageStore imageStore)
    {
        try {
            Scanner in = new Scanner(new File(filename));
            Functions.load(in, world, imageStore);
        }
        catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void scheduleActions(
            WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        for (Entity entity : world.getEntities()) {
            if (entity instanceof AnimatedEntity) {
                ((AnimatedEntity) entity).scheduleActions(scheduler, world, imageStore);
            }
        }
    }

    public static void parseCommandLine(String[] args) {
        if (args.length > 1)
        {
            if (args[0].equals("file"))
            {

            }
        }
        for (String arg : args) {
            switch (arg) {
                case FAST_FLAG:
                    timeScale = Math.min(FAST_SCALE, timeScale);
                    break;
                case FASTER_FLAG:
                    timeScale = Math.min(FASTER_SCALE, timeScale);
                    break;
                case FASTEST_FLAG:
                    timeScale = Math.min(FASTEST_SCALE, timeScale);
                    break;
            }
        }
    }

  public static void main(String[] args) {
        parseCommandLine(args);
        PApplet.main(VirtualWorld.class);
    }
}

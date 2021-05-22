import greenfoot.*;
import java.util.*;
import java.lang.Math;


public class Camera extends Actor
{
    public int posX;
    public int posY;
    public Color background;

    private AdvancedWorld world;
    public int worldWidth;
    public int worldHeight;
    private AdvancedActor follow;
    public float followStrength = 0.1f; //(0.001 - 1  , smaller = slower)

    public List<Line> geometry;
    
    public boolean smoothShadows = false;
    public float smoothingRadius = 5;
    public int lightRes = 1;
    public float lightFalloff = 0.8f;
    public float lightModifier = 0.1f;

    private final int THREAD_COUNT = 8;


    public Camera(AdvancedWorld world, int posx, int posy)
    {
        this.posX = posx;
        this.posY = posy;
        this.background = Color.WHITE;
        this.world = world;
        this.worldWidth = world.getWidth();
        this.worldHeight = world.getHeight();
    }

    public void centerInWorld()
    {
        setLocation(worldWidth/2, worldHeight/2);
    }

    public void act()
    {
        follow();
        renderActors();
        updateGeometry();
        renderLight();
        getWorld().repaint();
    }
    
    public void follow()
    {
        if (follow != null) {
            posX = (int)(posX + (follow.getX() - posX) * followStrength);
            posY = (int)(posY + (follow.getY() - posY) * followStrength);
        }
    }

    public void renderActors()
    {
        GreenfootImage displayed = new GreenfootImage(worldWidth, worldHeight);
        displayed.setColor(background);
        displayed.fill();

        for(Actor object : world.objectsInPaintOrder()){
            if (object == this) continue;
            //create rotated image of actors image
            GreenfootImage objectsImage = object.getImage();
            int diagonal = (int)Math.hypot(objectsImage.getWidth(), objectsImage.getHeight());
            int objX = object.getX(), objY = object.getY();
            objX -= posX; objY -= posY;
            objX += worldWidth /2; objY += worldHeight/2;

            //no 'else' because even if the world is bounded the 
            //objects might still not be visible to the camera
            if(
                objX + diagonal / 2 < 0 ||
                objY + diagonal / 2 < 0 ||
                objX - diagonal / 2 >= worldWidth ||
                objY - diagonal / 2 >= worldHeight
            ) continue; //Don't render it - it's outside of vision

            GreenfootImage image = new GreenfootImage(diagonal, diagonal);
            image.drawImage(objectsImage, 
                diagonal / 2 - objectsImage.getWidth() / 2, 
                diagonal / 2 - objectsImage.getHeight() / 2);
            image.rotate((int)object.getRotation());

            //draw rotated image onto displayed image
            displayed.drawImage(image, objX - diagonal / 2, objY - diagonal / 2);

            setImage(displayed);
        }

        setImage(displayed);
    }

    public void renderLight()
    {
        List<LightSource> lightSources = getWorld().getObjects(LightSource.class);
        GreenfootImage img = getImage();
        int prevEnd = 0;
        int sectorSize = (int)(img.getWidth()/THREAD_COUNT) + 1;
        List<Thread> lightingThreads = new ArrayList<>();
        
        for (int i = 0; i < THREAD_COUNT; i++) {
            Thread thread = new Thread(new LightingThread(prevEnd, prevEnd + sectorSize, 
                img, lightSources, this));
            thread.start();
            lightingThreads.add(thread);
            prevEnd += sectorSize;
        }
        
        for (int i = 0; i < lightingThreads.size(); i++) {
            try {
                lightingThreads.get(i).join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateGeometry()
    {
        geometry = new ArrayList<>();
        List<GeometryObject> objects = world.getObjects(GeometryObject.class);
        for (int i = 0; i < objects.size(); i++) {
            List<Line> lines = objects.get(i).getLines();
            for (int j = 0; j < lines.size(); j++) {
                Line line = lines.get(j).getScreenPos(this);
                if (line.a.inBounds(world) ||
                    line.b.inBounds(world)) {
                    geometry.add(line);
                }
            }
        }
    }

    public void setFollow(AdvancedActor obj)
    {
        this.follow = obj;
    }
}

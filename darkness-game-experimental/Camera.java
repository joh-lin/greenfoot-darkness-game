import greenfoot.*;
import java.util.*;
import java.lang.Math;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.MultipleGradientPaint.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import packages.*;


public class Camera extends Actor
{
    public int posX;
    public int posY;
    public Color background;
    public boolean debugMode = false;

    private AdvancedWorld world;
    public int worldWidth;
    public int worldHeight;
    private AdvancedActor follow;
    public float followStrength = 0.1f; //(0.001 - 1  , smaller = slower)

    public List<Line> geometry;


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
        List<Thread> lightingThreads = new ArrayList<>();
        List<LightingThread> lightingThreadsRunnables = new ArrayList<>();
        GreenfootImage lightImage = new GreenfootImage(worldWidth, worldHeight);
        lightImage.setColor(Color.BLACK);
        lightImage.fill();
        BufferedImage lightImageB = lightImage.getAwtImage();
        Graphics2D lightImgG = lightImageB.createGraphics();
        lightImgG.setComposite(BlendComposite.Add);

        for (int i = 0; i < lightSources.size(); i++) {
            LightingThread lt = new LightingThread(lightSources.get(i), this);
            Thread thread = new Thread(lt);
            thread.start();
            lightingThreads.add(thread);
            lightingThreadsRunnables.add(lt);
        }
        
        for (int i = 0; i < lightingThreads.size(); i++) {
            try {
                lightingThreads.get(i).join();
                LightingThread lt = lightingThreadsRunnables.get(i);

                LightSource ls = lightSources.get(i);
                java.awt.Color[] colors = {
                    new java.awt.Color(
                        ls.color[0]*ls.intensity*0.01f, 
                        ls.color[1]*ls.intensity*0.01f, 
                        ls.color[2]*ls.intensity*0.01f),
                    java.awt.Color.BLACK};
                float[] dist = {0.0f, 1.0f};
                float radius = ls.radius*ls.intensity;
                Point2D center = new Point2D.Float(ls.getScreenX(this), ls.getScreenY(this));
                RadialGradientPaint paint = new RadialGradientPaint(center, radius, dist, colors);
                lightImgG.setPaint(paint);
                lightImgG.fillPolygon(lt.xPoints, lt.yPoints, lt.xPoints.length);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        lightImgG.dispose();
        
        BufferedImage img = getImage().getAwtImage();
        Graphics2D imgG = img.createGraphics();
        imgG.setComposite(BlendComposite.Multiply);
        if (!debugMode) imgG.drawImage(lightImageB, 0, 0, null);
    }

    public void updateGeometry()
    {
        int offset = -5000;
        geometry = new ArrayList<>();
        List<GeometryObject> objects = world.getObjects(GeometryObject.class);
        for (int i = 0; i < objects.size(); i++) {
            List<Line> lines = objects.get(i).getLines();
            for (int j = 0; j < lines.size(); j++) {
                Line line = lines.get(j).getScreenPos(this);
                if (line.a.inBounds(world, offset) ||
                    line.b.inBounds(world, offset)) {
                    geometry.add(line);
                }
            }
        }
        geometry.add( new Line(offset, offset, worldWidth-offset, offset));
        geometry.add( new Line(offset, offset, offset, worldHeight-offset));
        geometry.add( new Line(worldWidth-offset, offset, worldWidth-offset, worldHeight-offset));
        geometry.add( new Line(worldWidth-offset, worldHeight-offset, offset, worldHeight-offset));
    }

    public void setFollow(AdvancedActor obj)
    {
        this.follow = obj;
    }

    public void testMath()
    {
        int ax = 5;
        int ay = 10;
        int bx = 5;
        int by = 5;
        double angle = Math.atan2(by - ay, bx - ax);

        System.out.println("Angle: " + angle);
        System.out.println("Angle 2: " + Math.toDegrees(angle));
        System.out.println(Math.cos(angle));
        System.out.println(Math.sin(angle));
    }
}

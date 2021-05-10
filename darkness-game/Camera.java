import greenfoot.*;
import java.util.*;
import java.lang.Math;


public class Camera extends Actor
{
    private int posX;
    private int posY;
    private Color background;

    private AdvancedWorld world;
    private int worldWidth;
    private int worldHeight;

    private List<Line> geometry;
    
    private boolean smoothShadows = true;
    private float smoothingRadius = 5;
    private int lightRes = 1;
    private float lightFalloff = 0.8f;
    private float lightModifier = 0.1f;


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
        renderActors();
        updateGeometry();
        renderLight();
    }

    public void renderActors()
    {
        GreenfootImage displayed = new GreenfootImage(worldWidth, worldHeight);
        displayed.setColor(background);
        displayed.fill();

        for(Actor object : world.objectsInPaintOrder()){
            //create rotated image of actors image
            GreenfootImage objectsImage = object.getImage();
            int diagonal = (int)Math.hypot(objectsImage.getWidth(), objectsImage.getHeight());
            int objX = object.getX(), objY = object.getY();
            objX -= posX; objY -= posY;
            objX += worldWidth /2; objY += worldHeight;

            //no 'else' because even if the world is bounded the objects might still not be visible to the camera
            if(
                objX + diagonal / 2 < 0 ||
                objY + diagonal / 2 < 0 ||
                objX - diagonal / 2 >= worldWidth ||
                objY - diagonal / 2 >= worldHeight
            ) continue; //Don't render it - it's outside of vision

            GreenfootImage image = new GreenfootImage(diagonal, diagonal);
            image.drawImage(objectsImage, diagonal / 2 - objectsImage.getWidth() / 2, diagonal / 2 - objectsImage.getHeight() / 2);
            image.rotate((int)object.getRotation());

            //draw rotated image onto displayed image
            displayed.drawImage(image, objX - diagonal / 2, objY - diagonal / 2);

            setImage(displayed);
        }

        setImage(displayed);
    }

    public void renderLight()
    {
        GreenfootImage image = getImage();

        float[][] lightPos;
        if (smoothShadows) {
            lightPos = new float[][] {
                {0f, 0f}, {-0.7f, -0.7f}, {-1f, 0f}, 
                {-0.7f, 0.7f}, {0f, -1f}, {0f, 1f}, 
                {0.7f, -0.7f}, {1f, 0f}, {0.7f, 0.7f}
            };
        }
        else {
            lightPos = new float[][] {{0, 0}};
        }

        List<LightSource> lightSources = getWorld().getObjects(LightSource.class);
        // iterate over pixels
        for (int x = 0; x < image.getWidth(); x+=lightRes) {
            for (int y = 0; y < image.getHeight(); y+=lightRes) {
                float[] colorStrength = new float[] {0, 0, 0};

                // iterate over light sources
                for (int i = 0; i < lightSources.size(); i++) {
                    LightSource ls = lightSources.get(i);
                    // iterate over smoothing light sources
                    for (int p = 0; p < lightPos.length; p++) {
                        int lsx = ls.getX() + (int)(lightPos[p][0] * smoothingRadius);// - posX + worldWidth/2;
                        int lsy = ls.getY() + (int)(lightPos[p][1] * smoothingRadius);// - posY + worldHeight/2;
                        // check if light is blocked

                        if (isLightReaching(lsx, lsy, x, y)) {
                            float angle = (float)Math.toDegrees(Math.atan2(y-lsy, x-lsx)) - ls.getRotation();
                            while (angle > 180) { angle -= 360; }
                            while (angle < -180) { angle += 360; }
                            // check if light is in area of light source
                            if (angle > -ls.angle/2 && angle < ls.angle/2) {
                                float distance = (float)Math.sqrt(Math.pow(x-lsx, 2) + Math.pow(y-lsy, 2)) + 1;
                                float strength = (float)(1/Math.pow(distance, lightFalloff))*(ls.intensity/lightPos.length);
                                colorStrength[0] += strength * ls.color[0];
                                colorStrength[1] += strength * ls.color[1];
                                colorStrength[2] += strength * ls.color[2];
                            }
                        }
                    }
                }

                // actually apply the light

                Color pixelColor = image.getColorAt(x, y);
                float[] newColor = new float[3];
                newColor[0] = pixelColor.getRed() * colorStrength[0] * lightModifier;
                newColor[1] = pixelColor.getGreen() * colorStrength[1] * lightModifier;
                newColor[2] = pixelColor.getBlue() * colorStrength[2] * lightModifier;

                if (newColor[0] < 0) newColor[0] = 0;
                if (newColor[1] < 0) newColor[1] = 0;
                if (newColor[2] < 0) newColor[2] = 0;
                
                int i = 0;
                if (newColor[1] > newColor[i]) i = 1;
                if (newColor[2] > newColor[i]) i = 2;
                
                if (newColor[i] > 255) {
                    newColor[0] = newColor[0] / newColor[i] * 255;
                    newColor[1] = newColor[1] / newColor[i] * 255;
                    newColor[2] = newColor[2] / newColor[i] * 255;
                }
                
                if (newColor[0] > 255) newColor[0] = 255;
                if (newColor[1] > 255) newColor[1] = 255;
                if (newColor[2] > 255) newColor[2] = 255;

                image.setColorAt(x, y, new Color((int)newColor[0], (int)newColor[1], (int)newColor[2], pixelColor.getAlpha()));
            }
        }
    }

    private boolean isLightReaching(int lsx, int lsy, int pixelX, int pixelY)
    {
        for (int i = 0; i < geometry.size(); i++) {
            Line line = new Line(
                new Point(lsx, lsy),
                new Point(pixelX, pixelY));
            if (geometry.get(i).intersects(line)) {
                return false;
            }
        }
        return true;
    }

    public void updateGeometry()
    {
        geometry = new ArrayList<>();
        List<GeometryObject> objects = world.getObjects(GeometryObject.class);
        for (int i = 0; i < objects.size(); i++) {
            List<Line> l = objects.get(i).getLines();
            for (int j = 0; j < l.size(); j++) {
                if (l.get(j).a.inBounds(world) ||
                    l.get(j).b.inBounds(world)) {
                    geometry.add(l.get(j));
                }
            }
        }
    }
}

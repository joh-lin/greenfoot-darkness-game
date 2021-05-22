import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.lang.Math;
import java.util.*;

/**
Intersecting Lines:
https://www.geeksforgeeks.org/check-if-two-given-line-segments-intersect/
https://stackoverflow.com/questions/3838329/how-can-i-check-if-two-segments-intersect
*/



/**
 * Write a description of class FovOverlay2 here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class FovOverlay extends Actor
{
    private GreenfootImage image;
    private int lightRes = 10;   // determines how many pixels to skip (bigger = faster) (default=4)
    private int pixelSkip = 1;  // default = 1 ; grid lines = 2

    private float lightMax = 25f;
    private float distanceModifier = 1f;
    private float lightFalloff = 0.8f;

    private boolean smoothShadows = true;
    private float smoothingRadius = 5;

    private List<Point[]> lines;

    public void act() 
    {
        updateLines();
        updateLight();
        //blurLight();
    }

    public void setLightMax(float lm) { this.lightMax = lm; }
    public void setDistanceModifier(float dm) { this.distanceModifier = dm; }
    public void setLightFalloff(float lf) { this.lightFalloff = lf; }

    public void updateLines()
    {
        lines = new ArrayList<>();
        List<Square> squares = getWorld().getObjects(Square.class);
        for (int i = 0; i < squares.size(); i++) {
            List<Point[]> l = squares.get(i).getLines();
            for (int j = 0; j < l.size(); j++) {
                if (l.get(j)[0].inBounds(getWorld()) ||
                    l.get(j)[1].inBounds(getWorld())) {
                    lines.add(l.get(j));
                }
            }
        }
    }
    
    public void updateLight()
    {
        float[][] lightPos;
        if (smoothShadows) {
            lightPos = new float[][]     {{0f, 0f}, {-0.7f, -0.7f}, {-1f, 0f}, 
                                                {-0.7f, 0.7f}, {0f, -1f}, {0f, 1f}, 
                                                {0.7f, -0.7f}, {1f, 0f}, {0.7f, 0.7f}};
        }
        else {
            lightPos = new float[][] {{0, 0}};
        }
        List<LightSource> lightSources = getWorld().getObjects(LightSource.class);
        for (int x = 0; x < image.getWidth(); x+=lightRes) {
            for (int y = 0; y < image.getHeight(); y+=lightRes) {
                float strength = 0;
                for (int i = 0; i < lightSources.size(); i++) {
                    LightSource ls = lightSources.get(i);
                    if (ls.isGlobal) {
                        strength += ls.getIntensity()/5;
                    }
                    else {
                        for (int p = 0; p < lightPos.length; p++) {
                            int lsx = ls.getX() + (int)(lightPos[p][0] * smoothingRadius);
                            int lsy = ls.getY() + (int)(lightPos[p][1] * smoothingRadius);
                            if (isLightReaching(lsx, lsy, x, y)) {
                                float angle = (float)Math.toDegrees(Math.atan2(y-lsy, x-lsx)) - ls.getRotation();
                                while (angle > 180) { angle -= 360; }
                                while (angle < -180) { angle += 360; }
                                if (angle > -ls.getAngle()/2 && angle < ls.getAngle()/2) {
                                    float distance = (float)Math.sqrt(Math.pow(x-lsx, 2) + Math.pow(y-lsy, 2));
                                    strength += (1/Math.pow(distance*distanceModifier, lightFalloff))*(ls.getIntensity()/lightPos.length);
                                }
                            }
                        }
                    }
                }

                Color color;
                int alpha;
                if (strength < 0) {strength = 0;}
                if (strength < lightMax) {
                    alpha = (int)((strength/lightMax*-1+1)*255);
                    color = new Color(0, 0, 0, alpha);
                }
                else {
                    alpha = 0;
                    color = new Color(255, 255, 255, 0);
                }

                if (!image.getColorAt(x, y).equals(color)) {
                    for (int x2 = 0; x2 < lightRes; x2+=pixelSkip) {
                        for (int y2 = 0; y2 < lightRes; y2+=pixelSkip) {
                            image.setColorAt(x+x2, y+y2, color);
                        }
                    }
                }
            }
        }
    }

    private boolean ccw(Point a, Point b, Point c)
    {
        return (c.y-a.y) * (b.x-a.x) > (b.y-a.y) * (c.x-a.x);
    }

    private boolean isLightReaching(int lsx, int lsy, int pixelX, int pixelY)
    {
        for (int i = 0; i < lines.size(); i++) {
            Point a = lines.get(i)[0];
            Point b = lines.get(i)[1];
            Point c = new Point(lsx, lsy);
            Point d = new Point(pixelX, pixelY);
            if (ccw(a, c, d) != ccw(b, c, d) && ccw(a, b, c) != ccw(a, b, d)) {
                return false;
            }
        }
        return true;
    }

    public void addedToWorld(World world)
    {
        image = new GreenfootImage(world.getWidth(), world.getHeight());
        image.setColor(new Color(0, 0, 0, 255));
        image.fill();
        setImage(image);
    }
}

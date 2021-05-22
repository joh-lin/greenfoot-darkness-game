import greenfoot.*;
import java.util.*;


public class LightingThread implements Runnable
{
    private Thread t;
    private GreenfootImage image;
    private boolean smoothShadows = true;
    private float smoothingRadius = 5;
    private int lightRes = 1;
    private float lightFalloff;
    private float lightModifier;
    private List<LightSource> lightSources;
    private List<Line> geometry;
    private int sectorStart;
    private int sectorEnd;
    private Camera cam;

    public LightingThread(int sectorStart, int sectorEnd, GreenfootImage image, List<LightSource> lightSources, Camera cam)
        {
            this.cam = cam;
            this.image = image;
            this.smoothShadows = cam.smoothShadows;
            this.smoothingRadius = cam.smoothingRadius;
            this.lightRes = cam.lightRes;
            this.lightFalloff = cam.lightFalloff;
            this.lightModifier = cam.lightModifier;
            this.lightSources = lightSources;
            this.geometry = cam.geometry;
            this.sectorStart = sectorStart;
            this.sectorEnd = sectorEnd;
            if (sectorEnd>image.getWidth()) {
                this.sectorEnd = image.getWidth();
            }
        }

    public void run()
    {
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

        // iterate over pixels
        for (int x = sectorStart; x < sectorEnd; x+=lightRes) {
            for (int y = 0; y < image.getHeight(); y+=lightRes) {
                float[] colorStrength = new float[] {0, 0, 0};

                // iterate over light sources
                for (int i = 0; i < lightSources.size(); i++) {
                    LightSource ls = lightSources.get(i);
                    // iterate over smoothing light sources
                    for (int p = 0; p < lightPos.length; p++) {
                        int lsx = ls.getX() + (int)(lightPos[p][0] * smoothingRadius) - (int)cam.posX + image.getWidth()/2;
                        int lsy = ls.getY() + (int)(lightPos[p][1] * smoothingRadius) - (int)cam.posY + image.getHeight()/2;
                        // check if light is blocked

                        if (isLightReaching(lsx, lsy, x, y)) {
                            float angle = (float)Math.toDegrees(Math.atan2(y-lsy, x-lsx)) - ls.getRotation();
                            while (angle > 180) { angle -= 360; }
                            while (angle < -180) { angle += 360; }
                            // check if light is in area of light source
                            if (angle > -ls.angle/2 && angle < ls.angle/2) {
                                float distance = (float)Math.sqrt(Math.pow(x-lsx, 2) + Math.pow(y-lsy, 2)) + 1;
                                float strength = (float)(1/Math.pow(distance, lightFalloff))*(ls.intensity/lightPos.length);
                                colorStrength[0] += strength * ls.color[0] * lightModifier;
                                colorStrength[1] += strength * ls.color[1] * lightModifier;
                                colorStrength[2] += strength * ls.color[2] * lightModifier;
                            }
                        }
                    }
                }

                // write light to image
                if (colorStrength[0] < 0) colorStrength[0] = 0;
                if (colorStrength[1] < 0) colorStrength[1] = 0;
                if (colorStrength[2] < 0) colorStrength[2] = 0;

                Color pixelColor = image.getColorAt(x, y);
                float[] newColor = new float[3];
                newColor[0] = pixelColor.getRed() * colorStrength[0];
                newColor[1] = pixelColor.getGreen() * colorStrength[1];
                newColor[2] = pixelColor.getBlue() * colorStrength[2];
                
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
}
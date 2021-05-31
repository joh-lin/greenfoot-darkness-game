import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)


public class LightSource extends AdvancedActor
{
    public float intensity;
    public float[] color = {0.0f, 0.0f, 0.0f};
    public int angle = 261;
    public float radius = 10;

    public LightSource(float intensity)
    {
        this(intensity, 1, 1, 1);
    }
    
    public LightSource(float intensity, float r, float g, float b)
    {
        this.intensity = intensity;
        this.color[0] = r;
        this.color[1] = g;
        this.color[2] = b; 
        //clearImage();
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }
    
    public void setColor(float r, float g, float b) {
        this.color[0] = r;
        this.color[1] = g;
        this.color[2] = b; 
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity; 
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
    
    public void update()
    {
        MouseInfo mi = Greenfoot.getMouseInfo();
        //if (mi!=null) setLocation(mi.getX(), mi.getY());
    }
}

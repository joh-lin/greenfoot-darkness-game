import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)


public class LightSource extends AdvancedActor
{
    public float intensity;
    public float[] color = new float[3];
    public int angle;

    public LightSource(float intensity)
    {
        this(intensity, new float[] {1, 1, 1}, 362);
    }

    public LightSource(float intensity, float[] color, int angle)
    {
        this.intensity = intensity;
        this.color = color;
        this.angle = angle;
        clearImage();
    }
    
    public void act()
    {
        MouseInfo mi = Greenfoot.getMouseInfo();
        //if (mi!=null) setLocation(mi.getX(), mi.getY());
    }
}

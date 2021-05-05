import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class MouseLightSource here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MouseLightSource extends LightSource
{
    public MouseLightSource(float intensity)
    {
        super(intensity);
    }

    public void act() 
    {
        MouseInfo mi = Greenfoot.getMouseInfo();
        if (mi != null) {
            setLocation(mi.getX(), mi.getY());
        }
    }    
}

import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class DirectionalLightSource here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class DirectionalLightSource extends LightSource
{
    public DirectionalLightSource(float intensity, int direction, int angle)
    {
        super(intensity);
        this.angle = angle;
    }
}

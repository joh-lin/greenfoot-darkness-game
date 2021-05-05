import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class GlobalLightSource extends LightSource
{
    public GlobalLightSource(float intensity)
    {
        super(intensity);
        this.isGlobal = true;
    } 
}

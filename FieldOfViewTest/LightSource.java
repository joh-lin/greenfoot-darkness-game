import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class LightSource here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class LightSource extends Actor
{
    public float intensity;
    public boolean isGlobal = false;
    public int angle = 362;
    
    public LightSource(float intensity)
    {
    	this.intensity = intensity;
    }
    
    public float getIntensity() { return this.intensity; }

    public void setIntensity(float intensity) { this.intensity = intensity; }
    
    public int getAngle() { return this.angle; }
    
    public void setAngle(int angle) {this.angle = angle; }
}
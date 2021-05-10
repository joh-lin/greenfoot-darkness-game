import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Image here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Image extends Actor
{
    public void act() 
    {
        int speed = 10;
        if (Greenfoot.isKeyDown("w")) setLocation(getX(), getY()-speed);
        if (Greenfoot.isKeyDown("s")) setLocation(getX(), getY()+speed);
        if (Greenfoot.isKeyDown("a")) setLocation(getX()-speed, getY());
        if (Greenfoot.isKeyDown("d")) setLocation(getX()+speed, getY());
    }    
}

import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class FpsDisplay here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class FpsDisplay extends Actor
{
    private long lastMillis;
    private double deltaTime;

    public void act(){
        long currentMillis = System.currentTimeMillis();
        deltaTime = (currentMillis - lastMillis) / 1000.0;
        lastMillis = currentMillis;
        deltaTime %= 1;

        setImage(new GreenfootImage(Integer.toString(fps()), 20, Color.WHITE, Color.BLACK));
    }

    public int fps(){
        if(deltaTime == 0) return 2000;
        return (int)(1 / deltaTime);
    }
}

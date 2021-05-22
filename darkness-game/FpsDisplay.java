import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)


public class FpsDisplay extends Actor
{
    private long lastMillis;
    private double deltaTime;
    private float smoothing = 0.7f;
    private float fps;

    public void act(){
        long currentMillis = System.currentTimeMillis();
        deltaTime = (currentMillis - lastMillis) / 1000.0;
        lastMillis = currentMillis;
        deltaTime %= 1;

        setImage(new GreenfootImage(Integer.toString(fps()), 20, Color.WHITE, Color.BLACK));
    }

    public int fps(){
        if(deltaTime == 0) return 2000;
        fps = (float)((1 / deltaTime) * (1-smoothing)) + (fps * smoothing);
        return (int)fps;
    }
}

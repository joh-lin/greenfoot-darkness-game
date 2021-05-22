import greenfoot.*;


public class AdvancedActor extends Actor
{

    public void act()
    {
        update();
    }

    public void update() {}
    
    public void clearImage()
    {
        setImage(new GreenfootImage(1, 1));
    }
}
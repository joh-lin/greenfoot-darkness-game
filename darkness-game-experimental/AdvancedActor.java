import greenfoot.*;


public class AdvancedActor extends Actor
{
    private AdvancedActor follow;
    
    public void setFollow(AdvancedActor a)
    {
        this.follow = a;
    }

    public void act()
    {
        if (follow != null) setLocation(follow.getX(), follow.getY());
        update();
    }

    public void update() {}
    
    public void clearImage()
    {
        setImage(new GreenfootImage(1, 1));
    }

    public int getScreenX(Camera cam)
    {
        return getX() - cam.posX + cam.worldWidth/2;
    }

    public int getScreenY(Camera cam)
    {
        return getY() - cam.posY + cam.worldHeight/2;
    }
}
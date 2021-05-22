import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)


public class Player extends AdvancedActor
{
    public float moveSpeed = 10;
    public LightSource lightSource;
    
    public Player()
    {
        GreenfootImage img = new GreenfootImage(10, 10);
        img.setColor(Color.BLUE);
        img.fill();
        setImage(img);
    }
    
    public void addedToWorld(World world)
    {
        lightSource = new LightSource(100);
        world.addObject(lightSource, 0, 0);
    }
    

    public void update()
    {
        movement();
    }

    public void movement()
    {
        if (Greenfoot.isKeyDown("w")) {
            setLocation(getX(), getY() - (int)moveSpeed);
        }
        if (Greenfoot.isKeyDown("s")) {
            setLocation(getX(), getY() + (int)moveSpeed);
        }
        if (Greenfoot.isKeyDown("a")) {
            setLocation(getX() - (int)moveSpeed, getY());
        }
        if (Greenfoot.isKeyDown("d")) {
            setLocation(getX() + (int)moveSpeed, getY());
        }
        lightSource.setLocation(getX(), getY());
    }
}
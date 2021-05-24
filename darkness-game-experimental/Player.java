import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)


public class Player extends AdvancedActor
{
    public float moveSpeed = 400;
    
    public Player()
    {
        GreenfootImage img = new GreenfootImage(10, 10);
        img.setColor(Color.BLUE);
        img.fill();
        setImage(img);
    }
    
    public void addedToWorld(World world)
    {
        //LightSource lightSource = new LightSource(0.5f, 1000);
        //world.addObject(lightSource, 0, 0);
        //lightSource.setFollow(this);
        Square s = new Square(20, 20);
        world.addObject(s, 0, 0);
        s.setFollow(this);
    }
    

    public void update()
    {
        movement();
    }

    public void movement()
    {
        if (Greenfoot.isKeyDown("w")) {
            setLocation(getX(), (int)(getY() - (int)moveSpeed * ((AdvancedWorld)getWorld()).deltaTime()));
        }
        if (Greenfoot.isKeyDown("s")) {
            setLocation(getX(), (int)(getY() + (int)moveSpeed * ((AdvancedWorld)getWorld()).deltaTime()));
        }
        if (Greenfoot.isKeyDown("a")) {
            setLocation((int)(getX() - (int)moveSpeed * ((AdvancedWorld)getWorld()).deltaTime()), getY());
        }
        if (Greenfoot.isKeyDown("d")) {
            setLocation((int)(getX() + (int)moveSpeed * ((AdvancedWorld)getWorld()).deltaTime()), getY());
        }
    }
}
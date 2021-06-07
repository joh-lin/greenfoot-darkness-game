import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.lang.Math;


public class Player extends AdvancedActor
{
    public float moveSpeed = 100;
    public float rotateSpeed = 150;
    private float velX = 0;
    private float velY = 0;
    private float velRot = 0;
    private float drag = 0.8f;
    private float rotateDrag = 0.6f;
    private LightSource lightSource;
    private boolean onlyKeyboard = false;
    
    public Player()
    {
        GreenfootImage img = new GreenfootImage(10, 10);
        img.setColor(Color.BLUE);
        img.fill();
        setImage(img);
    }
    
    public void addedToWorld(World world)
    {
        lightSource = new LightSource(50);
        world.addObject(lightSource, 0, 0);
        lightSource.setFollow(this);
        lightSource.setAngle(100);
        LightSource ls2 = new LightSource(40);
        world.addObject(ls2, 0 ,0);
        ls2.setFollow(this);
    }
    

    public void update()
    {
        movement();
    }

    public void movement()
    {
        MouseInfo mi = Greenfoot.getMouseInfo();
        float deltaTime = ((AdvancedWorld)getWorld()).deltaTime();
        if (onlyKeyboard) {
            if (Greenfoot.isKeyDown("a")) {
                velRot -= rotateSpeed * deltaTime;
            }
            if (Greenfoot.isKeyDown("d")) {
                velRot += rotateSpeed * deltaTime;
            }
            if (Greenfoot.isKeyDown("w")) {
                velX += Math.cos(Math.toRadians(getRotation())) * deltaTime * moveSpeed;
                velY += Math.sin(Math.toRadians(getRotation())) * deltaTime * moveSpeed;
            }
            if (Greenfoot.isKeyDown("s")) {
                velX -= Math.cos(Math.toRadians(getRotation())) * deltaTime * moveSpeed;
                velY -= Math.sin(Math.toRadians(getRotation())) * deltaTime * moveSpeed;
            }

            // drag
            velRot *= rotateDrag;
            velX *= drag;
            velY *= drag;

            setRotation(getRotation() + (int)velRot);
            setLocation(
                getX() + (int)velX,
                getY() + (int)velY
            );
        } else {
            if (Greenfoot.isKeyDown("w")) {
                velY -= moveSpeed * deltaTime;
            }
            if (Greenfoot.isKeyDown("s")) {
                velY += moveSpeed * deltaTime;
            }
            if (Greenfoot.isKeyDown("a")) {
                velX -= moveSpeed * deltaTime;
            }
            if (Greenfoot.isKeyDown("d")) {
                velX += moveSpeed * deltaTime;
            }
            if (mi != null) {
                double a = Math.atan2(mi.getY() - getScreenY(((AdvancedWorld)getWorld()).camera), mi.getX() - getScreenX(((AdvancedWorld)getWorld()).camera));
                setRotation((int)Math.toDegrees(a));
            }

            // drag
            velRot *= rotateDrag;
            velX *= drag;
            velY *= drag;

            setRotation(getRotation() + (int)velRot);
            setLocation(
                getX() + (int)velX,
                getY() + (int)velY
            );
        }
        lightSource.setRotation(getRotation());
    }
}
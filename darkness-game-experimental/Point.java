import greenfoot.*;

public class Point
{
    public float x;
    public float y;

    public Point(float x, float y)
    {
        this.x = x;
        this.y = y;
    }
    
    /**
       @param offset smaller = can be slightly out of bounds*/
    public boolean inBounds(World world, int offset) {
        return (x > offset && y > offset && x < world.getWidth()-offset && y < world.getHeight()-offset);
    }

    public Point getScreenPos(Camera cam)
    {
        return new Point(
            this.x - cam.posX + cam.worldWidth / 2,
            this.y - cam.posY + cam.worldHeight / 2
        );
    }
}
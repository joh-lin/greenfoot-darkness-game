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

    public boolean inBounds(World world) {
        return (x > 0 && y > 0 && x < world.getWidth() && y < world.getHeight());
    }
}
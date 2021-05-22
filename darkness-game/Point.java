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

    public Point getScreenPos(Camera cam)
    {
        return new Point(
            this.x - cam.posX + cam.worldWidth / 2,
            this.y - cam.posY + cam.worldHeight / 2
        );
    }
}
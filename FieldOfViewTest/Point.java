import greenfoot.*;
/**
 * Write a description of class Point here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Point  
{
    public int x;
    public int y;

    public Point(int x, int y)
    {
    	this.x = x;
    	this.y = y;
    }

    public boolean inBounds(World world) {
    	return (x > 0 && y > 0 && x < world.getWidth() && y < world.getHeight());
    }
}

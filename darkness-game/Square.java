import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * Write a description of class Square here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Square extends GeometryObject
{
    private int width;
    private int height;

    public Square(int width, int height)
    {
        this.width = width;
        this.height = height;
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public List<Line> getLines()
    {
        List<Line> lines = new ArrayList<>();
        Line line1 = new Line(
            new Point(getX()-width/2, getY()-height/2), 
            new Point(getX()+width/2, getY()-height/2));
        Line line2 = new Line(
            new Point(getX()-width/2, getY()+height/2), 
            new Point(getX()+width/2, getY()+height/2));
        Line line3 = new Line(
            new Point(getX()-width/2, getY()+height/2), 
            new Point(getX()-width/2, getY()-height/2));
        Line line4 = new Line(
            new Point(getX()+width/2, getY()+height/2), 
            new Point(getX()+width/2, getY()-height/2));
        lines.add(line1);
        lines.add(line2);
        lines.add(line3);
        lines.add(line4);
        return lines;
    }
}

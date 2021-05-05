import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
import java.lang.Math;

/**
 * Write a description of class Square here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Square extends Actor
{
    private int width;
    private int height;
    private GreenfootImage image;

    public Square(int width, int height)
    {
        this.width = width;
        this.height = height;
        image = new GreenfootImage(Math.abs(width), Math.abs(height));
        image.setColor(new Color(0, 0, 255));
        image.fill();
        setImage(image);
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }
    
    public void setVisible(boolean visible) {
        if (visible) {
            image.setTransparency(255);
        }
        else {
            image.setTransparency(0);
        }
    }
    
    public void updateImage() {
        try {
            image = new GreenfootImage(Math.abs(width), Math.abs(height));
            image.setColor(new Color(255, 0, 0));
            image.fill();
            setImage(image);
        }
        catch (IllegalArgumentException e) {}
    }

    public List<Point[]> getLines()
    {
        List<Point[]> lines = new ArrayList<>();
        Point[] line1 = new Point[] {
            new Point(getX()-width/2, getY()-height/2), 
            new Point(getX()+width/2, getY()-height/2)};
        Point[] line2 = new Point[] {
            new Point(getX()-width/2, getY()+height/2), 
            new Point(getX()+width/2, getY()+height/2)};
        Point[] line3 = new Point[] {
            new Point(getX()-width/2, getY()+height/2), 
            new Point(getX()-width/2, getY()-height/2)};
        Point[] line4 = new Point[] {
            new Point(getX()+width/2, getY()+height/2), 
            new Point(getX()+width/2, getY()-height/2)};
        lines.add(line1);
        lines.add(line2);
        lines.add(line3);
        lines.add(line4);
        return lines;
    }
}

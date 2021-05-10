import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * Write a description of class MyWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MyWorld extends World
{
    private int[] dragStart;
    private boolean dragging = false;
    private Square activeSquare;

    public MyWorld()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(1200,800, 1);
        setPaintOrder(FpsDisplay.class, Square.class, FovOverlay.class);
        addObject(new FovOverlay(), getWidth()/2, getHeight()/2);
        //addObject(new Image(), getWidth()/2, getHeight()/2);
        GreenfootImage img = new GreenfootImage(1, 1);
        img.setColor(Color.BLACK);
        img.fill();
        //setBackground(img);
        prepare();
        FpsDisplay fpsDisplay = new FpsDisplay();
        addObject(fpsDisplay, 20, 20);
    }

    public void act()
    {
        if (Greenfoot.mousePressed(null)) {
            MouseInfo mi = Greenfoot.getMouseInfo();
            dragStart = new int[] {mi.getX(), mi.getY()};
            dragging = true;
            activeSquare = new Square(1, 1);
            activeSquare.setVisible(true);
            addObject(activeSquare, mi.getX(), mi.getY());
        }
        else if (Greenfoot.mouseClicked(null)) {
            dragging = false;
            activeSquare.setVisible(false);
        }
        if (dragging) {
            MouseInfo mi = Greenfoot.getMouseInfo();
            int[] mousePos = new int[] {mi.getX(), mi.getY()};
            int[] distance = new int[] {mousePos[0]-dragStart[0], mousePos[1]-dragStart[1]};
            activeSquare.setLocation(dragStart[0]+distance[0]/2, dragStart[1]+distance[1]/2);
            activeSquare.setSize(distance[0], distance[1]);
            activeSquare.updateImage();
        }
    }

    /**
     * Prepare the world for the start of the program.
     * That is: create the initial objects and add them to the world.
     */
    private void prepare()
    {
        MouseLightSource mouseLightSource1 = new MouseLightSource(200);
        addObject(mouseLightSource1, 0, 0);
    }
}

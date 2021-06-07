import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)


public class LevelWorld extends AdvancedWorld
{
    enum Level {
        TestingLevel
    }

    public Level level;
    
    public LevelWorld()
    {
        this(Level.TestingLevel);
    }

    public LevelWorld(Level lvl)
    {
        this.level = lvl;
        loadLevel(lvl);
    }

    public LevelWorld(String lvl)
    {
        this(Level.valueOf(lvl));
    }

    private void loadLevel(Level lvl)
    {
        int cx = getWidth()/2;
        int cy = getHeight()/2;

        switch (lvl) {
            case TestingLevel:
                Player p = new Player();
                addObject(p, cx, cy);
                camera.setFollow(p);

                // Geometry
                addObject(new Square(10, 400), cx-200, cy); // starting box
                addObject(new Square(10, 400), cx+200, cy);
                addObject(new Square(390, 10), cx, cy+195);
                addObject(new Square(140, 10), cx-130, cy-195);
                addObject(new Square(140, 10), cx+130, cy-195);
                addObject(new Square(10, 120), cx-130, cy-340); // outside walls
                addObject(new Square(10, 120), cx+130, cy-340);
                addObject(new Square(10, 60), cx, cy-500);
                addObject(new Square(140, 10), cx, cy-535);
                for (int y = 0; y < 10; y++) {
                    addObject(new Square(40, 10), cx-300, cy-290-y*35);
                    addObject(new Square(40, 10), cx+300, cy-290-y*35);
                }
                for (int x = 0; x < 36; x++) {
                    addObject(new Square(5, 30), cx+(x-18)*15, cy-620);
                }
                break;
        }
    }
}

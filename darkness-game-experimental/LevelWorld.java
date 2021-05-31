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
                break;
        }
    }
}

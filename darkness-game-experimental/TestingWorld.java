import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class TestingWorld extends AdvancedWorld
{
    public TestingWorld()
    {
        super(800, 600);
        prepare();
    }

    public void prepare()
    {
        Player player = new Player();
        addObject(player, getWidth()/2, getHeight()/2);
        camera.setFollow(player);
        
        
        int cx = getWidth()/2;
        int cy = getHeight()/2;
        Square square1 = new Square(20, 20);
        Square square2 = new Square(20, 20);
        Square square3 = new Square(20, 20);
        addObject(square1, cx, cy-120);
        addObject(square2, cx-120, cy+110);
        addObject(square3, cx+120, cy+110);
        LightSource ls1 = new LightSource(80f, 1, 0, 0);
        LightSource ls2 = new LightSource(80f, 0, 1, 0);
        LightSource ls3 = new LightSource(80f, 0, 0, 1);
        addObject(ls1, cx+120, cy-110);
        addObject(ls2, cx-120, cy-110);
        addObject(ls3, cx, cy+120);
        Square square4 = new Square(600, 40);
        addObject(square4, cx, cy-580);
        Square square5 = new Square(600, 40);
        addObject(square5, cx, cy-1220);
        Square square6 = new Square(40, 600);
        addObject(square6, cx-280, cy-900);
        Square square7 = new Square(40, 600);
        addObject(square7, cx+280, cy-900);
        Square squar8 = new Square(250, 40);
        addObject(squar8, cx+150, cy-800);
        Square square9 = new Square(250, 40);
        addObject(square9, cx-150, cy-1000);
        Square square10 = new Square(40, 40);
        addObject(square10, cx-120, cy-800);
        Square square11 = new Square(40, 40);
        addObject(square11, cx+120, cy-1000);
        LightSource ls4 = new LightSource(30, 1, 0, 0);
        addObject(ls4, cx+250, cy-1190);
        LightSource ls5 = new LightSource(30, 1, 0, 0);
        addObject(ls5, cx-250, cy-600);
        LightSource ls6 = new LightSource(50);
        addObject(ls6, cx, cy + 700);
    }
}

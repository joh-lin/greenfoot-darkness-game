import greenfoot.*;
import java.util.*;


public class AdvancedWorld extends World
{
    // Camera
    private Camera camera;
    private FpsDisplay fpsDisplay;

    ArrayList<Class> paintOrder = new ArrayList<>();


    public AdvancedWorld()
    {
        this(800, 600);
    }
    
    public AdvancedWorld(int width, int height)
    {    
        super(width, height, 1, false);
        camera = new Camera(this, 0, 0);
        addObject(camera, 0, 0);
        camera.centerInWorld();
        super.setPaintOrder(FpsDisplay.class, Camera.class);
        fpsDisplay = new FpsDisplay();
        fpsDisplay.visible = true;
        addObject(fpsDisplay, 50, 50);

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
        LightSource ls1 = new LightSource(50f, 1, 0, 0);
        LightSource ls2 = new LightSource(50f, 0, 1, 0);
        LightSource ls3 = new LightSource(50f, 0, 0, 1);
        addObject(ls1, cx+120, cy-110);
        addObject(ls2, cx-120, cy-110);
        addObject(ls3, cx, cy+120);
    }
    
    public void setPaintOrder(Class... classes){
        paintOrder = new ArrayList<Class>();
        for(Class cls : classes) paintOrder.add(cls);
    }

    public List<Actor> objectsInPaintOrder(){
        ArrayList<Actor> ordered = new ArrayList<>();

        //Add non-listed class objects
        for(Actor current : getObjects(AdvancedActor.class)){
            if(paintOrder.contains(current.getClass())) continue;
            ordered.add(current);
        }

        //Add listed class objects in reverse to paint the highest last
        for(int i=paintOrder.size()-1; i>=0; i--){
            objectLoop:
            for(Actor current : getObjects(AdvancedActor.class)) {
                if(current.getClass() != paintOrder.get(i)) continue objectLoop;
                ordered.add(current);
            }
        }
        
        return ordered;
    }
    
    public float deltaTime()
    {
        return (float)fpsDisplay.deltaTime;
    }
}

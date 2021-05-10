import greenfoot.*;
import java.util.*;


public class AdvancedWorld extends World
{
    // Camera
    private Camera camera;

    ArrayList<Class> paintOrder = new ArrayList<>();


    public AdvancedWorld()
    {
        this(1000, 800);
    }
    
    public AdvancedWorld(int width, int height)
    {    
        super(width, height, 1, false);
        camera = new Camera(this, 0, 0);
        addObject(camera, 0, 0);
        camera.centerInWorld();
        super.setPaintOrder(Camera.class);
    }
    
    public void setPaintOrder(Class... classes){
        paintOrder = new ArrayList<Class>();
        for(Class cls : classes) paintOrder.add(cls);
    }

    public List<Actor> objectsInPaintOrder(){
        ArrayList<Actor> ordered = new ArrayList<>();

        //Add non-listed class objects
        for(Actor current : getObjects(Actor.class)){
            if(paintOrder.contains(current.getClass())) continue;
            ordered.add(current);
        }

        //Add listed class objects in reverse to paint the highest last
        for(int i=paintOrder.size()-1; i>=0; i--){
            objectLoop:
            for(Actor current : getObjects(Actor.class)) {
                if(current.getClass() != paintOrder.get(i)) continue objectLoop;
                ordered.add(current);
            }
        }
        
        return ordered;
    }
}

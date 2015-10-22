import java.util.Queue;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.awt.*;

/**
 * This class stores and updates all of the Platforms in the game.
 */
public class PlatformAnimator
{
    public int last_time; // for animation maths.
    public Queue<Platform> obj; // Objects to keep track of
    
    public PlatformAnimator()
    {
        //just create an empty array.
        obj = new ArrayDeque<Platform> ();//defaults to a capacity of 16
    }
    
    //Paints all animated platforms to a graphics object
    public void Paint(Graphics g, int time)
    {
        //Get iterator over objects
        Iterator<Platform> it = obj.iterator();
        
        //get time since last update
        int delta = time - last_time;
        
        //Paint and update simultaneously
        Color prev_col = g.getColor(); //get color so as not to disrupt other paint processes
        while (it.hasNext())
            it.next().Paint(g, delta); // paint all platforms wherever they are
        g.setColor(prev_col);//restore color
        
        //for next iteration
        last_time = time;
    }
    
    //Run this to clean up platforms
    public void ClearSkies() 
    {
        //delete all platforms that have left the screen, only from the start of the queue
        while (obj.peek() != null && obj.element().IsFinished())
            obj.remove();
    }
    
    //Gets platforms from queue, this is used by player to test collisions
    public Iterator<Platform> getPlatforms ()
    {
        return obj.iterator();//just use queue's iterator directly
    }
    
    //Adds an object to the animation queue
    public void BeginAnimation (Platform take)
    {
        obj.add(take);
    }
}

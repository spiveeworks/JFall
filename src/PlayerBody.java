import java.awt.Graphics;
import java.awt.Color;
import java.util.Iterator;

/**
 * This class keeps track of a movable square, which can move, jump, and land on floating platforms
 */
public class PlayerBody
{
    //Location
    private int x, y;
    private Platform ground = null;
    
    //Keeps track of input
    private boolean move_left = false, move_right = false;
    
    //Motion fields, these are updated as the player moves
    private int move_dir = 0, fall_speed = 0;
    
    
    public int fall_accel = 1, //larger values are unnecesary since it is pixels per frame per frame
               jump_impulse = -15, //Large impulse and large resistance is how I've chosen the controls to feel
               fall_resist = 200, //resistance here is measured as a permille, that is parts per thousand
               move_speed = 3; //horizontal movement speed, multiplied by move_dir to get a directional velocity
    
    //these four methods will update an input boolean, and then if only one boolean is set the move_dir direction will be updated.
    //This creates smooth movement updates, and intuitive handling when multiple movement keys are pressed at once.
    public void StartMoveLeft ()
    {
        move_left = true; //set bool
        if (!move_right) //only move left if not already moving right
            move_dir = -1;
    }
    public void StartMoveRight ()
    {
        move_right = true; //set bool
        if (!move_left) //only move right if not already moving left
            move_dir = +1;
    }
    public void StopMoveLeft ()
    {
        move_left = false; //set bool
        //Stop motion, or switch to moving right if right is still being held
        move_dir = move_right ? +1 : 0;
    }
    public void StopMoveRight ()
    {
        move_right = false; //set bool
        //Stop motion, or switch to moving left if right is still being held
        move_dir = move_left ? -1 : 0;
    }
    
    //Gives some vertical velocity, and detaches from the ground
    public void StartJump ()
    {
        //note that without something to stand on you will not jump
        if (ground != null)
        {
            fall_speed = ground.getFallSpeed() + jump_impulse; //gives you velocity from the platform (Newton's first law of motion) plus the jump impulse
            ground = null; //stop keeping track of the ground
        }
    }
    
    /**
     * Constructor for objects of class PlayerBody
     * Just copy coordinates from the arguments
     */
    public PlayerBody(int x_c, int y_c)
    {
        x = x_c;
        y = y_c;
    }

    // updates location and physics of the PlayerBody
    //args include platform iterator for testing collisions
    public void StepInTime(Iterator<Platform> platforms)
    {
        int new_x = x + move_speed * move_dir; 
        //since move_dir 0 will multiply to give 0, 
            //+1 will move to the right by move_speed, 
            //and -1 will move to the left by move_speed, thanks to the multiplication
        
        //Move with the platform if available
        if (ground != null)
            if (ground.stillStanding(x))
                y = ground.getPlayerPos(x, 10);
            else //must have fallen off the platform (walked off)
            {
                fall_speed = ground.getFallSpeed(); //get new velocity when falling off
                ground = null; //stop walking on platform
            }
        
        int new_y = y;
                
        //When not on platform, or even if it fell off in this call, run normal physics and test for a collision
        if (ground == null)
        {
            new_y += fall_speed; //fall: s = ut
            fall_speed += fall_accel; // change velocity: v = at + u
            if (fall_speed > 0) //only do air resistance when falling
                fall_speed -= fall_resist * fall_speed / 1000;
            while (platforms.hasNext()) //test each platform for a collision
            {
                Platform try_this = platforms.next();
                if (try_this.PathCollides(x, y, new_x, new_y)) //ask platform if it collided
                {
                    ground = try_this; //set ground to this since it collided
                    new_y = ground.getPlayerPos(x, 10); // move to the platform so that player doesn't render 'inside' platform
                    break; //don't need to test any more since it is no longer falling.
                }
            }
        }
        
        //update player to be wherever calculated
        x = new_x;
        y = new_y;
        //this staggered calculation is so that old coordinates can be accessed
    }
    
    //print the player onto a graphics object
    public void Paint (Graphics g)
    {
        g.setColor(Color.red);
        g.fillRect(x - 5, y - 10, 10, 10); //just paint a square above the player coords
    }
}

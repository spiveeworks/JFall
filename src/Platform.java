import java.awt.*;

public class Platform
{
    //Just a rectangle, and the fields you would expect of one.
    private int x, y, width, height;

    //copies rectangle data into class
    public Platform(int x_c, int y_c, int width_c, int height_c) 
    {
        x = x_c;
        y = y_c; // If this isn't 500 then the platform will appear out of nowhere which wouldn't make much sense, so make this 500
        width = width_c;
        height = height_c;
    }
    
    //checks to see if it is off the screen yet
    public boolean IsFinished()
    {
        return y < -height;
    }

    //Make platform paint itself onto a graphics object, while also updating its position
    public void Paint(Graphics g, int deltatime)
    {
        g.setColor(Color.blue);
        //paint a rectangle, with an updated position.
        g.fillRect(x, y -= deltatime, width, height); // Simple upward movement 
    }
    
    //Checks to see if player would have moved through it.
    //this is a semi-complex, mathematical method that will not have hitches regardless of framerate or player velocity.
    public boolean PathCollides(int x1, int y1, int x2, int y2)
    {
        if (!(y1 <= y && y <= y2))
            return false;
        int coll_x = x1 + (y-y1)*(x1-x2)/(y1-y2); // solve trajectory to find collision ordinate
        coll_x -= x; // get ordinate relative to platform edge
        return stillStanding(coll_x, 0);//if the player passes through the platform's y level then the only question is if it landed on the platform or not
    }
    
    //helps player animate itself when standing
    public int getPlayerPos(int player_x, int player_height)
    {
        return y;
    }
    //checks to see if player is still standing on a platform given its x position
    public boolean stillStanding(int player_x)
    {
        //just call the overload with an offset
        return stillStanding(player_x, x);
    }
    //helper overload
    public boolean stillStanding(int player_x, int offset_x)
    {
        //check to see if the player_x is between an offset and the platform's edge
        //by default the offset will correspond to the platform's other edge
        return (offset_x <= player_x && player_x - offset_x <= width); // this line assumes that width is positive
    }
    
    //Objects call this when leaving a platform to determine new velocity.
    public int getFallSpeed()
    {
        return -1; //for now platforms always move up at a constant speed
    }
}

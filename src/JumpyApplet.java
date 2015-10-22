
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;

/**
 * Class JumpyApplet - Applet to create and update a game of "fall", a down-scrolling platform game
 * 
 * @author SpiveeWorks (Jarvis Carroll)
 */
public class JumpyApplet extends JApplet
{
    //Keeps track of Platform objects, and executes batch operations on these
    private PlatformAnimator platform;
    //Generates new platforms according to implementation of derived class
    private PlatformGenerator source;
    //Keeps track of player position and movement, and interprets input events
    private PlayerBody player;
    
    //Used for animation and game updates
    private Timer timer;
    private int time;
    
    //Used for buffering (see init())
    private BufferedImage buffer;
    private Graphics buffer_g;
    
    
    /**
     * Called by the browser or applet viewer to inform this JApplet that it
     * has been loaded into the system. It is always called before the first 
     * time that the start method is called.
     */
    public void init()
    {
        // this is a workaround for a security conflict with some browsers
        // including some versions of Netscape & Internet Explorer which do 
        // not allow access to the AWT system event queue which JApplets do 
        // on startup to check access. May not be necessary with your browser. 
        JRootPane rootPane = this.getRootPane();    
        rootPane.putClientProperty("defeatSystemEventQueueCheck", Boolean.TRUE);
    
        Restart();//Initializes game objects and sets initial time.

        //create timer for screen and game updates, and start it straight away.
        timer = new Timer(10, new TimerListener()); //every 5 milliseconds
        timer.start();
        
        //Buffer initialization, this image will be used as a buffer when painting
        buffer = getGraphicsConfiguration().createCompatibleImage(500, 500);
        //graphics object to use instead of Graphics g
        buffer_g = buffer.getGraphics();
        
        //Create and initialize keyboard input
        addKeyListener (new KeyClosure());
        setFocusable(true);
        requestFocusInWindow();
        
    }
    
    private void Restart()
    {
        platform = new PlatformAnimator(); // Default (empty) initialization
        source = new UniformLevel (); // UniformLevel is not modifiable with arguments
        player = new PlayerBody(250, 100); // player in centre of screen
        time = 0;
    }

    class TimerListener implements ActionListener {
        public TimerListener () {}
        public void actionPerformed (ActionEvent e) {
            //increment timer, which helps animator keep track of animation
            time++;
            
            // Attempts to generate another platform right now
            Platform next_platform = source.getNext(time); 
            //if there is a new platform, add it to the animation
            if (next_platform != null) platform.BeginAnimation(next_platform); 
            
            // update Platform locations
            player.StepInTime(platform.getPlatforms()); 
            // remove any platforms that have left the screen
            platform.ClearSkies(); 
            
            //Render changes
            repaint(); 
        }
    }
    
    class KeyClosure extends KeyAdapter {
        public KeyClosure () {}
        public void keyPressed(KeyEvent e)
        {
            switch (e.getKeyCode())
            {
                case KeyEvent.VK_A:
                    player.StartMoveLeft(); // tells player that they are moving left
                    break;
                case KeyEvent.VK_D:
                    player.StartMoveRight(); // tells player they are moving right
                    break;
                case KeyEvent.VK_W:
                    player.StartJump(); // tells player they are jumping
                    break;
            }
        }
        public void keyReleased(KeyEvent e)
        {
            switch (e.getKeyCode())
            {
                case KeyEvent.VK_A:
                    player.StopMoveLeft(); // tells player they have stopped moving left
                    break;
                case KeyEvent.VK_D:
                    player.StopMoveRight(); // tells player they have stopped moving right
                    break;
                case KeyEvent.VK_ESCAPE: 
                    Restart(); // wipes all data and starts again when ESC is pressed
                    break;
            }
        }
    }
    
    /**
     * Paint method for applet.
     * 
     * @param  g   the Graphics object for this applet
     */
    public void paint(Graphics g)
    {
        //clear the buffer image to prepare for a screen update
        buffer_g.setColor(Color.white);
        buffer_g.fillRect(0, 0, 500, 500);
        
        //Display seconds since last reset
        buffer_g.setColor(Color.red);
        //the following calculation converts 100*10ms into 1s by dividing by 100
        //note that the calculation is correct, but the swing timer is out by 1/10th
        buffer_g.drawString("" + (time/100) + " seconds", 20, 20);
        
        //Display creator and year
        buffer_g.setColor(Color.blue);
        buffer_g.drawString("created by Spivee, 2015", 20, 40);
        
        //Paint platforms via animator class, and then paint player over the top.
        platform.Paint(buffer_g, time); //
        player.Paint(buffer_g);
        
        //Print the entire buffer image over the old screen all at once
        g.drawImage(buffer, 0, 0, this); 
    }

    //helper function for double buffering. 
    //Does not appear to be necessary in BlueJ applet viewer but may be important in browsers
    public void update(Graphics g) 
    { 
        paint(g); 
    } 


}

import java.util.Random;

/**
 * Simple class for randomly placing platforms on the screen
 */
public class UniformLevel implements PlatformGenerator
{
    //details of object to place next
    private int next_platform_time;
    private Platform next_platform;
    
    //Random seed for determining platform placement
    private Random numbers;
    
    public UniformLevel()
    {
        next_platform_time = 0; //place the first platform straight away
        next_platform = new Platform (210, 400, 80, 20); // in the middle of the screen, under the player
        
        numbers = new Random(); //prepare random seed for generating platforms
    }

    //Gets a platform if soon enough, or return null
    public Platform getNext (int time_now)
    {
        Platform return_platform = null;
        if (next_platform_time <= time_now) // test to see if platform is ready
        {
            return_platform = next_platform; // so that a new next platform can be set
            prepareNext(); //generate platform for next iteration
        }
        return return_platform; //return generated platform or null
    }
    
    //gets a random position and queues next platform time
    private void prepareNext()
    {
        //random x coord, anywhere on the screen
        next_platform = new Platform (numbers.nextInt(420), 500, 80, 20); //y=bottom of screen
        //add this random platform in 1.5 seconds
        next_platform_time += 150;
    }
}

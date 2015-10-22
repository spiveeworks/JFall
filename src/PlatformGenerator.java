
/**
 * Interface for platform generators. 
 * Currently only one implementation but this interface is good practice anyway
 */
public interface PlatformGenerator
{
    //Returns a paltform if it is time for one, or null if no platform should be created that tick.
    public Platform getNext (int time_now);
}

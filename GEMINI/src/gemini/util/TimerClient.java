package gemini.util ;

/**
 * An interface to be implemented by clients of Timer objects. I decided against
 * using the standard "Listener" and "Event" approach since the interaction is
 * different (the update method has a return value) and because clock tick
 * events could be fairly frequent.
 */
public interface TimerClient
{
	/**
     * Receive notification of a clock tick.
     * 
     * @return the time in milliseconds when the next tick should happen, or
     *         non-positive number to remove the client.
     */
	long tick( long time ) ;
}

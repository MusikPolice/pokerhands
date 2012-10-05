package pokerhands.hands;

import java.util.ArrayList;
import java.util.List;

/**
 * A five-card straight that also happens to be a flush.
 * @author jfritz
 */
public class StraightFlush extends Hand
{
    @Override
    public Hand getValidHand()
    {
        List<Hand> flushHands = new ArrayList<>();
        
        for (Hand h : getStraights())
        {
            Flush f = (Flush)h;
            if (f.isValid())
            {
                flushHands.add(f.getValidHand());
            }
        }
        
        //return the largest of the flush hands
        if (flushHands != null && flushHands.size() > 0)
        {
            return flushHands.get(flushHands.size() - 1);
        }
        
        return null;
    }
}

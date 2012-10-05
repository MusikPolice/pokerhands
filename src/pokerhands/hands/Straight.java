package pokerhands.hands;

import java.util.List;

/**
 * A straight is defined as five cards with contiguous rank.
 * Suit does not matter.
 * @author jfritz
 */
public class Straight extends Hand
{
    @Override
    public Hand getValidHand()
    {
        List<Hand> straights = getStraights();
        if (straights != null && straights.size() > 0)
        {
            //return the highest valued straight that we found
            return straights.get(straights.size() - 1);
        }

        return null;
    }
}

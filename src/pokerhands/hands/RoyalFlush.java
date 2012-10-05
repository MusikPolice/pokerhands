package pokerhands.hands;

import java.util.ArrayList;
import java.util.List;

/**
 * A Royal Flush is a five-card straight flush consisting of a 10, jack, 
 * queen, king, and ace.
 * @author jfritz
 */
public class RoyalFlush extends Hand
{
    public RoyalFlush(Hand h)
    {
        this.cards = h.getCards();
        h = this.getValidHand();
        if (h != null)
        {
            this.cards = h.getCards();
        }
        else
        {
            this.cards = null;
        }    
    }
    
    @Override
    public Hand getValidHand()
    {
        List<Hand> royalFlushHands = new ArrayList<>();
        
        for (Hand h : getStraights())
        {
            //the special case for a 10,J,K,Q,A straight starts with the ace
            //because all hands are sorted by rank ascending.
            Flush f = (Flush)h;
            Hand h2 = f.getValidHand();
            if (h2.isValid() &&
                h2.get(0).getRank() == 1 &&
                h2.get(1).getRank() == 10 &&
                h2.get(2).getRank() == 11 &&
                h2.get(3).getRank() == 12 &&
                h2.get(4).getRank() == 13)
            {
                royalFlushHands.add(h);
            }
        }
        
        //return the highest of the royal flush hands
        if (royalFlushHands != null && royalFlushHands.size() > 0)
        {
            return royalFlushHands.get(royalFlushHands.size() - 1);
        }
        
        return null;
    }
    
    @Override
    public int getScore()
    {
        return 10;
    }
}

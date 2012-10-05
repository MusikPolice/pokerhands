package pokerhands.hands;

import java.util.ArrayList;
import java.util.List;

/**
 * A five-card straight that also happens to be a flush.
 * @author jfritz
 */
public class StraightFlush extends Hand implements Comparable<StraightFlush>
{
    public StraightFlush(Hand h)
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
    
    @Override
    public int compareTo(StraightFlush t) 
    {
        //first handle aces because they're sorted to the bottom of the hand
        int index = 0;
        while (index < this.getNumCards())
        {
            if (this.get(index).getRank() == 1 && t.get(index).getRank() == 1)
            {
                index++;
            }
            else if (this.get(index).getRank() == 1 && t.get(index).getRank() != 1)
            {
                return 1;
            }
            else if (this.get(index).getRank() != 1 && t.get(index).getRank() == 1)
            {
                return -1;
            }
            else
            {
                //neither hand has an ace at index
                break;
            }
        }
        
        //then continue on handling high cards
        index = this.getNumCards() - 1;
        while (this.get(index).getRank() == t.get(index).getRank())
        {
            index--;
            if (index < 0) return 0;
        }
        
        if (this.get(index).getRank() < t.get(index).getRank())
        {
            return - 1;
        }
        else
        {
            return 1;
        }
    }
}

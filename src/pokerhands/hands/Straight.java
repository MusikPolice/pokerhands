package pokerhands.hands;

import java.util.List;

/**
 * A straight is defined as five cards with contiguous rank.
 * Suit does not matter.
 * @author jfritz
 */
public class Straight extends Hand implements Comparable<Straight>
{
    public Straight(Hand h)
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
        List<Hand> straights = getStraights();
        if (straights != null && straights.size() > 0)
        {
            //return the highest valued straight that we found
            return straights.get(straights.size() - 1);
        }

        return null;
    }
    
    @Override
    public int compareTo(Straight t) 
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

package pokerhands.hands;

import java.util.HashMap;
import java.util.List;
import pokerhands.Card;

/**
 * A full house is defined as three cards of one rank and two cards of another. 
 * Suit does not matter.
 * @author jfritz
 */
public class FullHouse extends Hand implements Comparable<FullHouse>
{
    int tripRank = 0;
    int pairRank = 0;
    
    public FullHouse(Hand h)
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
        if (cards.size() < 5) return null;
        
        //map each rank to the cards of that rank
        HashMap<Integer, List<Card>> rankBuckets = bucketCardsByRank(cards);
        
        Hand h = new Hand();
        boolean three = false;
        boolean pair = false;
        
        //aces are high, try to find three aces first
        if (rankBuckets.get(1) != null)
        {
            if (rankBuckets.get(1).size() >= 3)
            {
                for (int i = 0; i < 3; i++)
                {
                    h.add(rankBuckets.get(1).remove(0));
                }
                if (tripRank == 0) tripRank = 1;
                three = true;
            }
            if (rankBuckets.get(1).isEmpty()) rankBuckets.remove(1);
        }
        
        //didn't have three aces? try the next highest cards
        if (!three)
        {
            for (int r = 13; r > 1; r--)
            {
                if (rankBuckets.get(r) != null && rankBuckets.get(r).size() >= 3)
                {
                    for (int i = 0; i < 3; i++)
                    {
                        h.add(rankBuckets.get(r).remove(0));
                    }
                    if (rankBuckets.get(r).isEmpty()) rankBuckets.remove(r);
                    if (tripRank == 0) tripRank = r;
                    three = true;
                    break;
                }
            }
        }
        
        //if we still didn't get our three of a kind, dump out
        if (!three) return null;
        
        //aces are high, try to find two aces first
        if (rankBuckets.get(1) != null)
        {
            if (rankBuckets.get(1).size() >= 2)
            {
                for (int i = 0; i < 2; i++)
                {
                    h.add(rankBuckets.get(1).remove(0));
                }
                if (pairRank == 0) pairRank = 1;
                pair = true;
            }
            if (rankBuckets.get(1).isEmpty()) rankBuckets.remove(1);
        }
        
        //didn't have a pair of aces? try the next highest cards
        if (!pair)
        {
            for (int r = 13; r > 1; r--)
            {
                if (rankBuckets.get(r) != null && rankBuckets.get(r).size() >= 2)
                {
                    for (int i = 0; i < 2; i++)
                    {
                        h.add(rankBuckets.get(r).remove(0));
                    }
                    if (rankBuckets.get(r).isEmpty()) rankBuckets.remove(r);
                    if (pairRank == 0) pairRank = r;
                    pair = true;
                    break;
                }
            }
        }
        
        //return null if we couldn't build a valid hand
        if (!(three && pair)) return null;
        return h;
    }
    
    public int getTripRank()
    {
        return this.tripRank;
    }
    
    public int getPairRank()
    {
        return this.pairRank;
    }
    
    @Override
    public int compareTo(FullHouse t) 
    {
        if (this.tripRank > t.getTripRank())
        {
            return 1;
        }
        else if (this.tripRank < t.getTripRank())
        {
            return -1;
        }
        else
        {
            if (this.pairRank > t.getPairRank())
            {
                return 1;
            }
            else if (this.pairRank < t.getPairRank())
            {
                return -1;
            }
            else
            {
                return 0;
            }
        }
    }
}
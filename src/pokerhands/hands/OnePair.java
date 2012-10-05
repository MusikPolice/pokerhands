package pokerhands.hands;

import java.util.HashMap;
import java.util.List;
import pokerhands.Card;

/**
 * A pair is defined as any two cards that share the same rank. 
 * The one pair subset of a larger number of cards consists of the highest
 * pair of cards plus the remaining three highest cards of the set.
 * @author jfritz
 */
public class OnePair extends Hand implements Comparable<OnePair>
{
    //the rank of the highest set of pairs in this hand
    private int pairRank;
    
    public OnePair(Hand h)
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
        
        //aces are high, so if a pair exists, add it first
        if (rankBuckets.get(1) != null && rankBuckets.get(1).size() >= 2)
        {
            //take a pair of aces
            for (int i = 0; i < 2; i++)
            {
                h.add(rankBuckets.get(1).remove(0));
                if (pairRank == 0) pairRank = 1;
            }
            if (rankBuckets.get(1).isEmpty()) rankBuckets.remove(1);
        }
        
        //if we didn't get at least one pair of aces, take the next highest
        //pair of cards
        if (h.getNumCards() < 2)
        {
            for (int r = 13; r > 1; r--)
            {
                if (rankBuckets.get(r) != null && rankBuckets.get(r).size() >= 2)
                {
                    for (int i = 0; i < 2; i++)
                    {
                        h.add(rankBuckets.get(r).remove(0));
                        if (pairRank == 0) pairRank = r;
                    }
                    if (rankBuckets.get(r).isEmpty()) rankBuckets.remove(r);
                    break;
                }
            }
        }
        
        //if we still didn't get a pair, we aren't going to get one
        if (h.getNumCards() < 2) return null;
        
        //now pad the hand with high cards - aces first
        if (rankBuckets.get(1) != null)
        {
            for (Card c : rankBuckets.get(1))
            {
                if (h.getNumCards() == 5) break;
                h.add(c);
            }
        }
        for (int r = 13; r > 1; r--)
        {
            if (rankBuckets.get(r) != null)
            {
                for (Card c : rankBuckets.get(r))
                {
                    if (h.getNumCards() == 5) break;
                    h.add(c);
                }
            }
        }
        
        //return null if we couldn't build a valid hand
        if (h.getNumCards() != 5) return null;
        return h;
    }
    
    /*
     * Returns the rank of the pair
     */
    public int getPairRank()
    {
        return pairRank;
    }

    @Override
    public int compareTo(OnePair t) 
    {
        if (this.pairRank < t.getPairRank())
        {
            return -1;
        }
        else if (this.pairRank > t.getPairRank())
        {
            return 1;
        }
        else
        {
            int count1 = this.cards.size() - 1;
            int count2 = t.getNumCards() - 1;
            while (count1 >= 0 && count2 >= 0)
            {
                if (this.cards.get(count1).getRank() == this.pairRank)
                {
                    count1 -= 2;
                }
                if (t.get(count2).getRank() == t.getPairRank())
                {
                    count2 -= 2;
                }
                
                if (count1 < 0 || count2 < 0) break;
                
                if (this.cards.get(count1).getRank() < t.get(count2).getRank())
                {
                    return -1;
                }
                else if (this.cards.get(count1).getRank() > t.get(count2).getRank())
                {
                    return 1;
                }
                else
                {
                    count1--;
                    count2--;
                }
            }
            
            return 0;
        }
    }
}

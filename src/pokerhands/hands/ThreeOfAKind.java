package pokerhands.hands;

import java.util.HashMap;
import java.util.List;
import pokerhands.Card;

/**
 * Three of a kind is defined as a hand of five cards, three of which share
 * the same rank.
 * The remaining two cards in the hand are the highest cards available to 
 * the player.
 * @author jfritz
 */
public class ThreeOfAKind extends Hand implements Comparable<ThreeOfAKind>
{
    private int tripRank = 0;
    
    public ThreeOfAKind(Hand h)
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

    public int getTripRank()
    {
        return this.tripRank;
    }
    
    @Override
    public int compareTo(ThreeOfAKind t) 
    {
        //special handling for aces
        int myRank = this.tripRank;
        int otherRank = t.getTripRank();
        if (myRank == 1) myRank = 14;
        if (otherRank == 1) otherRank = 14;
        
        if (myRank < otherRank)
        {
            return -1;
        }
        else if (myRank > otherRank)
        {
            return 1;
        }
        else
        {
            int count1 = this.cards.size() - 1;
            int count2 = t.getNumCards() - 1;
            while (count1 >= 0 && count2 >= 0)
            {
                if (this.cards.get(count1).getRank() == this.tripRank)
                {
                    count1 -= 3;
                }
                if (t.get(count2).getRank() == t.getTripRank())
                {
                    count2 -= 3;
                }
                
                if (count1 < 0 || count2 < 0) break;
        
                //special handling for aces
                myRank = this.cards.get(count1).getRank();
                otherRank = t.get(count2).getRank();
                if (myRank == 1) myRank = 14;
                if (otherRank == 1) otherRank = 14;
        
                if (myRank < otherRank)
                {
                    return -1;
                }
                else if (myRank > otherRank)
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

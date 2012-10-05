package pokerhands.hands;

import java.util.HashMap;
import java.util.List;
import pokerhands.Card;

/**
 * Four of a kind is defined as any four cards with the same rank. 
 * Suit does not matter for this test.
 * @author jfritz
 */
public class FourOfAKind extends Hand implements Comparable<FourOfAKind>
{
    int fourRank = 0;
    
    public FourOfAKind(Hand h)
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
        boolean four = false;
        
        //aces are high, try to find four aces first
        if (rankBuckets.get(1) != null)
        {
            if (rankBuckets.get(1).size() >= 4)
            {
                for (int i = 0; i < 4; i++)
                {
                    h.add(rankBuckets.get(1).remove(0));
                }
                if (fourRank == 0) fourRank = 1;
                four = true;
            }
            if (rankBuckets.get(1).isEmpty()) rankBuckets.remove(1);
        }
        
        //didn't have four aces? try the next highest cards
        if (!four)
        {
            for (int r = 13; r > 1; r--)
            {
                if (rankBuckets.get(r) != null && rankBuckets.get(r).size() >= 4)
                {
                    for (int i = 0; i < 4; i++)
                    {
                        h.add(rankBuckets.get(r).remove(0));
                    }
                    if (rankBuckets.get(r).isEmpty()) rankBuckets.remove(r);
                    if (fourRank == 0) fourRank = r;
                    four = true;
                    break;
                }
            }
        }
        
        //if we still didn't get our four of a kind, dump out
        if (!four) return null;
        
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
    
    public int getFourRank()
    {
        return this.fourRank;
    }
    
    @Override
    public int compareTo(FourOfAKind t) 
    {
        //special handling for aces
        int myRank = this.getFourRank();
        int otherRank = t.getFourRank();
        if (myRank == 1) myRank = 14;
        if (otherRank == 1) otherRank = 14;
        
        if (myRank > otherRank)
        {
            return 1;
        }
        else if (myRank < otherRank)
        {
            return -1;
        }
        else
        {
            int count1 = this.cards.size() - 1;
            int count2 = t.getNumCards() - 1;
            while (count1 >= 0 && count2 >= 0)
            {
                if (this.cards.get(count1).getRank() == this.fourRank)
                {
                    count1 -= 4;
                }
                if (t.get(count2).getRank() == t.getFourRank())
                {
                    count2 -= 4;
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

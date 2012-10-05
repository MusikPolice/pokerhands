package pokerhands.hands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import pokerhands.Card;

/**
 * Represents a hand that contains two pairs of cards.
 * The two pair subset of a larger number of cards consists of the highest
 * two pairs of cards plus the highest remaining card of the set.
 * @author jfritz
 */
public class TwoPair extends Hand implements Comparable<TwoPair>
{
    private List<Integer> pairRankings = new ArrayList<>();
    
    public TwoPair(Hand h)
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
    
    public List<Integer> getPairRankings()
    {
        return this.pairRankings;
    }
    
    @Override
    public Hand getValidHand()
    {
        if (cards.size() < 5) return null;
        
        //map each rank to the cards of that rank
        HashMap<Integer, List<Card>> rankBuckets = bucketCardsByRank(this.cards);
        
        Hand h = new Hand();
        int pairs = 0;
        
        //aces are high, so if at least one pair exists, add them first
        if (rankBuckets.get(1) != null)
        {
            if (rankBuckets.get(1).size() >= 4)
            {
                //two pair of aces
                for (int i = 0; i < 4; i++)
                {
                    h.add(rankBuckets.get(1).remove(0));
                }
                if (!pairRankings.contains(1)) pairRankings.add(1);
                pairs = 2;
            }
            else if (rankBuckets.get(1).size() >= 2)
            {
                //one pair of aces
                for (int i = 0; i < 2; i++)
                {
                    h.add(rankBuckets.get(1).remove(0));
                }
                if (!pairRankings.contains(1)) pairRankings.add(1);
                pairs = 1;
            }
            if (rankBuckets.get(1).isEmpty()) rankBuckets.remove(1);
        }
        
        //if we didn't get two pairs of aces, take the next highest pair
        if (pairs < 2)
        {
            for (int r = 13; r > 1; r--)
            {
                if (rankBuckets.get(r) != null && rankBuckets.get(r).size() >= 2)
                {
                    for (int i = 0; i < 2; i++)
                    {
                        h.add(rankBuckets.get(r).remove(0));
                    }
                    if (!pairRankings.contains(r)) pairRankings.add(r);
                    if (rankBuckets.get(r).isEmpty()) rankBuckets.remove(r);
                    pairs++;
                    break;
                }
            }
        }
        
        //if we still didn't get two pairs, we aren't going to
        if (pairs < 2) return null;
        
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
    
    @Override
    public int compareTo(TwoPair t) 
    {
        //compare the first pair
        if (this.pairRankings.get(0) < t.getPairRankings().get(0))
        {
            return -1;
        }
        else if (this.pairRankings.get(0) > t.getPairRankings().get(0))
        {
            return 1;
        }
        
        //if they're the same, compare the second
        if (this.pairRankings.get(1) < t.getPairRankings().get(1))
        {
            return -1;
        }
        else if (this.pairRankings.get(1) > t.getPairRankings().get(1))
        {
            return 1;
        }
        
        //start comparing high cards
        int count1 = this.cards.size() - 1;
        int count2 = t.getNumCards() - 1;
        while (count1 >= 0 && count2 >= 0)
        {
            if (this.pairRankings.contains(this.cards.get(count1).getRank()))
            {
                count1 -= 2;
            }
            if (t.getPairRankings().contains(t.get(count2).getRank()))
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

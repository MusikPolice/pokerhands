package pokerhands.hands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import pokerhands.Card;

/**
 * A full house is defined as three cards of one rank and two cards of another. 
 * Suit does not matter.
 * @author jfritz
 */
public class FullHouse extends Hand
{
    @Override
    public Hand getValidHand()
    {
        if (cards.size() < 5) return null;
        
        //map each rank to the cards of that rank
        HashMap<Integer, List<Card>> rankBuckets = new HashMap<>();
        
        //sort the cards into buckets based on rank
        for (Card c : cards)
        {
            List<Card> rank = new ArrayList<>();
            if (rankBuckets.containsKey(c.getRank()))
            {
                rank.addAll(rankBuckets.get(c.getRank()));
            }
            rank.add(c);
            rankBuckets.put(c.getRank(), rank);
        }
        
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
                    pair = true;
                    break;
                }
            }
        }
        
        //return null if we couldn't build a valid hand
        if (!(three && pair)) return null;
        return h;
    }
}
package pokerhands.hands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import pokerhands.Card;

/**
 * A flush consists of five cards that share the same suit.
 * Rank does not matter.
 * @author jfritz
 */
public class Flush extends Hand implements Comparable<Flush>
{
    public Flush(Hand h)
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
        
        //map each suit to the cards of that suit
        HashMap<Card.SUIT, List<Card>> suitBuckets = new HashMap<>();
        
        //sort the cards into buckets based on rank
        for (Card c : cards)
        {
            List<Card> suit = new ArrayList<>();
            if (suitBuckets.containsKey(c.getSuit()))
            {
                suit.addAll(suitBuckets.get(c.getSuit()));
            }
            suit.add(c);
            suitBuckets.put(c.getSuit(), suit);
        }
        
        //if any of the suits have more than five cards, then there is a flush 
        //of that suit in the hand.
        for (Card.SUIT s : suitBuckets.keySet())
        {
            if (suitBuckets.get(s).size() >= 5)
            {
                //return the top five cards of that suit
                List<Card> suit = suitBuckets.get(s);
                Collections.sort(suit);
                
                Hand h = new Hand();
                for (int i = 0; i < suit.size(); i++)
                {
                    //take aces first because they are high
                    if (suit.get(i).getRank() == 1)
                    {
                        h.add(suit.get(i));
                    }
                    else
                    {
                        break;
                    }
                }
                for (int i = suit.size() - 1; i != 0; i--)
                {
                    if (h.getNumCards() == 5) break;
                    
                    //on this pass, ignore aces - we already dealt with them
                    if (suit.get(i).getRank() != 1) h.add(suit.get(i));
                }
                
                if (h.getNumCards() == 5) return h;
                return null;
            }
        }
        
        return null;
    }
    
    @Override
    public int compareTo(Flush t) 
    {
        int index = this.getNumCards() - 1;
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

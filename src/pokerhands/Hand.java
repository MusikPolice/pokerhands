package pokerhands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author jfritz
 */
public class Hand implements Iterable<Card>
{
    private List<Card> cards = new ArrayList<>();
    
    public Hand(Card c)
    {
        if (c != null)
        {
            cards.add(c);
        }
    }
    
    public Hand(List<Card> hand)
    {
        if (hand != null)
        {
            cards.addAll(hand);
        }
        Collections.sort(cards);
    }
    
    public Hand(Hand player, Hand community)
    {
        if (player != null)
        {
            cards.addAll(player.getCards());
        }
        if (community != null)
        {
            cards.addAll(community.getCards());
        }
        Collections.sort(cards);
    }
    
    public void add(Card c)
    {
        cards.add(c);
        Collections.sort(cards);
    }
    
    public boolean isEmpty()
    {
        return cards.isEmpty();
    }
    
    public List<Card> getCards()
    {
        return cards;
    }
    
    @Override
    public Iterator<Card> iterator() 
    {
        return cards.iterator();
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for (Card c : cards)
        {
            sb.append(c.toString());
            if (c != cards.get(cards.size() - 1))
            {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
    
    public static boolean isHighCard(Hand h)
    {
        if (h.isEmpty()) return false;
        return true;
    }
    
    public static boolean isPair(Hand h)
    {
        if (h.isEmpty()) return false;
        
        int prevRank = 0;
        
        for (Card c : h)
        {
            if (prevRank != 0)
            {
                if (c.getRank() == prevRank)
                {
                    return true;
                }
            }
            
            prevRank = c.getRank();
        }
        
        return false;
    }
}

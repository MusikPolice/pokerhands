package pokerhands.hands;

import pokerhands.Card;

/**
 * By definition, every hand of five cards is a high card hand.
 * The high card subset of a larger number of cards consists of the five cards
 * with the largest rank. Suit does not matter.
 * @author jfritz
 */
public class HighCard extends Hand implements Comparable<HighCard>
{   
    public HighCard(Hand h)
    {
        //choose the most appropriate set of five cards
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
        if (this.cards == null) return null;
        
        Hand h = new Hand();
        
        //aces are high, so start by adding all aces to the hand
        for (Card c : cards)
        {
            if (c.getRank() == 1)
            {
                h.add(c);
            }
            else
            {
                break;
            }
        }
        
        //no count backward so that highest cards are added first
        for (int i = cards.size() - 1; i != 0; i--)
        {
            //stop if we have five cards in our hand
            if (h.getNumCards() == 5) break;
            
            //stop if we find an ace, because they have already been handled
            if (cards.get(i).getRank() == 1) break;
            
            //otherwise, add the card to the hand
            h.add(cards.get(i));
        }
     
        //return null if we don't have a five card hand
        if (h.getNumCards() != 5) return null;
        return h;
    }
    
    @Override
    public int compareTo(HighCard t) 
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

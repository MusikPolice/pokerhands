package pokerhands.hands;

import pokerhands.Card;

/**
 * By definition, every hand of five cards is a high card hand.
 * The high card subset of a larger number of cards consists of the five cards
 * with the largest rank. Suit does not matter.
 * @author jfritz
 */
public class HighCard extends Hand
{
    @Override
    public Hand getValidHand() 
    {
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
}

package pokerhands;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import pokerhands.Card.SUIT;

/**
 *
 * @author jfritz
 */
public class Deck 
{
    ArrayList<Card> cards = new ArrayList<>();
    
    private int index = 0;
    
    /**
     * Creates a new Deck of cards.
     * A deck contains 52 total cards; 13 of each of the four suits.
     */
    public Deck()
    {
        for (SUIT s : Card.SUIT.values())
        {
            for (int r = 1; r < 14; r++)
            {
                cards.add(new Card(r, s));
            }
        }
    }
    
    public void shuffle()
    {
        Random generator = new Random();
        
        for (int i = 0; i < 1000; i++)
        {
            int index1 = generator.nextInt(52);
            cards.add(cards.remove(index1));
        }
        
        this.index = 0;
    }
    
    public Card drawCard()
    {
        this.index++;
        return cards.get(index - 1);
    }
    
    public List<Card> drawCards(int howMany)
    {
        ArrayList<Card> drawnCards = new ArrayList<>();
        for(int i = 0; i < howMany; i++)
        {
            drawnCards.add(this.drawCard());
        }
        return drawnCards;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for (Card c : cards)
        {
            sb.append(c.toString());
            sb.append(System.getProperty("line.separator"));
        }
        return sb.toString();
    }
}

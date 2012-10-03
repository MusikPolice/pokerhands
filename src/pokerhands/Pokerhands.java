package pokerhands;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jfritz
 */
public class Pokerhands 
{
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        Deck d = new Deck();
        d.shuffle();
        
        List<Card> hand1 = new ArrayList<>();
        List<Card> hand2 = new ArrayList<>();
        List<Card> community = new ArrayList<>();
        
        //deal two hands
        hand1.add(d.drawCard());
        hand2.add(d.drawCard());
        hand1.add(d.drawCard());
        hand2.add(d.drawCard());

        //deal the flop
        d.drawCard();
        community.addAll(d.drawCards(3));
        
        //deal the river
        d.drawCard();
        community.add(d.drawCard());
        
        //deal the turn
        d.drawCard();
        community.add(d.drawCard());
        
        
        System.out.println("Player 1:");
        for(Card c: hand1)
        {
            System.out.println(c.toString());
        }
        System.out.println();
        
        System.out.println("Player 2:");
        for(Card c: hand2)
        {
            System.out.println(c.toString());
        }
        System.out.println();
        
        System.out.println("Community:");
        for(Card c: community)
        {
            System.out.println(c.toString());
        }
        System.out.println();
    }
}

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
        
        //deal two hands
        Hand player1 = new Hand(d.drawCard());
        Hand player2 = new Hand(d.drawCard());
        player1.add(d.drawCard());
        player2.add(d.drawCard());
        
        //deal the flop
        d.drawCard();
        Hand community = new Hand(d.drawCards(3));
        
        //deal the river
        d.drawCard();
        community.add(d.drawCard());
        
        //deal the turn
        d.drawCard();
        community.add(d.drawCard());
        
        
        System.out.println("Player 1:");
        System.out.println(player1.toString());
        
        System.out.println("Player 2:");
        System.out.println(player2.toString());
        
        System.out.println("Community:");
        System.out.println(community.toString());
        
        Hand p1AllCards = new Hand(player1, community);
        if (p1AllCards.hasOnePair())
        {
            System.out.println("Player1 has a pair");
        }
        
        Hand p2AllCards = new Hand(player2, community);
        if (p2AllCards.hasOnePair())
        {
            System.out.println("Player2 has a pair");
        }
    }
}

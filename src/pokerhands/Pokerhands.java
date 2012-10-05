package pokerhands;

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
        int count = 0;
        
        while (count < 10)
        {
            Deck d = new Deck();
            d.shuffle();

            //deal two hands
            Hand player1 = new Hand(d.drawCard());
            //Hand player2 = new Hand(d.drawCard());
            player1.add(d.drawCard());
            //player2.add(d.drawCard());

            //deal the flop
            d.drawCard();
            Hand community = new Hand(d.drawCards(3));

            //deal the river
            d.drawCard();
            community.add(d.drawCard());

            //deal the turn
            d.drawCard();
            community.add(d.drawCard());
        
            
            Hand p1AllCards = new Hand(player1, community);
            if (p1AllCards.hasRoyalFlush())
            {
                System.out.println(p1AllCards.toString());
                System.out.println(p1AllCards.getRoyalFlushHand().toString());
                System.out.println();
                count++;
            }
        }
        
        /*
        System.out.println("Player 1:");
        System.out.println(player1.toString());
        
        System.out.println("Player 2:");
        System.out.println(player2.toString());
        
        System.out.println("Community:");
        System.out.println(community.toString());
        
        Hand p1AllCards = new Hand(player1, community);
        p1AllCards = p1AllCards.getTwoPairHand();
        if (p1AllCards != null)
        {
            System.out.println("Player1 has one pair: " + p1AllCards.toString());
        }
        * 
        */
        
        /*
        System.out.print("Player one has a ");
        int p1Score = scoreHand(p1AllCards);
        System.out.println();
        */
        
        /*
        Hand p2AllCards = new Hand(player2, community);
        p2AllCards = p2AllCards.getTwoPairHand();
        if (p2AllCards != null)
        {
            System.out.println("Player2 has one pair: " + p2AllCards.toString());
        }
        * 
        */
        
        /*
        System.out.print("Player two has a ");
        int p2Score = scoreHand(p2AllCards);
        System.out.println();
        */
        
        /*
        if (p1Score > p2Score)
        {
            System.out.println("Player 1 Wins!");
        }
        else if (p1Score < p2Score)
        {
            System.out.println("Player 2 Wins!");
        }
        else
        {
            System.out.println("Temporary tie");
        }
        */
    }
    
    /**
     * Checks the contents of the specified hand for winning combinations.
     * Returns an integer between 0 and 9 (inclusive) that indicates the score
     * of the hand, where 0 is high card and 9 is royal flush.
     * This is the first step of determining the winning hand. If two hands have
     * the same score, then they need to be compared in more detail.
     * @param h the hand to score
     * @return the score of the hand, an integer from 0 to 9
     */
    private static int scoreHand(Hand h)
    {
        if (h.hasRoyalFlush())
        {
            System.out.print("Royal Flush");
            return 9;
        }
        else if (h.hasStraightFlush())
        {
            System.out.print("Straight Flush");
            return 8;
        }
        else if (h.hasFourOfAKind())
        {
            System.out.print("Four of a Kind");
            return 7;
        }
        else if (h.hasFullHouse())
        {
            System.out.print("Full House");
            return 6;
        }
        else if (h.hasFlush())
        {
            System.out.print("Flush");
            return 5;
        }
        else if (h.hasStraight())
        {
            System.out.print("Straight");
            return 4;
        }
        else if (h.hasThreeOfAKind())
        {
            System.out.print("Three of a Kind");
            return 3;
        }
        else if (h.hasTwoPair())
        {
            System.out.print("Two Pair");
            return 2;
        }
        else if (h.hasOnePair())
        {
            System.out.print("One Pair");
            return 1;
        }
        else
        {
            System.out.print("High Card");
            return 0;
        }
    }
}

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
        Deck d = new Deck();
        d.shuffle();
        
        for (Card c : d.drawCards(10))
        {
            System.out.println(c.toString());
        }
    }
}

package pokerhands;

/**
 *
 * @author jfritz
 */
public class Card 
{
    public static enum SUIT
    {
        CLUBS,
        DIAMONDS,
        HEARTS,
        SPADES
    }
    
    /**
     * 1 is an ace
     * 14 is an ace
     */
    private int rank;
    
    private SUIT suit;
    
    public Card(int rank, SUIT suit)
    {
        if (rank > 0 && rank < 15)
        {
            this.rank = rank;
        }
        
        this.suit = suit;
    }
    
    public int getRank()
    {
        return this.rank;
    }
    public SUIT getSuit()
    {
        return this.suit;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        
        switch (rank)
        {
            case 1:
                sb.append("ACE");
                break;
            case 11:
                sb.append("JACK");
                break;
            case 12:
                sb.append("QUEEN");
                break;
            case 13:
                sb.append("KING");
                break;
            case 14:
                sb.append("ACE");
                break;
            default:
                sb.append(rank);
                break;
        }
        
        sb.append(" of ");
        
        switch (suit)
        {
            case CLUBS:
                sb.append("CLUBS");
                break;
            case DIAMONDS:
                sb.append("DIAMONDS");
                break;
            case HEARTS:
                sb.append("HEARTS");
                break;
            case SPADES:
                sb.append("SPADES");
                break;
        }
        
        return sb.toString();
    }
}
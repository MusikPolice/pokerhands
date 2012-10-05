package pokerhands.hands;

import java.util.*;
import pokerhands.Card;

/**
 *
 * @author jfritz
 */
public class Hand implements Iterable<Card>
{
    protected List<Card> cards = null;

    public Hand()
    {
        cards = new ArrayList<>();
    }
    
    public Hand(Card c)
    {
        this();
        
        if (c != null)
        {
            cards.add(c);
        }
    }
    
    public Hand(List<Card> hand)
    {
        this();
        
        if (hand != null)
        {
            cards.addAll(hand);
        }
        Collections.sort(cards);
    }
    
    public Hand(Hand player, Hand community)
    {
        this();
        
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
    
    /**
     * Adds the specified card to the hand.
     * After the card has been added, the hand is sorted by rank ascending
     * @param c the card to add
     */
    public void add(Card c)
    {
        cards.add(c);
        Collections.sort(cards);
    }
    
    /**
     * Adds the specified cards to the hand.
     * @param cards 
     */
    public void addAll(List<Card> cards)
    {
        for (Card c : cards)
        {
            this.add(c);
        }
    }
    
    /**
     * Returns the card at the specified index in the hand.
     * This operation does not remove the card from the hand.
     * @param index
     * @return the card at the specified index in the hand.
     */
    public Card get(int index)
    {
        return cards.get(index);
    }
    
    /**
     * Removes the card at the specified index from the hand.
     * Because the hand is always sorted by rank ascending, removing the
     * zeroth card in the hand is equivalent to removing the lowest card.
     * Similarly, removing the n-1th card in the hand is equivalent to 
     * removing the highest card.
     * @param index the index of the card to remove
     * @return the card that was removed from the hand
     */
    public Card remove(int index)
    {
        return cards.remove(index);
    }
    
    /**
     * Returns the number of cards that are in the hand.
     * @return the number of cards that are in the hand.
     */
    public int getNumCards()
    {
        return cards.size();
    }
    
    /**
     * Returns true if the hand is currently empty.
     * @return true if the hand is currently empty.
     */
    public boolean isEmpty()
    {
        return this.getNumCards() == 0;
    }
    
    /**
     * Returns a list of the cards that are in the hand.
     * The cards are guaranteed to be sorted by rank ascending.
     * @return a list of cards that are in the hand.
     */
    public List<Card> getCards()
    {
        return cards;
    }
    
    /**
     * Returns true if this is a valid five card poker hand.
     * @return true if this is a valid five card poker hand.
     */
    public boolean isValid()
    {
        return getValidHand() != null;
    }
    
    /**
     * Returns the first five cards in this hand.
     * @return 
     */
    public Hand getValidHand()
    {
        List<Card> validCards = new ArrayList<>();
        
        for (int i = 0; i < Math.min(5, cards.size()); i++)
        {
            validCards.add(cards.get(i));
        }
        
        if (validCards.size() == 5)
        {
            return new Hand(validCards);
        }
        else
        {
            return null;
        }
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
    
    /**
     * Returns a list of all sub-hands of this hand that constitute a five 
     * card straight.
     * The list is ordered from lowest-valued straight to highest.
     * @return a list of all sub-hands of this hand that constitute a five 
     * card straight.
     */
    protected List<Hand> getStraights()
    {
        List<Hand> straights = new ArrayList<>();
        if (cards.size() < 5) return straights;
        
        List<Card> straight = new ArrayList<>();
        int prevRank = 0;
        
        //assembles the longest straight in the hand
        for (Card c : cards)
        {
            if (prevRank != 0)
            {
                if (c.getRank() == prevRank + 1)
                {
                    //if the current card's rank is one greater than that of
                    //the previous card, it is part of the straight
                    straight.add(c);
                }
                else
                {
                    //the straight was broken. split it into five card runs
                    //and add each of them to our collection of straights.
                    straights.addAll(getSubRuns(straight, 5));
                    
                    //start fresh with just the current card
                    straight = new ArrayList<>();
                    straight.add(c);
                }
                
                if (c == cards.get(cards.size() - 1))
                {
                    //if the highest card in the hand is a king
                    if (c.getRank() == 13)
                    {
                        //and the lowest card is an ace
                        if (cards.get(0).getRank() == 1)
                        {
                            //then the both the king and the ace count 
                            //towards the straight
                            straight.add(cards.get(0));
                        }
                    }
                }
            }
            else
            {
                //always add the first card to our straight
                straight.add(c);
            }
            
            //always save the rank of the previous card
            prevRank = c.getRank();
        }
        
        //split the remaining straight into five card runs
        //and add each of them to our collection of straights.
        straights.addAll(getSubRuns(straight, 5));
        
        return straights;
    }
    
    /**
     * Splits the cards into runs.
     * Each run contains exactly runLength cards. If the number of cards in
     * the hand is not divisible by runLength, not all cards will be used in
     * the returned collection.
     * @param runLength
     * @return 
     */
    private List<Hand> getSubRuns(List<Card> cards, int runLength)
    {
        //in this function, the local declaration cards overrides the class-
        //level declaration of the same name.
        
        List<Hand> straights = new ArrayList<>();
        
        //we're starting with one long run, so a sub-run 
        //could be said to begin at any of the cards within it.
        for (int s = 0; s < cards.size(); s++)
        {
            Hand h = new Hand();
            for (int i = s; i < Math.min(s + runLength, cards.size()); i++)
            {
                //try to add the first runLength cards after s to the hand
                //this will be the sub-run that starts at index s
                h.add(cards.get(i));
            }

            if (h.getNumCards() == runLength) 
            {
                //if the sub-run has runLength cards in it, keep it
                straights.add(h);
            }
            else
            {
                //otherwise, none of the remaining sub-runs will either,
                //so there's no point in continuing.
                break;
            }
        }
        
        return straights;
    }
}

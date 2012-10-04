package pokerhands;

import java.util.*;

/**
 *
 * @author jfritz
 */
public class Hand implements Iterable<Card>
{
    private List<Card> cards = null;

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
     * Returns true if this hand has a high card.
     * In practice, this can only be false if the hand is empty.
     * @return true if this hand has a high card.
     */
    public boolean hasHighCard()
    {
        return !cards.isEmpty();
    }
    
    /**
     * Returns true if this hand contains at least one pair of cards.
     * A pair is defined as any two cards that share the same rank. For this
     * particular test, suit does not matter.
     * @return true if this hand contains at least one pair of cards.
     */
    public boolean hasOnePair()
    {
        if (cards.size() < 5) return false;
        
        //map each rank to the number of cards of that rank in the hand
        HashMap<Integer, Integer> rankCount = new HashMap<>();
        
        //count the number of instances of each suit
        for (Card c : cards)
        {
            int count = rankCount.containsKey(c.getRank()) ? rankCount.get(c.getRank()) : 0;
            rankCount.put(c.getRank(), count + 1);
        }
        
        //if any of the ranks have two or more cards, then there is one pair.
        for (int r = 1; r < 14; r++)
        {
            if (rankCount.get(r) >= 2) return true;
        }
        
        return false;
    }
    
    /**
     * Returns true if the hand contains two pairs.
     * @return true if the hand contains two pairs.
     */
    public boolean hasTwoPair()
    {
        if (cards.size() < 5) return false;
        
        //map each rank to the number of cards of that rank in the hand
        HashMap<Integer, Integer> rankCount = new HashMap<>();
        
        //count the number of instances of each suit
        for (Card c : cards)
        {
            int count = rankCount.containsKey(c.getRank()) ? rankCount.get(c.getRank()) : 0;
            rankCount.put(c.getRank(), count + 1);
        }
        
        int pairs = 0;
        
        //if any of the ranks have four or more cards, then there is a four of
        //a kind in the hand.
        for (int r = 1; r < 14; r++)
        {
            if (rankCount.get(r) >= 2)
            {
                pairs++;
            }
        }
        
        return pairs > 0;
    }
    
    public boolean hasThreeOfAKind()
    {
        if (cards.size() < 5) return false;
        
        //map each rank to the number of cards of that rank in the hand
        HashMap<Integer, Integer> rankCount = new HashMap<>();
        
        //count the number of instances of each suit
        for (Card c : cards)
        {
            int count = rankCount.containsKey(c.getRank()) ? rankCount.get(c.getRank()) : 0;
            rankCount.put(c.getRank(), count + 1);
        }
        
        //if any of the ranks have three or more cards, then there is three
        //of a kind in the hand.
        for (int r = 1; r < 14; r++)
        {
            if (rankCount.get(r) >= 3) return true;
        }
        
        return false;
    }
    
    /**
     * Returns true if this hand contains a straight.
     * A straight is defined as five cards with continuous rank. For this
     * particular test, suit does not matter.
     * @return true if this hand contains a straight.
     */
    public boolean hasStraight()
    {
        return !getStraights().isEmpty();
    }
    
    /**
     * Returns true if this hand contains a flush.
     * A flush is defined as five cards that share the same suit. For this
     * particular test, rank does not matter.
     * @return true if this hand contains a flush.
     */
    public boolean hasFlush()
    {
        if (cards.size() < 5) return false;
        
        //map each suit to the number of cards of that suit in the hand
        HashMap<Card.SUIT, Integer> suitCount = new HashMap<>();
        
        //count the number of instances of each suit
        for (Card c : cards)
        {
            int count = suitCount.containsKey(c.getSuit()) ? suitCount.get(c.getSuit()) : 0;
            suitCount.put(c.getSuit(), count + 1);
        }
        
        //if any of the suits have more than four cards, then there is a flush 
        //of that suit in the hand.
        for (Card.SUIT s : suitCount.keySet())
        {
            if (suitCount.get(s) >= 5) return true;
        }
        
        return false;
    }
    
    /**
     * Returns true if the hand contains a full house.
     * A full house is defined as three cards of one rank and two cards of
     * another. Suit does not matter.
     * @return true if the hand contains a full house.
     */
    public boolean hasFullHouse()
    {
        if (cards.size() < 5) return false;
        
        //map each rank to the number of cards of that rank in the hand
        HashMap<Integer, Integer> rankCount = new HashMap<>();
        
        //count the number of instances of each suit
        for (Card c : cards)
        {
            int count = rankCount.containsKey(c.getRank()) ? rankCount.get(c.getRank()) : 0;
            rankCount.put(c.getRank(), count + 1);
        }
        
        int pairRank = 0;
        int tripsRank = 0;
        
        //if any of the ranks have four or more cards, then there is a four of
        //a kind in the hand.
        for (int r = 1; r < 14; r++)
        {
            if (rankCount.get(r) == 2)
            {
                pairRank = r;
            }
            else if (rankCount.get(r) == 3)
            {
                tripsRank = r;
            }
        }
        
        return pairRank > 0 && tripsRank > 0;
    }
    
    /**
     * Returns true if the hand contains four of a kind.
     * Four of a kind is defined as any four cards with the same rank. Suit
     * does not matter for this test.
     * @return true if the hand contains four of a kind.
     */
    public boolean hasFourOfAKind()
    {
        if (cards.size() < 5) return false;
        
        //map each rank to the number of cards of that rank in the hand
        HashMap<Integer, Integer> rankCount = new HashMap<>();
        
        //count the number of instances of each suit
        for (Card c : cards)
        {
            int count = rankCount.containsKey(c.getRank()) ? rankCount.get(c.getRank()) : 0;
            rankCount.put(c.getRank(), count + 1);
        }
        
        //if any of the ranks have four or more cards, then there is a four of
        //a kind in the hand.
        for (int r = 1; r < 14; r++)
        {
            if (rankCount.get(r) >= 4) return true;
        }
        
        return false;
    }
    
    /**
     * Returns true if this hand contains a five-card straight that also
     * happens to be a flush.
     * @return true if this hand contains a five-card straight that also
     * happens to be a flush.
     */
    public boolean hasStraightFlush()
    {
        for (Hand h : getStraights())
        {
            if (h.hasFlush()) return true;
        }
        
        return false;
    }
    
    /**
     * Returns true if this hand contains a royal flush.
     * A Royal Flush is defined as a five-card straight flush consisting of
     * a 10, jack, queen, king, and ace.
     * @return 
     */
    public boolean hasRoyalFlush()
    {
        for (Hand h : getStraights())
        {
            //the special case for a 10,J,K,Q,A straight starts with the ace
            //because all hands are sorted by rank ascending.
            if (h.hasFlush() &&
                h.get(0).getRank() == 1 &&
                h.get(1).getRank() == 10 &&
                h.get(2).getRank() == 11 &&
                h.get(3).getRank() == 12 &&
                h.get(4).getRank() == 13)
            {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Finds the highest five card straight in the provided hand of cards.
     * Assumes that h has been sorted from lowest rank to highest, regardless of suit
     * @param h the hand to search for a straight
     * @return the highest straight in h, or null if no straight could be found
     */
    private Hand getHighestStraight()
    {
        List<Hand> straights = getStraights();
        
        if (!straights.isEmpty())
        {
            return straights.get(straights.size() - 1);
        }
        else
        {
            return null;
        }
    }
    
    /**
     * Returns a list of all sub-hands of this hand that constitute a five 
     * card straight.
     * The list is ordered from lowest-valued straight to highest.
     * @return a list of all sub-hands of this hand that constitute a five 
     * card straight.
     */
    private List<Hand> getStraights()
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
                else if (c == cards.get(cards.size() - 1))
                {
                    //if the highest card in the hand is a king
                    if (c.getRank() == 13)
                    {
                        //and the lowest card is an ace
                        if (cards.get(0).getRank() == 1)
                        {
                            //then the both the king and the ace count 
                            //towards the straight
                            straight.add(c);
                            straight.add(cards.get(0));
                        }
                    }
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

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
        return getHighCardHand() != null;
    }
    
    /**
     * Returns the five highest cards in the current hand.
     * @return a new hand comprised of the five highest cards in the current 
     *         hand or null if a five card hand couldn't be assembled.
     */
    public Hand getHighCardHand()
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
    
    /**
     * Returns true if this hand contains at least one pair of cards.
     * A pair is defined as any two cards that share the same rank. For this
     * particular test, suit does not matter.
     * @return true if this hand contains at least one pair of cards.
     */
    public boolean hasOnePair()
    {
        return getOnePairHand() != null;
    }
    
    /**
     * Returns a five-card hand that contains at least one high pair,
     * padded by other high cards.
     * @return 
     */
    public Hand getOnePairHand()
    {
        if (cards.size() < 5) return null;
        
        //map each rank to the cards of that rank
        HashMap<Integer, List<Card>> rankBuckets = new HashMap<>();
        
        //sort the cards into buckets based on rank
        for (Card c : cards)
        {
            List<Card> rank = new ArrayList<>();
            if (rankBuckets.containsKey(c.getRank()))
            {
                rank.addAll(rankBuckets.get(c.getRank()));
            }
            rank.add(c);
            rankBuckets.put(c.getRank(), rank);
        }
        
        Hand h = new Hand();
        
        //aces are high, so if a pair exists, add it first
        if (rankBuckets.get(1) != null && rankBuckets.get(1).size() >= 2)
        {
            //take a pair of aces
            for (int i = 0; i < 2; i++)
            {
                h.add(rankBuckets.get(1).remove(0));
            }
            if (rankBuckets.get(1).isEmpty()) rankBuckets.remove(1);
        }
        
        //if we didn't get at least one pair of aces, take the next highest
        //pair of cards
        if (h.getNumCards() < 2)
        {
            for (int r = 13; r > 1; r--)
            {
                if (rankBuckets.get(r) != null && rankBuckets.get(r).size() >= 2)
                {
                    for (int i = 0; i < 2; i++)
                    {
                        h.add(rankBuckets.get(r).remove(0));
                    }
                    if (rankBuckets.get(r).isEmpty()) rankBuckets.remove(r);
                    break;
                }
            }
        }
        
        //if we still didn't get a pair, we aren't going to get one
        if (h.getNumCards() < 2) return null;
        
        //now pad the hand with high cards - aces first
        if (rankBuckets.get(1) != null)
        {
            for (Card c : rankBuckets.get(1))
            {
                if (h.getNumCards() == 5) break;
                h.add(c);
            }
        }
        for (int r = 13; r > 1; r--)
        {
            if (rankBuckets.get(r) != null)
            {
                for (Card c : rankBuckets.get(r))
                {
                    if (h.getNumCards() == 5) break;
                    h.add(c);
                }
            }
        }
        
        //return null if we couldn't build a valid hand
        if (h.getNumCards() != 5) return null;
        return h;
    }
    
    /**
     * Returns true if the hand contains two pairs.
     * @return true if the hand contains two pairs.
     */
    public boolean hasTwoPair()
    {
        return getTwoPairHand() != null;
    }
    
    public Hand getTwoPairHand()
    {
        if (cards.size() < 5) return null;
        
        //map each rank to the cards of that rank
        HashMap<Integer, List<Card>> rankBuckets = new HashMap<>();
        
        //sort the cards into buckets based on rank
        for (Card c : cards)
        {
            List<Card> rank = new ArrayList<>();
            if (rankBuckets.containsKey(c.getRank()))
            {
                rank.addAll(rankBuckets.get(c.getRank()));
            }
            rank.add(c);
            rankBuckets.put(c.getRank(), rank);
        }
        
        Hand h = new Hand();
        int pairs = 0;
        
        //aces are high, so if at least one pair exists, add them first
        if (rankBuckets.get(1) != null)
        {
            if (rankBuckets.get(1).size() >= 4)
            {
                //two pair of aces
                for (int i = 0; i < 4; i++)
                {
                    h.add(rankBuckets.get(1).remove(0));
                }
                pairs = 2;
            }
            else if (rankBuckets.get(1).size() >= 2)
            {
                //one pair of aces
                for (int i = 0; i < 2; i++)
                {
                    h.add(rankBuckets.get(1).remove(0));
                }
                pairs = 1;
            }
            if (rankBuckets.get(1).isEmpty()) rankBuckets.remove(1);
        }
        
        //if we didn't get two pairs of aces, take the next highest pair
        if (pairs < 2)
        {
            for (int r = 13; r > 1; r--)
            {
                if (rankBuckets.get(r) != null && rankBuckets.get(r).size() >= 2)
                {
                    for (int i = 0; i < 2; i++)
                    {
                        h.add(rankBuckets.get(r).remove(0));
                    }
                    if (rankBuckets.get(r).isEmpty()) rankBuckets.remove(r);
                    pairs++;
                    break;
                }
            }
        }
        
        //if we still didn't get two pairs, we aren't going to
        if (pairs < 2) return null;
        
        //now pad the hand with high cards - aces first
        if (rankBuckets.get(1) != null)
        {
            for (Card c : rankBuckets.get(1))
            {
                if (h.getNumCards() == 5) break;
                h.add(c);
            }
        }
        for (int r = 13; r > 1; r--)
        {
            if (rankBuckets.get(r) != null)
            {
                for (Card c : rankBuckets.get(r))
                {
                    if (h.getNumCards() == 5) break;
                    h.add(c);
                }
            }
        }
        
        //return null if we couldn't build a valid hand
        if (h.getNumCards() != 5) return null;
        return h;
    }
    
    /**
     * Returns true if the hand contains three of a kind.
     * Three of a kind is defined as three cards with the same rank. Suit is not
     * considered for this comparison.
     * @return true if the hand contains three of a kind.
     */
    public boolean hasThreeOfAKind()
    {
        return getThreeOfAKindHand() != null;
    }
    
    public Hand getThreeOfAKindHand()
    {
        if (cards.size() < 5) return null;
        
        //map each rank to the cards of that rank
        HashMap<Integer, List<Card>> rankBuckets = new HashMap<>();
        
        //sort the cards into buckets based on rank
        for (Card c : cards)
        {
            List<Card> rank = new ArrayList<>();
            if (rankBuckets.containsKey(c.getRank()))
            {
                rank.addAll(rankBuckets.get(c.getRank()));
            }
            rank.add(c);
            rankBuckets.put(c.getRank(), rank);
        }
        
        Hand h = new Hand();
        boolean three = false;
        
        //aces are high, try to find three aces first
        if (rankBuckets.get(1) != null)
        {
            if (rankBuckets.get(1).size() >= 3)
            {
                for (int i = 0; i < 3; i++)
                {
                    h.add(rankBuckets.get(1).remove(0));
                }
                three = true;
            }
            if (rankBuckets.get(1).isEmpty()) rankBuckets.remove(1);
        }
        
        //didn't have three aces? try the next highest cards
        if (!three)
        {
            for (int r = 13; r > 1; r--)
            {
                if (rankBuckets.get(r) != null && rankBuckets.get(r).size() >= 3)
                {
                    for (int i = 0; i < 3; i++)
                    {
                        h.add(rankBuckets.get(r).remove(0));
                    }
                    if (rankBuckets.get(r).isEmpty()) rankBuckets.remove(r);
                    three = true;
                    break;
                }
            }
        }
        
        //if we still didn't get our three of a kind, dump out
        if (!three) return null;
        
        //now pad the hand with high cards - aces first
        if (rankBuckets.get(1) != null)
        {
            for (Card c : rankBuckets.get(1))
            {
                if (h.getNumCards() == 5) break;
                h.add(c);
            }
        }
        for (int r = 13; r > 1; r--)
        {
            if (rankBuckets.get(r) != null)
            {
                for (Card c : rankBuckets.get(r))
                {
                    if (h.getNumCards() == 5) break;
                    h.add(c);
                }
            }
        }
        
        //return null if we couldn't build a valid hand
        if (h.getNumCards() != 5) return null;
        return h;
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
     * Returns the highest straight sub-hand.
     * @return 
     */
    public Hand getStraightHand()
    {
        List<Hand> straights = getStraights();
        if (straights != null && straights.size() > 0)
        {
            //return the highest valued straight that we found
            return straights.get(straights.size() - 1);
        }
        
        return null;
    }
    
    /**
     * Returns true if this hand contains a flush.
     * A flush is defined as five cards that share the same suit. For this
     * particular test, rank does not matter.
     * @return true if this hand contains a flush.
     */
    public boolean hasFlush()
    {
        return getFlushHand() != null;
    }
    
    public Hand getFlushHand()
    {
        if (cards.size() < 5) return null;
        
        //map each suit to the cards of that suit
        HashMap<Card.SUIT, List<Card>> suitBuckets = new HashMap<>();
        
        //sort the cards into buckets based on rank
        for (Card c : cards)
        {
            List<Card> suit = new ArrayList<>();
            if (suitBuckets.containsKey(c.getSuit()))
            {
                suit.addAll(suitBuckets.get(c.getSuit()));
            }
            suit.add(c);
            suitBuckets.put(c.getSuit(), suit);
        }
        
        //if any of the suits have more than five cards, then there is a flush 
        //of that suit in the hand.
        for (Card.SUIT s : suitBuckets.keySet())
        {
            if (suitBuckets.get(s).size() >= 5)
            {
                //return the top five cards of that suit
                List<Card> suit = suitBuckets.get(s);
                Collections.sort(suit);
                
                Hand h = new Hand();
                for (int i = 0; i < suit.size(); i++)
                {
                    //take aces first because they are high
                    if (suit.get(i).getRank() == 1)
                    {
                        h.add(suit.get(i));
                    }
                    else
                    {
                        break;
                    }
                }
                for (int i = suit.size() - 1; i != 0; i--)
                {
                    if (h.getNumCards() == 5) break;
                    
                    //on this pass, ignore aces - we already dealt with them
                    if (suit.get(i).getRank() != 1) h.add(suit.get(i));
                }
                
                if (h.getNumCards() == 5) return h;
                return null;
            }
        }
        
        return null;
    }
    
    /**
     * Returns true if the hand contains a full house.
     * A full house is defined as three cards of one rank and two cards of
     * another. Suit does not matter.
     * @return true if the hand contains a full house.
     */
    public boolean hasFullHouse()
    {
        return getFullHouseHand() != null;
    }
    
    public Hand getFullHouseHand()
    {
        if (cards.size() < 5) return null;
        
        //map each rank to the cards of that rank
        HashMap<Integer, List<Card>> rankBuckets = new HashMap<>();
        
        //sort the cards into buckets based on rank
        for (Card c : cards)
        {
            List<Card> rank = new ArrayList<>();
            if (rankBuckets.containsKey(c.getRank()))
            {
                rank.addAll(rankBuckets.get(c.getRank()));
            }
            rank.add(c);
            rankBuckets.put(c.getRank(), rank);
        }
        
        Hand h = new Hand();
        boolean three = false;
        boolean pair = false;
        
        //aces are high, try to find three aces first
        if (rankBuckets.get(1) != null)
        {
            if (rankBuckets.get(1).size() >= 3)
            {
                for (int i = 0; i < 3; i++)
                {
                    h.add(rankBuckets.get(1).remove(0));
                }
                three = true;
            }
            if (rankBuckets.get(1).isEmpty()) rankBuckets.remove(1);
        }
        
        //didn't have three aces? try the next highest cards
        if (!three)
        {
            for (int r = 13; r > 1; r--)
            {
                if (rankBuckets.get(r) != null && rankBuckets.get(r).size() >= 3)
                {
                    for (int i = 0; i < 3; i++)
                    {
                        h.add(rankBuckets.get(r).remove(0));
                    }
                    if (rankBuckets.get(r).isEmpty()) rankBuckets.remove(r);
                    three = true;
                    break;
                }
            }
        }
        
        //if we still didn't get our three of a kind, dump out
        if (!three) return null;
        
        //aces are high, try to find two aces first
        if (rankBuckets.get(1) != null)
        {
            if (rankBuckets.get(1).size() >= 2)
            {
                for (int i = 0; i < 2; i++)
                {
                    h.add(rankBuckets.get(1).remove(0));
                }
                pair = true;
            }
            if (rankBuckets.get(1).isEmpty()) rankBuckets.remove(1);
        }
        
        //didn't have a pair of aces? try the next highest cards
        if (!pair)
        {
            for (int r = 13; r > 1; r--)
            {
                if (rankBuckets.get(r) != null && rankBuckets.get(r).size() >= 2)
                {
                    for (int i = 0; i < 2; i++)
                    {
                        h.add(rankBuckets.get(r).remove(0));
                    }
                    if (rankBuckets.get(r).isEmpty()) rankBuckets.remove(r);
                    pair = true;
                    break;
                }
            }
        }
        
        //return null if we couldn't build a valid hand
        if (!(three && pair)) return null;
        return h;
    }
    
    /**
     * Returns true if the hand contains four of a kind.
     * Four of a kind is defined as any four cards with the same rank. Suit
     * does not matter for this test.
     * @return true if the hand contains four of a kind.
     */
    public boolean hasFourOfAKind()
    {
        return getFourOfAKindHand() != null;
    }
    
    public Hand getFourOfAKindHand()
    {
        if (cards.size() < 5) return null;
        
        //map each rank to the cards of that rank
        HashMap<Integer, List<Card>> rankBuckets = new HashMap<>();
        
        //sort the cards into buckets based on rank
        for (Card c : cards)
        {
            List<Card> rank = new ArrayList<>();
            if (rankBuckets.containsKey(c.getRank()))
            {
                rank.addAll(rankBuckets.get(c.getRank()));
            }
            rank.add(c);
            rankBuckets.put(c.getRank(), rank);
        }
        
        Hand h = new Hand();
        boolean four = false;
        
        //aces are high, try to find four aces first
        if (rankBuckets.get(1) != null)
        {
            if (rankBuckets.get(1).size() >= 4)
            {
                for (int i = 0; i < 4; i++)
                {
                    h.add(rankBuckets.get(1).remove(0));
                }
                four = true;
            }
            if (rankBuckets.get(1).isEmpty()) rankBuckets.remove(1);
        }
        
        //didn't have four aces? try the next highest cards
        if (!four)
        {
            for (int r = 13; r > 1; r--)
            {
                if (rankBuckets.get(r) != null && rankBuckets.get(r).size() >= 4)
                {
                    for (int i = 0; i < 4; i++)
                    {
                        h.add(rankBuckets.get(r).remove(0));
                    }
                    if (rankBuckets.get(r).isEmpty()) rankBuckets.remove(r);
                    four = true;
                    break;
                }
            }
        }
        
        //if we still didn't get our four of a kind, dump out
        if (!four) return null;
        
        //now pad the hand with high cards - aces first
        if (rankBuckets.get(1) != null)
        {
            for (Card c : rankBuckets.get(1))
            {
                if (h.getNumCards() == 5) break;
                h.add(c);
            }
        }
        for (int r = 13; r > 1; r--)
        {
            if (rankBuckets.get(r) != null)
            {
                for (Card c : rankBuckets.get(r))
                {
                    if (h.getNumCards() == 5) break;
                    h.add(c);
                }
            }
        }
        
        //return null if we couldn't build a valid hand
        if (h.getNumCards() != 5) return null;
        return h;
    }
    
    /**
     * Returns true if this hand contains a five-card straight that also
     * happens to be a flush.
     * @return true if this hand contains a five-card straight that also
     * happens to be a flush.
     */
    public boolean hasStraightFlush()
    {
        return getStraightFlushHand() != null;
    }
    
    public Hand getStraightFlushHand()
    {
        List<Hand> flushHands = new ArrayList<>();
        
        for (Hand h : getStraights())
        {
            flushHands.add(h);
        }
        
        //return the largest of the flush hands
        if (flushHands != null && flushHands.size() > 0)
        {
            return flushHands.get(flushHands.size() - 1);
        }
        
        return null;
    }
    
    /**
     * Returns true if this hand contains a royal flush.
     * A Royal Flush is defined as a five-card straight flush consisting of
     * a 10, jack, queen, king, and ace.
     * @return 
     */
    public boolean hasRoyalFlush()
    {
        return getRoyalFlushHand() != null;
    }
    
    public Hand getRoyalFlushHand()
    {
        List<Hand> royalFlushHands = new ArrayList<>();
        
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
                royalFlushHands.add(h);
            }
        }
        
        //return the highest of the royal flush hands
        if (royalFlushHands != null && royalFlushHands.size() > 0)
        {
            return royalFlushHands.get(royalFlushHands.size() - 1);
        }
        
        return null;
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

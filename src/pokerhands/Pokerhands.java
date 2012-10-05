package pokerhands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import pokerhands.hands.*;

/**
 *
 * @author jfritz
 */
public class Pokerhands 
{
    public static enum Type
    {
        HighCard(0),
        OnePair(1),
        TwoPair(2),
        ThreeOfAKind(3),
        Straight(4),
        Flush(5),
        FullHouse(6),
        FourOfAKind(7),
        StraightFlush(8),
        RoyalFlush(9);

        private int score;

        Type(int score)
        {
            this.score = score;
        }

        public int getScore()
        {
            return this.score;
        }
        
        @Override
        public String toString()
        {
            switch (this.score)
            {
                case 0:
                    return "a High Card";
                case 1:
                    return "One Pair";
                case 2:
                    return "Two Pair";
                case 3:
                    return "Three of a Kind";
                case 4:
                    return "a Straight";
                case 5:
                    return "a Flush";
                case 6:
                    return "a Full House";
                case 7:
                    return "Four of a Kind";
                case 8:
                    return "a Straight Flush";
                case 9:
                    return "a Royal Flush";
                default:
                    return "an Unknown";
            }
        }
    }
    
    public Pokerhands()
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
        System.out.println();
        
        Hand p1AllCards = new Hand(player1, community);
        Hand p2AllCards = new Hand(player2, community);
        
        List<Hand> allHands = new ArrayList<>();
        allHands.add(p1AllCards);
        allHands.add(p2AllCards);
        
        HandType winner = determineWinningHand(allHands);
        
        System.out.println("The winning hand has " + winner.getType().toString() + ": " + winner.getHand().toString());
    }
    
    private HandType determineWinningHand(List<Hand> hands)
    {
        List<HandType> qualifiedHands = new ArrayList<>();
        
        for (Hand h : hands)
        {
            RoyalFlush rf = new RoyalFlush(h);
            if (rf.isValid())
            {
                qualifiedHands.add(new HandType(rf, Type.RoyalFlush));
            }

            StraightFlush sf = new StraightFlush(h);
            if (sf.isValid())
            {
                qualifiedHands.add(new HandType(sf, Type.StraightFlush));
            }

            FourOfAKind fk = new FourOfAKind(h);
            if (fk.isValid())
            {
                qualifiedHands.add(new HandType(fk, Type.FourOfAKind));
            }

            FullHouse fh = new FullHouse(h);
            if (fh.isValid())
            {
                qualifiedHands.add(new HandType(fh, Type.FullHouse));
            }

            Flush f = new Flush(h);
            if (f.isValid())
            {
                qualifiedHands.add(new HandType(f, Type.Flush));
            }

            Straight s = new Straight(h);
            if (s.isValid())
            {
                qualifiedHands.add(new HandType(s, Type.Straight));
            }

            ThreeOfAKind tk = new ThreeOfAKind(h);
            if (tk.isValid())
            {
                qualifiedHands.add(new HandType(tk, Type.ThreeOfAKind));
            }

            TwoPair tp = new TwoPair(h);
            if (tp.isValid())
            {
                qualifiedHands.add(new HandType(tp, Type.TwoPair));
            }

            OnePair op = new OnePair(h);
            if (op.isValid())
            {
                qualifiedHands.add(new HandType(op, Type.OnePair));
            }

            HighCard hc = new HighCard(h);
            if (hc.isValid())
            {
                qualifiedHands.add(new HandType(hc, Type.HighCard));
            }
        }
        
        Collections.sort(qualifiedHands, new Comparator<HandType>() {

            @Override
            public int compare(HandType t, HandType t1) 
            {
                if (t.getType().getScore() > t1.getType().getScore())
                {
                    return 1;
                }
                else if (t.getType().getScore() < t1.getType().getScore())
                {
                    return -1;
                }
                else
                {
                    // no need to explicitly test royal flush - it qualifies
                    // as a straight flush, and wins by the fact that it is the
                    // highest possible straight flush
                    if (t.getType() == Type.RoyalFlush || t.getType() == Type.StraightFlush)
                    {
                        StraightFlush sf1 = (StraightFlush)t.getHand();
                        StraightFlush sf2 = (StraightFlush)t1.getHand();
                        if (sf1.isValid() && sf2.isValid())
                        {
                            return sf1.compareTo(sf2);
                        }
                    }
                    
                    if (t.getType() == Type.FourOfAKind)
                    {
                        FourOfAKind fk1 = (FourOfAKind)t.getHand();
                        FourOfAKind fk2 = (FourOfAKind)t1.getHand();
                        if (fk1.isValid() && fk2.isValid())
                        {
                            return fk1.compareTo(fk2);
                        }
                    }
                    
                    if (t.getType() == Type.FullHouse)
                    {
                        FullHouse fh1 = (FullHouse)t.getHand();
                        FullHouse fh2 = (FullHouse)t1.getHand();
                        if (fh1.isValid() && fh2.isValid())
                        {
                            return fh1.compareTo(fh2);
                        }
                    }
                    
                    if (t.getType() == Type.Flush)
                    {
                        Flush f1 = (Flush)t.getHand();
                        Flush f2 = (Flush)t1.getHand();
                        if (f1.isValid() && f2.isValid())
                        {
                            return f1.compareTo(f2);
                        }
                    }
                    
                    if (t.getType() == Type.Straight)
                    {
                        Straight s1 = (Straight)t.getHand();
                        Straight s2 = (Straight)t1.getHand();
                        if (s1.isValid() && s2.isValid())
                        {
                            return s1.compareTo(s2);
                        }
                    }
                    
                    if (t.getType() == Type.ThreeOfAKind)
                    {
                        ThreeOfAKind tk1 = (ThreeOfAKind)t.getHand();
                        ThreeOfAKind tk2 = (ThreeOfAKind)t1.getHand();
                        if (tk1.isValid() && tk2.isValid())
                        {
                            return tk1.compareTo(tk2);
                        }
                    }

                    if (t.getType() == Type.TwoPair)
                    {
                        TwoPair tp1 = (TwoPair)t.getHand();
                        TwoPair tp2 = (TwoPair)t1.getHand();
                        if (tp1.isValid() && tp2.isValid())
                        {
                            return tp1.compareTo(tp2);
                        }
                    }

                    if (t.getType() == Type.OnePair)
                    {
                        OnePair op1 = (OnePair)t.getHand();
                        OnePair op2 = (OnePair)t1.getHand();
                        if (op1.isValid() && op2.isValid())
                        {
                            return op1.compareTo(op2);
                        }
                    }

                    if (t.getType() == Type.HighCard)
                    {
                        HighCard hc1 = (HighCard)t.getHand();
                        HighCard hc2 = (HighCard)t1.getHand();
                        if (hc1.isValid() && hc2.isValid())
                        {
                            return hc1.compareTo(hc2);
                        }
                    }
                    
                    //this can only happen if two identical crap hands are dealt
                    return 0;
                }
            }
            
        });
        
        //the highest valued hand is the winner
        return qualifiedHands.get(qualifiedHands.size() - 1);
    }
    
    private class HandType
    {       
        private Hand h;
        private Type t;
        
        public HandType(Hand h, Type t)
        {
            this.h = h;
            this.t = t;
        }
        
        public Hand getHand()
        {
            return h;
        }
        
        public Type getType()
        {
            return t;
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        Pokerhands p = new Pokerhands();
    }
}
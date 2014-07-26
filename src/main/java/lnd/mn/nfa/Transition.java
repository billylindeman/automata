package lnd.mn.nfa;

/**
 * Represents an Edge (transition) in our NFA
 *
 * Can either be an Epsilon transition (empty set, e.g. is always followed)
 * or it can be a single character transition
 *
 */
public class Transition {
    private boolean isEpsilon;
    private String label = "";
    private char   value = 0;

    private State incoming;
    private State outgoing;

    /**
     * Empty constructor returns an epsilon transition
     */
    public Transition() {
        isEpsilon = true;
    }

    /**
     * We can construct a transition with a char
     * @param value
     *      The value for this edge in the NFA
     */
    public Transition(char value) {
        this.value = value;
        isEpsilon = false;
    }

    /**
     * If this is an epsilon transition, always return true
     * If not, we only follow this edge if the incoming character is equal to our edge value
     * @param c
     * @return
     */
    public boolean consumes(char c) {
        if(isEpsilon) return true;
        else return value == c;
    }




    /**
     * Property Setters/Getters
     */
    public void setLabel(String l)      {label=l;}
    public String getLabel()            {return label;}
    public void setIncoming(State s)    {incoming = s;}
    public State getIncoming()          {return incoming;}
    public void setOutgoing(State s)    {outgoing = s;}
    public State getOutgoing()   {return outgoing;}
    public char getValue() {return value;}

}

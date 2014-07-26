package lnd.mn.nfa;


import java.util.LinkedList;

/**
 * Represents a state in the NFA
 *
 * Contains a label, and all incoming/outgoing transitions
 */
public class State {
    int identifier;
    public String label;
    private boolean isAccepting = false;

    public LinkedList<Transition> outgoingEdges = new LinkedList<Transition>();
    public LinkedList<Transition> incomingEdges = new LinkedList<Transition>();

    public State(int id) {
        identifier = id;
    }

    /**
     * Adds an epsiolon transition edge to s2
     * @param s2
     */
    public void addEdge(State s2) {
        Transition t = new Transition();
        t.setIncoming(this);
        t.setOutgoing(s2);
        this.outgoingEdges.add(t);
        s2.incomingEdges.add(t);
    }

    public void addEdgeWithSymbol(State s2, char c) {
        Transition t = new Transition(c);
        t.setIncoming(this);
        t.setOutgoing(s2);
        this.outgoingEdges.add(t);
        s2.incomingEdges.add(t);
    }

    public static State create() {
        return new State(State.getNewIdentifier());
    }

    public static State accept() {
        State s = State.create();
        s.isAccepting = true;
        return s;
    }

    public boolean isAccepting() {
        return isAccepting;
    }

    private static int state_identifiers = 0;
    public static int getNewIdentifier() {
        return ++state_identifiers;
    };
}

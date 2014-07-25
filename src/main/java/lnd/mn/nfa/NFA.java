package lnd.mn.nfa;


import java.io.File;
import java.util.HashSet;

/**
 * Nondeterministic Finite Automata
 *
 * This class will build an NFA from a RegExp string, set of States, or from a saved file
 */
public class NFA {
    State start;
    HashSet<State> accepting_states;

    /**
     * Builds NFA from a saved-instance
     * @param file
     *      File object representing a JFlap compatible NFA
     */
    public NFA(File file) {

    }

     /**
     * Builds a new NFA from a RegularExpression using Thompson Construction
     * @param regexp
     */
    public NFA(String regexp) {
        /**
         * Parse Regular Expression
         */



        /**
         * Build all Thompson machines
         */


    }

    public NFA(State start, State... accepting) {
        this.start = start;

        for(State s : accepting) {
            accepting_states.add(s);
        }
    }


    public static NFA Empty() {
        State start = State.create();
        State accept = State.accept();
        start.addEdge(accept);

        return new NFA(start,accept);
    }

    /**
     * Builds an NFA that accepts a single symbol
     * @param symbol
     * @return
     */
    public static NFA Symbol(char symbol) {
        State start = State.create();
        State accept = State.accept();
        start.addEdgeWithSymbol(accept, symbol);

        return new NFA(start, accept);
    }

    /**
     * Builds a Union Thompson Construction from two NFA's
     * @param nfa1
     *      First NFA to be in the Union NFA
     * @param nfa2
     *      Second NFA to be in the Union NFA
     * @return
     *      Union NFA built from two NFA's
     */
    public static NFA Union(NFA nfa1, NFA nfa2) {
        State start = State.create();
        State accept = State.accept();

        start.addEdge(nfa1.start);
        start.addEdge(nfa2.start);

        for(State s : nfa1.accepting_states) {
            s.addEdge(accept);
        }
        for(State s : nfa2.accepting_states) {
            s.addEdge(accept);
        }

        return new NFA(start, accept);
    }

    /**
     * Builds a Concat Thompson Construction from two NFA's
     * @param nfa1
     * @param nfa2
     * @return
     */
    public static NFA Concat(NFA nfa1, NFA nfa2) {
        State start = State.create();
        State accept = State.create();

        start.addEdge(nfa1.start);
        for(State s : nfa1.accepting_states) {
            s.addEdge(nfa2.start);
        }

        for(State s: nfa2.accepting_states) {
            s.addEdge(accept);
        }

        return new NFA(start,accept);
    }

    /**
     * Builds a Kleene Thompson Construction from one NFA
     * @param nfa
     *      NFA of the sub-expression
     * @return
     *      Kleen Closure of the sub-expression
     */
    public static NFA KleeneClosure(NFA nfa) {
        State start = State.create();
        State accept = State.create();

        start.addEdge(nfa.start);
        start.addEdge(accept);

        for(State s : nfa.accepting_states) {
            s.addEdge(nfa.start);
            s.addEdge(accept);
        }

        return new NFA(start,accept);
    }
}

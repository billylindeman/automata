package lnd.mn.nfa;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;

/**
 * Nondeterministic Finite Automata
 *
 * This class will build an NFA from a RegExp string, set of States, or from a saved file
 */
public class NFA {
    State start;

    HashSet<State> states = new HashSet<State>();
    HashSet<State> accepting_states = new HashSet<State>();

    /**
     * Builds NFA from a saved-instance
     * @param file
     *      File object representing a JFlap compatible NFA
     */
    public NFA(File file) {

    }


     /**
     * Builds a new NFA from a RegularExpression using Thompson Construction
     * @param regexString
     */
    public NFA(String regexString) {

    }



    public NFA(State start, State... accepting) {
        this.start = start;
        for(State s : accepting) {
            accepting_states.add(s);
        }
        followEdgesAndBuildStatesSet(start);
    }

    private void followEdgesAndBuildStatesSet(State s) {
        if(states.contains(s)) return;
        else {
            states.add(s);
            for(Transition t : s.outgoingEdges) {
                followEdgesAndBuildStatesSet(t.getOutgoing());
            }
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

    public void saveGraphviz(String filename) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter graph = new PrintWriter(filename, "UTF-8");

        graph.write("digraph NFA {\n");

        for(State s : states) {
            String shape = s.isAccepting() ? "doublecircle" : "circle";
            graph.write(s.identifier + " [ label = \"" + s.identifier + "\", shape =\"" + shape + "\" ] \n");
        }

        for(State s : states) {
            for(Transition t: s.outgoingEdges) {
                String label = t.getValue() == 0? "\u03B5" : ""+t.getValue();
                graph.write(t.getIncoming().identifier + "->" + t.getOutgoing().identifier + " [ label = \"" + label + "\" ]\n");
            }
        }

        graph.write("}\n");
        graph.close();
    }

    public void saveJFlap4(String filename) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter graph = new PrintWriter(filename, "UTF-8");

        graph.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n");
        graph.write("<structure>\n");
        graph.write("<type>fa</type>\n");
        graph.write("<automaton>\n");

        for(State s : states) {
            graph.write("<state id=\"" + s.identifier + "\" name=\"q" + s.identifier + "\">\n");

            if(s == start) {
                graph.write("\t<initial/>\n");
            }
            if(s.isAccepting()) {
                graph.write("\t<final/>\n");
            }
            graph.write("</state>\n");
        }

        for(State s : states) {
            for(Transition t: s.outgoingEdges) {
                graph.write("<transition>\n");
                graph.write("\t<from>" + t.getIncoming().identifier + "</from>\n");
                graph.write("\t<to>" + t.getOutgoing().identifier + "</to>\n");

                if(t.getValue() == 0) {
                    graph.write("\t<read/>\n");
                }else {
                    graph.write("\t<read>" + ""+t.getValue() + "</read>\n");
                }
                graph.write("</transition>\n");
            }
        }

        graph.write("</automaton>");
        graph.write("</structure>");
        graph.close();
    }

    public static void main(String[] args) {

        NFA n = NFA.Concat(
                NFA.Symbol('b'),
                    NFA.KleeneClosure(
                            NFA.Symbol('a')
                    )
                );
        try {
            n.saveGraphviz("graph.dot");
            n.saveJFlap4("nfa.jff");
        }catch(Exception e) {
            e.printStackTrace();
        }

    }

}

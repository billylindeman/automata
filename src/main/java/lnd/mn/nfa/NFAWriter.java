package lnd.mn.nfa;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * Created by billy on 7/26/14.
 */
public class NFAWriter {

    public static void saveGraphviz(NFA nfa, String filename) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter graph = new PrintWriter(filename, "UTF-8");

        graph.write("digraph NFA {\n");

        for(State s : nfa.states) {
            String shape = s.isAccepting() ? "doublecircle" : "circle";
            graph.write(s.identifier + " [ label = \"" + s.identifier + "\", shape =\"" + shape + "\" ] \n");
        }

        for(State s : nfa.states) {
            for(Transition t: s.outgoingEdges) {
                String label = t.getValue() == 0? "\u03B5" : ""+t.getValue();
                graph.write(t.getIncoming().identifier + "->" + t.getOutgoing().identifier + " [ label = \"" + label + "\" ]\n");
            }
        }

        graph.write("}\n");
        graph.close();
    }

    public static void saveJFlap4(NFA nfa, String filename) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter graph = new PrintWriter(filename, "UTF-8");

        graph.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n");
        graph.write("<structure>\n");
        graph.write("<type>fa</type>\n");
        graph.write("<automaton>\n");

        for(State s : nfa.states) {
            graph.write("<state id=\"" + s.identifier + "\" name=\"q" + s.identifier + "\">\n");

            if(s == nfa.start) {
                graph.write("\t<initial/>\n");
            }
            if(s.isAccepting()) {
                graph.write("\t<final/>\n");
            }
            graph.write("</state>\n");
        }

        for(State s : nfa.states) {
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

}

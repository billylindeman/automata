package lnd.mn.nfa;


import java.util.ArrayList;

/**
 * This class lexes and parses a regular expression into something resembling an AST
 *
 * Used by {@link NFA}
 */
public class RegExp {
    private ArrayList<Token> tokens;

    public RegExp(String regexp) {
        tokens = lex(regexp);
    }

    public ArrayList<Token> getTokens() {
        return tokens;
    }
    private ArrayList<Token> lex(String regexp) {
        ArrayList<Token> tokens = new ArrayList<Token>();

        for(char c : regexp.toCharArray()) {
            switch(c) {
                case '*': tokens.add(Token.star()); break;
                case '+': tokens.add(Token.plus()); break;
                case '|': tokens.add(Token.or()); break;
                case '(': tokens.add(Token.openParen()); break;
                case ')': tokens.add(Token.closeParen()); break;
                default:  tokens.add(Token.symbol(c)); break;
            }
        }
        return tokens;
    }

    public static class Token {
        public static enum Type {
            Symbol,
            Star,
            Plus,
            Or,
            OpenParen,
            CloseParen,
            OpenBracket,
            CloseBracket,
        }
        public static Token star() {
            return new Token(Token.Type.Star);
        }
        public static Token plus() {
            return new Token(Token.Type.Plus);
        }
        public static Token or() {
            return new Token(Token.Type.Or);
        }
        public static Token openParen() {
            return new Token(Type.OpenParen);
        }
        public static Token closeParen() {
            return new Token(Type.CloseParen);
        }
        public static Token symbol(char c) {
            return new Token(c);
        }

        public Token(Type t) {
            type = t;
        }
        public Token(char c) {
            type = Token.Type.Symbol;
            value = c;
        }
        private Type type;
        private char value;

        public Token.Type getType() {
            return type;
        }
        public char getValue() {
            return value;
        }
    }
}

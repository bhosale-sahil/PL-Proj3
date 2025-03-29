public class CompilerFrontendImpl extends CompilerFrontend {
    public CompilerFrontendImpl() {
        super();
    }

    public CompilerFrontendImpl(boolean debug_) {
        super(debug_);
    }

    /*
     * Initializes the local field "lex" to be equal to the desired lexer.
     * The desired lexer has the following specification:
     * 
     * NUM: [0-9]*\.[0-9]+
     * PLUS: \+
     * MINUS: -
     * TIMES: \*
     * DIV: /
     * WHITE_SPACE (' '|\n|\r|\t)*
     */
    @Override
    protected void init_lexer() {
        lex = new LexerImpl();

        // NUM: [0-9]*\.[0-9]+
        AutomatonImpl a_num = new AutomatonImpl();
        a_num.addState(0, true, false);
        a_num.addState(1, false, false);
        a_num.addState(2, false, false);
        a_num.addState(3, false, true);
    
        // [0-9]* (loop on 0)
        for (char d = '0'; d <= '9'; d++) {
            a_num.addTransition(0, d, 0);
            a_num.addTransition(0, d, 1); // handle .123
        }
    
        a_num.addTransition(0, '.', 2);   // Allow "."
        a_num.addTransition(1, '.', 2);   // Allow "123."
        
        // \.[0-9]+ (one or more digits after dot)
        for (char d = '0'; d <= '9'; d++) {
            a_num.addTransition(2, d, 3);
            a_num.addTransition(3, d, 3); // keep accepting digits
        }
    
        lex.add_automaton(TokenType.NUM, a_num);
    
        // PLUS: \+
        AutomatonImpl a_plus = new AutomatonImpl();
        a_plus.addState(0, true, false);
        a_plus.addState(1, false, true);
        a_plus.addTransition(0, '+', 1);
        lex.add_automaton(TokenType.PLUS, a_plus);
    
        // MINUS: -
        AutomatonImpl a_minus = new AutomatonImpl();
        a_minus.addState(0, true, false);
        a_minus.addState(1, false, true);
        a_minus.addTransition(0, '-', 1);
        lex.add_automaton(TokenType.MINUS, a_minus);
    
        // TIMES: \*
        AutomatonImpl a_times = new AutomatonImpl();
        a_times.addState(0, true, false);
        a_times.addState(1, false, true);
        a_times.addTransition(0, '*', 1);
        lex.add_automaton(TokenType.TIMES, a_times);
    
        // DIV: /
        AutomatonImpl a_div = new AutomatonImpl();
        a_div.addState(0, true, false);
        a_div.addState(1, false, true);
        a_div.addTransition(0, '/', 1);
        lex.add_automaton(TokenType.DIV, a_div);
    
        // LPAREN: \(
        AutomatonImpl a_lparen = new AutomatonImpl();
        a_lparen.addState(0, true, false);
        a_lparen.addState(1, false, true);
        a_lparen.addTransition(0, '(', 1);
        lex.add_automaton(TokenType.LPAREN, a_lparen);
    
        // RPAREN: \)
        AutomatonImpl a_rparen = new AutomatonImpl();
        a_rparen.addState(0, true, false);
        a_rparen.addState(1, false, true);
        a_rparen.addTransition(0, ')', 1);
        lex.add_automaton(TokenType.RPAREN, a_rparen);
    
        // WHITE_SPACE: (' '|\n|\r|\t)*
        AutomatonImpl a_ws = new AutomatonImpl();
        a_ws.addState(0, true, true);
        a_ws.addState(1, false, true);
        for (char c : new char[]{' ', '\n', '\r', '\t'}) {
            a_ws.addTransition(0, c, 1);
            a_ws.addTransition(1, c, 1);
        }
        lex.add_automaton(TokenType.WHITE_SPACE, a_ws);
    }

}


public class ParserImpl extends Parser {

    @Override
    public Expr do_parse() throws Exception {
        Expr result = parseT();

        if (tokens != null) {
            throw new Exception("Extra input after parsing");
        }

        return result;

    }

    private Expr parseT() throws Exception {
        Expr left = parseF();

        if (peek(TokenType.PLUS, 0) || peek(TokenType.MINUS, 0)) {
            Token op = consumeAddOp();
            Expr right = parseT();
            if (op.ty == TokenType.PLUS) {
                return new PlusExpr(left, right);
            } else {
                return new MinusExpr(left, right);
            }
        }

        return left;
    }

    private Expr parseF() throws Exception {
        Expr left = parseLit();

        if (peek(TokenType.TIMES, 0) || peek(TokenType.DIV, 0)) {
            Token op = consumeMulOp();
            Expr right = parseF();
            if (op.ty == TokenType.TIMES) {
                return new TimesExpr(left, right);
            } else {
                return new DivExpr(left, right);
            }
        }

        return left;
    }

    private Expr parseLit() throws Exception {
        if (peek(TokenType.NUM, 0)) {
            Token num = consume(TokenType.NUM);
            return new FloatExpr(Float.parseFloat(num.lexeme));
        } else if (peek(TokenType.LPAREN, 0)) {
            consume(TokenType.LPAREN);
            Expr inner = parseT();
            consume(TokenType.RPAREN);
            return inner;
        } else {
            throw new Exception("Expected NUM or (T)");
        }
    }

    private Token consumeAddOp() throws Exception {
        if (peek(TokenType.PLUS, 0)) {
            return consume(TokenType.PLUS);
        } else if (peek(TokenType.MINUS, 0)) {
            return consume(TokenType.MINUS);
        } else {
            throw new Exception("Expected PLUS or MINUS");
        }
    }

    private Token consumeMulOp() throws Exception {
        if (peek(TokenType.TIMES, 0)) {
            return consume(TokenType.TIMES);
        } else if (peek(TokenType.DIV, 0)) {
            return consume(TokenType.DIV);
        } else {
            throw new Exception("Expected TIMES or DIV");
        }
    }

}

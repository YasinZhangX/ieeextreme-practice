package com.yasin.tasks;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @author Yasin Zhang
 */
public class ImpactFactor {
    private static HashMap<Integer, Publication> publicationIndex = new HashMap<>();

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        in.nextLine();
        String json = in.nextLine();
        JSONParser parser = new JSONParser();
        JsonObject obj = (JsonObject) parser.fromJSON(json);
        JsonArray publicationArray = obj.getJsonArray("publications");
        for (Object o : publicationArray) {
            JsonObject tmp = (JsonObject) o;
            Publication p = new Publication();
            p.publicationTitle = (String) tmp.get("publicationTitle");
            p.publicationNumber = Integer.parseInt((String) tmp.get("publicationNumber"));
            JsonArray articleDetails = tmp.getJsonArray("articleCounts");
            p.year1 = Integer.parseInt((String) ((JsonObject)articleDetails.get(0)).get("year"));
            p.year2 = Integer.parseInt((String) ((JsonObject)articleDetails.get(1)).get("year"));
            p.y1PCnt = Integer.parseInt((String) ((JsonObject)articleDetails.get(0)).get("articleCount"));
            p.y2PCnt = Integer.parseInt((String) ((JsonObject)articleDetails.get(1)).get("articleCount"));
            publicationIndex.put(p.publicationNumber, p);
        }

        for (int i = 1; i < n; i++) {
            String tmp = in.nextLine();
            JsonObject article = (JsonObject) parser.fromJSON(tmp);
            JsonObject paperCitations = article.getJsonObject("paperCitations");
            JsonArray citations = paperCitations.getJsonArray("ieee");
            for (Object o : citations) {
                JsonObject citeObj = (JsonObject) o;
                Citation citation = new Citation();
                citation.year = Integer.parseInt((String) citeObj.get("year"));
                citation.publicationNumber =  Integer.parseInt((String) citeObj.get("publicationNumber"));
                flushPublicationsData(citation);
            }
        }

        List<Result> results = new ArrayList<>();
        for (Map.Entry<Integer, Publication> entry : publicationIndex.entrySet()) {
            Publication p = entry.getValue();
            double IF = div(add(p.y1CiteNum, p.y2CiteNum), add(p.y1PCnt, p.y2PCnt));
            DecimalFormat df = new DecimalFormat("#.00");
            String resultStr = p.publicationTitle + ": " + df.format(IF);
            Result result = new Result();
            result.IF = IF;
            result.result = resultStr;
            results.add(result);
        }
        Collections.sort(results, (o1, o2) -> {
            double subData = sub(o2.IF, o1.IF);
            if (subData > 0){
                return 1;
            } else if (subData < 0) {
                return -1;
            } else {
                return 0;
            }
        });

        for (Result r : results) {
            System.out.println(r.result);
        }
    }

    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    public static double div(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, 2, BigDecimal.ROUND_UP).doubleValue();
    }

    private static void flushPublicationsData(Citation citation) {
        Publication p = publicationIndex.get(citation.publicationNumber);
        if (p != null) {
            if (p.year1 == citation.year) {
                p.y1CiteNum++;
            }
            if (p.year2 == citation.year) {
                p.y2CiteNum++;
            }
        }
    }

}

class Result {
    double IF;
    String result;
}
class Publication {
    String publicationTitle;
    int publicationNumber;
    int year1;
    int year2;
    int y1PCnt;
    int y2PCnt;
    int y1CiteNum = 0;
    int y2CiteNum = 0;
}

class Citation {
    int year;
    int publicationNumber;
}

class JSONParser {

    private Tokenizer tokenizer = new Tokenizer();

    private Parser parser = new Parser();

    public Object fromJSON(String json) {
        try {
            CharReader charReader = new CharReader(new StringReader(json));
            TokenList tokens = tokenizer.tokenize(charReader);
            return parser.parse(tokens);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

class CharReader {

    private static final int BUFFER_SIZE = 1024;

    private Reader reader;

    private char[] buffer;

    private int pos;

    private int size;

    public CharReader(Reader reader) {
        this.reader = reader;
        buffer = new char[BUFFER_SIZE];
    }

    /**
     * 返回 pos 下标处的字符，并返回
     * @return
     */
    public char peek() {
        if (pos - 1 >= size) {
            return (char) -1;
        }

        return buffer[Math.max(0, pos - 1)];
    }

    /**
     * 返回 pos 下标处的字符，并将 pos + 1，最后返回字符
     * @return
     * @throws IOException
     */
    public char next() throws IOException {
        if (!hasMore()) {
            return (char) -1;
        }

        return buffer[pos++];
    }

    public void back() {
        pos = Math.max(0, --pos);
    }

    public boolean hasMore() throws IOException {
        if (pos < size) {
            return true;
        }

        fillBuffer();
        return pos < size;
    }

    void fillBuffer() throws IOException {
        int n = reader.read(buffer);
        if (n == -1) {
            return;
        }

        pos = 0;
        size = n;
    }
}

class Token {

    private TokenType tokenType;

    private String value;

    public Token(TokenType tokenType, String value) {
        this.tokenType = tokenType;
        this.value = value;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Token{" +
            "tokenType=" + tokenType +
            ", value='" + value + '\'' +
            '}';
    }
}


class Tokenizer {

    private CharReader charReader;

    private TokenList tokens;

    public TokenList tokenize(CharReader charReader) throws IOException {
        this.charReader = charReader;
        tokens = new TokenList();
        tokenize();

        return tokens;
    }

    private void tokenize() throws IOException {
        // 使用do-while处理空文件
        Token token;
        do {
            token = start();
            tokens.add(token);
        } while (token.getTokenType() != TokenType.END_DOCUMENT);
    }

    private Token start() throws IOException {
        char ch;
        for(;;) {
            if (!charReader.hasMore()) {
                return new Token(TokenType.END_DOCUMENT, null);
            }

            ch = charReader.next();
            if (!isWhiteSpace(ch)) {
                break;
            }
        }

        switch (ch) {
            case '{':
                return new Token(TokenType.BEGIN_OBJECT, String.valueOf(ch));
            case '}':
                return new Token(TokenType.END_OBJECT, String.valueOf(ch));
            case '[':
                return new Token(TokenType.BEGIN_ARRAY, String.valueOf(ch));
            case ']':
                return new Token(TokenType.END_ARRAY, String.valueOf(ch));
            case ',':
                return new Token(TokenType.SEP_COMMA, String.valueOf(ch));
            case ':':
                return new Token(TokenType.SEP_COLON, String.valueOf(ch));
            case 'n':
                return readNull();
            case 't':
            case 'f':
                return readBoolean();
            case '"':
                return readString();
            case '-':
                return readNumber();
        }

        if (isDigit(ch)) {
            return readNumber();
        }

        throw new RuntimeException("Illegal character");
    }

    private boolean isWhiteSpace(char ch) {
        return (ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n');
    }

    private Token readString() throws IOException {
        StringBuilder sb = new StringBuilder();
        for (;;) {
            char ch = charReader.next();
            if (ch == '\\') {
                if (!isEscape()) {
                    throw new RuntimeException("Invalid escape character");
                }
                sb.append('\\');
                ch = charReader.peek();
                sb.append(ch);
                if (ch == 'u') {
                    for (int i = 0; i < 4; i++) {
                        ch = charReader.next();
                        if (isHex(ch)) {
                            sb.append(ch);
                        } else {
                            throw new RuntimeException("Invalid character");
                        }
                    }
                }
            } else if (ch == '"') {
                return new Token(TokenType.STRING, sb.toString());
            } else if (ch == '\r' || ch == '\n') {
                throw new RuntimeException("Invalid character");
            } else {
                sb.append(ch);
            }
        }
    }

    private boolean isEscape() throws IOException {
        char ch = charReader.next();
        return (ch == '"' || ch == '\\' || ch == 'u' || ch == 'r'
            || ch == 'n' || ch == 'b' || ch == 't' || ch == 'f');

    }

    private boolean isHex(char ch) {
        return ((ch >= '0' && ch <= '9') || ('a' <= ch && ch <= 'f')
            || ('A' <= ch && ch <= 'F'));
    }

    private Token readNumber() throws IOException {
        char ch = charReader.peek();
        StringBuilder sb = new StringBuilder();
        if (ch == '-') {    // 处理负数
            sb.append(ch);
            ch = charReader.next();
            if (ch == '0') {    // 处理 -0.xxxx
                sb.append(ch);
                sb.append(readFracAndExp());
            } else if (isDigitOne2Nine(ch)) {
                do {
                    sb.append(ch);
                    ch = charReader.next();
                } while (isDigit(ch));
                if (ch != (char) -1) {
                    charReader.back();
                    sb.append(readFracAndExp());
                }
            } else {
                throw new RuntimeException("Invalid minus number");
            }
        } else if (ch == '0') {    // 处理小数
            sb.append(ch);
            sb.append(readFracAndExp());
        } else {
            do {
                sb.append(ch);
                ch = charReader.next();
            } while (isDigit(ch));
            if (ch != (char) -1) {
                charReader.back();
                sb.append(readFracAndExp());
            }
        }

        return new Token(TokenType.NUMBER, sb.toString());
    }

    private boolean isExp(char ch) throws IOException {
        return ch == 'e' || ch == 'E';
    }

    private boolean isDigit(char ch) {
        return ch >= '0' && ch <= '9';
    }

    private boolean isDigitOne2Nine(char ch) {
        return ch >= '0' && ch <= '9';
    }

    private String readFracAndExp() throws IOException {
        StringBuilder sb = new StringBuilder();
        char ch = charReader.next();
        if (ch ==  '.') {
            sb.append(ch);
            ch = charReader.next();
            if (!isDigit(ch)) {
                throw new RuntimeException("Invalid frac");
            }
            do {
                sb.append(ch);
                ch = charReader.next();
            } while (isDigit(ch));

            if (isExp(ch)) {    // 处理科学计数法
                sb.append(ch);
                sb.append(readExp());
            } else {
                if (ch != (char) -1) {
                    charReader.back();
                }
            }
        } else if (isExp(ch)) {
            sb.append(ch);
            sb.append(readExp());
        } else {
            charReader.back();
        }

        return sb.toString();
    }

    private String readExp() throws IOException {
        StringBuilder sb = new StringBuilder();
        char ch = charReader.next();
        if (ch == '+' || ch =='-') {
            sb.append(ch);
            ch = charReader.next();
            if (isDigit(ch)) {
                do {
                    sb.append(ch);
                    ch = charReader.next();
                } while (isDigit(ch));

                if (ch != (char) -1) {    // 读取结束，不用回退
                    charReader.back();
                }
            } else {
                throw new RuntimeException("e or E");
            }
        } else {
            throw new RuntimeException("e or E");
        }

        return sb.toString();
    }

    private Token readBoolean() throws IOException {
        if (charReader.peek() == 't') {
            if (!(charReader.next() == 'r' && charReader.next() == 'u' && charReader.next() == 'e')) {
                throw new RuntimeException("Invalid json string");
            }

            return new Token(TokenType.BOOLEAN, "true");
        } else {
            if (!(charReader.next() == 'a' && charReader.next() == 'l'
                && charReader.next() == 's' && charReader.next() == 'e')) {
                throw new RuntimeException("Invalid json string");
            }

            return new Token(TokenType.BOOLEAN, "false");
        }
    }

    private Token readNull() throws IOException {
        if (!(charReader.next() == 'u' && charReader.next() == 'l' && charReader.next() == 'l')) {
            throw new RuntimeException("Invalid json string");
        }

        return new Token(TokenType.NULL, "null");
    }
}

class Parser {

    private static final int BEGIN_OBJECT_TOKEN = 1;
    private static final int END_OBJECT_TOKEN = 2;
    private static final int BEGIN_ARRAY_TOKEN = 4;
    private static final int END_ARRAY_TOKEN = 8;
    private static final int NULL_TOKEN = 16;
    private static final int NUMBER_TOKEN = 32;
    private static final int STRING_TOKEN = 64;
    private static final int BOOLEAN_TOKEN = 128;
    private static final int SEP_COLON_TOKEN = 256;
    private static final int SEP_COMMA_TOKEN = 512;

    private TokenList tokens;

    public Object parse(TokenList tokens) {
        this.tokens = tokens;
        return parse();
    }

    private Object parse() {
        Token token = tokens.next();
        if (token == null) {
            return new JsonObject();
        } else if (token.getTokenType() == TokenType.BEGIN_OBJECT) {
            return parseJsonObject();
        } else if (token.getTokenType() == TokenType.BEGIN_ARRAY) {
            return parseJsonArray();
        } else {
            throw new RuntimeException("Parse error, invalid Token.");
        }
    }

    private JsonObject parseJsonObject() {
        JsonObject jsonObject = new JsonObject();
        int expectToken = STRING_TOKEN | END_OBJECT_TOKEN;
        String key = null;
        Object value = null;
        while (tokens.hasMore()) {
            Token token = tokens.next();
            TokenType tokenType = token.getTokenType();
            String tokenValue = token.getValue();
            switch (tokenType) {
                case BEGIN_OBJECT:
                    checkExpectToken(tokenType, expectToken);
                    jsonObject.put(key, parseJsonObject());    // 递归解析 json object
                    expectToken = SEP_COMMA_TOKEN | END_OBJECT_TOKEN;
                    break;
                case END_OBJECT:
                    checkExpectToken(tokenType, expectToken);
                    return jsonObject;
                case BEGIN_ARRAY:    // 解析 json array
                    checkExpectToken(tokenType, expectToken);
                    jsonObject.put(key, parseJsonArray());
                    expectToken = SEP_COMMA_TOKEN | END_OBJECT_TOKEN;
                    break;
                case NULL:
                    checkExpectToken(tokenType, expectToken);
                    jsonObject.put(key, null);
                    expectToken = SEP_COMMA_TOKEN | END_OBJECT_TOKEN;
                    break;
                case NUMBER:
                    checkExpectToken(tokenType, expectToken);
                    if (tokenValue.contains(".") || tokenValue.contains("e") || tokenValue.contains("E")) {
                        jsonObject.put(key, Double.valueOf(tokenValue));
                    } else {
                        Long num = Long.valueOf(tokenValue);
                        if (num > Integer.MAX_VALUE || num < Integer.MIN_VALUE) {
                            jsonObject.put(key, num);
                        } else {
                            jsonObject.put(key, num.intValue());
                        }
                    }
                    expectToken = SEP_COMMA_TOKEN | END_OBJECT_TOKEN;
                    break;
                case BOOLEAN:
                    checkExpectToken(tokenType, expectToken);
                    jsonObject.put(key, Boolean.valueOf(token.getValue()));
                    expectToken = SEP_COMMA_TOKEN | END_OBJECT_TOKEN;
                    break;
                case STRING:
                    checkExpectToken(tokenType, expectToken);
                    Token preToken = tokens.peekPrevious();
                    /*
                     * 在 JSON 中，字符串既可以作为键，也可作为值。
                     * 作为键时，只期待下一个 Token 类型为 SEP_COLON。
                     * 作为值时，期待下一个 Token 类型为 SEP_COMMA 或 END_OBJECT
                     */
                    if (preToken.getTokenType() == TokenType.SEP_COLON) {
                        value = token.getValue();
                        jsonObject.put(key, value);
                        expectToken = SEP_COMMA_TOKEN | END_OBJECT_TOKEN;
                    } else {
                        key = token.getValue();
                        expectToken = SEP_COLON_TOKEN;
                    }
                    break;
                case SEP_COLON:
                    checkExpectToken(tokenType, expectToken);
                    expectToken = NULL_TOKEN | NUMBER_TOKEN | BOOLEAN_TOKEN | STRING_TOKEN
                        | BEGIN_OBJECT_TOKEN | BEGIN_ARRAY_TOKEN;
                    break;
                case SEP_COMMA:
                    checkExpectToken(tokenType, expectToken);
                    expectToken = STRING_TOKEN;
                    break;
                case END_DOCUMENT:
                    checkExpectToken(tokenType, expectToken);
                    return jsonObject;
                default:
                    throw new RuntimeException("Unexpected Token.");
            }
        }

        throw new RuntimeException("Parse error, invalid Token.");
    }

    private JsonArray parseJsonArray() {
        int expectToken = BEGIN_ARRAY_TOKEN | END_ARRAY_TOKEN | BEGIN_OBJECT_TOKEN | NULL_TOKEN
            | NUMBER_TOKEN | BOOLEAN_TOKEN | STRING_TOKEN;
        JsonArray jsonArray = new JsonArray();
        while (tokens.hasMore()) {
            Token token = tokens.next();
            TokenType tokenType = token.getTokenType();
            String tokenValue = token.getValue();
            switch (tokenType) {
                case BEGIN_OBJECT:
                    checkExpectToken(tokenType, expectToken);
                    jsonArray.add(parseJsonObject());
                    expectToken = SEP_COMMA_TOKEN | END_ARRAY_TOKEN;
                    break;
                case BEGIN_ARRAY:
                    checkExpectToken(tokenType, expectToken);
                    jsonArray.add(parseJsonArray());
                    expectToken = SEP_COMMA_TOKEN | END_ARRAY_TOKEN;
                    break;
                case END_ARRAY:
                    checkExpectToken(tokenType, expectToken);
                    return jsonArray;
                case NULL:
                    checkExpectToken(tokenType, expectToken);
                    jsonArray.add(null);
                    expectToken = SEP_COMMA_TOKEN | END_ARRAY_TOKEN;
                    break;
                case NUMBER:
                    checkExpectToken(tokenType, expectToken);
                    if (tokenValue.contains(".") || tokenValue.contains("e") || tokenValue.contains("E")) {
                        jsonArray.add(Double.valueOf(tokenValue));
                    } else {
                        Long num = Long.valueOf(tokenValue);
                        if (num > Integer.MAX_VALUE || num < Integer.MIN_VALUE) {
                            jsonArray.add(num);
                        } else {
                            jsonArray.add(num.intValue());
                        }
                    }
                    expectToken = SEP_COMMA_TOKEN | END_ARRAY_TOKEN;
                    break;
                case BOOLEAN:
                    checkExpectToken(tokenType, expectToken);
                    jsonArray.add(Boolean.valueOf(tokenValue));
                    expectToken = SEP_COMMA_TOKEN | END_ARRAY_TOKEN;
                    break;
                case STRING:
                    checkExpectToken(tokenType, expectToken);
                    jsonArray.add(tokenValue);
                    expectToken = SEP_COMMA_TOKEN | END_ARRAY_TOKEN;
                    break;
                case SEP_COMMA:
                    checkExpectToken(tokenType, expectToken);
                    expectToken = STRING_TOKEN | NULL_TOKEN | NUMBER_TOKEN | BOOLEAN_TOKEN
                        | BEGIN_ARRAY_TOKEN | BEGIN_OBJECT_TOKEN;
                    break;
                case END_DOCUMENT:
                    checkExpectToken(tokenType, expectToken);
                    return jsonArray;
                default:
                    throw new RuntimeException("Unexpected Token.");
            }
        }

        throw new RuntimeException("Parse error, invalid Token.");
    }

    private void checkExpectToken(TokenType tokenType, int expectToken) {
        if ((tokenType.getTokenCode() & expectToken) == 0) {
            throw new RuntimeException("Parse error, invalid Token.");
        }
    }
}

class TokenList {

    private List<Token> tokens = new ArrayList<Token>();

    private int pos = 0;

    public void add(Token token) {
        tokens.add(token);
    }

    public Token peek() {
        return pos < tokens.size() ? tokens.get(pos) : null;
    }

    public Token peekPrevious() {
        return pos - 1 < 0 ? null : tokens.get(pos - 2);
    }

    public Token next() {
        return tokens.get(pos++);
    }

    public boolean hasMore() {
        return pos < tokens.size();
    }

    @Override
    public String toString() {
        return "TokenList{" +
            "tokens=" + tokens +
            '}';
    }
}

enum TokenType {
    BEGIN_OBJECT(1),
    END_OBJECT(2),
    BEGIN_ARRAY(4),
    END_ARRAY(8),
    NULL(16),
    NUMBER(32),
    STRING(64),
    BOOLEAN(128),
    SEP_COLON(256),
    SEP_COMMA(512),
    END_DOCUMENT(1024);

    TokenType(int code) {
        this.code = code;
    }

    private int code;

    public int getTokenCode() {
        return code;
    }
}

class JsonArray implements Iterable {

    private List list = new ArrayList();

    public void add(Object obj) {
        list.add(obj);
    }

    public Object get(int index) {
        return list.get(index);
    }

    public int size() {
        return list.size();
    }

    public JsonObject getJsonObject(int index) {
        Object obj = list.get(index);
        if (!(obj instanceof JsonObject)) {
            throw new RuntimeException("Type of value is not JsonObject");
        }

        return (JsonObject) obj;
    }

    public JsonArray getJsonArray(int index) {
        Object obj = list.get(index);
        if (!(obj instanceof JsonArray)) {
            throw new RuntimeException("Type of value is not JsonArray");
        }

        return (JsonArray) obj;
    }

    public Iterator iterator() {
        return list.iterator();
    }
}

class JsonObject {

    private Map<String, Object> map = new HashMap<String, Object>();

    public void put(String key, Object value) {
        map.put(key, value);
    }

    public Object get(String key) {
        return map.get(key);
    }

    public List<Map.Entry<String, Object>> getAllKeyValue() {
        return new ArrayList<>(map.entrySet());
    }

    public JsonObject getJsonObject(String key) {
        if (!map.containsKey(key)) {
            throw new IllegalArgumentException("Invalid key");
        }

        Object obj = map.get(key);
        if (!(obj instanceof JsonObject)) {
            throw new RuntimeException("Type of value is not JsonObject");
        }

        return (JsonObject) obj;
    }

    public JsonArray getJsonArray(String key) {
        if (!map.containsKey(key)) {
            throw new IllegalArgumentException("Invalid key");
        }

        Object obj = map.get(key);
        if (!(obj instanceof JsonArray)) {
            throw new RuntimeException("Type of value is not JsonArray");
        }

        return (JsonArray) obj;
    }
}

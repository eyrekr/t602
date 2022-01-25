package org.demo.tty;

public enum Ascii {
    Nothing(-2),
    Null(encode("00"), '\0'),
    EndOfText(encode("03")),
    EndOfTransmission(encode("04")),
    Tab(encode("09")),
    Enter(encode("0d")),
    FormFeed(encode("0c")),
    CarriageReturn(encode("0d")),
    Escape(encode("1b")),
    Space(encode("20"), ' '),
    ExclamationMark(encode("21"), '!'),
    DoubleQuotes(encode("22"), '"'),
    Hashtag(encode("23"), '#'),
    DollarSign(encode("24"), '$'),
    Percent(encode("25"), '%'),
    Ampersand(encode("26"), '&'),
    Tick(encode("27"), '\''),
    OpenParen(encode("28"), '('),
    CloseParen(encode("29"), ')'),
    Asterisk(encode("2a"), '*'),
    Plus(encode("2b"), '+'),
    Comma(encode("2c"), ','),
    Minus(encode("2d"), '-'),
    Dot(encode("2e"), '.'),
    Slash(encode("2f"), '/'),
    NumZero(encode("30"), '0'),
    NumOne(encode("31"), '1'),
    NumTwo(encode("32"), '2'),
    NumThree(encode("33"), '3'),
    NumFour(encode("34"), '4'),
    NumFive(encode("35"), '5'),
    NumSix(encode("36"), '6'),
    NumSeven(encode("37"), '7'),
    NumEight(encode("38"), '8'),
    NumNine(encode("39"), '9'),
    Colon(encode("3a"), ':'),
    Semicolon(encode("3b"), ';'),
    SmallerThan(encode("3c"), '<'),
    Equals(encode("3d"), '='),
    BiggerThan(encode("3e"), '>'),
    QuestionMark(encode("3f"), '?'),
    AtSign(encode("40"), '@'),
    CapitalA(encode("41"), 'A'),
    CapitalB(encode("42"), 'B'),
    CapitalC(encode("43"), 'C'),
    CapitalD(encode("44"), 'D'),
    CapitalE(encode("45"), 'E'),
    CapitalF(encode("46"), 'F'),
    CapitalG(encode("47"), 'G'),
    CapitalH(encode("48"), 'H'),
    CapitalI(encode("49"), 'I'),
    CapitalJ(encode("4a"), 'J'),
    CapitalK(encode("4b"), 'K'),
    CapitalL(encode("4c"), 'L'),
    CapitalM(encode("4d"), 'M'),
    CapitalN(encode("4e"), 'N'),
    CapitalO(encode("4f"), 'O'),
    CapitalP(encode("50"), 'P'),
    CapitalQ(encode("51"), 'Q'),
    CapitalR(encode("52"), 'R'),
    CapitalS(encode("53"), 'S'),
    CapitalT(encode("54"), 'T'),
    CapitalU(encode("55"), 'U'),
    CapitalV(encode("56"), 'V'),
    CapitalW(encode("57"), 'W'),
    CapitalX(encode("58"), 'X'),
    CapitalY(encode("59"), 'Y'),
    CapitalZ(encode("5a"), 'Z'),
    OpenBracket(encode("5b"), '['),
    Backslash(encode("5c"), '\\'),
    CloseBracket(encode("5d"), ']'),
    Roof(encode("5e"), '^'),
    Underscore(encode("5f"), '_'),
    Backtick(encode("60"), '`'),
    A(encode("61"), 'a'),
    B(encode("62"), 'b'),
    C(encode("63"), 'c'),
    D(encode("64"), 'd'),
    E(encode("65"), 'e'),
    F(encode("66"), 'f'),
    G(encode("67"), 'g'),
    H(encode("68"), 'h'),
    I(encode("69"), 'i'),
    J(encode("6a"), 'j'),
    K(encode("6b"), 'k'),
    L(encode("6c"), 'l'),
    M(encode("6d"), 'm'),
    N(encode("6e"), 'n'),
    O(encode("6f"), 'o'),
    P(encode("70"), 'p'),
    Q(encode("71"), 'q'),
    R(encode("72"), 'r'),
    S(encode("73"), 's'),
    T(encode("74"), 't'),
    U(encode("75"), 'u'),
    V(encode("76"), 'v'),
    W(encode("77"), 'w'),
    X(encode("78"), 'x'),
    Y(encode("79"), 'y'),
    Z(encode("7a"), 'z'),
    OpenCurly(encode("7b"), '{'),
    Pipe(encode("7c"), '|'),
    CloseCurly(encode("7d"), '}'),
    Tilde(encode("7e"), '~'),
    Backspace(encode("7f")),
    ArrowUp(encode("1b", "5b", "41")),
    ArrowDown(encode("1b", "5b", "42")),
    ArrowRight(encode("1b", "5b", "43")),
    ArrowLeft(encode("1b", "5b", "44")),
    F1(encode("1b", "4f", "50")),
    F2(encode("1b", "4f", "51")),
    F3(encode("1b", "4f", "52")),
    F4(encode("1b", "4f", "53")),
    F5(encode("1b", "5b", "31", "35", "7e")),
    F6(encode("1b", "5b", "31", "37", "7e")),
    F7(encode("1b", "5b", "31", "38", "7e")),
    F8(encode("1b", "5b", "31", "39", "7e")),
    F9(encode("1b", "5b", "32", "30", "7e")),
    F10(encode("1b", "5b", "32", "31", "7e")),
    ShiftTab(encode("1b", "5b", "5a")),
    Unsupported(-1),
    Error(-3);

    public final long code;
    public final char character;

    Ascii(long code, char character) {
        this.code = code;
        this.character = character;
    }

    Ascii(long code) {
        this(code, '\0');
    }

    static long encode(String... string) {
        long code = 0L;
        for (int i = 0; i < string.length; i++) {
            code |= Long.parseLong(string[i], 16) << (8 * i);
        }
        return code;
    }

    static long encode(byte[] bytes) {
        long code = 0L;
        for (int i = 0; i < 8; i++) {
            code |= ((long) bytes[i]) << (8 * i);
        }
        return code;
    }

    public static Ascii from(byte[] bytes) {
        long code = encode(bytes);
        for (Ascii key : Ascii.values()) {
            if (key.code == code) {
                return key;
            }
        }
        return Ascii.Unsupported;
    }
}
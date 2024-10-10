package Clases_Principales;

public class Token {

    private String lexema;
    private String traduccion;
    private String lenguaje;
    private String tipo;
    private String expresionRegular;  // Campo para la expresión regular
    private int fila;
    private int columna;

    // Constructor que incluye la expresión regular
    public Token(String lexema, String traduccion, String lenguaje, String tipo, String expresionRegular, int fila, int columna) {
        this.lexema = lexema;
        this.traduccion = traduccion;
        this.lenguaje = lenguaje;
        this.tipo = tipo;
        this.expresionRegular = expresionRegular;
        this.fila = fila;
        this.columna = columna;
    }

    // Getters para los atributos
    public String getLexema() {
        return lexema;
    }

    public String getTraduccion() {
        return traduccion;
    }

    public String getLenguaje() {
        return lenguaje;
    }

    public String getTipo() {
        return tipo;
    }

    public String getExpresionRegular() {
        return expresionRegular;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    @Override
    public String toString() {
        return "Token{"
                + "Lexema='" + lexema + '\''
                + ", Traducción='" + traduccion + '\''
                + ", Lenguaje='" + lenguaje + '\''
                + ", Tipo='" + tipo + '\''
                + ", Expresión Regular='" + expresionRegular + '\''
                + // Muestra la expresión regular
                ", Fila=" + fila
                + ", Columna=" + columna
                + '}';
    }
}

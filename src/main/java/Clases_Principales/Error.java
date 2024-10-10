package Clases_Principales;

public class Error {

    private String token;  // Token que causó el error
    private String lenguajeEncontrado;  // Lenguaje encontrado
    private String lenguajeSugerido;  // Lenguaje sugerido
    private int fila;  // Fila donde ocurrió el error
    private int columna;  // Columna donde ocurrió el error

    // Constructor para la clase Error
    public Error(String token, String lenguajeEncontrado, String lenguajeSugerido, int fila, int columna) {
        this.token = token;
        this.lenguajeEncontrado = lenguajeEncontrado;
        this.lenguajeSugerido = lenguajeSugerido;
        this.fila = fila;
        this.columna = columna;
    }

    // Getters para los atributos
    public String getToken() {
        return token;
    }

    public String getLenguajeEncontrado() {
        return lenguajeEncontrado;
    }

    public String getLenguajeSugerido() {
        return lenguajeSugerido;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    @Override
    public String toString() {
        return "Error{"
                + "Token='" + token + '\''
                + ", Lenguaje Encontrado='" + lenguajeEncontrado + '\''
                + ", Lenguaje Sugerido='" + lenguajeSugerido + '\''
                + ", Fila=" + fila
                + ", Columna=" + columna
                + '}';
    }
}

package Clases_Principales;

import java.util.ArrayList;
import java.util.List;

public abstract class Analizador_Lexico {

    protected List<Token> tokens;
    protected List<Error> errors;

    public Analizador_Lexico() {
        this.tokens = new ArrayList<>();
        this.errors = new ArrayList<>();
    }

    public abstract void Analizar(String linea, int fila);

    public List<Token> getTokens() {
        return tokens;
    }
    
    public List<Error> getErrors() {
        return errors; // Método para obtener la lista de errores
    }
}

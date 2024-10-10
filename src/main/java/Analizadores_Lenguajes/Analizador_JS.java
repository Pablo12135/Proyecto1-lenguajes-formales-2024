package Analizadores_Lenguajes;

import java.util.HashMap;
import Clases_Principales.Analizador_Lexico;
import Clases_Principales.Token;
import Clases_Principales.Error;

public class Analizador_JS extends Analizador_Lexico {

    private HashMap<String, String> palabrasReservadasJS;

    public Analizador_JS() {
        super();
        palabrasReservadasJS = new HashMap<>();
        // Palabras reservadas de JavaScript
        palabrasReservadasJS.put("function", "function");
        palabrasReservadasJS.put("const", "const");
        palabrasReservadasJS.put("let", "let");
        palabrasReservadasJS.put("document", "document");
        palabrasReservadasJS.put("event", "event");
        palabrasReservadasJS.put("alert", "alert");
        palabrasReservadasJS.put("for", "for");
        palabrasReservadasJS.put("forEach", "forEach");
        palabrasReservadasJS.put("display", "display");
        palabrasReservadasJS.put("background", "background");
        palabrasReservadasJS.put("while", "while");
        palabrasReservadasJS.put("if", "if");
        palabrasReservadasJS.put("else", "else");
        palabrasReservadasJS.put("return", "return");
        palabrasReservadasJS.put("console.log", "console.log");
        palabrasReservadasJS.put("null", "null");
        palabrasReservadasJS.put("document.querySelectorAll", "document.querySelectorAll");
        palabrasReservadasJS.put("document.getElementById", "document.getElementById");
        palabrasReservadasJS.put("window", "window");
        palabrasReservadasJS.put("querySelector", "querySelector");
    }

    @Override
    public void Analizar(String linea, int fila) {
        StringBuilder palabra = new StringBuilder();
        int columna = 1;
        boolean dentroDeCadena = false;
        char delimitadorCadena = '\0';

        for (int i = 0; i < linea.length(); i++) {
            char c = linea.charAt(i);

            // Si estamos dentro de una cadena
            if (dentroDeCadena) {
                palabra.append(c);
                if (c == delimitadorCadena) {
                    procesarPalabra(palabra.toString(), fila, columna);
                    palabra.setLength(0);
                    dentroDeCadena = false;
                }
                continue;
            }

            // Procesar delimitadores, operadores o espacios
            if (Character.isWhitespace(c) || ";,()[]{}".indexOf(c) != -1 || c == '.') {
                if (palabra.length() > 0) {
                    procesarPalabra(palabra.toString(), fila, columna);
                    palabra.setLength(0);
                }

                // Procesar el símbolo `:`
                if (c == ':') {
                    tokens.add(new Token(":", "Dos Puntos", "JavaScript", "Otro", ":", fila, columna));
                }

                // Procesar el punto (.) como un token separado
                if (c == '.') {
                    tokens.add(new Token(".", "Punto", "JavaScript", "Operador", ".", fila, columna));
                }

                // Procesar otros delimitadores, operadores o símbolos
                switch (c) {
                    case ';':
                        tokens.add(new Token(";", "Punto y Coma", "JavaScript", "Otro", ";", fila, columna));
                        break;
                    case ',':
                        tokens.add(new Token(",", "Coma", "JavaScript", "Otro", ",", fila, columna));
                        break;
                    case '{':
                        tokens.add(new Token("{", "Llave", "JavaScript", "Otro", "{", fila, columna));
                        break;
                    case '}':
                        tokens.add(new Token("}", "Llave", "JavaScript", "Otro", "}", fila, columna));
                        break;
                    case '[':
                        tokens.add(new Token("[", "Corchete", "JavaScript", "Otro", "[", fila, columna));
                        break;
                    case ']':
                        tokens.add(new Token("]", "Corchete", "JavaScript", "Otro", "]", fila, columna));
                        break;
                }

                columna++; // Avanzar la columna después de procesar el carácter
            } else if (c == '"' || c == '\'' || c == '`') {
                // Manejar cadenas (comillas simples, dobles o backticks)
                dentroDeCadena = true;
                delimitadorCadena = c;
                palabra.append(c);
            } else {
                // Añadir el carácter a la palabra en curso
                palabra.append(c);
            }
        }

        // Procesar la última palabra si la hay
        if (palabra.length() > 0) {
            procesarPalabra(palabra.toString(), fila, columna);
        }
    }

    // Método para procesar las palabras
    private void procesarPalabra(String palabra, int fila, int columna) {
        if (EsComentario(palabra)) {
            tokens.add(new Token(palabra, "Comentario", "JavaScript", "Comentario", "//", fila, columna));
        } else if (palabrasReservadasJS.containsKey(palabra)) {
            tokens.add(new Token(palabra, palabrasReservadasJS.get(palabra), "JavaScript", "PalabraReservada", palabra, fila, columna));
        } else if (EsIdentificador(palabra)) {
            tokens.add(new Token(palabra, "Identificador", "JavaScript", "[a-zA-Z]([a-zA-Z] | [0-9] | [ _ ])*", palabra, fila, columna));
        } else if (EsEntero(palabra)) {
            tokens.add(new Token(palabra, "Entero", "JavaScript", "[0-9]+", palabra, fila, columna));
        } else if (EsDecimal(palabra)) {
            tokens.add(new Token(palabra, "Decimal", "JavaScript", "[0-9]+ . [0-9]+", palabra, fila, columna));
        } else if (EsCadena(palabra)) {
            tokens.add(new Token(palabra, "Cadena", "JavaScript", "“Cualquier grupo Caracteres”,\n"
                    + "‘Cualquier grupo Caracteres’,\n"
                    + "`Cualquier grupo Caracteres`", palabra, fila, columna));
        } else if (palabra.equals("true") || palabra.equals("false")) {
            tokens.add(new Token(palabra, "Boolean", "JavaScript", "Booleano", palabra, fila, columna));
        } else if (EsOperadorLogico(palabra)) {
            tokens.add(new Token(palabra, "Operador Lógico", "JavaScript", "Logico", palabra, fila, columna));
        } else if (EsOperadorRelacional(palabra)) {
            tokens.add(new Token(palabra, "Operador Relacional", "JavaScript", "Relacional", palabra, fila, columna));
        } else if (EsOperadorAritmetico(palabra)) {
            tokens.add(new Token(palabra, "Operador Aritmetico", "JavaScript", "Aritmetico", palabra, fila, columna));
        } else if (EsIcrementales(palabra)) {
            tokens.add(new Token(palabra, "Operador Incremental", "JavaScript", "Incrementales", palabra, fila, columna));
        } else if (EsAsignacion(palabra)) {
            tokens.add(new Token(palabra, "Asignacion", "JavaScript", "Asignacion", palabra, fila, columna));
        } else if (esPalabraSoloLetras(palabra)) {
            tokens.add(new Token(palabra, "[a-z A-Z]", "JavaScript", "palabra", palabra, fila, columna));
        } else {
            if (!palabra.equals("")) {
                // Si no se reconoce, tratamos de sugerir a qué analizador podría pertenecer.
                String sugerencia = sugerirAnalizador(palabra);
                // Añadimos el token de error al reporte de errores.
                errors.add(new Error(palabra, "JavaScript", sugerencia, fila, columna));
            }
        }
    }

    // Método para detectar comentarios
    private boolean EsComentario(String palabra) {
        return palabra.startsWith("//");
    }

    private boolean EsOperadorLogico(String palabra) {
        return palabra.equals("&&") || palabra.equals("||") || palabra.equals("!");
    }

    // Método para validar identificadores
    private boolean EsIdentificador(String palabra) {
        // Verifica si la palabra está vacía o nula
        if (palabra == null || palabra.isEmpty()) {
            return false;
        }

        // El primer carácter debe ser una letra (a-zA-Z)
        if (!esLetra(palabra.charAt(0))) {
            return false;
        }

        // Los caracteres restantes deben ser letras, dígitos o guiones bajos
        for (int i = 1; i < palabra.length(); i++) {
            char c = palabra.charAt(i);
            if (!esLetraDigitoGuionBajo(c)) {
                return false;
            }
        }

        // Si se cumplen todas las condiciones, la palabra es válida
        return true;
    }

    public boolean esPalabraSoloLetras(String palabra) {
        // Verifica si la palabra está vacía o nula
        if (palabra == null || palabra.isEmpty()) {
            return false;
        }

        // Recorre cada carácter de la palabra
        for (int i = 0; i < palabra.length(); i++) {
            char c = palabra.charAt(i);

            // Si no es una letra (A-Z o a-z), retorna falso
            if (!esLetra(c)) {
                return false;
            }
        }

        // Si todos los caracteres son letras, retorna verdadero
        return true;
    }
// Método para verificar si un carácter es una letra (a-zA-Z)

    private boolean esLetra(char c) {
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
    }

// Método para verificar si un carácter es una letra, dígito o guion bajo
    private boolean esLetraDigitoGuionBajo(char c) {
        return esLetra(c) || Character.isDigit(c) || c == '_';
    }

    // Método para validar enteros
    private boolean EsEntero(String palabra) {
        if (palabra.isEmpty()) {
            return false;
        }
        for (char c : palabra.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    // Método para validar decimales
    private boolean EsDecimal(String palabra) {
        if (palabra.isEmpty()) {
            return false;
        }
        boolean puntoEncontrado = false;
        for (char c : palabra.toCharArray()) {
            if (c == '.') {
                if (puntoEncontrado) {
                    return false; // Solo un punto permitido
                }
                puntoEncontrado = true;
            } else if (!Character.isDigit(c)) {
                return false; // Solo dígitos permitidos
            }
        }
        return puntoEncontrado; // Debe haber al menos un punto
    }

    // Método para validar cadenas
    private boolean EsCadena(String palabra) {
        // comienzan y terminan con comillas simples o dobles
        return (palabra.startsWith("\"") && palabra.endsWith("\""))
                || (palabra.startsWith("'") && palabra.endsWith("'"))
                || (palabra.startsWith("`") && palabra.endsWith("`"));
    }

    private boolean EsOperadorRelacional(String palabra) {
        return palabra.equals("==") || palabra.equals("!=") || palabra.equals("<")
                || palabra.equals("<=") || palabra.equals(">") || palabra.equals(">=")
                || palabra.equals("=>");
    }

    private boolean EsOperadorAritmetico(String palabra) {
        return palabra.equals("+") || palabra.equals("-") || palabra.equals("*")
                || palabra.equals("/");
    }

    private boolean EsIcrementales(String palabra) {
        return palabra.equals("++") || palabra.equals("--");
    }

    private boolean EsAsignacion(String palabra) {
        return palabra.equals("=");
    }
    // Método para sugerir a qué analizador podría pertenecer el token.

    private String sugerirAnalizador(String palabra) {
        // Si parece una propiedad de CSS (tiene un ':')
        if (palabra.contains(":")) {
            return "CSS";
        }

        // Si parece una clase o id en CSS
        if (palabra.startsWith(".") || palabra.startsWith("#")) {
            return "CSS";
        }

        // Si no corresponde CSS, asumir que es HTML    
        return "HTML";
    }
}

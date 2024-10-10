package Analizadores_Lenguajes;

import java.util.HashMap;
import Clases_Principales.Analizador_Lexico;
import Clases_Principales.Token;
import Clases_Principales.Error;
import java.util.ArrayList;
import java.util.List;

public class Analizador_CSS extends Analizador_Lexico {

    private HashMap<String, String> ReglasCSS; // Reglas CSS definidas
    private StringBuilder cssContent; // Almacena el contenido CSS traducido
    private int tokenCount; // Contador de tokens
    private int tokenCountE; // Contador de tokens
    private final List<Token> tokens; // Lista para almacenar los tokens
    private final List<Error> errors; // Lista para almacenar los errores

    public Analizador_CSS() {
        super();
        // Inicializar el HashMap para las reglas CSS
        ReglasCSS = new HashMap<>();
        this.errors = new ArrayList<>(); // Inicializa la lista de errores
        this.tokens = new ArrayList<>(); // Inicializa la lista de tokens
        this.cssContent = new StringBuilder(); // Inicializa el StringBuilder para contenido CSS
        this.tokenCount = 0; // Inicializa el contador de tokens a 0

        // Reglas de estilo CSS
        inicializarReglasCSS();
    }

    // Método para inicializar las reglas CSS
    private void inicializarReglasCSS() {
        // Agregar las reglas CSS al HashMap
        String[] propiedades = {
            "color", "background-color", "background", "font-size", "font-weight", "font-family",
            "text-align", "width", "height", "min-width", "min-height", "max-width", "max-height",
            "display", "inline", "block", "inline-block", "flex", "grid", "none", "margin", "border",
            "padding", "content", "border-color", "border-style", "border-width", "border-top",
            "border-bottom", "border-left", "border-right", "box-sizing", "border-box", "position",
            "static", "relative", "absolute", "sticky", "fixed", "top", "bottom", "left", "right",
            "z-index", "justify-content", "align-items", "border-radius", "float", "list-style",
            "box-shadow", "url"
        };

        // Agregar propiedades al HashMap
        for (String propiedad : propiedades) {
            ReglasCSS.put(propiedad, propiedad);
        }
    }

    @Override
    public void Analizar(String linea, int fila) {
        int columna = 1; // Contador de columnas
        StringBuilder palabraActual = new StringBuilder(); // Almacena la palabra actual
        boolean enCadena = false; // Estado para detectar si estamos dentro de una cadena

        for (int i = 0; i < linea.length(); i++) {
            char caracter = linea.charAt(i);

            // Detectar inicio y fin de cadenas
            if (caracter == '\'') {
                enCadena = !enCadena; // Cambiar el estado de la cadena
                if (!enCadena) {
                    // Si cerramos la cadena, procesamos la palabra
                    procesarPalabra(palabraActual.toString() + caracter, fila, columna - palabraActual.length());
                    palabraActual.setLength(0); // Reiniciar el StringBuilder
                } else {
                    // Si abrimos una cadena, procesamos la palabra acumulada
                    if (palabraActual.length() > 0) {
                        procesarPalabra(palabraActual.toString(), fila, columna - palabraActual.length());
                        palabraActual.setLength(0); // Reiniciar el StringBuilder
                    }
                }
                columna++; // Aumentar la columna
                continue; // Continuar al siguiente carácter
            }

            // Ignorar las llaves
            if (caracter == '{' || caracter == '}') {
                continue; // Saltar el carácter y seguir con el análisis
            }

            // Procesar caracteres especiales y espacios
            if (caracter == ':' || caracter == ';' || caracter == ',') {
                // Procesar la palabra acumulada antes del token especial
                procesarPalabra(palabraActual.toString(), fila, columna - palabraActual.length());
                palabraActual.setLength(0); // Reiniciar el StringBuilder
                // Procesar el token especial
                procesarPalabra(String.valueOf(caracter), fila, columna);
            } else if (Character.isWhitespace(caracter)) {
                // Procesar palabra acumulada cuando hay espacio
                procesarPalabra(palabraActual.toString(), fila, columna - palabraActual.length());
                palabraActual.setLength(0); // Reiniciar el StringBuilder
            } else {
                palabraActual.append(caracter); // Acumular caracteres en la palabra actual
            }

            columna++; // Aumentar la columna
        }

        // Procesar la última palabra si existe
        procesarPalabra(palabraActual.toString(), fila, columna - palabraActual.length());
    }

    private void procesarPalabra(String palabra, int fila, int columna) {
        // Identificar el tipo de palabra y crear el token correspondiente
        if (palabra.equals(":")) {
            AgregarToken(new Token(palabra, "Dos puntos", "CSS", "Otros", ":", fila, columna));
        } else if (palabra.equals(";")) {
            AgregarToken(new Token(palabra, "Punto y coma", "CSS", "Otros", ";", fila, columna));
        } else if (palabra.equals(",")) {
            AgregarToken(new Token(palabra, "Coma", "CSS", "Otros", ",", fila, columna));
        } else if (EsComentario(palabra)) {
            AgregarToken(new Token(palabra, "Comentario", "CSS", "Comentario", palabra, fila, columna));
        } else if (ReglasCSS.containsKey(palabra)) {
            AgregarToken(new Token(palabra, "Propiedad", "CSS", "Propiedad", palabra, fila, columna));
        } else if (EsSelectorEtiqueta(palabra)) {
            AgregarToken(new Token(palabra, "Etiqueta", "CSS", "Etiqueta", palabra, fila, columna));
        } else if (EsSelectorUniversal(palabra)) {
            AgregarToken(new Token(palabra, "Selector Universal", "CSS", "Selector", "*", fila, columna));
        } else if (EsSelectorClase(palabra)) {
            AgregarToken(new Token(palabra, "Clase", "CSS", "De clase", palabra, fila, columna));
        } else if (EsSelectorID(palabra)) {
            AgregarToken(new Token(palabra, "ID", "CSS", "Selector ID", palabra, fila, columna));
        } else if (EsIdentificador(palabra)) {
            AgregarToken(new Token(palabra, "Identificador", "CSS", "Identificador", palabra, fila, columna));
        } else if (EsCombinador(palabra)) {
            AgregarToken(new Token(palabra, "Combinador", "CSS", "Combinador", palabra, fila, columna));
        } else if (EsPseudoclase(palabra)) {
            AgregarToken(new Token(palabra, "otro", "CSS", "Pseudoclase", palabra, fila, columna));
        } else if (EsValorMedida(palabra)) {
            AgregarToken(new Token(palabra, "Medida", "CSS", "Valor", palabra, fila, columna));
        } else if (EsColorHexadecimal(palabra) || EsColorRGBA(palabra)) {
            AgregarToken(new Token(palabra, "Color", "CSS", "Valor", palabra, fila, columna));
        } else if (EsCadena(palabra)) {
            AgregarToken(new Token(palabra, "Cadena", "CSS", "Cadena", palabra, fila, columna));
        } else if (EsEntero(palabra)) {
            AgregarToken(new Token(palabra, "Numero", "CSS", "Entero", palabra, fila, columna));
        } else if (EsDecimal(palabra)) {
            AgregarToken(new Token(palabra, "Numero", "CSS", "Decimal", palabra, fila, columna));
        } else if (esPalabraSoloLetras(palabra)) {
            AgregarToken(new Token(palabra, "'[A-Za-z]", "CSS", "otro", palabra, fila, columna));
        } else if (EsCadena(palabra)) {
            AgregarToken(new Token(palabra, "Arial', `Roman-ls`", "CSS", "cadena", palabra, fila, columna));
        } else {
            if (!palabra.equals("")){
// Si no se reconoce, tratamos de sugerir a qué analizador podría pertenecer.
            String sugerencia = sugerirAnalizador(palabra);
            // Añadimos el token de error al reporte de errores.
            System.out.println("palabra error"+palabra);
            AgregarTokenError(new Error(palabra, "CSS", sugerencia, fila, columna));
                    
            }

        }
    }
    // Método para verificar si una palabra es una cadena

    private boolean EsCadena(String palabra) {
        return (palabra.startsWith("'") && palabra.endsWith("'") && palabra.length() > 2)
                || (palabra.startsWith("//"));
    }

    private boolean EsComentario(String palabra) {
        // Un comentario en CSS generalmente comienza con '/*' y termina con '*/'
        return palabra.startsWith("/*") && palabra.endsWith("*/");
    }

    private boolean EsSelectorUniversal(String palabra) {
        // Verifica si el selector es universal ('*')
        return "*".equals(palabra);
    }

    // Verificar etiquetas válidas
    private boolean EsSelectorEtiqueta(String palabra) {
        // Lista simplificada de etiquetas HTML/CSS
        String[] etiquetasValidas = {
            "body", "header", "main", "nav", "aside", "div", "ul", "ol", "li",
            "a", "h1", "h2", "h3", "h4", "h5", "h6", "p", "span", "label",
            "textarea", "button", "section", "article", "footer", ".", "#",
            ">", "+", "~"
        };
        for (String etiqueta : etiquetasValidas) {
            if (etiqueta.equals(palabra)) {
                return true;
            }
        }
        return false;
    }

    private boolean EsSelectorClase(String palabra) {
        // Verifica que la palabra no esté vacía y que empiece con un punto
        if (palabra.isEmpty() || palabra.charAt(0) != '.') {
            return false;
        }

        // Verifica que la segunda letra sea una letra minúscula
        char secondChar = palabra.charAt(1);
        if (!Character.isLowerCase(secondChar)) {
            return false;
        }

        // Verifica el resto de los caracteres
        for (int i = 2; i < palabra.length(); i++) {
            char currentChar = palabra.charAt(i);
            // Permite letras minúsculas, dígitos y guiones
            if (!(Character.isLowerCase(currentChar) || Character.isDigit(currentChar) || currentChar == '-')) {
                return false;
            }
        }

        // Si se pasa todas las verificaciones, es un selector de clase válido
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

    private boolean esLetra(char c) {
        // Verifica si el carácter es una letra mayúscula (A-Z) o minúscula (a-z)
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
    }

    private boolean EsSelectorID(String palabra) {
        // Verifica que la palabra no sea nula y tenga al menos dos caracteres (# más una letra)
        // Verifica que la palabra no esté vacía y que empiece con un '#'
        if (palabra.isEmpty() || palabra.charAt(0) != '#') {
            return false;
        }

        // Verifica que la segunda letra sea una letra minúscula
        char secondChar = palabra.charAt(1);
        if (!Character.isLowerCase(secondChar)) {
            return false;
        }

        // Verifica el resto de los caracteres
        for (int i = 2; i < palabra.length(); i++) {
            char currentChar = palabra.charAt(i);
            // Permite letras minúsculas, dígitos y guiones
            if (!(Character.isLowerCase(currentChar) || Character.isDigit(currentChar) || currentChar == '-')) {
                return false;
            }
        }
        return true;
    }

    private boolean EsCombinador(String palabra) {
        // Combinadores en CSS: '>', '+', '~'
        return palabra.equals(">") || palabra.equals("+") || palabra.equals("~");
    }

    private boolean EsPseudoclase(String palabra) {
        String[] pseudoclases = {":hover", ":active", ":not", ":nth-child", ":before", ":after", ":focus"};
        for (String pseudoclase : pseudoclases) {
            if (pseudoclase.equals(palabra)) {
                return true;
            }
        }
        return false;
    }

    private boolean EsValorMedida(String palabra) {
        if (palabra.length() <= 2) {
            return false;
        }

        String unidad = palabra.substring(palabra.length() - 2);
        String valor = palabra.substring(0, palabra.length() - 2);

        // Verificar si la unidad es válida
        if (!esUnidadValida(unidad)) {
            return false;
        }

        // Verificar si el valor es un número entero
        return esNumeroEntero(valor);
    }

    private boolean esUnidadValida(String unidad) {
        String[] unidadesValidas = {"px", "%", "rem", "em", "vw", "vh", ":hover", ":active", ":not()", ":nth-child()",
            "odd", "even", "::before", "::after,", "(", ")"};
        for (String u : unidadesValidas) {
            if (u.equals(unidad)) {
                return true;
            }
        }
        return false;
    }

    private boolean EsColorHexadecimal(String palabra) {
        // Verifica si el color es hexadecimal (# seguido de 3 o 6 caracteres hexadecimales)
        if (palabra.startsWith("#")) {
            String hex = palabra.substring(1);
            // Verifica la longitud del valor hexadecimal
            if (hex.length() == 3 || hex.length() == 6) {
                // Verifica que cada carácter sea un dígito hexadecimal (0-9, a-f, A-F)
                for (char c : hex.toCharArray()) {
                    if (!esCaracterHexadecimal(c)) {
                        return false; // Si se encuentra un carácter no hexadecimal, retorna false
                    }
                }
                return true; // Es un color hexadecimal válido
            }
        }
        return false; // No es un color hexadecimal válido
    }

    private boolean esCaracterHexadecimal(char c) {
        // Verifica si el carácter es un dígito hexadecimal (0-9, a-f, A-F)
        return (c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F');
    }

    private boolean EsColorRGBA(String palabra) {
        if (!palabra.startsWith("rgba(") && !palabra.startsWith("rgb(")) {
            return false;
        }

        // Eliminar "rgba(" o "rgb("
        String parametros = palabra.substring(palabra.indexOf('(') + 1, palabra.length() - 1).trim();

        // Dividir los valores por coma
        String[] valores = parametros.split(",");

        if (valores.length < 3 || valores.length > 4) {
            return false;
        }

        // Validar los tres primeros valores (r, g, b)
        for (int i = 0; i < 3; i++) {
            if (!esNumeroEntero(valores[i].trim())) {
                return false;
            }
        }

        // Validar el cuarto valor si existe (alfa)
        if (valores.length == 4 && !esNumeroDecimal(valores[3].trim())) {
            return false;
        }

        return true;
    }

    // Métodos de validación para identificar diferentes tipos de tokens
    private boolean EsEntero(String palabra) {
        try {
            // Intenta convertir la palabra a un entero usando Integer.parseInt
            Integer.parseInt(palabra);
            // Si no se lanza excepción, la conversión fue exitosa, por lo que la palabra es un entero
            return true;
        } catch (NumberFormatException e) {
            // Si ocurre una excepción, la palabra no es un entero
            return false;
        }
    }

    private boolean EsDecimal(String palabra) {
        try {
            // Intenta convertir la palabra a un número decimal usando Double.parseDouble
            Double.parseDouble(palabra);
            // Verifica que la palabra contenga un punto, asegurando que sea un decimal, no un entero
            return palabra.contains(".");
        } catch (NumberFormatException e) {
            // Si ocurre una excepción, la palabra no es un decimal
            return false;
        }
    }

    private boolean esNumeroEntero(String valor) {
        try {
            int numero = Integer.parseInt(valor);
            return numero >= 0 && numero <= 255;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean esNumeroDecimal(String valor) {
        try {
            float numero = Float.parseFloat(valor);
            return numero >= 0 && numero <= 1;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean EsIdentificador(String palabra) {
        // Verifica que la palabra no esté vacía
        if (palabra.isEmpty()) {
            return false;
        }

        // Verifica que la primera letra sea una letra minúscula
        char firstChar = palabra.charAt(0);
        if (!Character.isLowerCase(firstChar)) {
            return false;
        }

        // Verifica el resto de los caracteres
        for (int i = 1; i < palabra.length(); i++) {
            char currentChar = palabra.charAt(i);
            // Permite letras minúsculas, dígitos y guiones
            if (!(Character.isLowerCase(currentChar) || Character.isDigit(currentChar) || currentChar == '-')) {
                return false;
            }
        }

        // Si se pasa todas las verificaciones, es un identificador válido
        return true;
    }

    // Método para agregar el token a la lista y al reporte
    private void AgregarToken(Token token) {
        if (!yaFueAgregado(token.getLexema())) { // Verificar duplicados
            tokens.add(token);
            this.tokenCount++; // Incrementar contador de tokens
        }
    }

    // Método para agregar el token a la lista y al reporte
    private void AgregarTokenError(Error errorr) {
        errors.add(errorr);
        this.tokenCountE++; // Incrementar contador de tokens
    }

    // Método para verificar si el token ya fue agregado
    private boolean yaFueAgregado(String lexema) {
        for (Token token : tokens) {
            if (token.getLexema().equals(lexema)) {
                return true; // El token ya fue agregado
            }
        }
        return false; // El token no fue agregado
    }

    public List<Token> getTokens() {
        return tokens; // Obtener los tokens identificados
    }

    public int getTokenCount() {
        return tokenCount; // Obtener el número total de tokens
    }

    public int getTokenCountE() {
        return tokenCountE; // Obtener el número total de tokens
    }

    public List<Error> getError() {
        return errors; // Obtener el número total de errores
    }

    private boolean EsNumero(String palabra) {
        // Verifica si la palabra es un número (entero o decimal)
        boolean puntoEncontrado = false; // Para rastrear si ya hemos encontrado un punto decimal
        boolean tieneDigitos = false; // Para asegurarnos de que hay al menos un dígito

        // Verificamos que no esté vacía
        if (palabra == null || palabra.isEmpty()) {
            return false;
        }

        for (int i = 0; i < palabra.length(); i++) {
            char caracter = palabra.charAt(i);

            // Verifica si el carácter es un dígito
            if (Character.isDigit(caracter)) {
                tieneDigitos = true; // Hay al menos un dígito
                continue; // Si es un dígito, seguimos
            }

            // Verifica si el carácter es un punto decimal
            if (caracter == '.') {
                if (puntoEncontrado) {
                    return false; // Si ya encontramos un punto, no es un número válido
                }
                puntoEncontrado = true; // Marcamos que hemos encontrado un punto
                continue; // Pasamos al siguiente carácter
            }

            // Si encontramos un carácter que no es un dígito ni un punto, no es un número
            return false;
        }

        // Debe haber al menos un dígito en la palabra
        return tieneDigitos;
    }

    private String sugerirAnalizador(String palabra) {

        if (palabra.startsWith("<") && palabra.endsWith(">")) {
            return "HTML";
        }// Si parece una etiqueta de HTML (abre con '<' y cierra con '>')
        if (palabra.startsWith("<") && palabra.endsWith(">")) {
            return "HTML";
        }

        // asumir que es js por efecto
        return "JS";
    }

}

package Analizadores_Lenguajes;

import Clases_Principales.Analizador_Lexico;
import Clases_Principales.Token;
import Clases_Principales.Error;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Analizador_HTML extends Analizador_Lexico {

    private HashMap<String, String> TraduccionesHTML;
    private HashMap<String, String> PalabrasReservadas;
    private final List<Token> tokens; // Lista para almacenar los tokens
    private final List<Error> errors; // Lista para almacenar los tokens
    private StringBuilder htmlContent; // Para almacenar el HTML traducido
    private int tokenCount; // Contador de tokens
    private int tokenCountE; // Contador de tokens error

    public Analizador_HTML() {
        super();
        TraduccionesHTML = new HashMap<>();
        inicializarTraduccionesHTML();
        PalabrasReservadas = new HashMap<>();
        inicializarPalabrasReservadas();
        this.tokens = new ArrayList<>(); // Inicializa la lista de tokens
        this.errors = new ArrayList<>();
        this.htmlContent = new StringBuilder(); // Inicializa el StringBuilder para contenido HTML
        this.tokenCount = 0; // Inicializa el contador de tokens a 0
        this.tokenCountE = 0; // Inicializa el contador de tokens a 0
    }

    // Método para inicializar las traducciones de etiquetas HTML personalizadas
    private void inicializarTraduccionesHTML() {
        TraduccionesHTML.put("<principal>", "<main>");
        TraduccionesHTML.put("<principal/>", "</main>");
        TraduccionesHTML.put("<encabezado>", "<header>");
        TraduccionesHTML.put("<encabezado/>", "</header>");
        TraduccionesHTML.put("<navegacion>", "<nav>");
        TraduccionesHTML.put("<navegacion/>", "</nav>");
        TraduccionesHTML.put("<apartado>", "<aside>");
        TraduccionesHTML.put("<apartado/>", "</aside>");
        TraduccionesHTML.put("<listaordenada>", "<ul>");
        TraduccionesHTML.put("<listaordenada/>", "</ul>");
        TraduccionesHTML.put("<listadesordenada>", "<ol>");
        TraduccionesHTML.put("<listadesordenada/>", "</ol>");
        TraduccionesHTML.put("<itemlista>", "<li>");
        TraduccionesHTML.put("<itemlista/>", "</li>");
        TraduccionesHTML.put("<anclaje>", "<a>");
        TraduccionesHTML.put("<anclaje/>", "</a>");
        TraduccionesHTML.put("<contenedor>", "<div>");
        TraduccionesHTML.put("<contenedor/>", "</div>");
        TraduccionesHTML.put("<seccion>", "<section>");
        TraduccionesHTML.put("<seccion/>", "</section>");
        TraduccionesHTML.put("<articulo>", "<article>");
        TraduccionesHTML.put("<articulo/>", "</article>");
        TraduccionesHTML.put("<parrafo>", "<p>");
        TraduccionesHTML.put("<parrafo/>", "</p>");
        TraduccionesHTML.put("<span>", "<span>");
        TraduccionesHTML.put("<span/>", "</span>");
        TraduccionesHTML.put("<entrada/>", "<input>");
        TraduccionesHTML.put("<formulario>", "<form>");
        TraduccionesHTML.put("<formulario/>", "</form>");
        TraduccionesHTML.put("<label>", "<label>");
        TraduccionesHTML.put("<label/>", "</label>");
        TraduccionesHTML.put("<area>", "<textarea>");
        TraduccionesHTML.put("<area/>", "</textarea>");
        TraduccionesHTML.put("<boton>", "<button>");
        TraduccionesHTML.put("<boton/>", "</button>");
        TraduccionesHTML.put("<titulo1>", "<h1>");
        TraduccionesHTML.put("<titulo1/>", "</h1>");
        TraduccionesHTML.put("<titulo2>", "<h2>");
        TraduccionesHTML.put("<titulo2/>", "</h2>");
        TraduccionesHTML.put("<titulo3>", "<h3>");
        TraduccionesHTML.put("<titulo3/>", "</h3>");
        TraduccionesHTML.put("<titulo4>", "<h4>");
        TraduccionesHTML.put("<titulo4/>", "</h4>");
        TraduccionesHTML.put("<titulo5>", "<h5>");
        TraduccionesHTML.put("<titulo5/>", "</h5>");
        TraduccionesHTML.put("<titulo6>", "<h6>");
        TraduccionesHTML.put("<titulo6/>", "</h6>");
        TraduccionesHTML.put("<piepagina>", "<footer>");
        TraduccionesHTML.put("<piepagina/>", "</footer>");
    }

    // Método para inicializar las palabras reservadas
    private void inicializarPalabrasReservadas() {
        PalabrasReservadas.put("class", "class");
        PalabrasReservadas.put("id", "id");
        PalabrasReservadas.put("href", "href");
        PalabrasReservadas.put("onclick", "onclick");
        PalabrasReservadas.put("style", "style");
        PalabrasReservadas.put("type", "type");
        PalabrasReservadas.put("placeholder", "placeholder");
        PalabrasReservadas.put("required", "required");
        PalabrasReservadas.put("name", "name");
        PalabrasReservadas.put("=", "=");
    }

    @Override
    public void Analizar(String linea, int fila) {
        int columna = 1;
        boolean dentroDeEtiqueta = false;
        StringBuilder palabraActual = new StringBuilder();

        for (int i = 0; i < linea.length(); i++) {
            char caracter = linea.charAt(i);

            if (caracter == '<') {
                if (palabraActual.length() > 0 && !dentroDeEtiqueta) {
                    procesarPalabra(palabraActual.toString(), fila, columna);
                    columna += palabraActual.length();
                    palabraActual.setLength(0);
                }
                palabraActual.append(caracter);
                dentroDeEtiqueta = true;
            } else if (caracter == '>') {
                palabraActual.append(caracter);
                procesarPalabra(palabraActual.toString(), fila, columna);
                columna += palabraActual.length();
                palabraActual.setLength(0);
                dentroDeEtiqueta = false;
            } else if (caracter == ' ' && !dentroDeEtiqueta) {
                if (palabraActual.length() > 0) {
                    procesarPalabra(palabraActual.toString(), fila, columna);
                    columna += palabraActual.length();
                    palabraActual.setLength(0);
                }
                columna++;
            } else {
                palabraActual.append(caracter);
            }
        }

        if (palabraActual.length() > 0) {
            procesarPalabra(palabraActual.toString(), fila, columna);
        }
    }

    private void procesarPalabra(String palabra, int fila, int columna) {
        palabra = palabra.trim();

        if (palabra.isEmpty()) {
            return;
        }

        // Verificación de si es una etiqueta HTML válida
        if (esEtiquetaHTML(palabra)) {
            if (TraduccionesHTML.containsKey(palabra)) {
                // Traducción de etiquetas personalizadas
                String traduccion = TraduccionesHTML.get(palabra);
                AgregarToken(new Token(palabra, palabra, "HTML", determinarTipo(palabra), traduccion, fila, columna));
                htmlContent.append(traduccion).append("\n");
            } else {
                // Extraer etiqueta base y atributos
                String etiquetaBase = extraerEtiquetaBase(palabra);
                String atributos = extraerAtributos(palabra);

                if (atributos != null && !atributos.isEmpty()) {
                    String etiquetaTraducida = traducirEtiqueta(etiquetaBase);

                    // Procesar cada atributo individualmente
                    for (String attr : atributos.split("\\s+")) {
                        int igualIndex = attr.indexOf('=');
                        if (igualIndex != -1) {
                            String clave = attr.substring(0, igualIndex).trim();
                            String valor = attr.substring(igualIndex + 1).trim();

                            // Quitar comillas del valor
                            if ((valor.startsWith("\"") && valor.endsWith("\"")) || (valor.startsWith("'") && valor.endsWith("'"))) {
                                valor = valor.substring(1, valor.length() - 1);
                            }

                            // Agregar token de atributo si es palabra reservada
                            if (PalabrasReservadas.containsKey(clave)) {
                                AgregarToken(new Token(clave, clave, "HTML", determinarAtribito(clave), clave + "=" + valor, fila, columna));
                            }
                        }
                    }

                    // Construir la etiqueta traducida con los atributos procesados
                    htmlContent.append("<").append(etiquetaTraducida).append(" ").append(atributos).append(">\n");
                } else {
                    // Manejar etiquetas sin atributos
                    htmlContent.append("<").append(etiquetaBase).append(">\n");
                }
            }
        } else if (!palabra.startsWith("<")) {
            // Procesar texto fuera de etiquetas
            AgregarToken(new Token(palabra, palabra, "HTML", "Texto", palabra, fila, columna));
            htmlContent.append(palabra).append(" ");
        } else {// Sugerencia para errores en etiquetas
            if (!palabra.equals("")) {
                String sugerencia = sugerirAnalizador(palabra);
                AgregarTokenError(new Error(palabra, "HTML", sugerencia, fila, columna));
                tokenCountE++;
            }
        }
    }

    private String traducirEtiqueta(String etiquetaBase) {
        // Lógica para traducir la etiqueta base
        switch (etiquetaBase.toLowerCase()) {
            case "boton":
                return "button"; // Puedes cambiar esto según tus necesidades
            case "anclaje":
                return "a";
            case "navegacion":
                return "nav";
            case "seccion":
                return "section";
            case "entrada/":
                return "input";
            case "formulario":
                return "form";
            case "area":
                return "textarea";
            case "contenedor":
                return "div";
            default:
                return etiquetaBase; // Devuelve la etiqueta original si no se encuentra una traducción
        }
    }

    private boolean esEtiquetaHTML(String palabra) {
        return palabra.startsWith("<") && palabra.endsWith(">");
    }

    // Método para extraer la etiqueta base
    private String extraerEtiquetaBase(String etiqueta) {
        int cierreEtiquetaIndex = etiqueta.indexOf('>');
        if (cierreEtiquetaIndex != -1) {
            String contenidoEtiqueta = etiqueta.substring(1, cierreEtiquetaIndex).trim();
            int espacioIndex = contenidoEtiqueta.indexOf(' '); // Buscar el primer espacio
            if (espacioIndex != -1) {
                return contenidoEtiqueta.substring(0, espacioIndex); // Retornar la parte antes del primer espacio
            } else {
                return contenidoEtiqueta; // Retornar la etiqueta completa si no hay espacios
            }
        }
        return etiqueta; // Retornar la etiqueta original si no se encuentra
    }

    private String extraerAtributos(String etiqueta) {
        StringBuilder atributos = new StringBuilder();
        int cierreEtiquetaIndex = etiqueta.indexOf('>');

        if (cierreEtiquetaIndex != -1) {
            // Obtener la parte de la etiqueta antes del cierre
            String contenidoEtiqueta = etiqueta.substring(1, cierreEtiquetaIndex).trim();

            int espacioIndex = contenidoEtiqueta.indexOf(' '); // Buscar el primer espacio

            if (espacioIndex != -1) {
                // Si hay un espacio, extraer todo lo que sigue como atributos
                atributos.append(contenidoEtiqueta.substring(espacioIndex + 1)); // Agregar atributos
            }
        }
        return atributos.toString().trim(); // Retornar los atributos sin espacios al inicio o al final
    }

    // Método para agregar un token a la lista
    private void AgregarToken(Token token) {
        tokens.add(token);
        tokenCount++;
    }

    // Método para agregar el token a la lista y al reporte
    private void AgregarTokenError(Error errorr) {
        errors.add(errorr);
        this.tokenCountE++; // Incrementar contador de tokens
    }

    // Método para obtener el contenido HTML generado
    public String getHtmlContent() {
        return htmlContent.toString();
    }

    // Método para obtener la lista de tokens
    public List<Token> getTokens() {
        return tokens;
    }

    public List<Error> getError() {
        return errors; // Obtener el número total de errores
    }

    private String determinarAtribito(String atributo) {
        switch (atributo) {
            case "class":
                return "palabra reservada";
            case "id":
                return "palabra reservada";
            case "href":
                return "palabra reservada";
            case "onclick":
                return "palabra reservada";
            case "style":
                return "palabra reservada";
            case "type":
                return "palabra reservada";
            case "placeholder":
                return "palabra reservada";
            case "required":
                return "palabra reservada";
            case "name":
                return "palabra reservada";
            default:
                return " "; // Valor por defecto para etiquetas no específicas
        }
    }

    // Método para determinar el tipo de etiqueta
    private String determinarTipo(String etiqueta) {
        switch (etiqueta) {
            case "<entrada/>":
                return "Una Línea";
            case "<area/>":
                return "Una Línea";
            case "<titulo1>":
                return "Una Línea";
            case "<titulo1/>":
                return "Una Línea";
            case "<titulo2>":
                return "Una Línea";
            case "<titulo2/>":
                return "Una Línea";
            case "<titulo3>":
                return "Una Línea";
            case "<titulo3/>":
                return "Una Línea";
            case "<titulo4>":
                return "Una Línea";
            case "<titulo4/>":
                return "Una Línea";
            case "<titulo5>":
                return "Una Línea";
            case "<titulo5/>":
                return "Una Línea";
            case "<titulo6>":
                return "Una Línea";
            case "<titulo6/>":
                return "Una Línea";
            case "<formulario>":
                return "Apertura";
            case "<formulario/>":
                return "Cierre";
            case "<label>":
                return "apertura";
            case "<label/>":
                return "cierre";
            case "<navegacion>":
                return "apertura";
            case "<navegacion/>":
                return "cierre";
            case "<apartado>":
                return "apertura";
            case "<apartado/>":
                return "cierre";
            case "<listaordenada>":
                return "apertura";
            case "<listaordenada/>":
                return "cierre";
            case "<listadesordenada>":
                return "apertura";
            case "<listadesordenada/>":
                return "cierre";
            case "<itemlista>":
                return "apertura";
            case "<itemlista/>":
                return "cierre";
            case "<anclaje>":
                return "apertura";
            case "<anclaje/>":
                return "cierre";
            case "<contenedor>":
                return "apertura";
            case "<contenedor/>":
                return "cierre";
            case "<seccion>":
                return "apertura";
            case "<seccion/>":
                return "cierre";
            case "<articulo>":
                return "apertura";
            case "<articulo/>":
                return "cierre";
            case "<parrafo>":
                return "apertura";
            case "<parrafo/>":
                return "cierre";
            case "<principal>":
                return "apertura";
            case "<principal/>":
                return "cierre";
            case "<encabezado>":
                return "apertura";
            case "<encabezado/>":
                return "cierre";
            case "<boton>":
                return "apertura";
            case "<boton/>":
                return "cierre";
            case "<piepagina>":
                return "apertura";
            case "<piepagina/>":
                return "Cierre"; // Estas etiquetas son de apertura y cierre
            default:
                return "Etiqueta"; // Valor por defecto para etiquetas no específicas
        }
    }

    private String sugerirAnalizador(String palabra) {

        // Si parece una propiedad de CSS (tiene un ':')
        if (palabra.contains(":")) {
            return "CSS";
        }

        // Si parece una clase o id en CSS
        if (palabra.startsWith(".") || palabra.startsWith("#")) {
            return "CSS";
        }

        // Si no corresponde a CSS, asumir que es JavaScript
        return "Js";
    }
}

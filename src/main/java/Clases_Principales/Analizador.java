package Clases_Principales;

import Analizadores_Lenguajes.Analizador_CSS;
import Analizadores_Lenguajes.Analizador_HTML;
import Analizadores_Lenguajes.Analizador_JS;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class Analizador {

    private String codigoFuente;
    private Gestor_Estados gestorEstados;
    private Analizador_HTML analizadorHTML;
    private Analizador_CSS analizadorCSS;
    private Analizador_JS analizadorJS;

    // Lista para almacenar todos los tokens
    private List<Token> todosLosTokens;
    private List<Token> tokensHtml;
    private List<Token> tokensJs;
    private List<Token> tokensCss;

    // Lista para almacenar todos los errores
    private List<Error> erroresEncontrados;
    private List<Error> tokensHtmlE;
    private List<Error> tokensJsE;
    private List<Error> tokensCssE;

    // Contador de líneas actual
    private int numeroDeLineaActual = 1;

    public Analizador(String codigoFuente) {
        this.codigoFuente = codigoFuente;
        this.gestorEstados = new Gestor_Estados();
        this.analizadorHTML = new Analizador_HTML();
        this.analizadorCSS = new Analizador_CSS();
        this.analizadorJS = new Analizador_JS();

        this.todosLosTokens = new ArrayList<>();
        this.tokensCss = new ArrayList<>();
        this.tokensHtml = new ArrayList<>();
        this.tokensCss = new ArrayList<>();

        // Inicializar la lista de tokens
        this.erroresEncontrados = new ArrayList<>(); // Inicializar la lista de errores
        this.tokensCssE = new ArrayList<>();
        this.tokensHtmlE = new ArrayList<>();
        this.tokensCssE = new ArrayList<>();

    }

    public List<Token> analizar() {
        List<String> lineas = new ArrayList<>();
        StringBuilder lineaActual = new StringBuilder();

        // Recorrer cada carácter en el código fuente
        for (int i = 0; i < codigoFuente.length(); i++) {
            char c = codigoFuente.charAt(i);
            if (c == '\n') {
                lineas.add(lineaActual.toString());
                lineaActual.setLength(0);
                // Contador de líneas actual
                numeroDeLineaActual++;
            } else {
                lineaActual.append(c);
            }
        }

        if (lineaActual.length() > 0) {
            lineas.add(lineaActual.toString());
        }

        StringBuilder cssContent = new StringBuilder();
        StringBuilder jsContent = new StringBuilder();
        StringBuilder htmlContent = new StringBuilder();

        String estadoActual = null;
        int numeroDeFila = 1;

        // Procesar cada línea del código fuente
        for (String linea : lineas) {
            linea = linea.trim();
            String nuevoEstado = obtenerEstado(linea);
            if (nuevoEstado != null) {
                estadoActual = nuevoEstado;
            } else {
                analizarLinea(linea, estadoActual, numeroDeFila, cssContent, jsContent, htmlContent);
            }
            numeroDeFila++;
        }
        this.todosLosTokens.addAll(tokensJs);
        this.todosLosTokens.addAll(tokensCss);
        this.todosLosTokens.addAll(tokensHtml);

        this.erroresEncontrados.addAll(tokensJsE);
        this.erroresEncontrados.addAll(tokensCssE);
        this.erroresEncontrados.addAll(tokensHtmlE);

        generarSalida(cssContent, jsContent, htmlContent);

        // Retornar los tokens obtenidos
        return todosLosTokens;
    }

    private void analizarLinea(String linea, String estadoActual, int numeroDeFila,
            StringBuilder cssContent, StringBuilder jsContent, StringBuilder htmlContent) {
        if (estadoActual != null) {
            switch (estadoActual) {
                case ">>[html]":
                    analizadorHTML.Analizar(linea, numeroDeFila);
                    htmlContent.append(linea.trim());
                    // Añadir los tokens HTML a la lista
                    this.tokensHtml = new ArrayList<>();
                    tokensHtml.addAll(analizadorHTML.getTokens());

                    this.tokensHtmlE = new ArrayList<>();
                    // Recoger los errores del analizador HTML
                    tokensHtmlE.addAll(analizadorHTML.getError());

                    break;
                case ">>[css]":
                    analizadorCSS.Analizar(linea, numeroDeFila);
                    cssContent.append(linea).append("\n");
                    // Añadir los tokens CSS a la lista
                    this.tokensCss = new ArrayList<>();
                    this.tokensCssE = new ArrayList<>();
                   
                    tokensCss.addAll(analizadorCSS.getTokens());
                    // Recoger los errores del analizador CSS
                    tokensCssE.addAll(analizadorCSS.getError());

                    break;
                case ">>[js]":
                    this.tokensJs = new ArrayList<>();
                    this.tokensJsE = new ArrayList<>();
                    analizadorJS.Analizar(linea, numeroDeFila);
                    jsContent.append(linea).append("\n");
                    // Añadir los tokens JS a la lista
                    tokensJs.addAll(analizadorJS.getTokens());
                    // Recoger los errores del analizador JS
                    tokensJsE.addAll(analizadorJS.getErrors());

                    break;
                default:
                    System.out.println("Estado no reconocido: " + linea);
            }
        }
    }

    private void generarSalida(StringBuilder cssContent, StringBuilder jsContent, StringBuilder htmlContent) {
        // Verificar si hay errores antes de proceder
        /*if (!erroresEncontrados.isEmpty()) {
            // Mostrar mensaje de advertencia sobre los errores
            JOptionPane.showMessageDialog(null,
                    "Existen errores en el código. No se puede generar la página HTML.\nPor favor, corrige los errores antes de continuar.",
                    "Errores encontrados", JOptionPane.ERROR_MESSAGE);
            return; // Salir del método para evitar la generación del archivo
        }*/

        // Si no hay errores, continuar con la generación del archivo HTML
        String contenidoHTMLTraducido = analizadorHTML.getHtmlContent();
        // Mostrar el contenido HTML en un cuadro de diálogo
        JOptionPane.showMessageDialog(null, "HTML traducido:\n" + contenidoHTMLTraducido,
                "Resultado de Traducción", JOptionPane.INFORMATION_MESSAGE);

        try {
            Generador_HTML generador = new Generador_HTML();
            generador.generarHTML(cssContent.toString(), jsContent.toString(), contenidoHTMLTraducido, "Generado.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String obtenerEstado(String linea) {
        String estado = null;

        // Determinar el estado basado en la línea
        if (linea.startsWith(">>[html]")) {
            estado = ">>[html]";
            System.out.println("Estado cambiado a HTML");
        } else if (linea.startsWith(">>[css]")) {
            estado = ">>[css]";
            System.out.println("Estado cambiado a CSS");
        } else if (linea.startsWith(">>[js]")) {
            estado = ">>[js]";
            System.out.println("Estado cambiado a JS");
        }

        // Si se ha cambiado el estado, crear y agregar el token correspondiente
        if (estado != null) {
            int fila = obtenerNumeroDeLinea(linea);
            int columna = obtenerNumeroDeColumna(linea);

            Token tokenEstado = new Token(
                    estado,
                    "",
                    "",
                    "Estado",
                    "",
                    fila,
                    columna
            );

            todosLosTokens.add(tokenEstado);
            
        }

        return estado;
    }

    private int obtenerNumeroDeLinea(String linea) {
        return 0;
    }

    private int obtenerNumeroDeColumna(String linea) {
        return 0;
    }

    // Método para obtener los tokens
    public List<Token> getTokens() {
        return todosLosTokens;
    }

    // Método para obtener los errores
    public List<Error> getErrores() {
        return erroresEncontrados;
    }
}

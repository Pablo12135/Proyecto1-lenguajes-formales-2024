package Reportes;

import Clases_Principales.Token;
import javax.swing.*;
import java.awt.*;
import java.util.List; // Asegúrate de importar List

public class Reporte_Panel extends JPanel {

    private final List<Token> tokens;
    private final int cantidadTokens;

    public Reporte_Panel(List<Token> tokens, int cantidadTokens) {
        this.tokens = tokens;
        this.cantidadTokens = cantidadTokens;
        inicializarPanel();
    }

    private void inicializarPanel() {
        setLayout(new BorderLayout());

        // Crear un JEditorPane para mostrar el contenido del reporte
        JEditorPane editorPane = new JEditorPane();
        editorPane.setEditable(false);
        editorPane.setContentType("text/html");

        // Generar el contenido HTML del reporte
        StringBuilder contenidoReporte = new StringBuilder();
        contenidoReporte.append("<html><body>")
                .append("<h1>Reporte de Tokens</h1>")
                .append("<table border='1' style='border-collapse:collapse; width: 100%;'>") // Tabla expandida al 100%
                .append("<tr>")
                .append("<th>Token</th>")
                .append("<th>Expresión Regular</th>")
                .append("<th>Lenguaje</th>")
                .append("<th>Tipo</th>")
                .append("<th>fila</th>")
                .append("<th>Columna</th>")
                .append("</tr>");

        // Recorrer todos los tokens y agregarlos a la tabla
        for (int i = 0; i < cantidadTokens; i++) {
            Token token = tokens.get(i);
            // Escapar los caracteres < y > para evitar interpretación HTML
            String lexemaEscapado = token.getLexema()
                    .replace("<", "&lt;")
                    .replace(">", "&gt;");

            contenidoReporte.append("<tr>")
                    .append("<td>").append(lexemaEscapado).append("</td>")
                    .append("<td>").append(token.getExpresionRegular()).append("</td>")
                    .append("<td>").append(token.getLenguaje()).append("</td>")
                    .append("<td>").append(token.getTipo()).append("</td>")
                    .append("<td>").append(token.getFila()).append("</td>")
                    .append("<td>").append(token.getColumna()).append("</td>")
                    .append("</tr>");
        }

        contenidoReporte.append("</table>")
                .append("</body></html>");

        // Asignar el contenido HTML al JEditorPane
        editorPane.setText(contenidoReporte.toString());

        // Crear un JScrollPane para agregar barras de desplazamiento automáticas
        JScrollPane scrollPane = new JScrollPane(editorPane);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // Barra vertical siempre visible
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); // Barra horizontal según sea necesario
        scrollPane.setPreferredSize(new Dimension(800, 600)); // Ajustar el tamaño según necesidad
        add(scrollPane, BorderLayout.CENTER); // Añadir el JScrollPane al panel principal

        // Agregar un botón de ir al inicio
        JButton volverAlInicioButton = new JButton("Volver al Inicio");
        volverAlInicioButton.addActionListener(e -> editorPane.setCaretPosition(0));

        // Panel para el botón
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(volverAlInicioButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}

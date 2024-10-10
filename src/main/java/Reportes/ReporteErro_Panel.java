package Reportes;

import Clases_Principales.Error;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ReporteErro_Panel extends JPanel {

    private final List<Error> errores; // Lista de errores

    public ReporteErro_Panel(List<Error> errores) {
        this.errores = errores;
        inicializarPanel(); // Inicializar el panel cuando se crea la instancia
    }

    // Método para inicializar el panel y añadir los componentes del reporte
    private void inicializarPanel() {
        setLayout(new BorderLayout());

        // Crear un área para mostrar el reporte en HTML
        JEditorPane editorPane = new JEditorPane();
        editorPane.setEditable(false);
        editorPane.setContentType("text/html"); // Establecer el tipo de contenido a HTML

        // Construir el contenido del reporte en HTML
        StringBuilder contenidoReporte = new StringBuilder();
        contenidoReporte.append("<html><body>")
                .append("<h1>Reporte de Errores</h1>")
                .append("<table border='1' style='border-collapse:collapse;'>")
                .append("<tr>")
                .append("<th>Token</th>")
                .append("<th>Lenguaje Encontrado</th>")
                .append("<th>Lenguaje Sugerido</th>")
                .append("<th>fila</th>")
                .append("<th>Columna</th>")
                .append("</tr>");

        // Añadir cada error a la tabla
        for (Error error : errores) {
            contenidoReporte.append("<tr>")
                    .append("<td>").append(error.getToken()).append("</td>")
                    .append("<td>").append(error.getLenguajeEncontrado()).append("</td>")
                    .append("<td>").append(error.getLenguajeSugerido()).append("</td>")
                    .append("<td>").append(error.getFila()).append("</td>")
                    .append("<td>").append(error.getColumna()).append("</td>")
                    .append("</tr>");
        }

        contenidoReporte.append("</table>")
                .append("</body></html>");

        // Establecer el contenido en el área de edición
        editorPane.setText(contenidoReporte.toString());

        // Ajustar el tamaño del JEditorPane
        editorPane.setPreferredSize(new Dimension(800, 600)); // Ajusta el tamaño según tus necesidades

        // Añadir el área de edición con un scroll
        JScrollPane scrollPane = new JScrollPane(editorPane);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.CENTER);

        // Botón para volver al inicio
        JButton volverAlInicioButton = new JButton("Volver al Inicio");
        volverAlInicioButton.addActionListener(e -> editorPane.setCaretPosition(0)); // Regresar al inicio del área de texto

        // Panel para contener el botón
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(volverAlInicioButton);

        // Añadir el panel de botones al sur
        add(buttonPanel, BorderLayout.SOUTH);
    }
}

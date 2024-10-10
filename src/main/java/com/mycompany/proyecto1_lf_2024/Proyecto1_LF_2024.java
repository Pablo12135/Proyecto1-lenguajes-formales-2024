package com.mycompany.proyecto1_lf_2024;

import Clases_Principales.Analizador;
import javax.swing.*;
import java.awt.*;
import Clases_Principales.Editor_Texto;
import Clases_Principales.Optimizar_Codigo;
import Clases_Principales.Token;
import Reportes.Reporte;
import Clases_Principales.Error;
import Reportes.Reporte_error;
import java.util.List;

public class Proyecto1_LF_2024 extends JFrame {

    private Editor_Texto editorTexto;
    private JLabel statusLabel;
    private List<Token> tokens;
    private List<Error> errors;

    public Proyecto1_LF_2024() {
      
        setTitle("Analizador y Traductor");
        setSize(1000, 600); // Tamaño inicial de la ventana
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Etiqueta de estado para mostrar la línea y columna del cursor
        statusLabel = new JLabel("Línea: 1, Columna: 1");

        // Inicializa el editor de texto con el JLabel para mostrar el estado
        editorTexto = new Editor_Texto(statusLabel);

        // Agrega el editor de texto dentro de un JScrollPane para que tenga barra de desplazamiento
        JScrollPane scrollPane = new JScrollPane(editorTexto);
        add(scrollPane, BorderLayout.CENTER);  // Coloca el área de texto en el centro del BorderLayout

        // Botón para analizar el código
        JButton analyzeButton = new JButton("Analizar Código");
        analyzeButton.addActionListener(e -> analizarCodigo());

        // Botón para generar el reporte
        JButton BotonReporte = new JButton("Generar Reporte");
        BotonReporte.addActionListener(e -> {
            if (tokens != null && !tokens.isEmpty()) {  // Asegurarse de que la lista no esté vacía
                Reporte reporte = new Reporte(tokens, tokens.size());
                reporte.generarReporte();
            } else {
                JOptionPane.showMessageDialog(this, "Primero analice el código para generar un reporte.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        // Botón para ver el reporte de errores
        JButton BotonReporteE = new JButton("Generar Reporte de Errores");
        BotonReporteE.addActionListener(e -> {
            if (errors != null && !errors.isEmpty()) {  // Asegurarse de que la lista no esté vacía
                Reporte_error reportes = new Reporte_error(errors);
                reportes.generarReporte();
            } else {
                JOptionPane.showMessageDialog(this, "Primero analice el código para generar un reporte.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Panel para los botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(analyzeButton);
        buttonPanel.add(BotonReporte);
        buttonPanel.add(BotonReporteE);

        // Agrega el panel de botones en la parte superior
        add(buttonPanel, BorderLayout.NORTH);

        // Agrega la etiqueta de estado en la parte inferior
        add(statusLabel, BorderLayout.SOUTH);

        // Configura la barra de menú usando el método estático del editor de texto
        setJMenuBar(Editor_Texto.crearMenu(editorTexto));
    }

    // Método para analizar el código
    private void analizarCodigo() {
        // Analiza el código fuente del EditorTexto
        String codigo = editorTexto.getText();

        // Crear una instancia de OptimizarCodigo
        Optimizar_Codigo optimizador = new Optimizar_Codigo(codigo);

        // Obtener el código optimizado
        String codigoOptimizado = optimizador.optimizar();

        // Crear una instancia de Analizador con el código optimizado
        Analizador analizador = new Analizador(codigoOptimizado);

        // Llamar al método de análisis
        analizador.analizar();

        // Obtiene los tokens analizados
        tokens = analizador.getTokens();
                
        errors = analizador.getErrores();
    }

    // Método principal para ejecutar la aplicación
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Proyecto1_LF_2024 frame = new Proyecto1_LF_2024();
            frame.setVisible(true);
        });
    }
}

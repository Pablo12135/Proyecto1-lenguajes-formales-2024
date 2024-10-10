package Reportes;

import Clases_Principales.Token;
import javax.swing.*;
import java.util.List; // Asegúrate de importar List

public class Reporte {
    private List<Token> tokens;
    private final int cantidadTokens;

    // Constructor que inicializa el reporte con una lista de tokens y su cantidad
    public Reporte(List<Token> tokens, int cantidadTokens) {
        this.tokens = tokens;
        this.cantidadTokens = cantidadTokens;
    }

    // Método para generar el reporte y mostrarlo en una ventana
    public void generarReporte() {
        // Crear el panel del reporte
        Reporte_Panel reportePanel = new Reporte_Panel(tokens, cantidadTokens);

        // Mostrar el panel en una ventana
        JOptionPane.showMessageDialog(null, reportePanel, "Reporte de Tokens", JOptionPane.PLAIN_MESSAGE);
    }
}
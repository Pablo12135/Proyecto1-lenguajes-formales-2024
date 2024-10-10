package Reportes;

import Clases_Principales.Error;
import java.util.List;
import javax.swing.JOptionPane;

public class Reporte_error {

    private List<Error> errores; // Lista de errores

    // Constructor modificado para aceptar una lista de errores
    public Reporte_error(List<Error> errores) {
        this.errores = errores;
    }

    // Método para agregar un error a la lista (opcional, ya que se pasa la lista al constructor)
    public void agregarError(String token, String lenguajeEncontrado, String lenguajeSugerido, int fila, int columna) {
        errores.add(new Error(token, lenguajeEncontrado, lenguajeSugerido, fila, columna));
        System.out.println("Error agregado: " + token); // Mensaje de depuración
    }

    // Método para generar el reporte y mostrarlo en una ventana
    public void generarReporte() {
        // Crear el panel del reporte de errores
        ReporteErro_Panel reportePanel = new ReporteErro_Panel(errores);

        // Mostrar el panel en una ventana
        JOptionPane.showMessageDialog(null, reportePanel, "Reporte de Errores", JOptionPane.PLAIN_MESSAGE);
    }
}

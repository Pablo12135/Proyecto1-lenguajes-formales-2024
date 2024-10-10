package Clases_Principales;

import java.util.List;

public class Optimizar_Codigo {

    private String codigoFuente;  // El código original

    // Constructor que toma el código fuente
    public Optimizar_Codigo(String codigoFuente) {
        this.codigoFuente = codigoFuente;
    }

    // Método para optimizar el código
    public String optimizar() {
        StringBuilder codigoOptimizado = new StringBuilder();
        String[] lineas = codigoFuente.split("\n");  // Divide el código en líneas

        for (String linea : lineas) {
            // Eliminar comentarios: si la línea contiene "//", se ignora toda la línea
            if (linea.trim().startsWith("//")) {
                continue;  // Salta las líneas con comentarios
            }

            // Eliminar líneas vacías: si una línea está vacía o solo tiene espacios, se ignora
            if (linea.trim().isEmpty()) {
                continue;  // Salta las líneas vacías
            }

            // Si la línea tiene contenido válido, se agrega al código optimizado
            codigoOptimizado.append(linea).append("\n");
        }

        return codigoOptimizado.toString();  // Devuelve el código optimizado
    }
}

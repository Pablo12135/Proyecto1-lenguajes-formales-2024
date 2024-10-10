package Clases_Principales;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Generador_HTML {

     public void generarHTML(String cssContent, String jsContent, String htmlContent, String nombreArchivo) throws IOException {
        // Eliminar el archivo anterior si existe
        File archivoGenerado = new File(nombreArchivo);
        if (archivoGenerado.exists()) {
            archivoGenerado.delete(); // Elimina el archivo antiguo
        }

        // Usar try-with-resources para asegurar que el archivo se cierre correctamente
        try (FileWriter writer = new FileWriter(nombreArchivo)) {
            // Escribir la cabecera del HTML
            writer.write("<!DOCTYPE html>\n");
            writer.write("<html lang=\"es\">\n");
            writer.write("<head>\n");
            writer.write("    <meta charset=\"UTF-8\">\n");
            writer.write("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
            writer.write("    <title>Documento</title>\n");

            // CSS
            writer.write("    <style>\n");
            writer.write(cssContent); // cssContent contenido
            writer.write("    </style>\n");

            // JavaScript
            writer.write("    <script>\n");
            writer.write(jsContent); // jsContent contenido
            writer.write("    </script>\n");

            writer.write("</head>\n");
            writer.write("<body>\n");

            // contenido HTML
            writer.write(htmlContent); // contenido HTML
            writer.write("</body>\n");
            writer.write("</html>\n");
        }
        

        // Abrir el archivo HTML en el navegador por defecto
        try {
            if (archivoGenerado.exists()) {
                Desktop.getDesktop().browse(archivoGenerado.toURI());
            } else {
                System.out.println("El archivo HTML no fue encontrado.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
package Clases_Principales;

import Analizadores_Lenguajes.Analizador_CSS;
import Analizadores_Lenguajes.Analizador_HTML;
import Analizadores_Lenguajes.Analizador_JS;

public class Gestor_Estados {

    private Analizador_Lexico analizadorActual;

    public void cambiarEstado(String tokenEstado) {
        switch (tokenEstado) {
            case ">>[html]":
                analizadorActual = new Analizador_HTML();
                break;
            case ">>[css]":
                analizadorActual = new Analizador_CSS();
                break;
            case ">>[js]":
                analizadorActual = new Analizador_JS();
                break;
            default:
                throw new IllegalArgumentException("Estado no válido: " + tokenEstado);
        }
    }

    public Analizador_Lexico getAnalizadorActual() {
        return analizadorActual;
    }
}

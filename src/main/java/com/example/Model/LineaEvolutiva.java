package com.example.Model;

import java.util.Objects;


public class LineaEvolutiva {

    private Pokemon faseActual;
    private int experienciaAcumulada;

    public LineaEvolutiva(Pokemon faseInicial) {
        this.faseActual = faseInicial;
        this.experienciaAcumulada = 0;
    }

    public Pokemon getFaseActual() {
        return faseActual;
    }

    public int getExperienciaAcumulada() {
        return experienciaAcumulada;
    }


    public void ganarExperiencia(int experienciaGanada) {
        this.experienciaAcumulada += experienciaGanada;
        intentarEvolucionar();
    }


    public boolean intentarEvolucionar() {
        boolean evoluciono = false;

        while (faseActual.getSiguienteEvolucion() != null
                && experienciaAcumulada >= faseActual.getExperienciaRequerida()) {

            faseActual = faseActual.getSiguienteEvolucion();
            evoluciono = true;
        }

        return evoluciono;
    }

    @Override
    public String toString() {
        return "LineaEvolutiva{" +
                "faseActual=" + faseActual +
                ", experienciaAcumulada=" + experienciaAcumulada +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof LineaEvolutiva linea)) return false;
        return experienciaAcumulada == linea.experienciaAcumulada && Objects.equals(faseActual, linea.faseActual);
    }

    @Override
    public int hashCode() {
        return Objects.hash(faseActual, experienciaAcumulada);
    }
}

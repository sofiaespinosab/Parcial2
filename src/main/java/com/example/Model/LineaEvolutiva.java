package com.example.Model;

import java.util.Objects;

/**
 * Lista enlazada simple que representa la línea evolutiva completa de un Pokémon
 * propiedad del jugador. {@link #faseActual} funciona como el head virtual, que
 * avanza sobre los nodos {@link Pokemon} a medida que se cumplen los requisitos
 * de evolución.
 */
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

    /**
     * Suma experiencia al total acumulado y valida inmediatamente si hay evolución.
     */
    public void ganarExperiencia(int experienciaGanada) {
        this.experienciaAcumulada += experienciaGanada;
        intentarEvolucionar();
    }

    /**
     * Mecanismo de evolución: valida si experienciaAcumulada >= experienciaRequerida
     * de la faseActual. Si se cumple, faseActual avanza hacia siguienteEvolucion.
     * Usa un ciclo por si la experiencia ganada alcanza para saltar más de una fase.
     *
     * @return true si hubo al menos una evolución.
     */
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

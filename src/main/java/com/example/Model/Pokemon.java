package com.example.Model;

import java.util.Objects;

/**
 * Nodo de la lista enlazada. Representa una fase evolutiva de un Pokémon.
 * El puntero {@link #siguienteEvolucion} conecta este nodo con su siguiente fase.
 */
public class Pokemon {

    private String nombre;
    private int puntosDeVidaMaximos;
    private int ataque;
    private int defensa;
    private int experienciaRequerida;

    // HP de uso interno durante la simulación de batallas (no es parte del enunciado
    // del nodo, pero es necesario para poder restar vida turno a turno).
    private int hpActual;

    private Pokemon siguienteEvolucion;

    public Pokemon(String nombre, int puntosDeVidaMaximos, int ataque, int defensa, int experienciaRequerida) {
        this.nombre = nombre;
        this.puntosDeVidaMaximos = puntosDeVidaMaximos;
        this.ataque = ataque;
        this.defensa = defensa;
        this.experienciaRequerida = experienciaRequerida;
        this.hpActual = puntosDeVidaMaximos;
        this.siguienteEvolucion = null;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPuntosDeVidaMaximos() {
        return puntosDeVidaMaximos;
    }

    public int getAtaque() {
        return ataque;
    }

    public int getDefensa() {
        return defensa;
    }

    public int getExperienciaRequerida() {
        return experienciaRequerida;
    }

    public int getHpActual() {
        return hpActual;
    }

    public Pokemon getSiguienteEvolucion() {
        return siguienteEvolucion;
    }

    public void setSiguienteEvolucion(Pokemon siguienteEvolucion) {
        this.siguienteEvolucion = siguienteEvolucion;
    }

    public void recuperarVidaCompleta() {
        this.hpActual = this.puntosDeVidaMaximos;
    }

    public void recibirDano(int dano) {
        this.hpActual -= dano;
        if (this.hpActual < 0) {
            this.hpActual = 0;
        }
    }

    public boolean estaDerrotado() {
        return this.hpActual <= 0;
    }

    @Override
    public String toString() {
        return "Pokemon{" +
                "nombre='" + nombre + '\'' +
                ", puntosDeVidaMaximos=" + puntosDeVidaMaximos +
                ", ataque=" + ataque +
                ", defensa=" + defensa +
                ", experienciaRequerida=" + experienciaRequerida +
                ", hpActual=" + hpActual +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Pokemon pokemon)) return false;
        return puntosDeVidaMaximos == pokemon.puntosDeVidaMaximos
                && ataque == pokemon.ataque
                && defensa == pokemon.defensa
                && experienciaRequerida == pokemon.experienciaRequerida
                && Objects.equals(nombre, pokemon.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, puntosDeVidaMaximos, ataque, defensa, experienciaRequerida);
    }
}

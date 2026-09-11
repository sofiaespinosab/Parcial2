package com.example;

import com.example.Model.LineaEvolutiva;
import com.example.Model.Pokemon;
import com.example.Service.EntrenamientoService;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Main {

    public static void main(String[] args) {


        Pokemon charizard = new Pokemon("Charizard", 78, 84, 78, -1);
        Pokemon charmeleon = new Pokemon("Charmeleon", 58, 64, 58, 5000);
        Pokemon charmander = new Pokemon("Charmander", 39, 52, 43, 1500);

        charmander.setSiguienteEvolucion(charmeleon);
        charmeleon.setSiguienteEvolucion(charizard);

        LineaEvolutiva miPokemon = new LineaEvolutiva(charmander);


        log.info("########## PRUEBA UNITARIA: {} vs Rattata salvaje ##########", charmander.getNombre());

        Pokemon rattata = new Pokemon("Rattata", 30, 56, 35, -1);
        EntrenamientoService.batallar(miPokemon.getFaseActual(), rattata);

        log.info("Rattata derrotado: {} | HP restante de Charmander: {}",
                rattata.estaDerrotado(), miPokemon.getFaseActual().getHpActual());

        miPokemon.ganarExperiencia(50);
        log.info("XP acumulada tras la prueba unitaria: {}", miPokemon.getExperienciaAcumulada());


        Pokemon charizardEstres = new Pokemon("Charizard", 78, 84, 78, -1);
        Pokemon charmeleonEstres = new Pokemon("Charmeleon", 58, 64, 58, 5000);
        Pokemon charmanderEstres = new Pokemon("Charmander", 39, 52, 43, 1500);

        charmanderEstres.setSiguienteEvolucion(charmeleonEstres);
        charmeleonEstres.setSiguienteEvolucion(charizardEstres);

        LineaEvolutiva entrenamiento = new LineaEvolutiva(charmanderEstres);

        Pokemon[] hordaCaterpie = EntrenamientoService.generarHorda(100_000, "Caterpie", 45, 30, 35);

        log.info("########## PRUEBA DE ESTRÉS: {} Caterpie ##########", hordaCaterpie.length);

        EntrenamientoService.iniciarEntrenamientoMasivo(entrenamiento, hordaCaterpie);
    }
}

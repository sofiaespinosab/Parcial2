package com.example;

import com.example.Model.LineaEvolutiva;
import com.example.Model.Pokemon;
import com.example.Service.CajaNegraService;
import com.example.Service.EntrenamientoService;
import lombok.extern.log4j.Log4j2;

import java.util.LinkedList;

@Log4j2
public class Main {

    public static void main(String[] args) {

        Pokemon venusaur = new Pokemon("Venusaur", 80, 82, 83, -1);
        Pokemon ivysaur = new Pokemon("Ivysaur", 60, 62, 63, 5000);
        Pokemon bulbasaur = new Pokemon("Bulbasaur", 45, 49, 49, 1500);

        bulbasaur.setSiguienteEvolucion(ivysaur);
        ivysaur.setSiguienteEvolucion(venusaur);

        Pokemon charizard = new Pokemon("Charizard", 78, 84, 78, -1);
        Pokemon charmeleon = new Pokemon("Charmeleon", 58, 64, 58, 5000);
        Pokemon charmander = new Pokemon("Charmander", 39, 52, 43, 1500);

        charmander.setSiguienteEvolucion(charmeleon);
        charmeleon.setSiguienteEvolucion(charizard);

        Pokemon blastoise = new Pokemon("Blastoise", 79, 83, 100, -1);
        Pokemon wartortle = new Pokemon("Wartortle", 59, 63, 80, 5000);
        Pokemon squirtle = new Pokemon("Squirtle", 44, 48, 65, 1500);

        squirtle.setSiguienteEvolucion(wartortle);
        wartortle.setSiguienteEvolucion(blastoise);

        LinkedList<LineaEvolutiva> equipo = new LinkedList<>();

        equipo.add(new LineaEvolutiva(bulbasaur));
        equipo.add(new LineaEvolutiva(charmander));
        equipo.add(new LineaEvolutiva(squirtle));

        Pokemon[] horda = EntrenamientoService.generarHorda(
                250,
                "Caterpie",
                45,
                30,
                35
        );

        CajaNegraService cajaNegra = new CajaNegraService(3);

        EntrenamientoService.iniciarCombateRotativo(
                equipo,
                horda,
                50,
                cajaNegra
        );
    }
}
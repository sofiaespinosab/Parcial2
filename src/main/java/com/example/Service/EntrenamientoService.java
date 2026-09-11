package com.example.Service;

import com.example.Service.CajaNegraService;
import com.example.Model.LineaEvolutiva;
import com.example.Model.Pokemon;
import lombok.extern.log4j.Log4j2;

import java.util.LinkedList;

@Log4j2
public class EntrenamientoService {

    private static final int XP_POR_VICTORIA = 50;


    public static Pokemon[] generarHorda(int cantidad, String nombre, int hp, int ataque, int defensa) {
        Pokemon[] horda = new Pokemon[cantidad];
        for (int i = 0; i < cantidad; i++) {
            horda[i] = new Pokemon(nombre, hp, ataque, defensa, -1);
        }
        return horda;
    }


    public static void batallar(Pokemon miPokemon, Pokemon enemigo) {

        miPokemon.recuperarVidaCompleta();
        enemigo.recuperarVidaCompleta();

        while (!miPokemon.estaDerrotado() && !enemigo.estaDerrotado()) {

            int danoDelJugador = Math.max(1, miPokemon.getAtaque() - enemigo.getDefensa());
            enemigo.recibirDano(danoDelJugador);

            log.debug("{} ataca a {} | daño: {} | HP restante de {}: {}",
                    miPokemon.getNombre(), enemigo.getNombre(), danoDelJugador, enemigo.getNombre(), enemigo.getHpActual());

            if (enemigo.estaDerrotado()) {
                break;
            }

            int danoDelEnemigo = Math.max(1, enemigo.getAtaque() - miPokemon.getDefensa());
            miPokemon.recibirDano(danoDelEnemigo);

            log.debug("{} ataca a {} | daño: {} | HP restante de {}: {}",
                    enemigo.getNombre(), miPokemon.getNombre(), danoDelEnemigo, miPokemon.getNombre(), miPokemon.getHpActual());
        }
    }


    public static void iniciarCombateRotativo(
            LinkedList<LineaEvolutiva> equipo,
            Pokemon[] horda,
            int k,
            CajaNegraService cajaNegra) {

        int indiceEnemigo = 0;

        while (indiceEnemigo < horda.length) {

            LineaEvolutiva pokemonActual = equipo.removeFirst();

            for (int i = 0; i < k && indiceEnemigo < horda.length; i++) {

                Pokemon enemigoActual = horda[indiceEnemigo];
                Pokemon faseAntes = pokemonActual.getFaseActual();
                String nombreQuePeleo = faseAntes.getNombre();

                batallar(pokemonActual.getFaseActual(), enemigoActual);

                pokemonActual.ganarExperiencia(XP_POR_VICTORIA);

                if (pokemonActual.getFaseActual() != faseAntes) {
                    log.info("¡Evolución! {} -> {}",
                            faseAntes.getNombre(),
                            pokemonActual.getFaseActual().getNombre());
                }

                cajaNegra.registrarVictoria(nombreQuePeleo, enemigoActual.getNombre());

                indiceEnemigo++;
            }

            equipo.addLast(pokemonActual);
        }

        log.info("=== CAJA NEGRA FINAL ===");

        for (var reporte : cajaNegra.getHistorial()) {
            log.info(reporte.toString());
        }
    }
}
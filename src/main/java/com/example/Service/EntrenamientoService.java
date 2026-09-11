package com.example.Service;

import com.example.Service.CajaNegraService;
import com.example.Model.LineaEvolutiva;
import com.example.Model.Pokemon;
import com.example.Util.PerformanceReporter;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.Locale;

@Log4j2
public class EntrenamientoService {

    private static final Logger loggerTiempos = LogManager.getLogger("tiempos");
    private static final int XP_POR_VICTORIA = 50;
    private static final int REPORTE_CADA_N_BATALLAS = 10_000;


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


    public static void iniciarEntrenamientoMasivo(LineaEvolutiva miPokemon, Pokemon[] hordaEnemigos) {

        log.info("=== Inicio de entrenamiento masivo | tamaño de la horda: {} ===", hordaEnemigos.length);

        PerformanceReporter.medirPesoObjeto(miPokemon, "LineaEvolutiva (inicial)");
        PerformanceReporter.medirPesoObjeto(miPokemon.getFaseActual(), "Pokemon (nodo inicial: " + miPokemon.getFaseActual().getNombre() + ")");
        PerformanceReporter.reportarMemoriaSistema();

        long inicioTiempoProceso = System.nanoTime();

        for (int i = 0; i < hordaEnemigos.length; i++) {

            Pokemon enemigoActual = hordaEnemigos[i];
            Pokemon faseAntesDeLaBatalla = miPokemon.getFaseActual();

            batallar(miPokemon.getFaseActual(), enemigoActual);

            miPokemon.ganarExperiencia(XP_POR_VICTORIA);

            if (miPokemon.getFaseActual() != faseAntesDeLaBatalla) {
                log.info("¡Evolución! {} -> {} | tras derrotar al enemigo #{} | xp acumulada: {}",
                        faseAntesDeLaBatalla.getNombre(), miPokemon.getFaseActual().getNombre(),
                        i + 1, miPokemon.getExperienciaAcumulada());
            }

            if ((i + 1) % REPORTE_CADA_N_BATALLAS == 0) {
                log.info("Progreso del entrenamiento: {} / {} enemigos derrotados | fase actual: {}",
                        i + 1, hordaEnemigos.length, miPokemon.getFaseActual().getNombre());
            }
        }

        double tiempoTotalSegundos = (System.nanoTime() - inicioTiempoProceso) / 1_000_000_000.0;

        loggerTiempos.info("Entrenamiento masivo | enemigos procesados: {} | tiempo total: {} segundos",
                hordaEnemigos.length, String.format(Locale.ROOT, "%.4f", tiempoTotalSegundos));

        PerformanceReporter.medirPesoObjeto(miPokemon, "LineaEvolutiva (final)");
        PerformanceReporter.medirPesoObjeto(miPokemon.getFaseActual(), "Pokemon (nodo final: " + miPokemon.getFaseActual().getNombre() + ")");
        PerformanceReporter.reportarMemoriaSistema();

        log.info("=== Fin de entrenamiento masivo | fase final: {} | xp acumulada: {} ===",
                miPokemon.getFaseActual().getNombre(), miPokemon.getExperienciaAcumulada());
    }


    public static void iniciarCombateRotativo(
            LinkedList<LineaEvolutiva> equipo,
            Pokemon @NonNull [] horda,
            int k,
            CajaNegraService cajaNegra) {

        int indiceEnemigo = 0;

        while (indiceEnemigo < horda.length) {

            LineaEvolutiva pokemonActual = equipo.removeFirst();

            for (int i = 0; i < k && indiceEnemigo < horda.length; i++) {

                Pokemon enemigoActual = horda[indiceEnemigo];
                Pokemon faseAntes = pokemonActual.getFaseActual();

                batallar(pokemonActual.getFaseActual(), enemigoActual);

                pokemonActual.ganarExperiencia(XP_POR_VICTORIA);

                if (pokemonActual.getFaseActual() != faseAntes) {
                    log.info("¡Evolución! {} -> {}",
                            faseAntes.getNombre(),
                            pokemonActual.getFaseActual().getNombre());
                }

                cajaNegra.registrarVictoria(
                        pokemonActual.getFaseActual().getNombre(),
                        enemigoActual.getNombre());

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
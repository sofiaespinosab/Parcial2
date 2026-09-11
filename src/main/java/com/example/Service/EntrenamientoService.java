package com.example.Service;

import com.example.Model.LineaEvolutiva;
import com.example.Model.Pokemon;
import com.example.Util.PerformanceReporter;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Locale;

/**
 * Motor de simulación de batallas por turnos y procesamiento masivo de la horda
 * de entrenamiento (Horde Training).
 */
@Log4j2
public class EntrenamientoService {

    private static final Logger loggerTiempos = LogManager.getLogger("tiempos");
    private static final int XP_POR_VICTORIA = 50;
    private static final int REPORTE_CADA_N_BATALLAS = 10_000;

    /**
     * Genera un arreglo de {@code cantidad} enemigos con las mismas estadísticas base.
     * Las fases del jugador no evolucionan por estos nodos: son nodos "hoja"
     * (experienciaRequerida = -1, sin siguienteEvolucion), ya que solo se usan como rivales.
     */
    public static Pokemon[] generarHorda(int cantidad, String nombre, int hp, int ataque, int defensa) {
        Pokemon[] horda = new Pokemon[cantidad];
        for (int i = 0; i < cantidad; i++) {
            horda[i] = new Pokemon(nombre, hp, ataque, defensa, -1);
        }
        return horda;
    }

    /**
     * Simula una batalla por turnos entre el Pokémon del jugador y un enemigo.
     * El Pokémon del jugador ataca primero. Ambos inician con su HP máximo.
     * Daño = max(1, Ataque_Atacante - Defensa_Defensor).
     */
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

    /**
     * Reto de simulación (Parte B y C): enfrenta a miPokemon contra cada enemigo de
     * la horda, otorga experiencia y evoluciona en tiempo de ejecución, mientras
     * instrumenta tiempos de ejecución (nanoTime), footprint de objetos (JOL) y
     * memoria del sistema (Oshi).
     */
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
}

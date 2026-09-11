package com.example.Service;

import com.example.Model.ReporteBatalla;

import java.util.LinkedList;

public class CajaNegraService {
    private LinkedList<ReporteBatalla> historial;
    private int capacidad;

public CajaNegraService(int capacidad){
    this.capacidad=capacidad;
    this.historial = new LinkedList<>();

}
public void registrarVictoria(String pokemon,String enemigo){
    if (historial.isEmpty()){
        historial.add(new ReporteBatalla(pokemon, enemigo));
        return;
    }
    ReporteBatalla ultimo =historial.getLast();
    if (ultimo.getNombrePokemon().equals(pokemon)
        && ultimo.getNombreEnemigo().equals(enemigo)){
        ultimo.incrementarDerrotados();

    } else{
    if (historial.size() == capacidad){
        historial.removeFirst();
    }
    historial.add(new ReporteBatalla(pokemon, enemigo));
    }
}
    public LinkedList<ReporteBatalla> getHistorial() {
        return historial;
    }
}


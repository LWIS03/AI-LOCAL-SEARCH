package IA.probAzamon;

import java.util.*;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

public class CreadorSuccesorsDoble implements SuccessorFunction {

    @Override
    public List getSuccessors(Object oEstado) {
        ArrayList<Successor> sucessors = new ArrayList<Successor>();

        Estado estado = (Estado) oEstado;

        int asignacionSize = estado.getAssignacions().size();
        int capRestantSize = estado.getCapRestantActual().size();

        //Generar mover_paquetes
        for (int i = 0; i < asignacionSize; i++) {
            for (int j = 0; j < capRestantSize; j++) {
                Estado new_estado = new Estado(estado);
                if (new_estado.mover_paquet(i, j)) {
                    sucessors.add(new Successor("Mover paquete " + i + " a oferta " + j + " Cost: " + new_estado.getCostTotal() + "\n", new_estado));
                }
            }
        }
        //Generar intercambiar paquetes
        for (int i = 0; i < asignacionSize; i++) {
            for (int j = 0; j < asignacionSize; j++) {
                Estado new_estado = new Estado(estado);
                if (new_estado.intercambiar_paquete(i, j)) {
                    sucessors.add(new Successor("Intercambiar paquete " + i + " por paquete " + j + " Cost: " + new_estado.getCostTotal() + "\n", new_estado));
                }
            }
        }

        return sucessors;
    }
}
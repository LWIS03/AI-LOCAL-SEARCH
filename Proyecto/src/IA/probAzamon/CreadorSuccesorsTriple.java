package IA.probAzamon;

import java.util.*;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

public class CreadorSuccesorsTriple implements SuccessorFunction {

    @Override
    public List getSuccessors(Object oEstado) {
        ArrayList<Successor> sucessors = new ArrayList<Successor>();

        Estado estado = (Estado) oEstado;

        int assignacionsSize = estado.getAssignacions().size();
        int capRestantSize = estado.getCapRestantActual().size();
        //Generar mover_paquetes
        for (int i = 0; i < assignacionsSize; i++) {
            for (int j = 0; j < capRestantSize; j++) {
                Estado new_estado = new Estado(estado);
                if (new_estado.mover_paquet(i, j)) {
                    sucessors.add(new Successor("Mover paquete " + i + " a oferta" + j + " Cost: " + new_estado.getCostTotal() + "\n", new_estado));
                }
            }
        }
        //Generar intercambiar paquetes
        for (int i = 0; i < assignacionsSize; i++) {
            for (int j = 0; j < assignacionsSize; j++) {
                Estado new_estado = new Estado(estado);
                if (new_estado.intercambiar_paquete(i, j)) {
                    sucessors.add(new Successor("Intercambiar paquete " + i + " por paquete " + j + " Cost: " + new_estado.getCostTotal() + "\n", new_estado));
                }
            }
        }

        //Generar intercambiar 2x1

        for (int i = 0; i < assignacionsSize; i++) {
            for (int j = 0; j < assignacionsSize; j++) {
                for (int k = 0; k < assignacionsSize; ++k) {
                    Estado new_estado = new Estado(estado);
                    if (new_estado.intercambiar_2x1_paquete(i, j, k)) {
                        sucessors.add(new Successor("Intercambiar 2x1 paquete " + i + " y " + j + " por paquete " + k + " Cost: " + new_estado.getCostTotal() + "\n", new_estado));
                    }
                }
            }
        }

        return sucessors;
    }
}
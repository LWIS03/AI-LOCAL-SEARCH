package IA.probAzamon;

import java.util.*;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

public class CreadorSuccesorsIntercambiar implements SuccessorFunction {

    @Override
    public List getSuccessors(Object oEstado) {
        ArrayList<Successor> sucessors = new ArrayList<Successor>();

        Estado estado = (Estado) oEstado;

        int assignacionsSize = estado.getAssignacions().size();
        //Generar intercambiar paquetes
        for (int i = 0; i < assignacionsSize; i++) {
            for (int j = 0; j < assignacionsSize; j++) {
                Estado new_estado = new Estado(estado);
                if (new_estado.intercambiar_paquete(i, j)) {
                    sucessors.add(new Successor("Intercambiar paquete " + i + " por paquete " + j + " Cost " + new_estado.getCostTotal() + "\n", new_estado));
                }
            }
        }

        return sucessors;
    }
}
package IA.probAzamon;

import java.util.*;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

public class CreadorSuccesorsMover implements SuccessorFunction {

    @Override
    public List getSuccessors(Object oEstado) {
        ArrayList<Successor> sucessors = new ArrayList();

        Estado estado = (Estado) oEstado;
        //System.out.println(" Cost : " + estado.getCostTotal());


        int assignacionsSize = estado.getAssignacions().size();
        int capRestantSize = estado.getCapRestantActual().size();
        //Generar mover_paquetes
        for (int i = 0; i < assignacionsSize; i++) {
            for (int j = 0; j < capRestantSize; j++) {
                Estado new_estado = new Estado(estado);
                if (new_estado.mover_paquet(i, j)) {
                    sucessors.add(new Successor("Mover paquete " + i + " a oferta" + j + " Cost " + new_estado.getCostTotal() + "\n", new_estado));
                    //System.out.println(" Cost : " + new_estado.getCostTotal());
                }
            }
        }

        return sucessors;
    }
}
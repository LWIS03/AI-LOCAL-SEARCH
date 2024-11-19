package IA.probAzamon;

import IA.Azamon.*;

import java.util.*;

public class Estado {

    static Paquetes paquets; //lista de paquetes a entregar
    static Transporte transport; //lista de ofertas de Transporte
    private ArrayList<Integer> assignacions;
    private ArrayList<Double> CapRestantActual; //Capacidad restante en cada oferta
    private int Felicitat;
    private double costTransport;
    private double costMagatzem;

    public Estado(Estado est) { // Constructora duplica un estat
        this.paquets = est.getPaquets();
        this.transport = est.getTransport();
        this.assignacions = new ArrayList<>(est.getAssignacions());
        this.CapRestantActual = new ArrayList<>(est.getCapRestantActual());
        this.costMagatzem = est.costMagatzem;
        this.costTransport = est.costTransport;
        this.Felicitat = est.Felicitat;
    }
    //Constructora de l'estat inicial
    public Estado(int npaq, int semilla_paq, double proporcio, int semilla_trans, int SolIni) {

        this.assignacions = new ArrayList<>();

        //No hi ha cap paquet assignat encara
        for (int i = 0; i < npaq; ++i) assignacions.add(-1);

        this.paquets = new Paquetes(npaq, semilla_paq);
        this.transport = new Transporte(paquets, proporcio, semilla_trans);
        this.CapRestantActual = new ArrayList<>();

        for (Oferta oferta : transport) CapRestantActual.add(oferta.getPesomax());

        this.Felicitat = 0;
        this.costMagatzem = 0.0;
        this.costTransport = 0.0;

        inicializarSolucion(SolIni);
    }

    private void inicializarSolucion(int solIni) {
        switch(solIni) {
            case 1:
                solucioInicial1V();
                break;
            case 2:
                solucioInicial2V();
                break;
            case 3:
                solucioInicial3V();
                break;
            default:
                break;
        }
        if (!totAssignat()) {
            System.err.println("Error: No se pudo asignar todos los paquetes");
            throw new RuntimeException("No se pudo asignar todos los paquetes ");
        }
    }

    //Solucion Estado Inicial -> A continuació
    private boolean assignPackages(ArrayList<Integer> paq_ord, ArrayList<Integer> of_ord, int currentIndex) {
        // Base case: If we have assigned all packages, return true
        if (currentIndex >= paq_ord.size()) {
            return true;  // All packages assigned successfully
        }

        int indexP = paq_ord.get(currentIndex);  // Get the current package index
        // Try to assign the current package to each available offer
        for (int j = 0; j < of_ord.size(); ++j) {
            if (mover_paquet(indexP, of_ord.get(j))) {  // Try to assign the package

                // Recur for the next package
                if (assignPackages(paq_ord, of_ord, currentIndex + 1)) {
                    return true;  // If the next package is assigned successfully, return true
                }

                // Backtrack: If the next package cannot be assigned, reset the current assignment
                desasignarPaquete(indexP);  // Function to remove the assignment
            }
        }

        // If no assignment was successful for this package, return false
        return false;
    }

    //Solució 1 assigna a la misma prioridad
    public void solucioInicial1V() {
        ArrayList<Integer> of_ord = getOfertesOrdenadesDies();  // Ofertas ordenadas por días disponibles
        ArrayList<Integer> paq_ord = getPaquetsOrdenatsPrioritat();  // Paquetes ordenados por prioridad

        if (!assignPackages(paq_ord, of_ord, 0)) {
            System.err.println("Error: No valid assignment found for all packages.");
        }
    }

    public void solucioInicial2V() {
        ArrayList<Integer> of_ord = getOfertesOrdenadesPreu();  // Ofertas ordenadas por precio
        ArrayList<Integer> paq_ord = getPaquetsOrdenatsTamany();  // Paquetes ordenados por tamaño
        if (!assignPackages(paq_ord, of_ord, 0)) {
            System.err.println("Error: No valid assignment found for all packages.");
        }
    }

    public void solucioInicial3V() {
        ArrayList<Integer> of_ord = getOfertesOrdenadesDiesPreu();  // Ofertas ordenadas por días i precio
        ArrayList<Integer> paq_ord = getPaquetsOrdenatsPrioritatTamany();  // Paquetes ordenados por prioridad y tamaño
        if (!assignPackages(paq_ord, of_ord, 0)) {
            System.err.println("Error: No valid assignment found for all packages.");
        }
    }

/*    public void solucioInicial1() {
        ArrayList<Integer> of_ord = getOfertesOrdenadesDies();  // Ofertas ordenadas por días disponibles
        ArrayList<Integer> paq_ord = getPaquetsOrdenatsPrioritat();  // Paquetes ordenados por prioridad

        int paquetsTotals = paquets.size();
        int ofertesTotals = transport.size();

        // Creamos un array para controlar qué paquetes han sido asignados (-1 si no ha sido asignado)
        for (int i = 0; i < paquetsTotals; i++) {
            assignacions.add(-1);  // Inicializamos las asignaciones
        }

        // Para cada oferta, intentamos asignar paquetes según prioridad y días disponibles
        for (int n_o = 0; n_o < ofertesTotals; ++n_o) {
            int indexO = of_ord.get(n_o);
            int dies = transport.get(indexO).getDias();  // Número de días de la oferta actual

            // Iteramos sobre los paquetes en orden de prioridad
            for (int n_p = 0; n_p < paquetsTotals; ++n_p) {
                int indexP = paq_ord.get(n_p);

                // Verificamos si el paquete ya ha sido asignado
                if (assignacions.get(indexP) == -1) {  // Si no ha sido asignado
                    int prioritat = paquets.get(indexP).getPrioridad();

                    // Verificamos si la prioridad y los días de la oferta son compatibles
                    if (OfertaIPaquetMateixaPrio(dies, prioritat)) {
                        if (PaquetCapOferta(indexP, indexO)) {  // Verificamos si el paquete cabe en la oferta
                            asignarPaquete(indexP, indexO);  // Asignamos el paquete a la oferta
                            break;  // Salimos del bucle para pasar a la siguiente oferta
                        }
                    }
                }
            }
        }
    }

    //Solucio 1.1 assigna a ofertas más economicas paquetes mas grandes
    public void solucioInicial2() {
        ArrayList<Integer> of_ord = getOfertesOrdenadesDiesPreu(); // Ofertas ordenadas por precio
        ArrayList<Integer> paq_ord = getPaquetsOrdenatsPrioritatTamany(); // Paquetes ordenados por prioridad y tamaño
        int paquetsTotals = paquets.size();
        int ofertesTotals = transport.size();

        // Iteramos sobre los paquetes
        for (int n_p = 0; n_p < paquetsTotals; n_p++) {
            int paquetIdx = paq_ord.get(n_p); // Índice del paquete actual

            // Intentamos asignar el paquete a una oferta
            boolean paqueteAsignado = false;
            int n_o = 0;

            // Este bucle asegura que el paquete será asignado a alguna oferta o moveremos otros paquetes para hacer espacio
            while (!paqueteAsignado && n_o < ofertesTotals) {
                int ofertaIdx = of_ord.get(n_o); // Índice de la oferta actual

                // Si la oferta es compatible pero no hay espacio suficiente
                if (PrioritatSuficient(paquetIdx, ofertaIdx)) {
                    if (PaquetCapOferta(paquetIdx, ofertaIdx) || intentarReubicarPaquetes(ofertaIdx, paquetIdx)) {
                        // Hay espacio y compatibilidad, asignamos el paquete
                        asignarPaquete(paquetIdx, ofertaIdx);
                        paqueteAsignado = true;
                    }
                }

                // Si no se pudo asignar el paquete, avanzamos a la siguiente oferta
                n_o++;
            }

            // Si no encontramos ninguna oferta adecuada, lanzar una excepción o manejar el caso de alguna manera
            if (!paqueteAsignado) {
                System.out.println("No se pudo asignar el paquete " + paquetIdx + ". Revisar opciones.");
            }
        }
    }

    //Assignamos a los productos con mayor prioridad las ofertas más rapidas
    public void solucioInicial3() {
        ArrayList<Integer> of_ord = getOfertesOrdenadesDies(); // Ofertas ordenadas por días
        ArrayList<Integer> paq_ord = getPaquetsOrdenatsPrioritat(); // Paquetes ordenados por prioridad
        int n_p = 0;
        int paquetsTotals = paquets.size();
        int ofertesTotals = transport.size();

        // Iteramos sobre los paquetes
        while (n_p < paquetsTotals) {
            int paquetIdx = paq_ord.get(n_p); // Índice del paquete actual
            boolean asignado = false; // Indicador de si el paquete ha sido asignado

            // Iteramos sobre las ofertas para asignar el paquete
            for (int n_o = 0; n_o < ofertesTotals && !asignado; n_o++) {
                int ofertaIdx = of_ord.get(n_o); // Índice de la oferta actual

                // Verificamos si el paquete cabe en la oferta y si la oferta cumple con la prioridad
                if (PaquetCapOferta(paquetIdx, ofertaIdx) && PrioritatSuficient(paquetIdx, ofertaIdx)) {
                    // Asignamos el paquete a la oferta
                    asignarPaquete(paquetIdx, ofertaIdx);
                    asignado = true; // Marcamos el paquete como asignado
                }
            }
            // Pasamos al siguiente paquete
            n_p++;
        }
    }
*/
    //Solucions inicials no valides

    // Funcion auxiliar para asignar un paquete a una oferta
    private void asignarPaquete(int paqueteIdx, int ofertaIdx) {
        afegirPaquet(paqueteIdx, ofertaIdx);
        afegirCost(paqueteIdx, ofertaIdx);
        afegirFelicitat(paqueteIdx, ofertaIdx);
    }

    private void desasignarPaquete(int paqueteIdx) {
        int ofertaIdx = assignacions.get(paqueteIdx);
        retirarCost(paqueteIdx, ofertaIdx);
        retirarFelicitat(paqueteIdx, ofertaIdx);
        treurePaquet(paqueteIdx);
    }

    // Modificadores
    //IMPORTANT!!!!!!!!!
    //Quan es facin els operadors reals tots els paquets han d'estar assignats a una oferta
    public boolean OfertaIPaquetMateixaPrio(int Dia, int Prioritat) {
        if(Prioritat == 0 && Dia == 1) return true;
        if(Prioritat == 1 && Dia <= 3) return true;
        if(Prioritat == 2 && Dia <= 5) return true;
        return false;
    }

    /**
     * Intenta liberar espacio en una oferta moviendo otros paquetes ya asignados
     *
     * @param ofertaIdx índice de la oferta en la que se necesita hacer espacio
     * @param paquetIdx paquete que se necesita asignar
     * @return true si se puede liberar espacio, false en caso contrario
     */
    private boolean intentarReubicarPaquetes(int ofertaIdx, int paquetIdx) {
        ArrayList<Integer> paquetesEnOferta = getPaquetesAsignados(ofertaIdx); // Paquetes asignados a la oferta actual
        for (Integer otroPaquetIdx : paquetesEnOferta) {
            // Buscamos otra oferta para mover el paquete
            for (int n_o = 0; n_o < transport.size(); n_o++) {
                if (n_o != ofertaIdx && PaquetCapOferta(otroPaquetIdx, n_o) && PrioritatSuficient(otroPaquetIdx, n_o)) {
                    // Reasignamos el paquete a una nueva oferta
                    reasignarPaquet(otroPaquetIdx, n_o);
                    return true; // Hemos movido un paquete y liberado espacio
                }
            }
        }
        return false; // No se pudo mover ningún paquete para liberar espacio
    }

    //Afegeix paquet a oferta
    //Pre: Ha de existir paquet, ha d'existir oferta i ha de cabre el paquet a l'oferta
    private void afegirPaquet(int pos_paq, int pos_of) {
        assignacions.set(pos_paq, pos_of);
        CapRestantActual.set(pos_of, CapRestantActual.get(pos_of) - paquets.get(pos_paq).getPeso());
    }

    //Treu paquet d'una oferta
    //Pre: Aquell paquet està assignat a aquella oferta
    private void treurePaquet(int pos_paq) {
        int pos_of = assignacions.get(pos_paq);
        assignacions.set(pos_paq, -1);
        CapRestantActual.set(pos_of, CapRestantActual.get(pos_of) + paquets.get(pos_paq).getPeso());
    }

    private void afegirCost(int pos_paq, int pos_of) {
        double dies = transport.get(pos_of).getDias();
        costMagatzem += paquets.get(pos_paq).getPeso() * dies * 0.25;
        costTransport += transport.get(pos_of).getPrecio() * paquets.get(pos_paq).getPeso();
    }

    private void retirarCost(int pos_paq, int pos_of) {
        double dies = transport.get(pos_of).getDias();
        costMagatzem -= paquets.get(pos_paq).getPeso() * dies * 0.25;
        costTransport -= transport.get(pos_of).getPrecio() * paquets.get(pos_paq).getPeso();
    }

    private void afegirFelicitat(int pos_paq, int pos_of) {
        int dies = transport.get(pos_of).getDias();
        Felicitat += dies - getMaxDays(pos_paq);
    }

    private void retirarFelicitat(int pos_paq, int pos_of) {
        int dies = transport.get(pos_of).getDias();
        Felicitat -= dies - getMaxDays(pos_paq);
    }

    private void reasignarPaquet(int paq_idx, int n_of) {

        int an_n_of = assignacions.get(paq_idx);
        retirarCost(paq_idx, an_n_of);
        retirarFelicitat(paq_idx, an_n_of);
        treurePaquet(paq_idx);
        asignarPaquete(paq_idx, n_of);
    }

    //Operadores ----------------------------------------- Puede que los podamos mover a la clase operdores

    /**
     * @brief Comprueva si el paquete se puede mover a nueva_oferta i lo mueve, sino devuelve false
     * @pre paq_idx y n_of han de estar en el rang de paquetes i ofertas
     */
    public boolean mover_paquet(int paq_idx, int n_of) {
        if (assignacions.get(paq_idx) == n_of) return false;
        if (!PrioritatSuficient(paq_idx, n_of)) return false;
        if (!PaquetCapOferta(paq_idx, n_of)) return false;
        if (assignacions.get(paq_idx) != -1) reasignarPaquet(paq_idx, n_of);
        else {
            afegirPaquet(paq_idx, n_of);
            afegirFelicitat(paq_idx, n_of);
            afegirCost(paq_idx, n_of);
        }
        return true;
    }

    /**
     * @brief Comprueva si el paquete 1 se pude intercambiar por el paq2 y lo hace, sino devuelve false
     * @pre paq1 y paq2 han de estar en el rang de paquetes y han de estar assignados
     */
    public boolean intercambiar_paquete(int paq1, int paq2) {
        int of1 = assignacions.get(paq1);
        int of2 = assignacions.get(paq2);

        if (paq1 == paq2) return false;
        if (of1 == of2) return false;
        if (!PrioritatSuficient(paq1, of2)) return false;
        if (!PrioritatSuficient(paq2, of1)) return false;
        if (!PaquetsCapCanviOferta(paq1, of1, paq2, of2)) return false;

        reasignarPaquet(paq1, of2);
        reasignarPaquet(paq2, of1);
        return true;


    }

    /**
     * @brief Comprueva si el paquete 1 y el paq2 se puden intercambiar por el paq3 y lo hace, sino devuelve false
     * @pre paq1, paq2 y paq3 han de estar en el rang de paquetes y han de estar assignados
     */
    public boolean intercambiar_2x1_paquete(int paq1, int paq2, int paq3) {
        int of1 = assignacions.get(paq1);
        int of2 = assignacions.get(paq2);
        int of3 = assignacions.get(paq3);

        if (paq1 == paq2) return false;
        if (of1 != of2) return false;
        if (of1 == of3) return false;
        if (!PrioritatSuficient(paq1, of3)) return false;
        if (!PrioritatSuficient(paq3, of1)) return false;
        if (!PaquetsCap2x1Canvi(paq1, paq2, of1, paq3, of3)) return false;

        reasignarPaquet(paq1, of3);
        reasignarPaquet(paq2, of3);
        reasignarPaquet(paq3, of1);
        return true;

    }

    public int getFelicitat() {
        return Felicitat;
    }

    //Consultores
    public static Paquetes getPaquets() {
        return paquets;
    }
    public static Transporte getTransport() {
        return transport;
    }
    public ArrayList<Integer> getAssignacions() {
        return assignacions;
    }
    public ArrayList<Double> getCapRestantActual() {
        return CapRestantActual;
    }

    public boolean PaquetCapOferta(int pos_paq, int pos_of) {
        return paquets.get(pos_paq).getPeso() <= CapRestantActual.get(pos_of);
    }

    private boolean PaquetsCapCanviOferta(int paq1, int of1, int paq2, int of2) {
        double p1 = paquets.get(paq1).getPeso();
        double p2 = paquets.get(paq2).getPeso();
        double disp1 = CapRestantActual.get(of1);
        double disp2 = CapRestantActual.get(of2);

        if ((disp2 + p2) < p1) return false;
        if ((disp1 + p1) < p2) return false;
        return true;
    }

    private boolean PaquetsCap2x1Canvi(int paq1, int paq2, int of1, int paq3, int of3) {
        double p1 = paquets.get(paq1).getPeso();
        double p2 = paquets.get(paq2).getPeso();
        double p3 = paquets.get(paq3).getPeso();
        double disp1 = CapRestantActual.get(of1);
        double disp3 = CapRestantActual.get(of3);

        if ((disp3 + p3) < (p1 + p2)) return false;
        if ((disp1 + p1 + p2) < p3) return false;
        return true;
    }
    //Retorna una llista amb les posicions dels paquets de més pes a menys pes
    public ArrayList<Integer> getPaquetsOrdenatsTamany() {
        ArrayList<Integer> indexPaquets = new ArrayList<>();

        // Añadir todos los índices de paquetes
        for (int i = 0; i < paquets.size(); i++) {
            indexPaquets.add(i);
        }

        // Ordenar los índices en función del peso de los paquetes
        indexPaquets.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer a, Integer b) {
                // Comparar el peso de los paquetes (de mayor a menor)
                return Double.compare(paquets.get(b).getPeso(), paquets.get(a).getPeso());
            }
        });

        return indexPaquets;
    }

    //retorna una llista dels paquets ordenats de més prioritat a menys prioritat
    public ArrayList<Integer> getPaquetsOrdenatsPrioritat() {
        ArrayList<Integer> indexPaquets = new ArrayList<>();

        // Añadir todos los índices de paquetes
        for (int i = 0; i < paquets.size(); i++) {
            indexPaquets.add(i);
        }

        // Ordenar los índices en función de la prioridad de los paquetes
        indexPaquets.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer a, Integer b) {
                // Comparar la prioridad de los paquetes (de menor a mayor)
                return Integer.compare(paquets.get(a).getPrioridad(), paquets.get(b).getPrioridad());
            }
        });

        return indexPaquets;
    }

    public ArrayList<Integer> getPaquetsOrdenatsPrioritatTamany() {
        ArrayList<Integer> indexPaquets = new ArrayList<>();

        // Añadir todos los índices de paquetes
        for (int i = 0; i < paquets.size(); i++) {
            indexPaquets.add(i);
        }

        // Ordenar los índices en función de la prioridad de los paquetes
        indexPaquets.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer a, Integer b) {
                // Comparar la prioridad de los paquetes (de menor a mayor)
                int cmp = Integer.compare(paquets.get(a).getPrioridad(), paquets.get(b).getPrioridad());

                if (cmp == 0) Double.compare(paquets.get(b).getPeso(), paquets.get(a).getPeso());

                return cmp;
            }
        });

        return indexPaquets;
    }

    //Ordena les ofertes de major a menor espai disponible
    public ArrayList<Integer> getOfertesOrdenadesPreu() {
        ArrayList<Integer> indexPaquets = new ArrayList<>();

        // Añadir todos los índices de paquetes
        for (int i = 0; i < transport.size(); i++) {
            indexPaquets.add(i);
        }

        // Ordenar los índices en función del peso disponible
        indexPaquets.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer a, Integer b) {
                // Comparar el peso dispnible (de mayor a menor)
                return Double.compare(transport.get(a).getPrecio(), transport.get(b).getPrecio());
            }
        });

        return indexPaquets;
    }

    //Ordena les ofertes de menor a major temps de entrega
    public ArrayList<Integer> getOfertesOrdenadesDies() {
        ArrayList<Integer> indexOfertes = new ArrayList<>();

        // Añadir todos los índices de ofertas
        for (int i = 0; i < transport.size(); i++) {
            indexOfertes.add(i);
        }

        // Ordenar los índices en función del tiempo de entrega
        indexOfertes.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer a, Integer b) {
                // Comparar el tiempo de entrega de las ofertas (de menor a menor)
                return Integer.compare(transport.get(a).getDias(), transport.get(b).getDias());
            }
        });

        return indexOfertes;
    }

    public ArrayList<Integer> getOfertesOrdenadesDiesPreu() {
        ArrayList<Integer> indexOfertes = new ArrayList<>();

        // Añadir todos los índices de ofertas
        for (int i = 0; i < transport.size(); i++) {
            indexOfertes.add(i);
        }

        // Ordenar los índices en función del tiempo de entrega
        indexOfertes.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer a, Integer b) {
                // Comparar el tiempo de entrega de las ofertas (de menor a menor)
                int cmp = Integer.compare(transport.get(a).getDias(), transport.get(b).getDias());
                if (cmp == 0) cmp = Double.compare(transport.get(a).getPrecio(), transport.get(b).getPrecio());
                return cmp;
            }
        });

        return indexOfertes;
    }

    public boolean totAssignat() {
        for (Integer a : assignacions) {
            if (a == -1) return false;
        }
        return true;
    }

    private int getMaxDays(int pos_paq) {
        int pri = paquets.get(pos_paq).getPrioridad();

        if (pri == 0) return 1;
        else if (pri == 1) return 3;
        else return 5;
    }

    public boolean PrioritatSuficient(int pos_paq, int pos_of) {
        return getMaxDays(pos_paq) >= transport.get(pos_of).getDias();
    }

    public double getCostTotal() {
        return costTransport + costMagatzem;
    }

    public double getHeuristic2() {
        return getCostTotal() - getHeuristic2();
    }

    private ArrayList<Integer> getPaquetesAsignados(int ofertaIdx) {
        ArrayList<Integer> paquetesAsignados = new ArrayList<>();

        // Recorremos todas las asignaciones para ver qué paquetes están asignados a esta oferta
        for (int i = 0; i < assignacions.size(); i++) {
            // Si el paquete está asignado a la oferta con índice ofertaIdx, lo añadimos a la lista
            if (assignacions.get(i) == ofertaIdx) {
                paquetesAsignados.add(i);
            }
        }

        return paquetesAsignados;

    }

    public void descr_estado() {

    }
}
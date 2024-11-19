package IA.probAzamon;

import aima.search.framework.*;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;

import java.util.Scanner;

import java.util.Locale;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Que seed para los paquetes quieres usar ?");
        int seedP = scanner.nextInt();

        System.out.println("QUe seed para el transporte quieres usar ?");
        int seedT = scanner.nextInt();

        System.out.println("Cuantos paquetes quieres generar?");
        int n_p = scanner.nextInt();

        System.out.println("Que proporción quieres usar ?");
        double Proporcion = scanner.nextDouble();

        System.out.println("Que estado inicial quiere usar?");
        System.out.println("1: asigna paquetes de mayor prioridad a ofertas más rapidas");
        System.out.println("2: asigna paquetes más grandes a ofertas más economicas");
        System.out.println("3: asigna paquets de mayor prioridad i grandes a ofertas más ràpidas y economicas");

        int SolIni = scanner.nextInt();

        Search search;
        System.out.println("Que Algoritmo de busqueda quieres usar?");
        System.out.println("1: Hill Climbing");
        System.out.println("2: Simulated Annealing");

        int Alg = scanner.nextInt();

        if(Alg == 1) search = new HillClimbingSearch();
        else {
            System.out.println("Selecciona steps, stiter, k, lamb");
            int steps = scanner.nextInt();
            int stiter = scanner.nextInt();
            int k = scanner.nextInt();

            double lamb = scanner.nextDouble();
            search = new SimulatedAnnealingSearch(steps, stiter, k, lamb);
        }

        HeuristicFunction heuristicFunction;
        System.out.println("Que Heuristico quieres usar?");
        System.out.println("1: Para Coste Total");
        System.out.println("2: Para Coste total i felicidad");
        int Heuristico = scanner.nextInt();
        if (Heuristico == 1) heuristicFunction = new Heuristic1();
        else heuristicFunction = new Heuristic2();

        SuccessorFunction funcionSucesora;
        System.out.println("Que Generador de sucesores quieres usar?");
        System.out.println("1: Solo mover Paquetes");
        System.out.println("2: Solo intercanviar paquetes");
        System.out.println("3: Intercanviar i mover paquetes");
        int GenS = scanner.nextInt();


        if (GenS == 1) funcionSucesora = new CreadorSuccesorsMover();
        else if(GenS == 2) funcionSucesora =  new CreadorSuccesorsIntercambiar();
        else if(GenS == 3)funcionSucesora = new CreadorSuccesorsDoble();
        else funcionSucesora = new CreadorSuccesorsTriple();
        System.out.println("/////////////////////////////////////");
        System.out.println("//// NOS PONEMOS EN MARCHA MA'AM/////");
        System.out.println("/////////////////////////////////////");



        try{

            Problem problem = new Problem(new Estado(n_p, seedP, Proporcion, seedT, SolIni), funcionSucesora, new AzamonGoalTest(), heuristicFunction);

            long Inicio = System.currentTimeMillis();
            SearchAgent agent = new SearchAgent(problem, search);
            long Final = System.currentTimeMillis();

            //for (Object action : agent.getActions()) System.out.println(action.toString());
            //System.out.print(search.getGoalState().toString());
            System.out.println("Resultados de la ejecucion:");
            long timeElapsed = Final - Inicio;
            System.out.println("Tiempo usado en hallar la solución : " + timeElapsed);
            Estado last = (Estado) search.getGoalState();
            if (Heuristico == 1) System.out.println("Coste total : " + last.getCostTotal());
            else System.out.println("Coste total : " + last.getHeuristic2());

            for (Object o : agent.getInstrumentation().keySet()) {
                String key = (String) o;
                String property = agent.getInstrumentation().getProperty(key);
                System.out.println(key + " : " + property);
            }

            //System.out.println("Coste total: " + cost);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

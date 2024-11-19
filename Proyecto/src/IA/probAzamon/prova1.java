package IA.probAzamon;

import aima.search.framework.*;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;

import java.util.Scanner;

/**
 * @class Main
 * @brief Clase de visualitzacion i generadora de los Problemas
 */
public class prova1 {

    public static void main(String[] args) {

        int fallos = 0;
        for (int i = 0; i < 999; i++) {
            Estado e = new Estado(100, i, 1.2, i, 2);
            if (!e.totAssignat()) ++fallos;
        }
        System.out.println(fallos);

    }
}

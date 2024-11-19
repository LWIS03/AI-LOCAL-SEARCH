package IA.probAzamon;

import aima.search.framework.HeuristicFunction;

public class Heuristic1 implements HeuristicFunction {

    @Override
    public double getHeuristicValue(Object o) {

        Estado e = (Estado) o;
        return e.getCostTotal();
    }
}

package IA.probAzamon;

import aima.search.framework.HeuristicFunction;

public class Heuristic2 implements HeuristicFunction {

    @Override
    public double getHeuristicValue(Object o) {
        float a = 1;
        float b = 1;
        Estado e = (Estado) o;
        return a * e.getCostTotal() - b * e.getFelicitat();
    }
}

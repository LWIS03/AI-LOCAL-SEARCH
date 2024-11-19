# AI-LOCAL-SEARCH

This repository contains a project that applies local search algorithms to solve a logistics optimization problem for a fictional company, **Azamon**. The project is part of an Artificial Intelligence course and focuses on optimizing package distribution while minimizing costs and maximizing customer satisfaction.

## Project Overview

The main objective of this project is to explore and implement **Hill Climbing** and **Simulated Annealing** algorithms in a logistics context. By evaluating different strategies for initial state generation, operators, and heuristics, the project aims to find optimal solutions for package assignment to delivery offers.

## Problem Description

The optimization problem involves:
- Assigning packages to transport offers based on constraints like weight limits, delivery deadlines, and storage costs.
- Minimizing transport and storage costs while considering customer satisfaction, quantified by delivery time differences.

## Features

- **Algorithms**: 
  - Hill Climbing: Efficient but may converge to local optima.
  - Simulated Annealing: Slower but capable of exploring a broader search space.
- **Heuristics**: 
  - Minimize transport and storage costs.
  - Balance cost reduction with customer satisfaction.
- **Operators**: 
  - Move package.
  - Swap packages (1x1 or 2x1).
- **Experiments**:
  - Evaluate operator efficiency.
  - Compare different heuristics.
  - Test algorithm scalability with increasing problem size.

## Results

The experiments demonstrate:
- **Trade-offs**: More complex operators achieve better results but increase execution time.
- **Scalability**: Both algorithms struggle with large-scale problems, but Simulated Annealing adapts better to complex scenarios.
- **Heuristic Impact**: Adjusting heuristics can significantly influence outcomes, highlighting the importance of tailoring them to specific objectives.

## Conclusion

This project showcases the practical application of local search algorithms in logistics optimization and provides insights into their strengths, limitations, and tunability. It also serves as an excellent exercise in collaborative development and experimentation with AI techniques.

---

Feel free to explore the code and results in this repository. Contributions are welcome!



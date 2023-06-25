package bearmaps.proj2c;

import bearmaps.proj2ab.ArrayHeapMinPQ;
import bearmaps.proj2ab.ExtrinsicMinPQ;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex> {
    private final Vertex start;
    private final Vertex end;

    private SolverOutcome outcome;

    private double solutionWeight;

    private final HashMap<Vertex, WeightedEdge<Vertex>> edgeTo;

    private double timeSpent;

    private int numStatesExplored;

    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout) {
        this.start = start;
        this.end = end;

        Stopwatch stopwatch = new Stopwatch();

        ExtrinsicMinPQ<Vertex> pq = new ArrayHeapMinPQ<>();

        solutionWeight = 0.;
        numStatesExplored = 0;
        outcome = SolverOutcome.UNSOLVABLE;

        HashMap<Vertex, Double> distTo = new HashMap<>();
        edgeTo = new HashMap<>();

        pq.add(start, input.estimatedDistanceToGoal(start, end));
        distTo.put(start, 0.);
        edgeTo.put(start, null);

        while (pq.size() > 0) {
            Vertex v = pq.removeSmallest();
            solutionWeight = distTo.get(v);
            numStatesExplored++;

            timeSpent = stopwatch.elapsedTime();
            if (timeSpent >= timeout) {
                outcome = SolverOutcome.TIMEOUT;
                break;
            }

            if (v.equals(end)) {
                outcome = SolverOutcome.SOLVED;
                break;
            }

            for (WeightedEdge<Vertex> e: input.neighbors(v)) {
                Vertex t = e.to();
                double p = distTo.get(v);
                double q = distTo.getOrDefault(t, Double.MAX_VALUE);
                double w = e.weight();

                if (p + w < q) {
                    distTo.put(t, p + w);
                    edgeTo.put(t, e);
                    double f = p + w + input.estimatedDistanceToGoal(t, end);
                    if (pq.contains(t)) {
                        pq.changePriority(t, f);
                    } else {
                        pq.add(t, f);
                    }
                }
            }
        }


        if (outcome.equals(SolverOutcome.TIMEOUT) || outcome.equals(SolverOutcome.UNSOLVABLE)) {
            solutionWeight = 0;
        }
    }

    @Override
    public SolverOutcome outcome() {
        return outcome;
    }

    @Override
    public List<Vertex> solution() {
        List<Vertex> solution = new LinkedList<>();

        Vertex v = end;

        solution.add(v);

        while (!v.equals(start)) {
            WeightedEdge<Vertex> e = edgeTo.get(v);
            v = e.from();
            solution.add(0, v);
        }

        return solution;
    }

    @Override
    public double solutionWeight() {
        return solutionWeight;
    }

    @Override
    public int numStatesExplored() {
        return numStatesExplored;
    }

    @Override
    public double explorationTime() {
        return timeSpent;
    }
}

package bearmaps;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import edu.princeton.cs.algs4.Stopwatch;

public class KDTreeTest {
    @Test
    public void initSanityCheck() {
        Random rand = new Random();

        List<Point> points = new ArrayList<>(2000000);
        for (int i = 0; i < 2000000; i++) {
            double x = 1000 * rand.nextDouble();
            double y = 1000 * rand.nextDouble();
            Point p = new Point(x, y);
            points.add(p);
        }

        KDTree kdTree = new KDTree(points);
    }

    private static double calcSqrDistance(Point p1, Point p2) {
        return (p1.getX() - p2.getX()) * (p1.getX() - p2.getX()) + (p1.getY() - p2.getY()) * (p1.getY() - p2.getY());
    }


    @Test
    public void nearestCheck() {
        Random rand = new Random(2023);
        for (int i = 0; i < 50; i++) {
            nearestSanityCheck(rand);
        }
    }


    public void nearestSanityCheck(Random rand) {
        double x = 1000 * rand.nextDouble();
        double y = 1000 * rand.nextDouble();
        Point target = new Point(x, y);
        double minSqrDistance = Double.MAX_VALUE;

        List<Point> points = new ArrayList<>(500000);
        for (int i = 0; i < 500000; i++) {
            x = 1000 * rand.nextDouble();
            y = 1000 * rand.nextDouble();
            Point p = new Point(x, y);

            double sqrDistance = calcSqrDistance(target, p);

            if (sqrDistance < minSqrDistance) {
                minSqrDistance = sqrDistance;
            }

            points.add(p);
        }

        KDTree kdTree = new KDTree(points);

        Point nearestPoint = kdTree.nearest(target.getX(), target.getY());

        double nearestSqrDistance = calcSqrDistance(target, nearestPoint);
        assertEquals(minSqrDistance, nearestSqrDistance, 1e-4);
    }
}

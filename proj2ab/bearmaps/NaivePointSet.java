package bearmaps;

import java.util.ArrayList;
import java.util.List;

public class NaivePointSet implements PointSet{
    private final List<Point> points;

    public NaivePointSet(List<Point> points) {
        this.points = new ArrayList<>(points);
    }

    @Override
    public Point nearest(double x, double y) {
        if (points.isEmpty()) {
            return null;
        }

        double minDist = Double.MAX_VALUE;
        Point nearestPoint = points.get(0);
        for (Point point: points) {
            double dist = (point.getX() - x) * (point.getX() - x) + (point.getY() - y) * (point.getY() - y);
            if (dist < minDist) {
                nearestPoint = point;
            }
         }

        return new Point(nearestPoint.getX(), nearestPoint.getY());
    }
}

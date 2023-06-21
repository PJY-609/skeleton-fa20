package bearmaps;

import java.util.List;

public class KDTree implements PointSet {
    private Node root;

    private class Node {
        Node left;
        Node right;
        Point point;

        int depth;

        Node(Point p, int d) {
            point = p;
            depth = d;
        }
    }

    public KDTree(List<Point> points) {
        if (points == null || points.isEmpty()) {
            return;
        }

        root = new Node(points.get(0), 0);

        for (int i = 1; i < points.size(); i++) {
            buildTreeHelper(points.get(i), root, 0);
        }
    }

    private void buildTreeHelper(Point point, Node parent, int depth) {
        double EPSILON = 1e-4;

        double val1 = point.getX();
        double val2 = parent.point.getX();
        if (depth % 2 == 1) {
            val1 = point.getY();
            val2 = parent.point.getY();
        }

        depth++;

        if (val1 - val2 < EPSILON) {
            if (parent.left == null) {
                parent.left = new Node(point, depth);
            } else {
                buildTreeHelper(point, parent.left, depth);
            }
        } else {
            if (parent.right == null) {
                parent.right = new Node(point, depth);
            } else {
                buildTreeHelper(point, parent.right, depth);
            }
        }

    }

    @Override
    public Point nearest(double x, double y) {
        if (root == null) {
            return null;
        }

        Point nearestPoint = new Point(Double.MAX_VALUE, Double.MAX_VALUE);
        Point target = new Point(x, y);

        double minSqrDistance = Double.MAX_VALUE;

        nearestPoint = nearestHelper(root, target, nearestPoint, minSqrDistance);

        return nearestPoint;
    }

    private double calcSqrDistance(Point p1, Point p2) {
        return (p1.getX() - p2.getX()) * (p1.getX() - p2.getX()) + (p1.getY() - p2.getY()) * (p1.getY() - p2.getY());
    }

    private Point nearestHelper(Node node, Point target, Point nearestPoint, double minSqrDistance) {
        if (node == null) {
            return nearestPoint;
        }

        double sqrDistance = calcSqrDistance(node.point, target);

        if (sqrDistance < minSqrDistance) {
            minSqrDistance = sqrDistance;
            nearestPoint = node.point;
        }

        double dist = node.point.getX() - target.getX();
        if (node.depth % 2 == 1) {
            dist = node.point.getY() - target.getY();
        }

        Node goodSide = node.right;
        Node badSide = node.left;
        if (dist > 0) {
            goodSide = node.left;
            badSide = node.right;
        }

        Point nearestPointFromGood = nearestHelper(goodSide, target, nearestPoint, minSqrDistance);
        double sqrDistanceFromGood = calcSqrDistance(nearestPointFromGood, target);

        if (sqrDistanceFromGood < dist * dist) {
            return nearestPointFromGood;
        }

        Point nearestPointFromBad = nearestHelper(badSide, target, nearestPointFromGood, minSqrDistance);

        if (sqrDistanceFromGood > calcSqrDistance(nearestPointFromBad, target)) {
            return nearestPointFromBad;
        }

        return nearestPointFromGood;
    }
}

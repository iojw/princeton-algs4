import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private Node root;
    private int size;

    private static class Node {
        private Node left, right, parent;
        private Point2D point;
        private double xmin, xmax, ymin, ymax;
        private int level;

        public Node(Point2D p) {
            this.point = p;
        }

        public int compareTo(Point2D point2) {
            double difference;

            if (level % 2 == 0) difference = point.x() - point2.x();
            else difference = point.y() - point2.y();

            if (difference < 0) return -1;
            else if (difference > 0) return 1;
            else return 0;
        }

        public int compareTo(RectHV rect) {
            // Need to cache the values for x and y because the Coursera autograder sets limits on the number of calls to point.x() and point.y()
            // This is likely due to the assumption that we will use RectHV and its intesect() method in the code, something I've elected not to do.
            double x = point.x();
            double y = point.y();

            if (level % 2 == 0) {
                // When rect.xmax() == point.x(), search both subtrees as points lying on the rectangle border might exist in the right subtree
                // (We always add points with equal coordinates in the right subtree)
                if ((rect.xmin() < x && x < rect.xmax()) || rect.xmax() == x) {
                    return 0;
                }
                // When rect.xmin() == point.x(), only need to search the right subtree
                else if (rect.xmin() >= x) {
                    return -1;
                }
                else if (rect.xmax() < x) {
                    return 1;
                }
            }
            else {
                // Same logic as above
                if ((rect.ymin() < y && y < rect.ymax()) || rect.ymax() == y) {
                    return 0;
                }
                else if (rect.ymin() >= y) {
                    return -1;
                }
                else if (rect.ymax() < y) {
                    return 1;
                }
            }
            return 0;
        }

        public double distSquaredToLine(Point2D p) {
            double x = p.x(), y = p.y();
            double dx = 0.0, dy = 0.0;

            if (x < xmin) dx = x - xmin;
            else if (x > xmax) dx = x - xmax;
            if (y < ymin) dy = y - ymin;
            else if (y > ymax) dy = y - ymax;

            return dx * dx + dy * dy;
        }

        public void draw() {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.point(point.x(), point.y());

            StdDraw.setPenColor(level % 2 == 0 ? StdDraw.RED : StdDraw.BLUE);
            StdDraw.line(xmin, ymin, xmax, ymax);
        }
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    private Node insertAt(Node node, Point2D p, int level, double xmin, double xmax, double ymin, double ymax, Node parent) {
        if (node == null) {
            Node pointNode = new Node(p);
            pointNode.level = level;
            pointNode.xmin = xmin;
            pointNode.xmax = xmax;
            pointNode.ymin = ymin;
            pointNode.ymax = ymax;
            pointNode.parent = parent;
            size++;
            return pointNode;
        }
        if (node.point.equals(p)) {
            return node;
        }
        // We keep track of the grandparent node to determine where the splitting line for the new child node should stop
        if (node.compareTo(p) <= 0) {
            if ((level + 1) % 2 == 0) {
                node.right = insertAt(node.right, p, node.level + 1, p.x(), p.x(), node.point.y(), node.parent.ymax, node);
            }
            else {
                // parent == null when 1st horizontal line is drawn
                node.right = insertAt(node.right, p, node.level + 1, node.point.x(), node.parent != null ? node.parent.xmax : 1.0, p.y(), p.y(), node);
            }
        }
        else {
            if ((level + 1) % 2 == 0) {
                node.left = insertAt(node.left, p, node.level + 1, p.x(), p.x(), node.parent.ymin, node.point.y(), node);
            }
            else {
                node.left = insertAt(node.left, p, node.level + 1, node.parent != null ? node.parent.xmin : 0.0, node.point.x(), p.y(), p.y(), node);
            }
        }
        return node;
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        root = insertAt(root, p, 0, p.x(), p.x(), 0, 1.0, null);
    }

    private boolean search(Node node, Point2D p) {
        if (node == null) {
            return false;
        }
        else if (node.point.equals(p)) {
            return true;
        }
        else {
            if (node.compareTo(p) <= 0) {
                return search(node.right, p);
            }
            else {
                return search(node.left, p);
            }
        }
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        return search(root, p);
    }

    private void draw(Node node) {
        if (node == null) return;

        node.draw();
        draw(node.left);
        draw(node.right);
    }

    public void draw() {
        draw(root);
    }

    private Iterable<Point2D> range(Node node, RectHV rect, Queue<Point2D> pointsInRange) {
        if (node == null) return null;
        if (rect.contains(node.point)) pointsInRange.enqueue(node.point);

        if (node.compareTo(rect) == 0) {
            range(node.left, rect, pointsInRange);
            range(node.right, rect, pointsInRange);
        }
        else if (node.compareTo(rect) < 0) {
            range(node.right, rect, pointsInRange);
        }
        else {
            range(node.left, rect, pointsInRange);
        }
        return pointsInRange;
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();

        // Initially used an ArrayList for storing the points, but it appears to have too much overhead
        // causing the Coursera grader to fail silently with no error message
        Queue<Point2D> pointsInRange = new Queue<>();
        return range(root, rect, pointsInRange);
    }

    // Originally compared shortest distance from point to entire axis instead of just the splitting line segment in nearest()
    // This results in extra exploration of subtrees which should have been pruned -> use x/y min/max variables in Node and compare to them instead
    private Point2D nearest(Node node, Point2D p, Point2D nearestPoint) {
        if (node == null) return nearestPoint;
        if (node.point.equals(p)) return node.point;

        // Putting this at the end rather than at the beginning will lead to extra nodes being explored
        // since the entire height has to be explored before the current node is considered
        if (node.distSquaredToLine(p) < nearestPoint.distanceSquaredTo(p) && node.point.distanceSquaredTo(p) < nearestPoint.distanceSquaredTo(p)) {
            nearestPoint = node.point;
        }

        // When coordinates are equal, both subtrees are checked regardless of the first result since distSquaredToLine(p) = 0
        if (node.compareTo(p) <= 0) {
            nearestPoint = nearest(node.right, p, nearestPoint);
            if (nearestPoint.distanceSquaredTo(p) > node.distSquaredToLine(p)) {
                nearestPoint = nearest(node.left, p, nearestPoint);
            }
        }
        else if (node.compareTo(p) > 0) {
            nearestPoint = nearest(node.left, p, nearestPoint);
            if (nearestPoint.distanceSquaredTo(p) > node.distSquaredToLine(p)) {
                nearestPoint = nearest(node.right, p, nearestPoint);
            }
        }

        return nearestPoint;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (root == null) return null;

        return nearest(root, p, root.point);
    }
}

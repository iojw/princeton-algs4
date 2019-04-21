import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.TreeSet;
import java.util.stream.Collectors;

public class PointSET {
    private final TreeSet<Point2D> pointSet;

    public PointSET() {
        pointSet = new TreeSet<>();
    }

    public boolean isEmpty() {
        return pointSet.isEmpty();
    }

    public int size() {
        return pointSet.size();
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        pointSet.add(p);
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        return pointSet.contains(p);
    }

    public void draw() {
        pointSet.forEach(Point2D::draw);
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();

        return pointSet.stream().filter(rect::contains).collect(Collectors.toList());
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        return pointSet.stream().min((o1, o2) -> Double.compare(o1.distanceSquaredTo(p), o2.distanceSquaredTo(p))).orElse(null);
    }
}

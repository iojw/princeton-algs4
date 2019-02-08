import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private final LineSegment[] segmentsArray;

    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }
        Point[] pointsArray = new Point[points.length];
        Point[] pointsCopy = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException();
            }
            pointsCopy[i] = points[i];
            pointsArray[i] = points[i];
        }

        ArrayList<LineSegment> segmentsList = new ArrayList<>();
        Arrays.sort(pointsArray);
        for (int i = 0; i < pointsArray.length; i++) {
            if (i < pointsArray.length - 1 && pointsArray[i].compareTo(pointsArray[i + 1]) == 0) {
                throw new IllegalArgumentException();
            }

            Arrays.sort(pointsCopy, pointsArray[i].slopeOrder());
            // Ignore j = 0 as pointsCopy[0] = pointsArray[i] with a slope of Double.NEGATIVE_INFINITY
            int j = 1;
            while (j < pointsCopy.length) {
                int duplicateCount = 0;
                double currentSlope = pointsArray[i].slopeTo(pointsCopy[j]);
                while (j < pointsCopy.length && currentSlope == pointsArray[i].slopeTo(pointsCopy[j])) {
                    duplicateCount++;
                    j++;
                }
                if (duplicateCount >= 3) {
                    Point[] collinearPoints = new Point[duplicateCount + 1];
                    for (int copyTo = 0, copyFrom = j - duplicateCount; copyTo < duplicateCount; copyTo++, copyFrom++) {
                        collinearPoints[copyTo] = pointsCopy[copyFrom];
                    }
                    collinearPoints[duplicateCount] = pointsArray[i];
                    Point minPoint = collinearPoints[0], maxPoint = collinearPoints[0];
                    for (int k = 0; k < collinearPoints.length; k++) {
                        Point p = collinearPoints[k];
                        if (p.compareTo(minPoint) < 0) {
                            minPoint = p;
                        }
                        if (p.compareTo(maxPoint) > 0) {
                            maxPoint = p;
                        }
                    }
                    // To eliminate duplicates
                    if (pointsArray[i].compareTo(minPoint) == 0) {
                        segmentsList.add(new LineSegment(minPoint, maxPoint));
                    }
                }
            }
        }
        segmentsArray = segmentsList.toArray(new LineSegment[0]);
    }

    public int numberOfSegments() {
        return segmentsArray.length;
    }

    public LineSegment[] segments() {
        return Arrays.copyOf(segmentsArray, segmentsArray.length);
    }

    // Test client taken from assignment
    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private final LineSegment[] segmentsArray;

    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }
        Point[] pointsArray = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException();
            }
            pointsArray[i] = points[i];
        }

        ArrayList<LineSegment> segmentsList = new ArrayList<>();
        Arrays.sort(pointsArray);
        for (int i = 0; i < pointsArray.length; i++) {
            if (i < pointsArray.length - 1 && pointsArray[i].compareTo(pointsArray[i + 1]) == 0) {
                throw new IllegalArgumentException();
            }
            for (int j = i + 1; j < pointsArray.length; j++) {
                for (int a = j + 1; a < pointsArray.length; a++) {
                    if (pointsArray[i].slopeTo(pointsArray[j]) != pointsArray[j]
                            .slopeTo(pointsArray[a])) continue;
                    for (int b = a + 1; b < pointsArray.length; b++) {
                        if (pointsArray[i].slopeTo(pointsArray[j]) == pointsArray[j].slopeTo(pointsArray[a])
                                && pointsArray[j].slopeTo(pointsArray[a]) == pointsArray[a].slopeTo(pointsArray[b])) {
                            Point[] collinearPoints = {pointsArray[i], pointsArray[j], pointsArray[a], pointsArray[b]};
                            // Array already sorted
                            segmentsList.add(new LineSegment(collinearPoints[0], collinearPoints[collinearPoints.length - 1]));
                        }
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
}

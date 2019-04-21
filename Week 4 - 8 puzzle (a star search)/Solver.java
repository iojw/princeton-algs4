import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Solver {
    private boolean isSolvable;
    private Stack<Board> solution;

    private class Node {
        private Board board;
        private int moves;
        private Node predecessor;
        private int priority;
    }

    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        solution = new Stack<>();

        Comparator<Node> manhattanComparator = (o1, o2) -> (o1.priority - o2.priority);
        MinPQ<Node> mainPQ = new MinPQ<>(manhattanComparator);
        Node initialMainNode = new Node();
        initialMainNode.board = initial;
        initialMainNode.priority = initialMainNode.board.manhattan();
        mainPQ.insert(initialMainNode);

        Node initialTwinNode = new Node();
        initialTwinNode.board = initial.twin();
        initialTwinNode.priority = initialTwinNode.board.manhattan();
        MinPQ<Node> twinPQ = new MinPQ<>(manhattanComparator);
        twinPQ.insert(initialTwinNode);

        while (!mainPQ.min().board.isGoal() && !twinPQ.min().board.isGoal()) {
            insertNeigbours(mainPQ);
            insertNeigbours(twinPQ);
        }

        isSolvable = mainPQ.min().board.isGoal();
        if (isSolvable) {
            Node currentNode = mainPQ.min();
            solution.push(currentNode.board);
            while (currentNode.predecessor != null) {
                currentNode = currentNode.predecessor;
                solution.push(currentNode.board);
            }
        }
    }

    private void insertNeigbours(MinPQ<Node> pq) {
        Node minNode = pq.delMin();
        for (Board neighbour: minNode.board.neighbors()) {
            if (minNode.predecessor != null && neighbour.equals(minNode.predecessor.board)) continue;
            Node neigbourNode = new Node();
            neigbourNode.board = neighbour;
            neigbourNode.predecessor = minNode;
            neigbourNode.moves = minNode.moves + 1;
            neigbourNode.priority = neigbourNode.board.manhattan() + neigbourNode.moves;
            pq.insert(neigbourNode);
        }
    }

    public boolean isSolvable() {
        return isSolvable;
    }

    public int moves() {
        return solution.size() - 1;
    }

    public Iterable<Board> solution() {
        return isSolvable ? solution : null;
    }

    // test client taken from assignment text
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}

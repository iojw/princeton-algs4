import java.util.ArrayList;

public class Board {
    private final int[][] blocks;
    private int hamming;
    private int manhattan;
    private boolean isGoal;

    public Board(int[][] blocks) {
        this.blocks = copy2DArray(blocks);
        isGoal = true;

        // since Board is immutable, properties can be computed in the constructor
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks.length; j++) {
                // compare to goal board
                if (isGoal && blocks[i][j] != getAtGoal(i, j)) isGoal = false;

                // do not include blank square in calculating priority
                if (blocks[i][j] == 0) continue;

                // calculate Hamming priority - moves
                if (blocks[i][j] != getAtGoal(i, j)) hamming++;

                // calculate Manhattan priority - moves
                int goalRow = (int) Math.ceil((double) blocks[i][j] / blocks.length) - 1;
                int goalCol = blocks[i][j] % blocks.length - 1;
                if (goalCol == -1) goalCol = blocks.length - 1;
                manhattan += (Math.abs(goalRow - i) + Math.abs(goalCol - j));
            }
        }
    }

    public int dimension() {
        return blocks.length;
    }

    public int hamming() {
        return hamming;
    }

    public int manhattan() {
        return manhattan;
    }

    public boolean isGoal() {
        return isGoal;
    }

    public Board twin() {
        int[][] twin = copy2DArray(blocks);
        // arbitrarily swaps 2 items in square for 2 <= N
        // blank square must not be swapped
        if (blocks[0][0] != 0 && blocks[0][1] != 0) {
            twin[0][0] = blocks[0][1];
            twin[0][1] = blocks[0][0];
        }
        else {
            twin[1][0] = blocks[1][1];
            twin[1][1] = blocks[1][0];
        }
        return new Board(twin);
    }

    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;

        Board compareTo = (Board) y;
        if (blocks.length != compareTo.blocks.length) return false;
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks.length; j++) {
                if (blocks[i][j] != compareTo.blocks[i][j]) return false;
            }
        }
        return true;
    }

    public Iterable<Board> neighbors() {
        ArrayList<Board> neighboursList = new ArrayList<>();
        int blankRow = 0;
        int blankCol = 0;
        outer:
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks.length; j++) {
                if (blocks[i][j] == 0) {
                    blankRow = i;
                    blankCol = j;
                    break outer;
                }
            }
        }
        validateNeighbour(blankRow - 1, blankCol, blankRow, blankCol, neighboursList);
        validateNeighbour(blankRow + 1, blankCol, blankRow, blankCol, neighboursList);
        validateNeighbour(blankRow, blankCol - 1, blankRow, blankCol, neighboursList);
        validateNeighbour(blankRow, blankCol + 1, blankRow, blankCol, neighboursList);
        return neighboursList;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(blocks.length + "\n");
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks.length; j++) {
                sb.append(" " + blocks[i][j]);
            }
            sb.append(" \n");
        }
        return sb.toString();
    }

    private int getAtGoal(int i, int j) {
        if (i == blocks.length - 1 && j == blocks.length - 1) return 0;
        else return i * blocks.length + j + 1;
    }

    private int[][] copy2DArray(int[][] array) {
        int[][] copy = new int[array.length][array.length];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array.length; j++) {
                copy[i][j] = array[i][j];
            }
        }
        return copy;
    }

    private void validateNeighbour(int i, int j, int blankRow, int blankCol, ArrayList<Board> neighboursList) {
        if (i < 0 || i >= blocks.length || j < 0 || j >= blocks.length) return;
        int[][] neighbour = copy2DArray(blocks);
        neighbour[blankRow][blankCol] = neighbour[i][j];
        neighbour[i][j] = 0;
        neighboursList.add(new Board(neighbour));
    }
}

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]), count = 0;
        RandomizedQueue<String> queue = new RandomizedQueue<>();

        // Implement reservoir sampling algorithm to tackle the bonus task
        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();
            count++;
            if (queue.size() < k) {
                queue.enqueue(s);
            }
            else {
                if (StdRandom.uniform() < (double) k / count) {
                    queue.dequeue();
                    queue.enqueue(s);
                }
            }
        }

        for (String s : queue) {
            System.out.println(s);
        }
    }
}
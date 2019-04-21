import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] items = (Item[]) new Object[1];
    private int size;

    private void resize(int n) {
        Item[] newArray = (Item[]) new Object[n];

        for (int i = 0; i < size; i++) {
            newArray[i] = items[i];
        }
        items = newArray;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (size == items.length) {
            resize(items.length * 2);
        }
        items[size++] = item;
    }

    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        if (size == items.length / 4) {
            resize(items.length / 2);
        }
        int random = StdRandom.uniform(size);
        Item randomItem = items[random];
        // Order in queue does not matter, so replace removed element with last element in array
        if (random < size - 1) {
            items[random] = items[size - 1];
            // Prevent loitering
            items[size - 1] = null;
        }
        else {
            items[random] = null;
        }
        size--;
        return randomItem;
    }

    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        int random = StdRandom.uniform(size);
        return items[random];
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private int current = 0;
        private Item[] itemsRandom = (Item[]) new Object[size];

        public RandomizedQueueIterator() {
            for (int i = 0; i < size; i++) {
                itemsRandom[i] = items[i];
            }
            StdRandom.shuffle(itemsRandom);
        }

        public Item next() {
            if (current >= size) {
                throw new NoSuchElementException();
            }
            return itemsRandom[current++];
        }

        public boolean hasNext() {
            return current < size;
        }
    }

    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    public static void main(String[] args) {
        RandomizedQueue<String> queue = new RandomizedQueue<>();
        System.out.println("Adding 1-5");
        queue.enqueue("1");
        queue.enqueue("2");
        queue.enqueue("3");
        queue.enqueue("4");
        queue.enqueue("5");
        for (String s: queue) {
            System.out.print(s + " ");
        }
        System.out.printf("\nRandom item: %s\n", queue.sample());
        System.out.println("Removing 2 items");
        queue.dequeue();
        queue.dequeue();
        for (String s: queue) {
            System.out.print(s + " ");
        }
        System.out.printf("\nSize: %d", queue.size());
    }
}

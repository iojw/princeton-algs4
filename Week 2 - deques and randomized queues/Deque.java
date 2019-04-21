import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private class Node {
        public Item item;
        public Node next;
        public Node before;
    }

    private Node first, last = null;
    private int size;

    public boolean isEmpty() {
        return first == null;
    }

    public int size() {
        return size;
    }

    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }

        Node add = new Node();
        add.next = first;
        add.item = item;

        if (first == null && last == null) {
            last = add;
        }
        else {
            first.before = add;
        }

        first = add;
        size++;
    }

    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }

        Node add = new Node();
        add.before = last;
        add.item = item;

        if (first == null && last == null) {
            first = add;
        }
        else {
            last.next = add;
        }

        last = add;
        size++;
    }

    public Item removeFirst() {
        if (first == null) {
            throw new NoSuchElementException();
        }

        Item item = first.item;

        if (first.next == null) {
            last = null;
        }
        else {
            first.next.before = null;
        }

        first = first.next;
        size--;
        return item;
    }

    public Item removeLast() {
        if (last == null) {
            throw new NoSuchElementException();
        }

        Item item = last.item;

        if (last.before == null) {
            first = null;
        }
        else {
            last.before.next = null;
        }

        last = last.before;
        size--;
        return item;
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        public Item next() {
            if (current == null) {
                throw new NoSuchElementException();
            }
            Item next = current.item;
            current = current.next;
            return next;
        }

        public boolean hasNext() {
            return current != null;
        }
    }

    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    public static void main(String[] args) {
        Deque<String> deque = new Deque<>();
        deque.addFirst("to chew bubblegum");
        deque.addLast("and");
        deque.addFirst("I'm here");
        deque.addLast("kick ass");
        for (String s: deque) {
            System.out.print(s + " ");
        }
        deque.removeFirst();
        deque.removeLast();
        deque.addLast("I'm all out of bubblegum");
        deque.removeFirst();
        System.out.println();
        for (String s: deque) {
            System.out.print(s + " ");
        }
    }
}

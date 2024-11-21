import java.util.function.Predicate;

public class LinkedList<T> {
    Node<T> root = null;
    Node<T> tail = null;
    int size = 0;

    public void add(T value) {
        Node<T> newNode = new Node<>(value);
        if (root == null) {
            root = newNode;
        } else {
            tail.next = newNode;
        }
        tail = newNode;
        size++;
    }

    public T get(int i) {
        Node<T> node = root;
        for (int j = 0; j < i; j++) {
            node = node.next;
        }
        return node.value;
    }

    public T search(Predicate<T> predicate) {
        Node<T> node = root;
        while (node != null) {
            if (predicate.test(node.value)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public T[] toArray() {
        T[] arr = (T[]) new Object[size];
        Node<T> node = root;
        for (int i = 0; i < size; i++) {
            arr[i] = node.value;
            node = node.next;
        }
        return arr;
    }

    public boolean remove(Predicate<T> predicate) {
        Node<T> prev = null;
        Node<T> node = root;
        while (node != null) {
            if (predicate.test(node.value)) {
                if (prev == null) {
                    root = node.next;
                } else {
                    prev.next = node.next;
                }
                size--;
                return true;
            }
            prev = node;
            node = node.next;
        }
        return false;
    }

    public int size() {
        return size;
    }

    public Node<T> getRoot() {
        return root;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LinkedList ");
        Node<T> node = root;
        while (node != null) {
            sb.append("[").append(node.value).append("]");
            if (node.next != null) {
                sb.append(" - ");
            }
            node = node.next;
        }
        return sb.toString();
    }



    public static class Node<T> {
        T value;
        Node<T> next;

        public Node() {
            this(null);
        }

        public Node(T value) {
            this(value, null);
        }

        public Node(T value, Node<T> next) {
            this.value = value;
            this.next = next;
        }
    }
}

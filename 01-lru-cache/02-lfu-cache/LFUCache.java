import java.util.HashMap;

/**
 * LFU Cache
 *
 * Design:
 * 1. keyMap  -> O(1) average key lookup
 * 2. freqMap -> frequency -> doubly linked list
 * 3. minFreq -> O(1) identification of the minimum frequency
 * 4. Doubly Linked List -> O(1) insertion/removal and LRU tie-breaking
 *
 * Eviction policy:
 * 1. Least Frequently Used
 * 2. If frequencies are equal, Least Recently Used
 *
 * Complexity:
 * get()  -> O(1) average
 * put()  -> O(1) average
 * Space   -> O(capacity)
 */
class LFUCache {

    /**
     * Represents one cache entry.
     */
    class Node {
        int key;
        int value;
        int freq;

        Node prev;
        Node next;

        Node(int k, int v) {
            key = k;
            value = v;
            freq = 1;
        }
    }

    /**
     * Doubly linked list used for one frequency group.
     *
     * Ordering:
     *
     * HEAD -> Most Recently Used -> ... -> Least Recently Used -> TAIL
     */
    class DoublyLinkedList {

        Node head;
        Node tail;
        int size;

        DoublyLinkedList() {
            head = new Node(0, 0);
            tail = new Node(0, 0);

            head.next = tail;
            tail.prev = head;

            size = 0;
        }

        /**
         * Add a node to the front.
         * The front represents the most recently used node.
         */
        void add(Node node) {

            node.next = head.next;
            node.prev = head;

            head.next.prev = node;
            head.next = node;

            size++;
        }

        /**
         * Remove a specific node in O(1).
         */
        void remove(Node node) {

            node.prev.next = node.next;
            node.next.prev = node.prev;

            size--;
        }

        /**
         * Remove the least recently used node.
         */
        Node removeLast() {

            if (size > 0) {
                Node node = tail.prev;

                remove(node);

                return node;
            }

            return null;
        }
    }

    private int capacity;
    private int minFreq;

    /**
     * key -> Node
     */
    private HashMap<Integer, Node> keyMap;

    /**
     * frequency -> Doubly Linked List
     */
    private HashMap<Integer, DoublyLinkedList> freqMap;

    public LFUCache(int capacity) {

        this.capacity = capacity;

        keyMap = new HashMap<>();
        freqMap = new HashMap<>();

        minFreq = 0;
    }

    /**
     * Returns the value associated with the key.
     *
     * Accessing a key increases its frequency
     * and makes it the most recently used node
     * within the new frequency group.
     */
    public int get(int key) {

        if (!keyMap.containsKey(key)) {
            return -1;
        }

        Node node = keyMap.get(key);

        updateFrequency(node);

        return node.value;
    }

    /**
     * Inserts or updates a key-value pair.
     */
    public void put(int key, int value) {

        if (capacity == 0) {
            return;
        }

        /*
         * Case 1:
         * Key already exists.
         */
        if (keyMap.containsKey(key)) {

            Node node = keyMap.get(key);

            node.value = value;

            updateFrequency(node);

        }

        /*
         * Case 2:
         * Key does not exist.
         */
        else {

            /*
             * Cache is full.
             * Evict the LFU node.
             */
            if (keyMap.size() == capacity) {

                DoublyLinkedList list = freqMap.get(minFreq);

                Node nodeToRemove = list.removeLast();

                keyMap.remove(nodeToRemove.key);
            }

            /*
             * New nodes start with frequency 1.
             */
            Node newNode = new Node(key, value);

            keyMap.put(key, newNode);

            DoublyLinkedList list =
                    freqMap.getOrDefault(
                            1,
                            new DoublyLinkedList()
                    );

            list.add(newNode);

            freqMap.put(1, list);

            /*
             * A newly inserted node always
             * establishes frequency 1 as the minimum.
             */
            minFreq = 1;
        }
    }

    /**
     * Moves a node from its current frequency
     * group to the next frequency group.
     */
    private void updateFrequency(Node node) {

        int freq = node.freq;

        DoublyLinkedList list = freqMap.get(freq);

        /*
         * Remove from current frequency group.
         */
        list.remove(node);

        /*
         * If this was the minimum frequency
         * and its list became empty,
         * the minimum frequency increases.
         */
        if (freq == minFreq && list.size == 0) {
            minFreq++;
        }

        /*
         * Increase frequency.
         */
        node.freq++;

        /*
         * Get/create the new frequency group.
         */
        DoublyLinkedList newList =
                freqMap.getOrDefault(
                        node.freq,
                        new DoublyLinkedList()
                );

        /*
         * Insert at the front:
         * this node is now the MRU
         * of its new frequency group.
         */
        newList.add(node);

        freqMap.put(node.freq, newList);
    }
}

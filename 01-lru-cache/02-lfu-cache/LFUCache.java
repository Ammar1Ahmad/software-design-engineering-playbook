/**
 * LFU Cache
 *
 * Design:
 * - HashMap<Integer, Node> for O(1) key lookup
 * - HashMap<Integer, DoublyLinkedList> for O(1) frequency-group access
 * - Doubly Linked List for O(1) recency management
 * - minFreq for O(1) LFU identification
 *
 * Eviction:
 * 1. Lowest frequency
 * 2. Least recently used among frequency ties
 *
 * Complexity:
 * - get(): O(1) average
 * - put(): O(1) average
 * - Space: O(capacity)
 */

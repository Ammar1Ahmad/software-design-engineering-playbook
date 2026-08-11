# Kth Largest Element in a Stream

> Level 2 · System & Software Design Quest · Q1

[![Java](https://img.shields.io/badge/Java-17-ED8B00?style=flat-square&logo=openjdk&logoColor=white)](https://www.java.com/)
[![Difficulty](https://img.shields.io/badge/Difficulty-Easy-1F883D?style=flat-square)](https://leetcode.com/problems/kth-largest-element-in-a-stream/)
[![Time](https://img.shields.io/badge/Time-O(log%20k)-0969DA?style=flat-square)](#complexity)
[![Space](https://img.shields.io/badge/Space-O(k)-8250DF?style=flat-square)](#complexity)

Maintain the `k`th largest value in a continuously growing stream using
a fixed-size **min-heap**.

---

## 1. Problem

Given:

- an integer `k`
- an initial stream of numbers

implement:

```java
KthLargest(int k, int[] nums)
int add(int val)
```

After every `add(val)`, return the current `k`th largest element.

### Example

```text
k = 3
stream = [4, 5, 8, 2]

add(3)  → 4
add(5)  → 5
add(10) → 5
add(9)  → 8
add(4)  → 8
```

---

## 2. Design

The key observation is:

> We do not need to store every element in sorted order.  
> We only need the **largest `k` elements**.

A **min-heap of size `k`** gives exactly what we need.

```text
              Min Heap
           ┌─────────────┐
           │      5      │ ← kth largest
           ├─────────────┤
           │      8      │
           ├─────────────┤
           │     10      │
           └─────────────┘

          size = k = 3
```

The smallest element inside these top `k` elements is the overall:

```text
kth largest element
```

Therefore:

```java
minHeap.peek()
```

is the answer.

---

## 3. Why a Min-Heap?

Consider:

```text
Numbers:
[2, 4, 5, 8, 10]

k = 3
```

The three largest values are:

```text
[5, 8, 10]
```

Using a min-heap:

```text
        5
       / \
      8  10
```

The root is:

```text
5
```

which is the:

```text
3rd largest element
```

When a new value arrives, we insert it and remove the smallest value if
the heap exceeds size `k`.

---

## 4. Algorithm

For every incoming value:

```text
add value
    ↓
insert into min-heap
    ↓
heap size > k?
   /       \
 Yes        No
  ↓          ↓
remove min  keep heap
    ↓
peek()
    ↓
kth largest
```

### Pseudocode

```text
add(value):

    heap.add(value)

    if heap.size > k:
        heap.poll()

    return heap.peek()
```

---

## 5. Java Implementation

```java
import java.util.PriorityQueue;

class KthLargest {

    private PriorityQueue<Integer> minHeap;
    private int k;

    public KthLargest(int k, int[] nums) {

        this.k = k;
        this.minHeap = new PriorityQueue<>();

        for (int num : nums) {
            add(num);
        }
    }

    public int add(int val) {

        minHeap.add(val);

        if (minHeap.size() > k) {
            minHeap.poll();
        }

        return minHeap.peek();
    }
}
```

Complete source:

[`KthLargest.java`](./KthLargest.java)

---

## 6. Code → Design Mapping

| Code | Responsibility |
|---|---|
| `PriorityQueue<Integer>` | Maintain top `k` elements |
| `k` | Maximum heap size |
| `minHeap.add()` | Insert incoming value |
| `minHeap.poll()` | Remove smallest value |
| `minHeap.peek()` | Return `k`th largest |
| `add()` | Maintain the stream |

The implementation therefore keeps only the information required to answer
the query efficiently.

---

## 7. Example Walkthrough

Given:

```text
k = 3
nums = [4, 5, 8, 2]
```

### Initialization

```text
add(4)

[4]
```

```text
add(5)

[4, 5]
```

```text
add(8)

[4, 5, 8]

peek() = 4
```

The three largest values are:

```text
4, 5, 8
```

Therefore:

```text
3rd largest = 4
```

---

### `add(2)`

```text
[2, 4, 5, 8]
```

Heap size exceeds `k`.

Remove minimum:

```text
2
```

Remaining top `k` values:

```text
[4, 5, 8]
```

Answer:

```text
4
```

---

### `add(10)`

Insert:

```text
[4, 5, 8, 10]
```

Remove minimum:

```text
4
```

Remaining:

```text
[5, 8, 10]
```

Answer:

```text
5
```

---

### `add(9)`

Insert:

```text
[5, 8, 9, 10]
```

Remove:

```text
5
```

Remaining:

```text
[8, 9, 10]
```

Answer:

```text
8
```

---

## 8. Complexity

Let `k` be the heap size.

| Operation | Complexity |
|---|---:|
| Insert | `O(log k)` |
| Remove minimum | `O(log k)` |
| Peek | `O(1)` |
| `add()` | **O(log k)** |
| Space | **O(k)** |

Initialization processes every element through `add()`:

```text
O(n log k)
```

where `n` is the number of initial elements.

---

## 9. Why Not Sort Every Time?

A straightforward approach would be:

```text
Add value
   ↓
Sort entire stream
   ↓
Return kth largest
```

For every incoming value, sorting repeatedly is unnecessarily expensive.

The heap approach maintains only:

```text
top k elements
```

and therefore avoids storing and sorting the entire stream.

---

## 10. Why Not Use a Max-Heap?

A max-heap gives quick access to the **largest** element.

But we need the:

```text
kth largest
```

not simply the maximum.

A min-heap of exactly `k` elements is more appropriate because:

```text
Top k elements
       ↓
Smallest among them
       ↓
kth largest overall
```

---

## 11. Design Invariant

The implementation maintains the following invariant:

> After every `add()`, the heap contains the `k` largest elements seen so far,
> whenever at least `k` elements have been processed.

Therefore:

```java
minHeap.peek()
```

always represents the `k`th largest value.

---

## 12. Visual Model

```text
             Incoming Stream
                    │
                    ▼
              +-----------+
              | Min-Heap  |
              | size = k  |
              +-----------+
                    │
                    ▼
              Remove minimum
                    │
                    ▼
              +-----------+
              | Top k     |
              | elements  |
              +-----------+
                    │
                    ▼
              heap.peek()
                    │
                    ▼
            Kth Largest Value
```

---

## 13. Interview Questions

### Why use a min-heap?

Because the smallest element among the largest `k` elements is exactly
the `k`th largest element.

### Why keep the heap size at `k`?

Everything below the top `k` elements can never become the `k`th largest
while the current top `k` remain.

### What does `peek()` represent?

The smallest value among the current top `k` values, which is the
`k`th largest value overall.

### What happens when a new value is smaller than the current root?

It enters the heap, then is immediately removed because the heap is
restricted to `k` elements.

### What happens when a new value is larger than the root?

It can replace the current `k`th largest candidate.

---

## 14. Key Takeaway

The core design is:

```text
Stream
  ↓
Min-Heap of size k
  ↓
Keep only top k values
  ↓
Minimum of top k
  ↓
Kth largest
```

This is a classic example of using a **bounded heap** to process a
continuous data stream without storing or sorting the entire dataset.

---

## Repository Navigation

| Previous | Current | Next |
|---|---|---|
| — | **Kth Largest Element in a Stream** | Coming Soon |

---

## References

- [LeetCode — Kth Largest Element in a Stream](https://leetcode.com/problems/kth-largest-element-in-a-stream/)
- [System & Software Design Quest](https://leetcode.com/quest/system-and-software-design-quest/)

---

<div align="center">

**Software Design Engineering Playbook**

`Understand → Design → Implement → Analyze`

</div>

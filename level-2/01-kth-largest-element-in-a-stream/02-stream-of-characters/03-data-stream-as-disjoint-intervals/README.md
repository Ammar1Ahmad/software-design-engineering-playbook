# Data Stream as Disjoint Intervals

> Level 2 · System & Software Design Quest · Q3

[![Java](https://img.shields.io/badge/Java-17-ED8B00?style=flat-square&logo=openjdk&logoColor=white)](https://www.java.com/)
[![Difficulty](https://img.shields.io/badge/Difficulty-Hard-B31B1B?style=flat-square)](https://leetcode.com/problems/data-stream-as-disjoint-intervals/)
[![Add](https://img.shields.io/badge/addNum-O(log%20n)-0969DA?style=flat-square)](#complexity)
[![Ordered Map](https://img.shields.io/badge/Data%20Structure-TreeMap-8250DF?style=flat-square)](#design)

Maintain a sorted set of disjoint intervals while integers arrive
incrementally through a data stream.

---

## 1. Problem

Given a stream of non-negative integers, maintain a summary of all
numbers seen so far as **disjoint intervals**.

Implement:

```java
SummaryRanges()
void addNum(int value)
int[][] getIntervals()
```

The returned intervals must:

- contain every number seen so far,
- be mutually disjoint,
- be sorted by their starting value.

---

## 2. Design

The key observation is that we do not need to store every number
individually.

Instead, maintain:

```text
interval start → interval end
```

using a:

```java
TreeMap<Integer, Integer>
```

Example:

```text
1 → 3
6 → 7
```

represents:

```text
[1, 3]
[6, 7]
```

Because `TreeMap` keeps keys sorted, the intervals are automatically
maintained in ascending order.

---

## 3. Why TreeMap?

When a new value arrives, we only need to inspect its two neighboring
intervals:

```text
left  = interval immediately before value
right = interval immediately after value
```

`TreeMap` provides both directly:

```java
map.floorKey(value)
map.ceilingKey(value)
```

Therefore we can determine whether the new value:

```text
1. already exists
2. extends the left interval
3. extends the right interval
4. connects two intervals
5. creates a new interval
```

---

## 4. Data Model

The map stores:

```text
start → end
```

For:

```text
[1, 3], [7, 9]
```

the structure is:

```text
TreeMap

1 → 3
7 → 9
```

The key is always the **start** of an interval.

The value is always the **end**.

---

## 5. Core Algorithm

For every incoming number:

```text
addNum(value)
      │
      ▼
Already present?
   │       │
  Yes      No
   │       │
 return   Find neighbors
              │
        ┌─────┴─────┐
        ▼           ▼
      left         right
        │           │
        └─────┬─────┘
              ▼
        Check adjacency
              │
      ┌───────┼────────┐
      ▼       ▼        ▼
    Left    Right    Neither
    only    only      │
      │       │       ▼
      └───┬───┘    New interval
          │
          ▼
      Merge/update
```

---

## 6. Neighbor Detection

For a new value:

```java
Integer left = map.floorKey(value);
Integer right = map.ceilingKey(value);
```

These represent:

```text
floorKey(value)
    ↓
interval starting at or before value

ceilingKey(value)
    ↓
interval starting at or after value
```

This gives us exactly the neighboring intervals needed for merging.

---

## 7. Merge Rules

Suppose the current intervals are:

```text
[1, 3]    [7, 9]
```

### Case 1 — Extends left interval

Add:

```text
4
```

Since:

```text
3 + 1 = 4
```

merge:

```text
[1, 4]
```

---

### Case 2 — Extends right interval

Add:

```text
6
```

Since:

```text
6 + 1 = 7
```

merge:

```text
[6, 9]
```

---

### Case 3 — Bridges both intervals

Current:

```text
[1, 3]    [5, 7]
```

Add:

```text
4
```

The new value connects both:

```text
[1, 7]
```

---

### Case 4 — Creates a new interval

Current:

```text
[1, 3]    [7, 9]
```

Add:

```text
12
```

No neighboring interval is adjacent.

Create:

```text
[12, 12]
```

---

## 8. Java Implementation

```java
import java.util.Map;
import java.util.TreeMap;

class SummaryRanges {

    private final TreeMap<Integer, Integer> map;

    public SummaryRanges() {
        map = new TreeMap<>();
    }

    public void addNum(int value) {

        if (map.containsKey(value)) {
            return;
        }

        Integer left = map.floorKey(value);
        Integer right = map.ceilingKey(value);

        int start = value;
        int end = value;

        // Merge with left interval.
        if (left != null && map.get(left) + 1 >= value) {
            start = left;
            end = Math.max(end, map.get(left));

            map.remove(left);
        }

        // Merge with right interval.
        if (right != null && right - 1 <= value) {
            end = Math.max(end, map.get(right));

            map.remove(right);
        }

        map.put(start, end);
    }

    public int[][] getIntervals() {

        int[][] result = new int[map.size()][2];

        int index = 0;

        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {

            result[index][0] = entry.getKey();
            result[index][1] = entry.getValue();

            index++;
        }

        return result;
    }
}
```

Complete source:

[`SummaryRanges.java`](./SummaryRanges.java)

---

## 9. Code → Design Mapping

| Code | Responsibility |
|---|---|
| `TreeMap` | Maintain sorted intervals |
| `floorKey()` | Find left neighboring interval |
| `ceilingKey()` | Find right neighboring interval |
| `map.get(start)` | Retrieve interval end |
| `map.remove()` | Remove merged intervals |
| `map.put()` | Store resulting interval |
| `entrySet()` | Generate sorted output |

---

## 10. Example Walkthrough

Start with:

```text
{}
```

### `addNum(1)`

```text
[1, 1]
```

Map:

```text
1 → 1
```

---

### `addNum(3)`

No adjacent interval:

```text
[1, 1] [3, 3]
```

Map:

```text
1 → 1
3 → 3
```

---

### `addNum(7)`

```text
[1, 1] [3, 3] [7, 7]
```

---

### `addNum(2)`

Neighbors:

```text
left  = [1, 1]
right = [3, 3]
```

Both are adjacent to `2`.

Therefore:

```text
[1, 3]
```

State:

```text
[1, 3] [7, 7]
```

---

### `addNum(6)`

Right interval:

```text
[7, 7]
```

is adjacent to `6`.

Merge:

```text
[6, 7]
```

Final state:

```text
[1, 3] [6, 7]
```

---

## 11. Visual Model

```text
Incoming Value
      │
      ▼
   TreeMap
      │
      ├──────────────┐
      ▼              ▼
   floorKey       ceilingKey
      │              │
      ▼              ▼
 Left Interval   Right Interval
      │              │
      └──────┬───────┘
             ▼
        Adjacency?
             │
             ▼
           Merge
             │
             ▼
       Store interval
```

---

## 12. Complexity

Let `n` be the number of stored intervals.

| Operation | Complexity |
|---|---:|
| `containsKey()` | `O(log n)` |
| `floorKey()` | `O(log n)` |
| `ceilingKey()` | `O(log n)` |
| `remove()` | `O(log n)` |
| `put()` | `O(log n)` |
| `addNum()` | **O(log n)** |
| `getIntervals()` | **O(n)** |
| Space | **O(n)** |

The important property is that each insertion examines only the
neighboring intervals rather than scanning the entire stream.

---

## 13. Why Not Store Every Number?

A simple approach could maintain:

```text
Set<Integer>
```

and rebuild the intervals whenever `getIntervals()` is called.

That introduces unnecessary work because the entire set may need to be
traversed repeatedly.

Instead, this design maintains the interval representation **incrementally**
as values arrive.

```text
Incoming value
      ↓
Local interval update
      ↓
Updated summary
```

---

## 14. Why Not Use an Array?

An array could work under some bounded-value constraints, but it would
not naturally provide:

```text
sorted intervals
nearest left interval
nearest right interval
```

`TreeMap` directly provides the ordered-neighbor operations required by
the design.

---

## 15. Design Invariant

The implementation maintains:

> Every key in the `TreeMap` is the start of one disjoint interval, and
> its mapped value is that interval's end.

Therefore:

```text
start ≤ end
```

and no two stored intervals overlap or touch.

For example:

```text
[1, 3] [6, 8]
```

is valid, while:

```text
[1, 3] [3, 8]
```

must be merged.

---

## 16. Interview Questions

### Why use `TreeMap`?

It maintains sorted keys and provides `floorKey()` and `ceilingKey()` in
O(log n).

### What does `floorKey(value)` represent?

The start of the closest interval whose start is less than or equal to
`value`.

### What does `ceilingKey(value)` represent?

The start of the closest interval whose start is greater than or equal
to `value`.

### When do two intervals merge?

When the new value is adjacent to an existing interval, or when it bridges
two neighboring intervals.

### Why remove the old intervals?

After merging, their separate representations are replaced by one
combined interval.

### Why is `getIntervals()` sorted?

`TreeMap` iterates through keys in ascending order.

### Can this be implemented with a HashMap?

A HashMap does not provide ordered neighboring keys, so it would require
additional structures to efficiently locate adjacent intervals.

---

## 17. Key Takeaway

The central design is:

```text
Data Stream
     ↓
TreeMap
     ↓
Find neighboring intervals
     ↓
Check adjacency
     ↓
Merge
     ↓
Maintain disjoint representation
```

The important engineering idea is to **maintain the summary incrementally**
instead of rebuilding it from the complete stream after every operation.

---

## Repository Navigation

| Previous | Current | Next |
|---|---|---|
| [02 — Stream of Characters](../02-stream-of-characters/) | **03 — Data Stream as Disjoint Intervals** | Coming Soon |

---

## References

- [LeetCode — Data Stream as Disjoint Intervals](https://leetcode.com/problems/data-stream-as-disjoint-intervals/)
- [System & Software Design Quest](https://leetcode.com/quest/system-and-software-design-quest/)

---

<div align="center">

**Software Design Engineering Playbook**

`Understand → Design → Implement → Analyze`

</div>

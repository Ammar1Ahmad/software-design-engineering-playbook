# Stream of Characters

> Level 2 · System & Software Design Quest · Q2

[![Java](https://img.shields.io/badge/Java-17-ED8B00?style=flat-square&logo=openjdk&logoColor=white)](https://www.java.com/)
[![Difficulty](https://img.shields.io/badge/Difficulty-Hard-B31B1B?style=flat-square)](https://leetcode.com/problems/stream-of-characters/)
[![Time](https://img.shields.io/badge/Query-O(L)-0969DA?style=flat-square)](#complexity)
[![Space](https://img.shields.io/badge/Space-O(NL)-8250DF?style=flat-square)](#complexity)

Detect whether the current character stream ends with any word from a
given dictionary.

---

## 1. Problem

Given a dictionary of words and a continuously arriving stream of
characters, return `true` whenever the current stream has a suffix that
matches one of the dictionary words.

For example:

```text
words = ["cd", "f", "kl"]

stream:
a → false
b → false
c → false
d → true     ("cd")
e → false
f → true     ("f")
...
k → false
l → true     ("kl")
```

The important part is that only the **suffix ending at the newest
character** matters.

---

## 2. Design

A normal Trie stores words from:

```text
left → right
```

But the query asks about:

```text
suffix → ending at the current character
```

Therefore, the Trie is built in reverse.

### Example

For:

```text
"cd"
```

store:

```text
d → c
```

For:

```text
"kl"
```

store:

```text
l → k
```

Then each query can walk backward through the stream.

```text
Stream:

a → x → y → z
            ↑
          newest

Check:

z → y → x → a
```

This aligns the stream traversal directly with suffix matching.

---

## 3. Architecture

```text
                  StreamChecker
                       │
              ┌────────┴────────┐
              │                 │
              ▼                 ▼
        Reversed Trie       Stream Buffer
              │                 │
              │                 │
              ▼                 ▼
        suffix matching    recent characters
              │
              ▼
        Word detected?
```

The implementation uses:

| Component | Responsibility |
|---|---|
| Reversed Trie | Store dictionary words backwards |
| `StringBuilder` | Maintain the incoming stream |
| `maxLength` | Limit how far a query must scan |
| `isWord` | Mark complete dictionary words |

---

## 4. Why a Reversed Trie?

Suppose:

```text
words = ["abc"]
```

A normal Trie stores:

```text
a → b → c
```

But after:

```text
... → a → b → c
```

we need to determine whether the stream **ends with** `"abc"`.

The newest character is:

```text
c
```

So we naturally want to search:

```text
c → b → a
```

Therefore the Trie stores:

```text
c → b → a
```

This makes suffix matching a direct Trie traversal.

---

## 5. Trie Structure

For:

```text
["cd", "f", "kl"]
```

the reversed Trie becomes conceptually:

```text
ROOT
├── d
│   └── c *
│
├── f *
│
└── l
    └── k *
```

`*` indicates:

```text
isWord = true
```

---

## 6. Query Algorithm

When a new character arrives:

```text
query(letter)
      ↓
append character to stream
      ↓
start at Trie root
      ↓
read stream backwards
      ↓
follow Trie edges
      ↓
missing edge?
   ├── yes → false
   └── no
       ↓
     isWord?
       ├── yes → true
       └── no → continue
```

Only up to:

```text
maxLength
```

characters need to be examined.

---

## 7. Java Implementation

```java
class StreamChecker {

    class TrieNode {

        TrieNode[] children = new TrieNode[26];
        boolean isWord = false;
    }

    private TrieNode root;
    private StringBuilder stream;
    private int maxLength = 0;

    public StreamChecker(String[] words) {

        root = new TrieNode();
        stream = new StringBuilder();

        // Build a reversed Trie.
        for (String word : words) {

            maxLength = Math.max(
                    maxLength,
                    word.length()
            );

            TrieNode node = root;

            for (int i = word.length() - 1;
                 i >= 0;
                 i--) {

                int index = word.charAt(i) - 'a';

                if (node.children[index] == null) {
                    node.children[index] = new TrieNode();
                }

                node = node.children[index];
            }

            node.isWord = true;
        }
    }

    public boolean query(char letter) {

        stream.append(letter);

        TrieNode node = root;

        for (int i = stream.length() - 1;
             i >= 0 &&
             stream.length() - i <= maxLength;
             i--) {

            int index = stream.charAt(i) - 'a';

            if (node.children[index] == null) {
                return false;
            }

            node = node.children[index];

            if (node.isWord) {
                return true;
            }
        }

        return false;
    }
}
```

Complete source:

[`StreamChecker.java`](./StreamChecker.java)

---

## 8. Code → Design Mapping

| Code | Purpose |
|---|---|
| `TrieNode[] children` | Character transitions |
| `isWord` | Marks a complete dictionary word |
| `root` | Trie entry point |
| `stream` | Stores received characters |
| `maxLength` | Bounds the backward search |
| Reversed insertion | Enables suffix matching |
| Backward query | Matches newest suffix first |

---

## 9. Example Walkthrough

Dictionary:

```text
["cd", "f", "kl"]
```

Reversed Trie:

```text
d → c
f
l → k
```

### Query: `a`

```text
stream = "a"

a
↓
no Trie edge

false
```

### Query: `c`

```text
stream = "abc"

c
↓
Trie has c? No

false
```

### Query: `d`

```text
stream = "abcd"

d
↓
c
↓
complete word

true
```

Because:

```text
suffix = "cd"
```

matches the dictionary.

### Query: `f`

```text
stream = "abcdef"

f
↓
complete word

true
```

---

## 10. Why `maxLength`?

Suppose the longest dictionary word has length:

```text
200
```

There is no reason to inspect a stream suffix longer than 200 characters.

Therefore:

```java
maxLength
```

limits every query to the only part of the stream that can possibly
match a word.

This keeps the search independent of the total stream length.

---

## 11. Complexity

Let:

```text
L = maximum word length
N = total number of characters across all words
```

### Construction

Building the reversed Trie:

```text
Time:  O(N)
Space: O(N)
```

### Query

At most `L` characters are examined:

```text
Time: O(L)
```

The stream stores received characters:

```text
Space: O(S)
```

where `S` is the number of streamed characters stored.

Therefore the practical query complexity is:

```text
O(maxWordLength)
```

---

## 12. Why Not Check Every Word?

A straightforward solution could test:

```text
every word
    ↓
compare against stream suffix
```

With up to thousands of words, this repeats the same character comparisons.

The Trie shares common suffix paths:

```text
words
 ↓
Reversed Trie
 ↓
shared prefixes
 ↓
single traversal
```

This makes the design substantially more efficient.

---

## 13. Why Not Use a Normal Trie?

A normal Trie is optimized for:

```text
prefix matching
```

This problem requires:

```text
suffix matching
```

Reversing both the dictionary words and the traversal direction converts
the suffix problem into a standard Trie-prefix traversal.

```text
Suffix problem
      ↓
Reverse words
      ↓
Reverse stream traversal
      ↓
Trie lookup
```

That transformation is the key design insight.

---

## 14. Design Invariant

The Trie maintains this invariant:

> Every path from the root represents a reversed dictionary word prefix.

During `query()`:

> The Trie traversal corresponds to the suffix of the stream ending at
> the newest character.

Therefore, reaching:

```java
node.isWord == true
```

means the current stream contains a matching suffix.

---

## 15. Visual Model

```text
Incoming Stream

a → b → c → d
            ↑
          newest


Read backwards

d → c → b → a
│   │
│   └── Trie path
│
└────── newest character


Reversed Trie

ROOT
 │
 d
 │
 c *
```

When the traversal reaches `*`:

```text
suffix exists in dictionary
```

---

## 16. Interview Questions

### Why reverse the Trie?

Because the problem asks for suffix matching, while a Trie naturally
supports prefix traversal.

### Why traverse the stream backwards?

The newest character is the end of the potential matching word.

### Why track `maxLength`?

No dictionary word can be longer than the longest word, so longer suffixes
cannot produce a match.

### Why use `StringBuilder`?

It provides an efficient mutable character sequence for the stream.

### What does `isWord` represent?

It marks that the current Trie path represents a complete dictionary word.

### What happens when a Trie edge is missing?

The current suffix cannot match any dictionary word, so `query()` returns
`false`.

### Could this be implemented using a HashSet?

Yes, but repeatedly constructing and checking suffix strings introduces
additional work. The Trie shares common paths and directly performs the
character traversal.

---

## 17. Key Takeaway

The central transformation is:

```text
Suffix Matching
      ↓
Reverse the Dictionary
      ↓
Build a Trie
      ↓
Read the Stream Backwards
      ↓
Match Against Trie
```

The result is a clean streaming design with query time bounded by the
maximum dictionary word length.

---

## Repository Navigation

| Previous | Current | Next |
|---|---|---|
| [01 — Kth Largest Element in a Stream](../01-kth-largest-element-in-a-stream/) | **02 — Stream of Characters** | Coming Soon |

---

## References

- [LeetCode — Stream of Characters](https://leetcode.com/problems/stream-of-characters/)
- [System & Software Design Quest](https://leetcode.com/quest/system-and-software-design-quest/)

---

<div align="center">

**Software Design Engineering Playbook**

`Understand → Design → Implement → Analyze`

</div>

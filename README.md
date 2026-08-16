# Software Design Engineering Playbook

A structured collection of **LeetCode problems, Low-Level Design, Object-Oriented Design, SOLID principles, Design Patterns, UML, and production-oriented software engineering**.

Each problem is treated as an engineering case study rather than just a coding solution.

---

## Repository Structure

```text
software-design-engineering-playbook/
│
├── 01-lru-cache/
│   ├── 02-lfu-cache/
│   │   └── README.md
│   └── README.md
│
├── level-2/
│   └── 01-kth-largest-element-in-stream/
│       ├── 02-stream-of-characters/
│       │   ├── README.md
│       │   └── ...
│       │
│       ├── KthLargest.java
│       └── README.md
│
├── LICENSE
└── README.md
```

---

## Problems

### Level 01

| Problem   | Documentation                                   | Implementation                       |
| --------- | ----------------------------------------------- | ------------------------------------ |
| LRU Cache | [README](./01-lru-cache/README.md)              | [Open](./01-lru-cache/)              |
| LFU Cache | [README](./01-lru-cache/02-lfu-cache/README.md) | [Open](./01-lru-cache/02-lfu-cache/) |

### Level 02

| Problem                         | Documentation                                                                          | Implementation                                                              |
| ------------------------------- | -------------------------------------------------------------------------------------- | --------------------------------------------------------------------------- |
| Kth Largest Element in a Stream | [README](./level-2/01-kth-largest-element-in-stream/README.md)                         | [Java](./level-2/01-kth-largest-element-in-stream/KthLargest.java)          |
| Stream of Characters            | [README](./level-2/01-kth-largest-element-in-stream/02-stream-of-characters/README.md) | [Open](./level-2/01-kth-largest-element-in-stream/02-stream-of-characters/) |

---

## Engineering Approach

Every problem is studied through multiple layers:

```text
Problem
   ↓
Requirements
   ↓
Object-Oriented Design
   ↓
SOLID Principles
   ↓
Design Patterns
   ↓
UML / Sequence Diagrams
   ↓
Java Implementation
   ↓
Complexity Analysis
   ↓
Scalability & Trade-offs
```

---

## What This Repository Covers

### Software Design

* Object-Oriented Programming
* Encapsulation
* Abstraction
* Inheritance
* Polymorphism
* Composition
* Dependency Injection

### Design Principles

* SOLID
* DRY
* KISS
* Separation of Concerns
* High Cohesion
* Low Coupling

### Design Patterns

* Factory
* Builder
* Singleton
* Strategy
* Observer
* Adapter
* Decorator
* Proxy
* Command
* State
* Template Method

### System Design

* Low-Level Design
* High-Level Design
* API Design
* Database Design
* Caching
* Messaging
* Load Balancing
* Scalability
* Fault Tolerance
* Distributed Systems

---

## Solution Structure

Each problem is documented using a consistent engineering format:

```text
Problem
Requirements
Design
Class Diagram
Sequence Diagram
Implementation
Complexity
Design Decisions
Scalability
Trade-offs
Interview Questions
```

This makes every solution useful for both **learning and technical interview preparation**.

---

## Technology

| Technology | Purpose               |
| ---------- | --------------------- |
| Java       | Implementation        |
| UML        | System modeling       |
| Mermaid    | Diagrams              |
| Draw.io    | Architecture diagrams |
| Markdown   | Documentation         |

---

## Design Philosophy

The objective is to move from:

```text
Writing Code
     ↓
Designing Components
     ↓
Understanding Trade-offs
     ↓
Designing Systems
     ↓
Building Production-Ready Software
```

The repository focuses on **engineering decisions, not just accepted solutions**.

---

## Goals

* Build strong LLD fundamentals
* Master practical software design principles
* Apply design patterns to real problems
* Improve Java design and implementation skills
* Develop HLD and system-design thinking
* Understand scalability and architectural trade-offs
* Build an interview-ready engineering portfolio

---

## Progress

| Area                      | Status      |
| ------------------------- | ----------- |
| OOP & Design Fundamentals | In Progress |
| SOLID Principles          | In Progress |
| Design Patterns           | In Progress |
| UML & Modeling            | In Progress |
| Low-Level Design          | In Progress |
| High-Level Design         | Planned     |
| Production Architecture   | Planned     |

---

## License

This project is licensed under the [MIT License](./LICENSE).

---

<div align="center">

**Software Design Engineering Playbook**

*Design systems. Understand trade-offs. Build for scale.*

</div>

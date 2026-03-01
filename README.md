# Railway Cargo Reachability

This project solves a reachability problem on a directed graph with state propagation.

Each station unloads one cargo type on arrival and loads one cargo type before departure.
Starting from a given station with empty cargo, the goal is to determine which cargo types
**may be present on a train when it arrives** at each station.

## Approach

The solution uses a monotone data-flow analysis with a worklist:
- for each station, we track the set of possible cargo types on arrival
- propagate state only from already discovered (reachable) predecessors. Unreachable stations remain with empty arrival set and are printed as empty.
- sets grow monotonically until a fixpoint is reached

Cycles and returning to the start station are handled naturally by the iteration.

## Complexity of CargoReachabilityAnalysis

Let `V` be the number of stations, `E` the number of tracks, and `C` the number of cargo types.

### Time

- **Time:** `O(E · C²)` (worst case)

**Explanation:**
- For each vertex `v`, we maintain a set of cargo types that may be present on arrival.
- The analysis is monotone: cargo types are only added, never removed. And when it is added for some vertex, we push that vertex in a queue so that we can visit it later.
- Therefore, number of `updates(v)` is at most C, where `updates(v)` means how many times we visited vertex `v` during the execution.
- Processing a vertex `v` requires merging information from all incoming edges, which costs `O(deg_in(v) · C)`.

The total running time is therefore bounded by: Σ over v ∈ V (updates(v) · deg_in(v) · C). And since `updates(v) ≤ C` and `Σ deg_in(v) = E`, the total time complexity is: O(E · C²)
### Space

- **Space:** `O(V · C)`

**Explanation:**
- For each vertex, we store the set of cargo types that may be present on arrival.
- In the worst case, each vertex may store all `C` cargo types.

## Input

```
S T
s c_unload c_load      (S lines)
s_from s_to            (T lines)
s0
```

## Notes

- Unreachable stations are reported with an empty set.

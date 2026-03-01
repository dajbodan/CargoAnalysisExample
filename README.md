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

- Time: `O(E · C^2)`,
  - because merging predecessor costs $deg_in(v) * C$ for some vertex v that we're computing.
  - And it is possible in algorithm to visit one vertex $v$. Let's call $updates(v)$, how many times we visit v during the exection.
  - Each time we visit v, we should gain new cargo. Otherwise, we don't visit v. Therefore number of updates is at most |C|
  - In total, we are iterating for each vertex $Σ(updates(v) * deg_in(v) * C) <= Σ(|C| * deg_in(v) * C) <= Σ( |E| * C^2) = O(|E| * |C|^2)$
- Space: `O(V · C)`, because for each vertex we store cargos

## Input

```
S T
s c_unload c_load      (S lines)
s_from s_to            (T lines)
s0
```

## Notes

- Cargo types are abstract labels; quantity does not matter.
- Unreachable stations are reported with an empty set.

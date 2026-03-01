# Railway Cargo Reachability

This project solves a reachability problem on a directed graph with state propagation.

Each station unloads one cargo type on arrival and loads one cargo type before departure.
Starting from a given station with empty cargo, the goal is to determine which cargo types
**may be present on a train when it arrives** at each station.

## Approach

The solution uses a monotone data-flow analysis with a worklist:
- for each station, we track the set of possible cargo types on arrival
- cargo is propagated along edges using the rule: unload first, then load
- sets grow monotonically until a fixpoint is reached

Cycles and returning to the start station are handled naturally by the iteration.

## Complexity

Let `V` be the number of stations, `E` the number of tracks, and `C` the number of cargo types.

- Time: `O(E · C)`
- Space: `O(V · C)`

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

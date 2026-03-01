package org.example

import kotlin.collections.getOrPut



class CargoReachabilityAnalysis(val graph: Graph) {

    private val reversedEdges: MutableMap<Node, MutableList<Node>> = mutableMapOf()

    init {
        graph.edges.forEach {
            (vertex, edges) -> edges.forEach { edge ->
                reversedEdges.getOrPut(edge){ mutableListOf() }.add(vertex)
            }
        }
    }

    fun compute(startingVertexId: Int) : Map<Node, Set<CargoType>>{
        val queue: ArrayDeque<Node> = ArrayDeque()
        val arrivalCargo: MutableMap<Node, MutableSet<CargoType>> = mutableMapOf()
        val startingNode = graph.vertices.find { it.id == startingVertexId } ?: error("Can't find node $startingVertexId")

        graph.edges[startingNode]?.let { it.forEach { queue.addLast(it) } }
        arrivalCargo[startingNode] = mutableSetOf()

        while (queue.isNotEmpty()) {
            val vertex = queue.removeFirst()

            val previousCargos = arrivalCargo.getOrPut(vertex) { mutableSetOf() }
            val newCargos = mergePredecessor(vertex, arrivalCargo)

            // arrivalCargo[v] grows monotonically; we propagate only when it gains new cargo types
            if (previousCargos.addAll(newCargos))
                graph.edges[vertex]?.forEach(queue::addLast)

        }
        graph.vertices.forEach { arrivalCargo.computeIfAbsent(it) { mutableSetOf() } }
        return arrivalCargo.toSortedMap(compareBy { it.id } )
    }



    // NOTE: Cargo sets are represented as Set<Int> for clarity.
    // For performance, this can be replaced with a BitSet
    private fun mergePredecessor(vertex: Node, arrivalCargo: MutableMap<Node, MutableSet<CargoType>>) : Set<CargoType>
    {
        val result = mutableSetOf<CargoType>()
        val predecessor = reversedEdges[vertex]?.let{ it.filter { arrivalCargo.contains(it) }}
        if (predecessor != null)
        {
            for( it in predecessor)
            {
                val predArriveCargo = arrivalCargo[it] ?: throw error("No previous arrive cargos found for edge $it")
                for(itCargos in predArriveCargo)
                    if(itCargos != it.c_unload)
                        result.add(itCargos)

                result.add(it.c_load)
            }
        }
        return result
    }
}
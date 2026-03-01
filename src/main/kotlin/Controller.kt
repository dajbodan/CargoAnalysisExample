package org.example

import java.lang.Appendable

class Controller(){
    fun readGraphFromInput(input: ReadInput) : Pair<Graph, VertexId> {
        return input.parseInput()
    }
    fun run(output : Appendable, startingVertex: VertexId, analysis: CargoReachabilityAnalysis)
    {
        val arrivalCargo = analysis.compute(startingVertex)
        formatResult(output, arrivalCargo)
    }

    fun formatResult(res: kotlin.text.Appendable, arrivalCargo: Map<Node, Set<Int>>) : Unit
    {
        arrivalCargo
            .forEach { (v, cargos) ->
                res.append(v.id.toString())
                res.append(": ")
                cargos
                    .toSortedSet()
                    .forEach { c ->
                        res.append(c.toString())
                        res.append(" ")
                    }

                res.append('\n')
            }
    }

    fun printGraph(graph: Graph) : String
    {
        var res = "\nvertices and corresponding cargo (<unload>, <load>):\n"
        graph.vertices.forEach { v -> res += v.id.toString() + " ("+ v.c_unload.toString() +"," + v.c_load.toString() + ")" +"; " }
        res += "\nedges: "
        graph.edges.forEach { u -> u.value.forEach { v ->  res += "{" + u.key.id.toString() + "," + v.id.toString() + "}; " }}

        return res
    }
}
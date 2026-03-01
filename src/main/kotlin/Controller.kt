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
}
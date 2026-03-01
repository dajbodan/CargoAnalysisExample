package org.example

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream

class CargoReachabilityAnalysisTest {

    private fun runAndParse(graph: Graph, startId: Int): Map<Int, Set<Int>> {
        val analysis = CargoReachabilityAnalysis(graph)
        val controller = Controller()
        val out = StringBuilder()
        controller.formatResult( out,analysis.compute(startId))

        // Expected output format per line: "<stationId>: <c1> <c2> ..."
        // Example: "3: 2 5 9 "
        val result = mutableMapOf<Int, Set<Int>>()
        out.toString()
            .trim()
            .lines()
            .filter { it.isNotBlank() }
            .forEach { line ->
                val parts = line.split(":")
                val id = parts[0].trim().toInt()
                val cargos =
                    if (parts.size < 2) emptySet()
                    else parts[1].trim()
                        .split(" ")
                        .filter { it.isNotBlank() }
                        .map { it.toInt() }
                        .toSet()
                result[id] = cargos
            }
        return result
    }

    /**
     * Graph:
     *   0 -> 1 -> 0
     *
     * Station 0: unload 99, load 1
     * Station 1: unload 1,  load 2
     */
    @Test
    fun startStationMustAccumulateCargoWhenCycleReturnsToStart() {
        val n0 = Node(id = 0, c_unload = 99, c_load = 1)
        val n1 = Node(id = 1, c_unload = 1, c_load = 2)

        val graph = Graph(
            vertices = listOf(n0, n1),
            edges = mapOf(
                n0 to listOf(n1),
                n1 to listOf(n0),
            )
        )

        val res = runAndParse(graph, startId = 0)

        assertTrue(res.containsKey(0), "Output should include station 0")
        assertTrue(2 in (res[0] ?: emptySet()), "Station 0 must include cargo 2 on arrival due to cycle 0->1->0")
    }

    /**
     * Graph:
     *   0 -> 1
     *   2 isolated (unreachable)
     */
    @Test
    fun shouldOutputUnreachableStationsAsEmpty() {
        val n0 = Node(id = 0, c_unload = 10, c_load = 1)
        val n1 = Node(id = 1, c_unload = 20, c_load = 2)
        val n2 = Node(id = 2, c_unload = 30, c_load = 3) // unreachable

        val graph = Graph(
            vertices = listOf(n0, n1, n2),
            edges = mapOf(
                n0 to listOf(n1)
                // n2 has no edges
            )
        )

        val res = runAndParse(graph, startId = 0)

        assertTrue(res.containsKey(2), "Output should include unreachable station 2 (as empty set)")
        assertEquals(emptySet<Int>(), res[2], "Unreachable station 2 should have empty cargo set on arrival")
    }

    @Test
    fun output_mustContainAllStations_evenUnreachable() {
        val input = """
            3 1
            0 10 11
            1 20 21
            2 30 31
            0 1
            0
        """.trimIndent() + "\n"

        val (graph, start) = Controller().readGraphFromInput(ReadInput(ByteArrayInputStream(input.toByteArray())))
        val analysis = CargoReachabilityAnalysis(graph)

        val out = StringBuilder()
        Controller().run(out, start, analysis)

        val linesById = out.toString().trim().lines().associateBy { it.substringBefore(":").trim().toInt() }

        assertEquals(true, linesById.containsKey(2), "Output must include unreachable station 2")
    }

    @Test
    fun unload_then_load_order_matters_when_unload_equals_load() {
        val n0 = Node(id = 0, c_unload = 999, c_load = 5)
        val n1 = Node(id = 1, c_unload = 5, c_load = 5)   // trap: unload==load
        val n2 = Node(id = 2, c_unload = 999, c_load = 999)

        val graph = Graph(
            vertices = listOf(n0, n1, n2),
            edges = mapOf(
                n0 to listOf(n1),
                n1 to listOf(n2),
                n2 to emptyList()
            )
        )

        val got = runAndParse(graph, startId = 0)
        val cargosAt2 = got[2] ?: emptySet()

        assertTrue(
            5 in cargosAt2,
            "Expected cargo 5 to be possible on ARRIVAL to station 2 (0->1->2), but got $cargosAt2. " +
                    "This usually fails if implementation does LOAD before UNLOAD."
        )
    }

    // Station 0: unloads 10, loads 20. Start at 0. There is self-loop
    @Test
    fun testArrivalExcludesCurrentLoadOnInitialStart() {

        val n0 = Node(id = 0, c_unload = 10, c_load = 20)
        val graph = Graph(listOf(n0), mapOf(n0 to listOf(n0)))

        val analysis = CargoReachabilityAnalysis(graph)
        val out = StringBuilder()
        val controller = Controller()
        controller.formatResult( out,analysis.compute(n0.id))


        val result = out.toString().trim()
        assertTrue(result.contains("0: 20"), "Candidate includes 20 due to cycle-back-to-start logic.")
    }
}
package org.example

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.StringTokenizer

class ReadInput(input : InputStream){
    private val br = BufferedReader(InputStreamReader(input))
    private var st = StringTokenizer("")

    fun parseInput() : Pair<Graph, VertexId>
    {
        val S : Int = next().toInt()
        val T : Int = next().toInt()
        val vertices : MutableList<Node> = mutableListOf()
        val byId: MutableMap<Int, Node> = mutableMapOf()
        repeat(S){
            val s = next().toInt()
            val c_unload = next().toInt()
            val c_load = next().toInt()
            val tempVertex = Node(s,c_unload, c_load)
            val retValId = byId.put(s, tempVertex)
            require(retValId == null) { "Duplicate station id: $s" }
            vertices.add(tempVertex)
        }
        val edges: MutableMap<Node, MutableList<Node>> = mutableMapOf()
        repeat(T)
        {
            val fromId = next().toInt()
            val toId = next().toInt()
            val from = byId[fromId] ?: error("Unknown station id: $fromId")
            val to = byId[toId] ?: error("Unknown station id: $toId")

            edges.getOrPut(from) { mutableListOf() }.add(to)

        }

        return Pair(Graph(vertices, edges), next().toInt())
    }


    private fun next(): String {
        while (!st.hasMoreTokens()) {
            val line = br.readLine()
                ?: throw NoSuchElementException("Unexpected end of input")
            st = StringTokenizer(line)
        }
        return st.nextToken()
    }
}
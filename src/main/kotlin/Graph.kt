package org.example

typealias VertexId = Int
typealias CargoType = Int

data class Node(
    val id: VertexId,
    val c_unload: CargoType,
    val c_load: CargoType,
)

data class Graph (
    val vertices: List<Node>,
    val edges: Map<Node, List<Node>>
)
import org.example.CargoReachabilityAnalysis
import org.example.Controller
import org.example.ReadInput
import java.io.FileInputStream




fun main(args: Array<String>) {

    println("DEMO DEMONSTRATION")

    val controller = Controller()

    val fileStr = "input.txt"
    println("Reading file : $fileStr")
    val (graph, startVertex) = controller.readGraphFromInput(ReadInput(FileInputStream(fileStr)))
    println("Graph: ${controller.printGraph(graph)}")
    println("Starting analysis and writing output for starting vertex $startVertex:")

    controller.run(System.out,startVertex,  CargoReachabilityAnalysis(graph))
    println("Finished analysis")
    println("DEMO is finished")
}

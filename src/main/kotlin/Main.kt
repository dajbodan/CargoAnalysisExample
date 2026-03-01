import org.example.CargoReachabilityAnalysis
import org.example.Controller
import org.example.ReadInput

fun main(args: Array<String>) {

    val controller = Controller()
    val (graph, startVertex) = controller.readGraphFromInput(ReadInput(System.`in`))

    controller.run(System.out,startVertex,  CargoReachabilityAnalysis(graph))
}
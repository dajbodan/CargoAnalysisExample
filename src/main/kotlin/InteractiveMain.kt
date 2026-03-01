import org.example.CargoReachabilityAnalysis
import org.example.Controller
import org.example.ReadInput
import java.io.FileInputStream
import java.io.InputStream

private fun chooseInput(): InputStream {
    println("Choose input method:")
    println("1 — read from standard input (paste data)")
    println("2 — read from file path")
    print("> ")

    return when (readLine()?.trim()) {
        "1" -> System.`in`
        "2" -> {
            print("Enter path: ")
            FileInputStream(readLine()!!.trim())
        }
        else -> error("Invalid choice")
    }
}

fun main() {
    val input = chooseInput()
    val controller = Controller()
    val (graph, startVertex) = controller.readGraphFromInput(ReadInput(input))
    controller.run(System.out, startVertex, CargoReachabilityAnalysis(graph))
}
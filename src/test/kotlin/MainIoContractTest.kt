package org.example

import main
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.nio.file.Path

class MainIoContractTest {

    @TempDir
    lateinit var tmpDir: Path

    @Test
    fun main_mustReadFromStdin_notHardcodedInputTxt() {

        val input = """
            1 0
            0 10 20
            0
        """.trimIndent() + "\n"

        val oldIn = System.`in`
        val oldOut = System.out
        val oldUserDir = System.getProperty("user.dir")

        val outBuffer = ByteArrayOutputStream()

        try {
            System.setProperty("user.dir", tmpDir.toString())

            System.setIn(ByteArrayInputStream(input.toByteArray()))
            System.setOut(PrintStream(outBuffer))

            assertDoesNotThrow({ main(arrayOf()) }, "main() must read from stdin;")

            val output = outBuffer.toString()

            assertTrue(output.contains("0:"), "Expected output to contain station 0 line, got: $output")
        } finally {
            System.setIn(oldIn)
            System.setOut(oldOut)
            System.setProperty("user.dir", oldUserDir)
        }
    }
}
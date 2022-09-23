import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import uk.org.webcompere.systemstubs.jupiter.SystemStub
import uk.org.webcompere.systemstubs.jupiter.SystemStubsExtension
import uk.org.webcompere.systemstubs.stream.SystemOut
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SystemStubsExtension::class)
internal class MainKtTest {

    @SystemStub
    private val systemOut: SystemOut = SystemOut()

    @ParameterizedTest
    @MethodSource("testCommandsForStage1")
    fun `verify message output for each command`(commands: TestCommands) {
        systemOut.clear()
        main(arrayOf(commands.command))
        assertEquals(commands.expected, systemOut.linesNormalized)
    }

    data class TestCommands(val command: String, val expected: String)

    private fun testCommandsForStage1() = Stream.of(
        TestCommands("config", "Get and set a username.\n"),
        TestCommands("add", "Add a file to the index.\n"),
        TestCommands("log", "Show commit logs.\n"),
        TestCommands("commit", "Save changes.\n"),
        TestCommands("checkout", "Restore a file.\n"),
        TestCommands("--help",
            """
            These are SVCS commands:
            config     Get and set a username.
            add        Add a file to the index.
            log        Show commit logs.
            commit     Save changes.
            checkout   Restore a file.
            
            """.trimIndent()
        ),
        TestCommands("",
            """
            These are SVCS commands:
            config     Get and set a username.
            add        Add a file to the index.
            log        Show commit logs.
            commit     Save changes.
            checkout   Restore a file.
            
            """.trimIndent()
        )
    )
}

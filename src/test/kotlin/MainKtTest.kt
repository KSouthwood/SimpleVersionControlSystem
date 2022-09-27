import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import uk.org.webcompere.systemstubs.jupiter.SystemStub
import uk.org.webcompere.systemstubs.jupiter.SystemStubsExtension
import uk.org.webcompere.systemstubs.stream.SystemOut
import java.io.File
import java.util.stream.Stream
import kotlin.test.BeforeTest

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
//        TestCommands("config", "Get and set a username.\n"),
//        TestCommands("add", "Add a file to the index.\n"),
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

    @BeforeTest
    fun `prepare directory for testing`() {
        val configDir = File("vcs")
        if (configDir.exists()) {
            configDir.listFiles()?.forEach { it.delete() }
            configDir.delete()
        }
    }

    @Test
    fun `test config command`() {
        systemOut.clear()
        main(arrayOf("config"))
        main(arrayOf("config", "John"))
        main(arrayOf("config"))
        main(arrayOf("config", "Susan"))
        assertEquals("""
            Please, tell me who you are.
            The username is John.
            The username is John.
            The username is Susan.
            
            """.trimIndent(),
        systemOut.linesNormalized)
    }

    @Test
    fun `test add command`() {
        systemOut.clear()
        main(arrayOf("add"))
        main(arrayOf("add", "file_A.txt"))
        main(arrayOf("add"))
        main(arrayOf("add", "file_B.txt"))
        main(arrayOf("add"))
        main(arrayOf("add", "file_Z.txt"))
        assertEquals("""
            Add a file to the index.
            The file 'file_A.txt' is tracked.
            Tracked files:
            file_A.txt
            The file 'file_B.txt' is tracked.
            Tracked files:
            file_A.txt
            file_B.txt
            Can't find 'file_Z.txt'.
            
        """.trimIndent(),
        systemOut.linesNormalized)
    }
}

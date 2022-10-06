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
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SystemStubsExtension::class)
internal class MainKtTest {
    private val fileA = File("test_tracked_file1.txt")
    private val fileB = File("test_tracked_fileA.txt")
    private val fileUntracked = File("test_untracked.txt")

    @SystemStub
    private val systemOut: SystemOut = SystemOut()

    @ParameterizedTest
    @MethodSource("testCommandsForStage1")
    fun `verify message output for each command`(commands: TestCommands) {
        systemOut.clear()
        main(commands.command)
        assertEquals(commands.expected, systemOut.linesNormalized)
    }

    data class TestCommands(val command: Array<String>, val expected: String)

    private fun testCommandsForStage1() = Stream.of(
//        TestCommands("config", "Get and set a username.\n"),
//        TestCommands("add", "Add a file to the index.\n"),
//        TestCommands("log", "Show commit logs.\n"),
//        TestCommands("commit", "Save changes.\n"),
        TestCommands(arrayOf("checkout"), "Restore a file.\n"),
        TestCommands(arrayOf("--help"),
            """
            These are SVCS commands:
            config     Get and set a username.
            add        Add a file to the index.
            log        Show commit logs.
            commit     Save changes.
            checkout   Restore a file.
            
            """.trimIndent()
        ),
        TestCommands(arrayOf(),
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

    @AfterTest
    fun `clean up files and directory`() {
        fileA.delete()
        fileB.delete()
        fileUntracked.delete()
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

    @Test
    fun `test log and commit`() {
        fileA.createNewFile()
        fileB.createNewFile()
        fileUntracked.createNewFile()

        fileA.writeText("I am the first file.\n")
        fileB.writeText("I am the second file.\n")
        fileUntracked.writeText("I am ignored.\n")

        systemOut.clear()
        main(arrayOf("log"))
        main(arrayOf("commit"))
        assertEquals("No commits yet.\nMessage was not passed.\n", systemOut.linesNormalized)
        systemOut.clear()

        main(arrayOf("add", "test_tracked_file1.txt"))
        main(arrayOf("add", "test_tracked_fileA.txt"))
        assertEquals("""
            The file 'test_tracked_file1.txt' is tracked.
            The file 'test_tracked_fileA.txt' is tracked.
            
        """.trimIndent(), systemOut.linesNormalized)
        systemOut.clear()

        main(arrayOf("commit", "\"First revision.\""))
        assertEquals("Changes are committed.\n", systemOut.linesNormalized)
        systemOut.clear()


    }
}

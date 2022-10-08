import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import uk.org.webcompere.systemstubs.jupiter.SystemStub
import uk.org.webcompere.systemstubs.jupiter.SystemStubsExtension
import uk.org.webcompere.systemstubs.stream.SystemOut
import java.io.File

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SystemStubsExtension::class)
internal class MainKtTest {
    private val filenameA = "test_tracked_file1.txt"
    private val filenameB = "test_tracked_fileA.txt"
    private val filenameU = "test_untracked.txt"
    private val fileA = File(filenameA)
    private val fileB = File(filenameB)
    private val fileU = File(filenameU)

    private val separator: String = File.separator
    private val workingDir = ".${separator}"
    private val vcsDir = File("${workingDir}vcs")
    private val logFile = File("${vcsDir}${separator}log.txt")

    private val helpMsg = """
            These are SVCS commands:
            config     Get and set a username.
            add        Add a file to the index.
            log        Show commit logs.
            commit     Save changes.
            checkout   Restore a file.
            
            """.trimIndent()

    private val logEntry1 = """
            commit 480fe88cafc31a203a3282afe7b52b48ef47e5ffa8e9202a59db602d1e638dd9
            Author: Richard
            First revision.
        """.trimIndent()

    private val logEntry2 = """
            commit 538b8d7459204ff56fbf80f3d64895635011ed0a91ede5c83b235da377ed1d39
            Author: Susan
            Add to file A.
        """.trimIndent()

    private val logEntry3 = """
            commit 4eb1f3896bd2a0dab575946db720fd1b9cd00a83f572818fd448ad6f8f2625c0
            Author: Jason
            Changed file B.
        """.trimIndent()

    @SystemStub
    private val systemOut: SystemOut = SystemOut()

    @BeforeEach
    fun `prepare directory for testing`() {
        vcsDir.deleteRecursively()
    }

    @AfterEach
    fun `clean up files and directory`() {
        fileA.delete()
        fileB.delete()
        fileU.delete()
        vcsDir.deleteRecursively()
    }

    private fun assertCommandOutput(command: String, expected: String) {
        assertCommandOutput(arrayOf(command), expected)
    }

    private fun assertCommandOutput(command: Array<String> = arrayOf(), expected: String) {
        systemOut.clear()
        main(command)
        assertEquals(expected, systemOut.linesNormalized)
    }

    @Test
    fun `test invalid commands`() {
        assertCommandOutput("find", "'find' is not a SVCS command.\n")
        assertCommandOutput("quit", "'quit' is not a SVCS command.\n")
        assertCommandOutput("exit", "'exit' is not a SVCS command.\n")
    }

    @Test
    fun `test displaying help message`() {
        assertCommandOutput("--help", helpMsg)
        assertCommandOutput(expected = helpMsg)
    }

    @Test
    fun `test command output before anything is added or set`() {
        assertCommandOutput("config", "Please, tell me who you are.\n")
        assertCommandOutput("add", "Add a file to the index.\n")
        assertCommandOutput("log", "No commits yet.\n")
        assertCommandOutput("commit", "Message was not passed.\n")
        assertCommandOutput("checkout", "Commit id was not passed.\n")
    }

    @Test
    fun `test add command`() {
        fileA.createNewFile()
        fileB.createNewFile()
        systemOut.clear()

        // run commands
        main(arrayOf("add"))
        main(arrayOf("add", filenameA))
        main(arrayOf("add"))
        main(arrayOf("add", filenameB))
        main(arrayOf("add"))
        main(arrayOf("add", filenameU))

        // check output
        assertEquals("""
            Add a file to the index.
            The file '$filenameA' is tracked.
            Tracked files:
            $filenameA
            The file '$filenameB' is tracked.
            Tracked files:
            $filenameA
            $filenameB
            Can't find '$filenameU'.
            
        """.trimIndent(),
        systemOut.linesNormalized)
    }

    @Test
    fun `test sample workflow with commits and checkout`() {
        assertCommandOutput("config", "Please, tell me who you are.\n")
        assertCommandOutput(arrayOf("config", "Richard"), "The username is Richard.\n")
        createInitialTestFiles()
        assertCommandOutput(arrayOf("add", filenameA), "The file '$filenameA' is tracked.\n")
        assertCommandOutput(arrayOf("add", filenameB), "The file '$filenameB' is tracked.\n")
        assertCommandOutput("add", "Tracked files:\n$filenameA\n$filenameB\n")
        assertCommandOutput("log", "No commits yet.\n")
        assertCommandOutput(arrayOf("commit", "First revision."), "Changes are committed.\n")
        checkLogFileInitial()
        assertCommandOutput(arrayOf("config", "Susan"), "The username is Susan.\n")
        assertCommandOutput(arrayOf("config"), "The username is Susan.\n")
        firstEdit()
        assertCommandOutput(arrayOf("commit", "Add to file A."), "Changes are committed.\n")
        checkLogFileFirstEdit()
        assertCommandOutput(arrayOf("config", "Jason"), "The username is Jason.\n")
        assertCommandOutput(arrayOf("commit", "This should fail."), "Nothing to commit.\n")
        secondEdit()
        assertCommandOutput(arrayOf("commit", "Changed file B."), "Changes are committed.\n")
        checkLogFileSecondEdit()
        assertCommandOutput(arrayOf("checkout", "DEADBEEF"), "Commit does not exist.\n")
        assertCommandOutput(arrayOf("checkout", "480fe88cafc31a203a3282afe7b52b48ef47e5ffa8e9202a59db602d1e638dd9"),
        "Switched to commit 480fe88cafc31a203a3282afe7b52b48ef47e5ffa8e9202a59db602d1e638dd9.\n")
        verifyCheckedOutFiles()
        assertCommandOutput("log", "$logEntry3\n$logEntry2\n$logEntry1\n")
    }

    private fun createInitialTestFiles() {
        fileA.createNewFile()
        fileB.createNewFile()
        fileU.createNewFile()

        fileA.writeText("I am the first file.\n")
        fileB.writeText("I am the second file.\n")
        fileU.writeText("I am ignored.\n")
    }

    private fun firstEdit() {
        fileA.appendText("First edit.")
    }

    private fun secondEdit() {
        fileB.appendText("Second edit.")
    }

    private fun getLatestCommitMessage(): String {
        return logFile.readLines().subList(0, 3).joinToString("\n")
    }

    private fun checkLogFileInitial() {
        val actual = getLatestCommitMessage()
        assertEquals(logEntry1, actual)
    }

    private fun checkLogFileFirstEdit() {
        val actual = getLatestCommitMessage()
        assertEquals(logEntry2, actual)
    }

    private fun checkLogFileSecondEdit() {
        val actual = getLatestCommitMessage()
        assertEquals(logEntry3, actual)
    }

    private fun verifyCheckedOutFiles() {
        assertEquals("I am the first file.\n", fileA.readText())
        assertEquals("I am the second file.\n", fileB.readText())
    }
}

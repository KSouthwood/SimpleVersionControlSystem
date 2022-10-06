import java.io.File
import java.security.MessageDigest

class Operations {
    private val separator: String = File.separator
    private val workingDir = ".${separator}"
    private val vcsDir = File("${workingDir}vcs")
    private val configFile = File("${vcsDir}${separator}config.txt")
    private val indexFile = File("${vcsDir}${separator}index.txt")
    private val logFile = File("${vcsDir}${separator}log.txt")

    init {
        if (!vcsDir.exists()) {
            vcsDir.mkdir()
            configFile.createNewFile()
            indexFile.createNewFile()
            logFile.createNewFile()
        }
    }

    fun config(name: List<String>) {
        if (name.isEmpty() && configFile.length() == 0L) {
            println("Please, tell me who you are.")
            return
        }

        if (name.isNotEmpty()) {
            configFile.writeText(name.first())
        }

        println("The username is ${configFile.readText()}.")
    }

    fun add(files: List<String>) {
        when (files.isEmpty()) {
            true -> {
                if (indexFile.length() == 0L) {
                    println("Add a file to the index.")
                } else {
                    println("Tracked files:")
                    indexFile.forEachLine { println(it) }
                }
            }
            false -> {
                files.forEach {
                    when (File(it).exists()) {
                        true -> {
                            indexFile.appendText("$it\n")
                            println("The file '$it' is tracked.")
                        }
                        false -> println("Can't find '$it'.")
                    }
                }
            }
        }
    }

    fun log() {
        if (logFile.length() == 0L) {
            println("No commits yet.")
            return
        }

        logFile.forEachLine { println(it) }
    }

    fun commit(message: List<String>) {
        if (message.isEmpty()) {
            println("Message was not passed.")
            return
        }

        val msgDigest = MessageDigest.getInstance("SHA-256")

        indexFile.forEachLine {
            val file = File("$workingDir$it")
            file.forEachLine { line -> msgDigest.update(line.toByteArray()) }
        }

        val digest = msgDigest.digest().joinToString { "%02x".format(it) }
        val prevDigest = if (logFile.length() != 0L)
            logFile.bufferedReader().use { it.readLine() }.drop(7)
        else
            ""

        if (prevDigest == digest) {
            println("Nothing to commit.")
            return
        }

        // changes were made, copy files to commits directory
        val commitDir = File("$commitsDir$separator$digest")
        commitDir.mkdir()
        indexFile.forEachLine { File("$workingDir$it").copyTo(commitDir) }

        // write commit message
        val oldLogFile = File("${vcsDir}${separator}log.old")
        if (!logFile.renameTo(oldLogFile)) {
            println("Rename operation failed.")
            return
        }
        logFile.createNewFile()
        logFile.writeText("""
            commit $digest
            Author: ${configFile.readText()}
            ${message.first()}
            
        """.trimIndent())
        oldLogFile.forEachLine { logFile.appendText(it) }
        println("Changes are committed.")
    }

    fun checkout(branch: List<String>) {
        println("Restore a file.")
    }
}

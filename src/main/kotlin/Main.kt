import java.io.File

fun main(args: Array<String>) {
    when (if (args.isNotEmpty()) args[0] else "") {
        "config" -> config(args)
        "add" -> add(args)
        "log" -> log(args)
        "checkout" -> checkout(args)
        "commit" -> commit(args)
        "--help", "" -> help()
        else -> println("'${args[0]}' is not a SVCS command.")
    }
}

private fun help() {
    println(
        """
        These are SVCS commands:
        config     Get and set a username.
        add        Add a file to the index.
        log        Show commit logs.
        commit     Save changes.
        checkout   Restore a file.
    """.trimIndent()
    )
}

private fun vcsDirectoryExists() {
    if (!File("vcs").exists()) {
        File("vcs").mkdir()
    }
}

private fun config(args: Array<String>) {
    vcsDirectoryExists()
    val config = File("vcs${File.separator}config.txt")
    if (!config.exists()) {
        config.createNewFile()
    }
    when (args.size) {
        1 -> {
            val name = config.readText()
            println(
                if (name.isEmpty()) "Please, tell me who you are."
                else "The username is $name."
            )
        }

        2 -> {
            config.writeText(args[1])
            println("The username is ${args[1]}.")
        }
    }
}

private fun add(args: Array<String>) {
    vcsDirectoryExists()
    val tracked = File("vcs${File.separator}index.txt")
    tracked.createNewFile()
    when (args.size) {
        1 -> if (tracked.length() == 0L) {
            println("Add a file to the index.")
        } else {
            println("Tracked files:")
            tracked.forEachLine { println(it) }
        }
        2 -> {
            val addToIndex = File(args[1])
            if (addToIndex.exists()) {
                tracked.appendText("${args[1]}\n")
                println("The file '${args[1]}' is tracked.")
            } else {
                println("Can't find '${args[1]}'.")
            }
        }
    }
}

private fun log(args: Array<String>) {
    println("Show commit logs.")
}

private fun commit(args: Array<String>) {
    println("Save changes.")
}

private fun checkout(args: Array<String>) {
    println("Restore a file.")
}

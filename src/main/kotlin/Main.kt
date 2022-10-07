fun main(args: Array<String>) {
    val ops = Operations()
    when (args.firstOrNull()) {
        "config" -> ops.config(args.drop(1))
        "add" -> ops.add(args.drop(1))
        "log" -> ops.log()
        "checkout" -> ops.checkout(args.drop(1))
        "commit" -> ops.commit(args.drop(1))
        "--help", null -> help()
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

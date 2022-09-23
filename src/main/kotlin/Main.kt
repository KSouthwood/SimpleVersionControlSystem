fun main(args: Array<String>) {
    when (if (args.isNotEmpty()) args[0] else "") {
        "config" -> config(args)
        "add" -> add(args)
        "log" -> log(args)
        "checkout" -> checkout(args)
        "commit" -> commit(args)
        "--help", "" -> help()
        else -> println("${args[0]} is not a SVCS command.")
    }
}

private fun help() {
    println("""
        These are SVCS commands:
        config     Get and set a username.
        add        Add a file to the index.
        log        Show commit logs.
        commit     Save changes.
        checkout   Restore a file.
    """.trimIndent())
}

private fun config(args: Array<String>) {
    println("Get and set a username.")
}

private fun add(args: Array<String>) {
    println("Add a file to the index.")
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

# [Version Control System](https://hyperskill.org/projects/177)

## About

The ability to roll back to the previous versions is crucial for software 
development. In this project, you will get acquainted with the idea of 
version control and write a simple version control system.

## Learning Outcomes

Use your knowledge of functions, files, exceptions, and hash handling to write
a simple software that can track changes of files.

### Stage 1: [Help page](https://hyperskill.org/projects/177/stages/909/implement)

#### _Create a simple interface with a help page and a list of commands._

A **version control system** is software that can keep track of the changes 
that were implemented to a program. There are several popular version control 
systems (like Git, SVN, or Mercurial). Each of them has its pros and cons. 
They share one common idea. A version control system remembers **who** changed 
the file, when it was done, and **why**. It allows you to roll back to the 
previous versions as well.

In this project, you need to implement a simple version control system. It 
will be your own Git. By the way, if you want to learn how to work with Git, 
take a look at the [Git How To](https://githowto.com/).

Take a look at the commands you will need to implement during this project:

- `config` sets or outputs the name of a commit author;
- `--help` prints the help page;
- `add` adds a file to the list of tracked files or outputs this list;
- `log` shows all commits;
- `commit` saves file changes and the author name;
- `checkout` allows you to switch between commits and restore a previous 
  file state.

In this stage, your program should be able to accept a single argument and, 
depending on the argument, print help information.

### Stage 2: [Add & config](https://hyperskill.org/projects/177/stages/910/implement)

#### _Add first commands to your VCS that will allow you to add new files._

In this stage, your program should allow a user to set their name and add the
files they want to track. Store a username in _config.txt_.

_index.txt_ stores the files that were added to the index. Don't forget to 
store all the files of the version control system in the _vcs_ directory. 
You should create this directory programmatically. It may look something 
like this:
```text
|---- vcs
|     |---- config.txt
|     |---- index.txt
|
|---- tracked_file.txt
|---- untracked_file.txt
```

You need to work on the following commands:
- `config` should allow the user to set their own name or output an already 
  existing name. If a user wants to set a new name, the program must 
  overwrite the old one.
- `add` should allow the user to set the name of a file that they want to 
  track or output the names of tracked files. If the file does not exist, 
  the program should inform a user that the file does not exist.

### Stage 3: [Log & commit](https://hyperskill.org/projects/177/stages/911/implement)

#### _Continue implementing new commands to save file changes and log the results._

In this stage, you should implement two commands. `commit` will allow a 
user to save file changes; `log` will allow the viewing the commit history.

The purpose of this project is to work with files. Store different file 
versions in the index according to the commits and make sure that each 
commit has a unique ID. ID should allow you to find a specific file 
version that matches this commit. You need to think about the concept of a 
commit ID.

You can store commits in _vsc/commits_. Each commit will be saved to a 
different directory. These directories will include a commits ID and 
additional information that you can store in _vsc/log.txt_.

You will also need to find out if a file has been changed since the last 
commit. For that, you need to calculate the hash of the current file and 
the hash of the last commit. If these values are different, then the file has
been changed. Use **Java Cryptography Architecture** (JCA). JCA includes 
solutions that are based on various cryptographic algorithms such as 
**SHA1**, **SHA256**, and others. Hash functions are optional, you can use 
a different solution.

Implement the following commands:
- `commit` must be passed to the program along with a message. (i.e. 
  `commit "sample commit message"`.) Save all 
  changes. Each commit must be assigned a unique ID. If there were no 
  changes since the last commit, do not create a new commit. You don't 
  need to optimize the storage of changes, just copy all the staged files 
  to the commit folder every time.
- `log` should show all the commits in reverse order.

### Stage 4: [Checkout time](https://hyperskill.org/projects/177/stages/912/implement)

#### _Add the last command to switch between commits and retrieve the file contents._

In the last stage, implement the `checkout` command. It allows a user to 
switch between commits and restore the contents of the files according to 
the current commit.

Get the files you need from the commit directory by the commit ID and 
rewrite the current files.

The `checkout` command must be passed to the program together with the 
commit ID to indicate which commit should be used. If a commit with 
the given ID exists, the contents of the tracked file should be restored in 
accordance with this commit.

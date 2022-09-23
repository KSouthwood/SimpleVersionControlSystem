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


### Stage 3: [Log & commit](https://hyperskill.org/projects/177/stages/911/implement)

#### _Continue implementing new commands to save file changes and log the results._

### Stage 4: [Checkout time](https://hyperskill.org/projects/177/stages/912/implement)

#### _Add the last command to switch between commits and retrieve the file contents._

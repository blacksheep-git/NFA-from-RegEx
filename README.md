# Project 3 (Regular Expressions)

 * Authors: Mateo Ortegon and Quinn Shultz
 * Class: CS361
 * Semester: Fall 2021

## Overview

In this project, we were asked to write code that constructs an NFA for a given regular expression
(RegEx). We pased the RegEx using the suggested recursive
descent parsing algorithm

## Compiling and Using

To run the program, execute the following commands:
```bash
javac  javac -cp ".:./CS361FA.jar" re/REDriver.java
java  java -cp ".:./CS361FA.jar" re.REDriver ./tests/<TEST_NAME>.txt
```

## Specification
```bash
|-- re
| |-REDriver.java
| |-REInterface.java
|-- tests
| |-- p3tc1.txt
| |-- p3tc2.txt
| |-- p3tc3.txt
|-- CS361FA.jar
|-- CS361FAdocs.zip
```
## Discussion
By far, the largest piece of the puzzle was the getNFA() method. The algorithm 
outlined in the [suggested resource](https://matt.might.net/articles/parsing-regex-with-recursive-descent/) was incredibly useful.

Our code ended up looking very similar to the code on the example - but we made an
effort to write as much of our own code as possible. That said, there was utility in 
staying consistent with the naming convention for sanity checking - so some of the method
and variable names are identical.

Another challenge with this specific project was having to use the .jar file, since we initially
couldn't look at the source code, to see how we could interact with it. Here, two things helped: 1) We were fairly
comfortable with our NFA implementation from our last project, so we referenced this initially. 2) We were able to
decompile the .jar file and look at the source code directly.

## Testing
For testing, we ran each of the input test files included under the 'tests' 
folder. We then compared our program's output to the sample output provided
in P3 handout. We went as far as ensuring the order of our programs output
matched the sample output perfectly.

## Sources Used
* [Recursive Descent Algorithm](https://matt.might.net/articles/parsing-regex-with-recursive-descent/)
* [Relaxing Music](https://www.youtube.com/watch?v=5qap5aO4i9A)
* [README Template](https://raw.githubusercontent.com/BoiseState/CS121-resources/master/projects/README_TEMPLATE.md)
* [Mark Down](https://github.com/adam-p/markdown-here/wiki/Markdown-Cheatsheet#links)


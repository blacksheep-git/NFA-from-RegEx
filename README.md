# Project 3: Project 3 (Regular Expressions)

 * Authors: Mateo Ortegon and Quinn Shultz
 * Class: CS361
 * Semester: Fall 2021

## Overview

We implemented two classes that model an instance of a nondeterministicfinite 
automaton (NFA) including a method that computes an equivalent DFA to a NFA’s instance.

## Compiling and Using

To run the program, execute the following commands:
```bash
javac fa/nfa/NFADriver.java
java fa.nfa.NFADriver ./tests/<TEST_NAME>.txt
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
By far, the largest piece of the puzzle was the getDFA() method. It helps that the
algorithm for transforming an NFA to DFA is fairly translatable as pseudocode from 
class notes to code, but this was still a huge pain. 

The part that is always a challenge with these assignments is sticking to the 
guidelines of "use Set data structure", and "you must use these methods and 
implement these interfaces" and "you can't modify any of the code here". For 
example, the very first bit of effort was spent figuring out how the NFADriver 
worked.

Another challenge was implementing the eClosure method recursively. To keep eClosure
one public method we had to create sets as instance variables that were accessible 
to all recursive calls. The issue was that after recursion had returned a value we
needed to clear the contents of the instance variable sets so that they would be 
empty for future calls. Alternatively, one could split the eClosure method into two,
one that creates the set variables that are only accessible during the lifetime of the
method call. And another that performs the recursion. We chose to keep everything as 
one method as it seemed more straightforward.

## Testing
For testing, we ran each of the input test files included under the 'tests' 
folder. We then compared our program's output to the sample output provided
in P2 handout. We went as far as ensuring the order of our programs output
matched the sample output perfectly.

## Sources Used
* [LinkedHashMap](https://docs.oracle.com/javase/8/docs/api/java/util/HashMap.html)
* [LinkedHashSet](https://docs.oracle.com/javase/7/docs/api/java/util/LinkedHashSet.html)
* [HashSet](https://docs.oracle.com/javase/7/docs/api/java/util/HashSet.html)
* [BFS](https://www.geeksforgeeks.org/breadth-first-search-or-bfs-for-a-graph/)
* [DFS](https://www.geeksforgeeks.org/depth-first-search-or-dfs-for-a-graph/)
* [Relaxing Music](https://www.youtube.com/watch?v=5qap5aO4i9A)
* [README Template](https://raw.githubusercontent.com/BoiseState/CS121-resources/master/projects/README_TEMPLATE.md)
* [Mark Down](https://github.com/adam-p/markdown-here/wiki/Markdown-Cheatsheet#links)


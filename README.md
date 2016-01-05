<sub><sup>Rev. 0.1-final</sub></sup>

# FAP
**FAP** is for Fuzzy Automata Platfrom. It's environment for [FAL](https://github.com/IngvarJackal/FAP/blob/master/README.md#fal) execution (Fuzzy Automata Language). Current reference implementation of abstract FAP-machine is [jFAP](https://github.com/IngvarJackal/FAP/blob/master/README.md#jfap).

# FAL
**FAL** consists of descriptions of **non-deterministic nested stack automata** with ability to call other automata. It's non typed language -- everything is unicode string.

There are unlimited number of stacks you can create, but they live only in automaton where they are created; no closures. You can manipulate stacks using commands (see [Commands](https://github.com/IngvarJackal/FAP/blob/master/README.md#commands) section).

To call another automaton, you should provide its name and name of a stack to ```call automatonname stackname``` command (for ```insert automatonname``` see [Fuzziness](https://github.com/IngvarJackal/FAP/blob/master/README.md#fuzziness-aka-multithreading-or-non-determinism) section). 

Descriptions of automata must be in **TGF** format. Automata are oriented graphs. Nodes of a graph contain commands for FAP, edges contain conditions for state transition. Both nodes and edges are strings of **unicode alphabet mixed with literals**. Name of automaton is name of a file before  ```.tgf ``` and in one file must be only one automaton.

Beginning of automaton is state with ```start``` command. . There can be multiple final states denoted with ```end stackname``` command. In the case of multiple reached final states random one is used (see [Fuzziness](https://github.com/IngvarJackal/FAP/blob/master/README.md#fuzziness-aka-multithreading-or-non-determinism) section).

An input of automaton is always in stack ```in```.

### Commands

 * to **create stack** you should write ```create stackname```. You can create stack literals (stack with multiple characters) using ```create stackname string``` to create ```s|t|r|i|n|g``` stack with ```g```  on the top.
 * to **destroy stack**, following syntax is used: ```destroy stackname```
 * to **pop** a value from a stack, you should write ```pop stackname```. Result is outputted from state
 * to **push** string into a stack, write ```push stackname``` to push input character of state precondition. Result of state is input character.
 * you can **clone stack** with ```clone stackname stackname2```, this command will create or overwrite stack named ```stackname2```

 * to **invoke automaton**, use ```call automatonname stackname```. It will load ```automatonname.tgf``` file and register it into FAP to be called. Input of this automaton will be concatenated string of ```stackname```, e.g. ```ccc|bbb|aaa``` stack will become ```aaabbbccc```. The result of automaton call will be placed into ```stackname``` after execution
 * to **substitute** state with an automaton, use ```insert automatonname```, see [Fuzziness](https://github.com/IngvarJackal/FAP/blob/master/README.md#fuzziness-aka-multithreading-or-non-determinism) for details.
 * to mark **starting state**, use ```start``` command. There must be only one state with that entry point.
 * **final states** are marked with ```end stackname``` command. In the case of multiple reached final states, see [Fuzziness](https://github.com/IngvarJackal/FAP/blob/master/README.md#fuzziness-aka-multithreading-or-non-determinism) section.
 
 * ``` ``` (empty or whitespace contents of state) means ```pop in``` command

To use contents of stack in commands instead of predefined literals, use ```#stackname```. It will concatenate stack contents as mentioned in ```call``` command.

### Literals
Escape character is ```\```. To escape ```\``` itself use double escaping ```\\\\```.

 * ```\e``` -- end of a string
 * ```\?``` -- any character, used only in edges

### Fuzziness (aka multithreading or non-determinism)

Every state of automaton may have multiple non-exclusive outgoing edges. If condition of transition function met, then new thread of execution spawned. Every thread uses own stacks and doesn't interfere with others.

It is guaranteed every thread to be executed until dead end or final state. If there are multiple final states, then one is chosen randomly, e.g. if 3 threads ended in 'a' final state, 1 in 'b' and 1 in 'c', then the probablilty of 'a' result is 3/5.

In the case of ```insert automatonname``` usage, there will be no final collapsing of results. Instead of that, new thread will be spawned for each result if any.

# jFAP
**jFAP** is the current reference implementation of abstract FAP-machine. It's interpreter of FAL for JVM on Scala.

### Features of jFAP

 * Java Virtual Machine
 * green threads
 * new ```invoke ru.org.codingteam.fap.death.Adder.apply stackname``` command, which calls java method from Adder class in registered jar library. It works like ```call``` command.

Header. Please write your name and the name of the paper C you have chosen.
• Description - Paper C (10 marks). Here you should briefly describe the main approach proposed by the authors, as well as their reasoning and assumptions.
• Results - Paper C (10 marks). Here you should briefly summarize the important results of the paper.
• Discussion - Paper C (40 marks). Here you should write your own assessment of the paper, focusing on issues and questions outlined above.
• Description - Paper B (10 marks). Here you should briefly describe the main approach proposed by the authors, as well as their reasoning and assumptions.
• Results - Paper B (10 marks). Here you should briefly summarize the important results of the paper.
• Discussion - Paper B (20 marks). Here you should write your own assessment of the paper, focusing on issues and questions outlined above.

1 | A Single-Chip Multiprocessor - Paper B
1.1 | Description
The paper in question introduces two main concepts of tackling the problem of a large number of transistors - simultaneous multithreading (SMT) and a chip multiprocessor (CMP) and compares both to a superscalar processor. Additionally, the paper discusses concerns about the latency of interconnect delays, the need for parallelism and the impact of increased number of transistors on design complexity.
Firstly, an SMT design proposed focuses on the use of multiple threads of execution each capable of issuing multiple instructions at once. It features dynamic selection of instructions to increase utilization. In the absence of thread level parallelism, the SMT appears as a conventional superscalar processor. 
Secondly, a CMP design focuses on utilizing multiple cores on a single die with the ability to issue multiple instructions at the same time, however, with no multithreading at the core level. The primary target of this design are environments where multi processes can be utilized and therefore allow each core handle the execution of an individual thread. 
Additionally, the paper raises concerns about the increased delay of interconnect wires due to increased size. As a result, it proposes the use of partitioned sections of the architecture in order to keep interconnects short and simple. Furthermore, the paper emphasises the need for parallelism for future computing environments in order to provide a more sophisticated computing environment. Finally, the paper addresses concerns of design complexity and the space (area) requirements for the chip. In both instances, it is argued that increased complexity and computing performance will lead to disproportionately larger increase in the size of the chip. It is argued that the CMP solution is better suited to tackle both complexity and space challenges.

2.2 | Results
The results presented to support the arguments of the paper are obtained through a simulation of the hardware architecutre in question based on a DRAM with 1 billion transistors. 




2.3 | Discussion

3 | Paper C
3.1 | Description

3.2 | Results

3.3 | Discussion
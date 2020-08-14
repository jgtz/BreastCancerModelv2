# Simulation of cell-specific ER+ *PIK3CA* mutant breast cancer dynamic network models

This program simulates the cell-specific ER+ *PIK3CA* breast cancer network models in Zañudo et al. 2020. These models are based on an updated version of the breast cancer network model in [Zañudo et al. 2017](https://doi.org/10.1186/s41236-017-0007-6)

For more detais on the model, see the accompanying manuscript:

##	Reproducing the model results

The simplest way to reproduce the model result is through the Jupyter notebook in the [Jupyter notebook](https://github.com/jgtz/BreastCancerModelv2/tree/master/Jupyter%20notebook) folder. You can view the Jupyter notebook [here](https://github.com/jgtz/BreastCancerModelv2/blob/master/Jupyter%20notebook/SimulationsZanudoEtAl2020.ipynb) and we also provide an [HTML version](https://github.com/jgtz/BreastCancerModelv2/blob/master/Jupyter%20notebook/SimulationsZanudoEtAl2020.html) of the results of running the notebook.

##  Model simulations

The breast cancer network models in Zañudo et al. 2020 are discrete network models. To simulate these discrete network models, we first map these models into a Boolean model, which was then simulated using the [BooleanDynamicModeling Java library](https://github.com/jgtz/BooleanDynamicModeling). To simulate multi-level nodes, we use a Boolean variable to denote each level greater than 1. For example, for a 3-level node with states 0, 1, and 2, we have 2 variables (Node and Node_2), and for a 4-level node we have 3 variables (Node, Node_2, and Node_3).

The model starts from a cancer steady state initial condition that can be primed (BIM_T=1) or unprimed (BIM_T=0). The model uses stochastic asynchronous updating, where a single node is updated at each update step and this node is chosen at random. The updating probabilities are chosen by categorizing nodes into either a fast or slow node, according to whether activation of the node denotes a (fast) signaling event or a (slow) transcriptional or translational event. We take fast probability taken to be 5 times faster than the slow probability (note that the real timescale difference between these type of events is much larger). We perform 10,000 simulations in each modeled scenario. The number of time units is 25 for all simulations, where a time unit is equal to the average number of time steps needed to update a slow node.

##	Java library instructions

The different java classes perform different simulations:

•	NetworkSimulations - simulates the model under specified single, double, triple and no perturbations. 
•	NetworkSinglePerturbationSimulations - simulates the model in the presence of Alpelisib under all possible single node perturbations.
•	NetworkDoublePerturbationSimulations - simulates the model in the presence of Alpelisib under all possible double node perturbations.

The perturbations in each simulation are started at either time step 0 (for NetworkSinglePerturbationSimulations or NetworkDoublePerturbationSimulations) or time step 2 (NetworkSimulations).

##	Running the Java library

To run the program, go to the command line, navigate to the folder where the "BreastCancerModel.jar" file and the "lib" folder are located. Once there type the command:

java -jar BreastCancerModel.jar BreastCancerModel.txt

where "BreastCancerModel.txt" is the name of the TXT file with the functions of the ER+ breast cancer networ model. The "BreastCancerModel.txt" file has the following format:

"
#BOOLEAN RULES
Node1 *= Node2 or Node3
Node2 *= Node1 and Node2
Node3 *= ((not Node3 or Node4) and not Node1)
Node4 *= 1
Node5 *= 0
...
NodeN *= not Node1 or (Node1 and Node2)
"

In the above, the text before the "*=" symbol is the node name, while the text after the "*=" symbol is the Boolean function of the node.

NODE NAMES

For the node n*ames use only alphanumeric characters (A-Z,a-z), numbers (0-9) and "_". The reserved words for the program, which shouldn't be used for node names, are: "True", "False", "true", "false", "0", "1", "and", "or", and "not".

BOOLEAN FUNCTIONS

For the Boolean functions use only the node names, the logical operators "and", "or", "not", and the parentheses symbols ")" and "(". In case the Boolean function is constant, use "0" or "1", depending on the constant state of the function. The logical function does not need to be written in a disjunctive normal form; the program will take the logical form in the TXT file and transform it into its disjunctive normal form using the Quine–McCluskey algorithm.

##	OUTPUT

The program will produce the following:

•	Tab separated TXT files "timecourse_X.txt" with the average trajectory of each node, where X specifies the perturbations simulated with the model (Alpelisib=1;Alpelisib=1_Everolimus=1;Alpelisib=1_MCL1=0;Alpelisib=1_Palbociclib=1_Fulvestrant=1;Alpelisib=1_PIM=1).
•	A tab separated TXT file "BreastCancerSinglePerturbations.txt" with the results of the simulating the model under each possible single node perturbation. The file contains the average Apoptosis (Apoptosis, Apoptosis_2, Apoptosis_3, Apoptosis_norm) and Proliferation (Proliferation, Proliferation_2, Proliferation_3, Proliferation_4, Proliferation_norm) values at the end of the simulation of each perturbation.
•	A tab separated TXT file "BreastCancerDoublePerturbations.txt" with the results of the simulating the model under each possible double node perturbation. The file contains the average Apoptosis (Apoptosis, Apoptosis_2, Apoptosis_3, Apoptosis_norm) and Proliferation (Proliferation, Proliferation_2, Proliferation_3, Proliferation_4, Proliferation_norm) values at the end of the simulation of each perturbation.

##	SOFTWARE USED AND LICENSES

**BooleanDynamicModeling**

The simulations of Boolean dynamics use the [BooleanDynamicModeling Java library](https://github.com/jgtz/BooleanDynamicModeling).

**JGraphT**

Several functions from the [JGraphT java class library](https://github.com/jgrapht/jgrapht) by Barak Naveh and Contributors are used. JGraphT is available under GNU LESSER GENERAL PUBLIC LICENSE Version 2.1.

**Quine-McCluskey_algorithm**

An implementation of the Quine-McCluskey_algorithm in the “Term.java” and “Formula.java” classes were retrieved in 2013 from [here](http://en.literateprograms.org/Quine-McCluskey_algorithm_(Java)?action=history&offset=20110925122251). The “Term.java” and “Formula.java” classes are available under the MIT License.

##	COPYRIGHT

The MIT License (MIT)

Copyright (c) 2020 Jorge G. T. Zañudo.

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

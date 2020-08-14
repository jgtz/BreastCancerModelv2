# Simulation of cell-specific ER+ *PIK3CA* mutant breast cancer dynamic network models

This program simulates the cell-specific ER+ *PIK3CA* breast cancer network models in Zañudo et al. 2020. These models are based on an updated version of the breast cancer network model in [Zañudo et al. 2017](https://doi.org/10.1186/s41236-017-0007-6)

For more detais on the model, see the accompanying manuscript:

##	Reproducing the model results

The simplest way to reproduce the model result is through the Jupyter notebook in the [Jupyter notebook](https://github.com/jgtz/BreastCancerModelv2/tree/master/Jupyter%20notebook) folder. You can view the Jupyter notebook [here](https://github.com/jgtz/BreastCancerModelv2/blob/master/Jupyter%20notebook/SimulationsZanudoEtAl2020.ipynb) and we also provide an [HTML version](https://github.com/jgtz/BreastCancerModelv2/blob/master/Jupyter%20notebook/SimulationsZanudoEtAl2020.html) of the results of running the notebook.

##  Model simulations

The breast cancer network models in Zañudo et al. 2020 are discrete network models. To simulate these discrete network models, we first map these models into a Boolean model, which was then simulated using the [BooleanDynamicModeling Java library](https://github.com/jgtz/BooleanDynamicModeling). To simulate multi-level nodes, we use a Boolean variable to denote each level greater than 1. For example, for a 3-level node with states 0, 1, and 2, we have 2 variables (Node and Node_2), and for a 4-level node we have 3 variables (Node, Node_2, and Node_3).

The model starts from a cancer steady state initial condition that can be primed (BIM_T=1) or unprimed (BIM_T=0). The model uses stochastic asynchronous updating, where a single node is updated at each update step and this node is chosen at random. The updating probabilities are chosen by categorizing nodes into either a fast or slow node, according to whether activation of the node denotes a (fast) signaling event or a (slow) transcriptional or translational event. We take fast probability taken to be 5 times faster than the slow probability (note that the real timescale difference between these type of events is much larger). We perform 10,000 simulations in each modeled scenario. The number of time units is 25 for all simulations, where a time unit is equal to the average number of time steps needed to update a slow node.

##	Java library structure

The Java library consists of four classes:

1) *Networkrun.java*. This is the main class of the Java library. From this class one can
   - Call *NetworkSimulations.java* to generate the model Boolean tables. One needs to generate these tables before running simulations on the model.
   - Call *NetworkSimulations.java* to run simulations on the model without perturbations or with up to 4 perturbations.
   - Run all the model simulations in [Zañudo et al. 2017](https://doi.org/10.1186/s41236-017-0007-6). This call all three classes.

2) *NetworkSimulations.java*. This class contains the bulk of the funcitonality of the Java library. This class contains functions to generate the model Boolean tables, run simulations of the model with or without perturbations, write the timecourse of the simulations, and several others related functions.

3) *NetworkDoublePerturbationSimulations.java*. Generates the simulations for all the double node perturbation in the [Zañudo et al. 2017](https://doi.org/10.1186/s41236-017-0007-6) model. Could be used for the same purpose in the Zañudo et al. 2020 model, but this hasn't been tested.

4) *NetworkSinglePerturbationSimulations.java*. Generates the simulations for all the single node perturbation in the [Zañudo et al. 2017](https://doi.org/10.1186/s41236-017-0007-6) model. Could be used for the same purpose in the Zañudo et al. 2020 model, but this hasn't been tested.


##	Running the Java library

To reproduce the results of the model, we recommend using the Jupyter notebook (see the **Reproducing the model results** section in this README). To run the Java library, we need the executable JAR file *BreastCancerModel.jar* and the library folder *lib*, both of which are located in the [dist folder](https://github.com/jgtz/BreastCancerModelv2/tree/master/dist). With these, we can run the Java library through the command line with:

```
java -jar BreastCancerModel.jar args
```

where *arg* is a set of space-separated arguments specified [here](https://github.com/jgtz/BreastCancerModelv2/blob/master/src/bcnetwork/Networkrun.java#L28).

##	Model files

The Boolean functions that specify each of the models are in the [Models folder](https://github.com/jgtz/BreastCancerModelv2/tree/master/Models).

The [Models folder](https://github.com/jgtz/BreastCancerModelv2/tree/master/Models) contains the MCF7-specific (*BreastCancerModel_ZanudoEtAl2020_MCF7*), T47D-specific (*BreastCancerModel_ZanudoEtAl2020_T47D*), and the more general model in which these two are based on (*BreastCancerModel_ZanudoEtAl2020*). We also include the [Zañudo et al. 2017](https://doi.org/10.1186/s41236-017-0007-6) model. For the *BreastCancerModel_ZanudoEtAl2020* and *BreastCancerModel_ZanudoEtAl2017* models we also include the "raw" rules, in which we do do not add Boolean terms to enforce the multi-level property of the Boolean variables corresponding a multi-level node.

In addition, we provide the MCF7-specific, T47D-specific, and the more general model in Systems Biology Markup Language (SBML) format. For this we use [bioLQM](https://github.com/colomoto/bioLQM) (see [Naldi 2018](https://doi.org/10.3389/fphys.2018.01605)). 

##	Format of the model files

The file specifying the Booelan functions follows the Booleannet format (see [here](https://github.com/ialbert/booleannet) or [here](http://colomoto.org/biolqm/doc/formats.html)). In this format the text before the "*=" symbol is the node name, while the text after the "*=" symbol is the Boolean function of the node:

```
#BOOLEAN RULES
Node1 *= Node2 or Node3
Node2 *= Node1 and Node2
Node3 *= ((not Node3 or Node4) and not Node1)
Node4 *= 1
Node5 *= 0
...
NodeN *= not Node1 or (Node1 and Node2)
```

**Node names**

For the node names use only alphanumeric characters (A-Z,a-z), numbers (0-9) and "_". The reserved words for the program, which shouldn't be used for node names, are: "True", "False", "true", "false", "0", "1", "and", "or", and "not".

**Boolean functions**

For the Boolean functions use only the node names, the logical operators "and", "or", "not", and the parentheses symbols ")" and "(". In case the Boolean function is constant, use "0" or "1", depending on the constant state of the function. The logical function does not need to be written in a disjunctive normal form; the program will take the logical form in the TXT file and transform it into its disjunctive normal form using the Quine–McCluskey algorithm.

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

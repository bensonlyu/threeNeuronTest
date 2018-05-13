# threeNeuronTest

This repository contains Benson's codes to implement the three-neuron two-layer network based on Michele Lombardi's [demonstration](https://www.researchgate.net/profile/Michele_Lombardi/publication/270891264_A_New_Propagator_for_Two-Layer_Neural_Networks_in_Empirical_Model_Learning/links/54b7fdc20cf28faced616f55.pdf). To get familiar with the neuron constraints principle behind the codes, please refer to `demonstration.PNG`.

*03/19/2018*

`threeNNCPLEX.java` is updated to simulate the three-neuron network by using the IBM CPLEX java package. It solves two network respectively with the linear and sigmoid function in the first layer. The linear network is solved but the sigmoid function needs improvement by using the `OptimalGlobal` offered by the latest CPLEX package to solve this non-convex model. An example can be previewed in the `GlobalQPex1.java`. 

Note that the tansig function cannot be applied in the CPLEX as it makes the model non-linear and requires indefinite iteration, even if the exponent may be approximated by the Taylor Series. Instead, `y = x / (0.5 + |x|)` is used as the sigmoid function, with the single x variable replaced by two x varibles to handle the `abs()` condition. All viaribles are defined as `int` with a resolution of 1/100 rather than `double` to speed up processing. Also, the weights and bias are pre-defined with the assumption that they can be trained by offline Machine Learning. 

Before running `threeNNCPLEX.java`, CPLEX java packages need to be imported and IDE settings should be configured properly. Please refer to this [video](https://www.youtube.com/watch?v=51CcmaISSX0&t=231s) to crank up.

*04/07/2018*

A set of `threeNeuronTest` files (`.run`, `.mod`, `.dat`) has been uploaded with non-linear actiavtion functions solved by using the [MINOS solver](https://web.stanford.edu/group/SOL/guides/minos55.pdf) included in the [AMPL](https://ampl.com/products/solvers/all-solvers-for-ampl/) IDE, whose library integrates a group of versatile solvers targeting various problem types.

To run the program, please [download](https://ampl.com/products/ampl/ampl-for-students/#Demo) the free demo version AMPL IDE and set it up by following its instructions. After changing the IDE workspace to the directory where three `threeNeuronTest` files are saved, type 'include threeNeuronTest.run' in the console and you should be able to see the similar results shown in the `sampleOutput.txt` file.

Future updates may include the [Java api](https://ampl.com/api/latest/java/index.html) used to run the program outside the (antique) IDE and allow data processing in a larger scale.

*05/12/2018*

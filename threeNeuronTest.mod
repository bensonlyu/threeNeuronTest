# This .mod file contains the formulation for a three neuron network
# implemented in the AMPL IDE.
# 
# Beichen Lyu, unbenson.lyu@gmail.com
# 05/12/2018

#parameter initialization
param xInput; #number of input x values
param neuronInput; #number of neurons in the input (first) layer

#sets of indices
set I := {1..xInput}; 
set J := {1..neuronInput};

#weights for the first and second layer sum functions, prepared by off-line training
param weightsFirst {I,J};
param weightsSecond {J};

#lower bound of input variables for each neuron
var x{I} >= (-1);
var y{I} >= (-1);

#linear activation function y = x
maximize z: sum {j in J} weightsSecond[j] * y[j]; 

#upper bound of variables for each neuron
s.t. upperBoundX {i in I}: x[i] <= 1;
s.t. upperBoundY {j in J}: y[j] <= 1;

#tansig activation function integrated with the sum function in the first layer
s.t. neuronConstraintFirst {j in J}:
(2/(1+exp((-2)*(sum {i in I} weightsFirst[i, j] * x[i]))))-1 = y[j]; 

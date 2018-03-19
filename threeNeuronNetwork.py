# Created by Beichen Lyu, unbenson.lyu@gmail.com, on 03/19/2018
#
# This program is used to simulate the two-layer three-neuron network demonstrated in the slides page 10 & 11 from http://cp2013.a4cp.org/slides/175.pdf
#
# There are two details about the demonstration unresolved. One is the way of updating weights and another is how to get the real maximum network output of 1.515.


import numpy
import random

# define the neuron sum operation
def neuron(x1, x2, w1, w2, b):
    z = x1*w1 + x2*w2 + b
    return z

# define the functions of sigmoid and linear for the first and second layer neurons respectively
def sigmoid(x):
    return 2/(1 + numpy.exp(-2*x)) - 1

def linear(y):
    return y

# initialize network running parameters
layerOneNeuronNumber = 2
trainingTimes = 1000

# initialize the output and input of the first layer
yLayerOne = []
x1 = random.uniform(-1, 1)
x2 = random.uniform(-1, 1)

# initialize the weights within -1 and 1
w1 = random.uniform(-1, 1) 
w2 = random.uniform(-1, 1)

print"The output of the two neurons of the first layer are:"

# run the operation on the layer-one neurons
for i in range(layerOneNeuronNumber):
    
    z0 = 0
    
    for j in range(trainingTimes):

        b = 0
        w1 += (sigmoid(2)-sigmoid(neuron(x1, x2, w1, w2, b)))*x1 # update the weights based on the difference between desired and output values
        w2 += (sigmoid(2)-sigmoid(neuron(x1, x2, w1, w2, b)))*x2

        z0 = sigmoid(neuron(x1, x2, w1, w2, b))
        
    print z0
    yLayerOne.append(z0)

# initialize the output of the second layer
z1 = 0

# again initialize the weights within -1 and 1
w1 = random.uniform(-1, 1)
w2 = random.uniform(-1, 1)

# run the operation on the layer-two neuron (third neuron)
for k in range(trainingTimes):

    x1 = yLayerOne[0]
    x2 = yLayerOne[1]
    b = 0
    w1 += (linear(x1+x2)-linear(neuron(x1, x2, w1, w2, b)))*x1 # update the weights based on the difference between desired and output values
    w2 += (linear(x1+x2)-linear(neuron(x1, x2, w1, w2, b)))*x2

    z1 = linear(neuron(x1, x2, w1, w2, b))

print "The final output of the third neuron on the second layer is:"
print z1

print "The real maximum network output is:"
print "1.515" # the real maximum value given by the reference. but I am confused about how it comes out

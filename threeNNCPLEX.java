/*
Created by Beichen Lyu, unbenson.lyu@gmail.com, on 04/07/2018

This program simulates a two-layer three-neuron network using
IBM CPLEX Java package.
 */

import ilog.concert.*;
import ilog.cplex.IloCplex;

import java.sql.SQLOutput;
import java.util.Random;
import java.util.Scanner;

public class threeNNCPLEX {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        System.out.println("Please select the activation function for the first layer (sigmoid or linear):");

        while(s.hasNextLine()) {
            String choice = s.nextLine();
            if (choice.toUpperCase().equals("SIGMOID")) {
                sigmoidNN(); //failed, because the solution integrated with sigmoid function will become non-convex, which
                break;
            } else if (choice.toUpperCase().equals("LINEAR")) {
                linearNN(); //successfully run, with output 200
                break;
            } else if (choice.isEmpty() || choice.equals("exit")) {
                System.out.println("Thanks for using threeNNCPLEX!");
                break;
            }else {
                System.out.println("Please enter sigmoid or linear.");
                continue;
            }
        }
    }

    //sigmoid activation function (y = x/(1+|x|)) in the first layer
   public static void sigmoidNN(){
       //preparation
       int layerOne = 2; // number of neurons in layer one
       int n = 2; // number of input x
       int outputReal = 152;
       int[][] weightsOne = {{1, 1}, {-1, 1}}; //weights and bias should be pre-trained by offline ML
       int[] weightsTwo = {1, 1};
       int[] biasOne = new int[layerOne];
       int biasTwo = 0;

//        Random r = new Random();
       for (int i = 0; i < layerOne; i++) {
           biasOne[i] = 0;
//            weightsTwo[i] = r.nextGaussian();
//            for (int j = 0; j < n; j++) {
//                weightsOne[i][j] = r.nextGaussian();
//            }
       }

       //model
       try {
           IloCplex cplex = new IloCplex();

           cplex.setParam(IloCplex.Param.OptimalityTarget,
                   IloCplex.OptimalityTarget.OptimalGlobal);

           //variables
           IloIntVar[] x = cplex.intVarArray(n, -100, 100); //input for first layer
           IloIntVar[] y1a = cplex.intVarArray(layerOne, 0, Integer.MAX_VALUE); //activity for first layer
           IloIntVar[] y1b = cplex.intVarArray(layerOne, 0, Integer.MAX_VALUE); //handle abs condition in the sigmoid
           IloIntVar[] z1 = cplex.intVarArray(layerOne, -Integer.MAX_VALUE, Integer.MAX_VALUE); //input for second layer (output of first layer)
           IloIntVar y3 = cplex.intVar(-Integer.MAX_VALUE, Integer.MAX_VALUE); //activity of second layer

           //constraints
           //sum function for first layer
           for (int i = 0; i < layerOne; i++) {
               IloLinearIntExpr expY1 = cplex.linearIntExpr();
               for (int j = 0; j < n; j++) {
                   expY1.addTerm(weightsOne[i][j], x[i]);
               }
               expY1.addTerm(-1, y1a[i]);
               expY1.addTerm(1, y1b[i]);
               cplex.addEq(expY1, -biasOne[i]);
           }

           //sigmoid activation function for first layer
           for (int i = 0; i < layerOne; i++) {
               IloLQIntExpr expZ1 = cplex.lqIntExpr();
               expZ1.addTerm(1, z1[i]);
               expZ1.addTerm(-2, y1a[i]);
               expZ1.addTerm(2, y1b[i]);
               expZ1.addTerm(2, y1a[i], z1[i]);
               expZ1.addTerm(2, y1b[i], z1[i]);
               cplex.addEq(expZ1, 0);
           }

           //sum function for second layer
           IloLinearIntExpr expY3 = cplex.linearIntExpr();
           for (int i = 0; i < layerOne; i++) {
               expY3.addTerm(weightsTwo[i], z1[i]);
           }
           expY3.addTerm(-1, y3);
           cplex.addEq(expY3, -biasTwo);

           //objective
           //linear activation function of second layer
           cplex.addMaximize(y3);

           //solve
           cplex.solve();
           System.out.println("Output z3 (y3):");
           System.out.println(cplex.getValue(y3));// getValue() can only be used after solve()
           for (int i = 0; i < n; i++) {
               System.out.printf("Input x%d:\n", i);
               System.out.println(cplex.getValue(x[i]));
           }
           System.out.println("Real output:");
           System.out.println(outputReal);

           //end
           cplex.end();
       } catch (IloException i) {
           i.printStackTrace();
       }
   }

   //linear activation function (y = x) in the first layer
    public static void linearNN(){
        //preparation
        int layerOne = 2; // number of neurons in layer one
        int n = 2; // number of input x
        int outputReal = 152;
        int[][] weightsOne = {{1, 1}, {-1, 1}}; //weights and bias should be pre-trained by offline ML
        int[] weightsTwo = {1, 1};
        int[] biasOne = new int[layerOne];
        int biasTwo = 0;

//        Random r = new Random();
        for (int i = 0; i < layerOne; i++) {
            biasOne[i] = 0;
//            weightsTwo[i] = r.nextGaussian();
//            for (int j = 0; j < n; j++) {
//                weightsOne[i][j] = r.nextGaussian();
//            }
        }

        //model
        try {
            IloCplex cplex = new IloCplex();

            //variables
            IloIntVar[] x = cplex.intVarArray(n, -100, 100); //input for first layer
            IloIntVar[] y1 = cplex.intVarArray(layerOne, -Integer.MAX_VALUE, Integer.MAX_VALUE); //activity for first layer
            IloIntVar[] z1 = cplex.intVarArray(layerOne, -Integer.MAX_VALUE, Integer.MAX_VALUE); //input for second layer (output of first layer)
            IloIntVar y3 = cplex.intVar(-Integer.MAX_VALUE, Integer.MAX_VALUE); //activity of second layer

            //constraints
            //sum function for first layer
            for (int i = 0; i < layerOne; i++) {
                IloLinearIntExpr expY1 = cplex.linearIntExpr();
                for (int j = 0; j < n; j++) {
                    expY1.addTerm(weightsOne[i][j], x[i]);
                }
                expY1.addTerm(-1, y1[i]);
                cplex.addEq(expY1, -biasOne[i]);
            }

            //sigmoid activation function for first layer
            for (int i = 0; i < layerOne; i++) {
                cplex.addEq(y1[i], z1[i]);
            }

            //sum function for second layer
            IloLinearIntExpr expY3 = cplex.linearIntExpr();
            for (int i = 0; i < layerOne; i++) {
                expY3.addTerm(weightsTwo[i], z1[i]);
            }
            expY3.addTerm(-1, y3);
            cplex.addEq(expY3, -biasTwo);

            //objective
            //linear activation function of second layer
            cplex.addMaximize(y3);

            //solve
            cplex.solve();
            System.out.println("Output z3 (y3):");
            System.out.println(cplex.getValue(y3));// getValue() can only be used after solve()
            for (int i = 0; i < n; i++) {
                System.out.printf("Input x%d:\n", i);
                System.out.println(cplex.getValue(x[i]));
            }
            System.out.println("Real output:");
            System.out.println(outputReal);

            //end
            cplex.end();
        } catch (IloException i) {
            i.printStackTrace();
        }
    }
}
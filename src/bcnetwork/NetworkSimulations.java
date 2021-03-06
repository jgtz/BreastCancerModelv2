/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bcnetwork;

import booleandynamicmodeling.Network;
import booleandynamicmodeling.OtherMethods;
import booleandynamicmodeling.ReadWriteFiles;
import booleandynamicmodeling.UpdateMethods;
import fileOperations.FileToWrite;
import fileOperations.FileToRead;
import java.util.Random;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javax.script.ScriptException;

/**
 *
 * @author Jorge G. T. Zañudo
 * @date April 2020
 */
public class NetworkSimulations {

    //This program simulates the ER+ breast cancer network model, starting from the
    //cancer steady state initial condition, and under the presence of the perturbations specified.
    
    //The regulatory functions of the model are given in the TXT file "BreastCancerModel_ZanudoEtAl2017.txt".
    //The model dynamics are governed by the stochastic general asynchronous updating
    //scheme. Every time step corresponds to several (Ntimes) updates, with one time
    //step corresponding to the average number of updates needed to update a slow node. 
    //The average trajectory of each node is output in the "timecourse_X.txt" TXT file,
    //where X specifies the perturbations simulated with the model.

     /**
     * @param args args[0] is the name of the TXT file where the model rules are. For the breast cancer model it is "BreastCancerModel_ZanudoEtAl2017.txt".
     * args[1] is the number of initial conditions
     * args[2] is the number of normalized timesteps (number of timesteps equal to the average time needed to update a slow node)
     * args[3] is an option of whether to write to file the timecourse of the simulation
     * @param nw Network object with the model already imported
     * @param ThirdTimescale Boolean variable, whether to use 3 timescales instead of the 2 timescales used throughout the model.
     */

    public static void BaselineTimecourse(String[] args, Network nw, boolean ThirdTimescale) {
        
        String fileName=args[0]; //This file contains the Boolean rules of the model              
        String timecourseFileName="timecourse"+fileName.split("\\.")[0]+".txt";
        String timecourseFileName_2="timecourseSEM"+fileName.split("\\.")[0]+".txt";
        int seed=1000;
        int numberOfPerturbations=0;
        int timePerturbationStart=2;
        boolean writeTimecourse=false;
        if("true".equals(args[3])){writeTimecourse=true;}
        if(ThirdTimescale){
            timecourseFileName="timecourse"+fileName.split("\\.")[0]+"_ThreeTimeScales.txt";
            timecourseFileName_2="timecourseSEM"+fileName.split("\\.")[0]+"_ThreeTimeScales.txt";
         }
        
        System.out.println("Perturbation\tApofrac1\tApofrac2\tApofrac3\tApofrac\tProlfrac1\tProlfrac2\tProlfrac3\tProlfrac4\tProlfrac");                
        List timecourseResult;
        if(!ThirdTimescale){timecourseResult =runTimecourse(nw,args,numberOfPerturbations,timePerturbationStart, writeTimecourse,timecourseFileName,timecourseFileName_2,seed);}
        else{timecourseResult =runTimecourse_ThirdTimescale(nw,args,numberOfPerturbations,timePerturbationStart, writeTimecourse,timecourseFileName,timecourseFileName_2,seed);}                
        double Apofraction1=(double) timecourseResult.get(0);
        double Apofraction2=(double) timecourseResult.get(1);
        double Apofraction3=(double) timecourseResult.get(2);
        double Prolfraction1=(double) timecourseResult.get(3);
        double Prolfraction2=(double) timecourseResult.get(4);
        double Prolfraction3=(double) timecourseResult.get(5);
        double Prolfraction4=(double) timecourseResult.get(6);
        double Apofraction=(double) timecourseResult.get(7);
        double Prolfraction=(double) timecourseResult.get(8);
        System.out.println("No Perturbation"+"\t"+Apofraction1+"\t"+Apofraction2+"\t"+Apofraction3+"\t"+Apofraction+"\t"+Prolfraction1+"\t"+Prolfraction2+"\t"+Prolfraction3+"\t"+Prolfraction4+"\t"+Prolfraction);                
 
         
    }
    
    /**
     * @param args args[0] is the name of the TXT file where the model is. For the breast cancer model it is "BreastCancerModel_ZanudoEtAl2017.txt".
     * args[1] is the number of initial conditions
     * args[2] is the number of normalized timesteps (number of timesteps equal to the average time needed to update a slow node)
     * args[3] is an option of whether to write to file the timecourse of the simulation
     * args[4] is the node name of the first perturbation, args[5] is the state of the first perturbation
     * @param nw Network object with the model already imported
     * @param ThirdTimescale Boolean variable, whether to use 3 timescales instead of the 2 timescales used throughout the model.
     */
    
    public static void SinglePerturbationTimecourse(String[] args, Network nw, boolean ThirdTimescale) {
        
        String fileName=args[0]; //This file contains the Boolean rules of the model       
        String PertNodeString=args[4]; //Node that wil be perturbed
        String PertNodeState=args[5]; //Node state of the perturbed node. Must be 0 or 1
        int seed=1000;
        String timecourseFileName="timecourse"+fileName.split("\\.")[0]+"_"+PertNodeString+"="+PertNodeState+".txt";
        String timecourseFileName_2="timecourseSEM"+fileName.split("\\.")[0]+"_"+PertNodeString+"="+PertNodeState+".txt";
        int numberOfPerturbations=1;
        int timePerturbationStart=2; 
        boolean writeTimecourse=false;
        if("true".equals(args[3])){writeTimecourse=true;}
        if(ThirdTimescale){
            timecourseFileName="timecourse"+fileName.split("\\.")[0]+"_"+PertNodeString+"="+PertNodeState+"_ThreeTimeScales.txt";
            timecourseFileName_2="timecourseSEM"+fileName.split("\\.")[0]+"_"+PertNodeString+"="+PertNodeState+"_ThreeTimeScales.txt";
         }
        
        System.out.println("Perturbation\tApofrac1\tApofrac2\tApofrac3\tApofrac\tProlfrac1\tProlfrac2\tProlfrac3\tProlfrac4\tProlfrac");                
        List timecourseResult;
        if(!ThirdTimescale){timecourseResult =runTimecourse(nw,args,numberOfPerturbations,timePerturbationStart, writeTimecourse,timecourseFileName,timecourseFileName_2,seed);}
        else{timecourseResult =runTimecourse_ThirdTimescale(nw,args,numberOfPerturbations,timePerturbationStart, writeTimecourse,timecourseFileName,timecourseFileName_2,seed);} 
        double Apofraction1=(double) timecourseResult.get(0);
        double Apofraction2=(double) timecourseResult.get(1);
        double Apofraction3=(double) timecourseResult.get(2);
        double Prolfraction1=(double) timecourseResult.get(3);
        double Prolfraction2=(double) timecourseResult.get(4);
        double Prolfraction3=(double) timecourseResult.get(5);
        double Prolfraction4=(double) timecourseResult.get(6);
        double Apofraction=(double) timecourseResult.get(7);
        double Prolfraction=(double) timecourseResult.get(8);
        System.out.println(PertNodeString+"="+PertNodeState+"\t"+Apofraction1+"\t"+Apofraction2+"\t"+Apofraction3+"\t"+Apofraction+"\t"+Prolfraction1+"\t"+Prolfraction2+"\t"+Prolfraction3+"\t"+Prolfraction4+"\t"+Prolfraction);                       
        
    }

     /**
     * @param args args[0] is the name of the TXT file where the model is. For the breast cancer model it is "BreastCancerModel_ZanudoEtAl2017.txt".
     * args[1] is the number of initial conditions
     * args[2] is the number of normalized timesteps (number of timesteps equal to the average time needed to update a slow node)
     * args[3] is an option of whether to write to file the timecourse of the simulation
     * args[4] is the node name of the first perturbation, args[5] is the state of the first perturbation
     * args[6] is the node name of the second perturbation, args[7] is the state of the second perturbation
     * @param nw Network object with the model already imported
     * @param ThirdTimescale Boolean variable, whether to use 3 timescales instead of the 2 timescales used throughout the model.
     */
    
    public static void DoublePerturbationTimecourse(String[] args, Network nw, boolean ThirdTimescale) {
        
        String fileName=args[0]; //This file contains the Boolean rules of the model               
        String PertNodeString1=args[4]; //Node 1 that wil be perturbed
        String PertNodeState1=args[5]; //Node state of the perturbed node 1. Must be 0 or 1
        String PertNodeString2=args[6]; //Node 2 that wil be perturbed
        String PertNodeState2=args[7]; //Node state of the perturbed node 2. Must be 0 or 1
        int seed=1000;
        String timecourseFileName="timecourse"+fileName.split("\\.")[0]+"_"+PertNodeString1+"="+PertNodeState1+"_"+PertNodeString2+"="+PertNodeState2+".txt";
        String timecourseFileName_2="timecourseSEM"+fileName.split("\\.")[0]+"_"+PertNodeString1+"="+PertNodeState1+"_"+PertNodeString2+"="+PertNodeState2+".txt";
        int numberOfPerturbations=2;
        int timePerturbationStart=2; 
        boolean writeTimecourse=false;
        if("true".equals(args[3])){writeTimecourse=true;}
        if(ThirdTimescale){
            timecourseFileName="timecourse"+fileName.split("\\.")[0]+"_"+PertNodeString1+"="+PertNodeState1+"_"+PertNodeString2+"="+PertNodeState2+"_ThreeTimeScales.txt";
            timecourseFileName_2="timecourseSEM"+fileName.split("\\.")[0]+"_"+PertNodeString1+"="+PertNodeState1+"_"+PertNodeString2+"="+PertNodeState2+"_ThreeTimeScales.txt";
         }
        
        System.out.println("Perturbation1\tPerturbation2\tApofrac1\tApofrac2\tApofrac3\tApofrac\tProlfrac1\tProlfrac2\tProlfrac3\tProlfrac4\tProlfrac");                
        List timecourseResult;
        if(!ThirdTimescale){timecourseResult =runTimecourse(nw,args,numberOfPerturbations,timePerturbationStart, writeTimecourse,timecourseFileName,timecourseFileName_2,seed);}
        else{timecourseResult =runTimecourse_ThirdTimescale(nw,args,numberOfPerturbations,timePerturbationStart, writeTimecourse,timecourseFileName,timecourseFileName_2,seed);} 
        double Apofraction1=(double) timecourseResult.get(0);
        double Apofraction2=(double) timecourseResult.get(1);
        double Apofraction3=(double) timecourseResult.get(2);
        double Prolfraction1=(double) timecourseResult.get(3);
        double Prolfraction2=(double) timecourseResult.get(4);
        double Prolfraction3=(double) timecourseResult.get(5);
        double Prolfraction4=(double) timecourseResult.get(6);
        double Apofraction=(double) timecourseResult.get(7);
        double Prolfraction=(double) timecourseResult.get(8);
        System.out.println(PertNodeString1+"="+PertNodeState1+"\t"+PertNodeString2+"="+PertNodeState2+"\t"+Apofraction1+"\t"+Apofraction2+"\t"+Apofraction3+"\t"+Apofraction+"\t"+Prolfraction1+"\t"+Prolfraction2+"\t"+Prolfraction3+"\t"+Prolfraction4+"\t"+Prolfraction);
                
    }

     /**
     * @param args args[0] is the name of the TXT file where the model is. For the breast cancer model it is "BreastCancerModel_ZanudoEtAl2017.txt".
     * args[1] is the number of initial conditions
     * args[2] is the number of normalized timesteps (number of timesteps equal to the average time needed to update a slow node)
     * args[3] is an option of whether to write to file the timecourse of the simulation
     * args[4] is the node name of the first perturbation, args[5] is the state of the first perturbation
     * args[6] is the node name of the second perturbation, args[7] is the state of the second perturbation
     * args[8] is the node name of the third perturbation, args[9] is the state of the third perturbation
     * @param nw Network object with the model already imported
     * @param ThirdTimescale Boolean variable, whether to use 3 timescales instead of the 2 timescales used throughout the model.
     */    
    
    public static void TriplePerturbationTimecourse(String[] args, Network nw, boolean ThirdTimescale) {
        
        String fileName=args[0]; //This file contains the Boolean rules of the model               
        String PertNodeString1=args[4]; //Node 1 that wil be perturbed
        String PertNodeState1=args[5]; //Node state of the perturbed node 1. Must be 0 or 1
        String PertNodeString2=args[6]; //Node 2 that wil be perturbed
        String PertNodeState2=args[7]; //Node state of the perturbed node 2. Must be 0 or 1        
        String PertNodeString3=args[8]; //Node 3 that wil be perturbed
        String PertNodeState3=args[9]; //Node state of the perturbed node 3. Must be 0 or 1
        int seed=1000;
        int numberOfPerturbations=3;
        int timePerturbationStart=2; 
        String timecourseFileName="timecourse"+fileName.split("\\.")[0]+"_"+PertNodeString1+"="+PertNodeState1+"_"+PertNodeString2+"="+PertNodeState2+"_"+PertNodeString3+"="+PertNodeState3+".txt"; 
        String timecourseFileName_2="timecourseSEM"+fileName.split("\\.")[0]+"_"+PertNodeString1+"="+PertNodeState1+"_"+PertNodeString2+"="+PertNodeState2+"_"+PertNodeString3+"="+PertNodeState3+".txt"; 
        boolean writeTimecourse=false;
        if("true".equals(args[3])){writeTimecourse=true;}
        if(ThirdTimescale){
            timecourseFileName="timecourse"+fileName.split("\\.")[0]+"_"+PertNodeString1+"="+PertNodeState1+"_"+PertNodeString2+"="+PertNodeState2+"_"+PertNodeString3+"="+PertNodeState3+"_ThreeTimeScales.txt"; 
            timecourseFileName_2="timecourseSEM"+fileName.split("\\.")[0]+"_"+PertNodeString1+"="+PertNodeState1+"_"+PertNodeString2+"="+PertNodeState2+"_"+PertNodeString3+"="+PertNodeState3+"_ThreeTimeScales.txt";
        }

        System.out.println("Perturbation1\tPerturbation2\tPerturbation3\tApofrac1\tApofrac2\tApofrac3\tApofrac\tProlfrac1\tProlfrac2\tProlfrac3\tProlfrac4\tProlfrac");
        List timecourseResult;
        if(!ThirdTimescale){timecourseResult =runTimecourse(nw,args,numberOfPerturbations,timePerturbationStart, writeTimecourse,timecourseFileName,timecourseFileName_2,seed);}
        else{timecourseResult =runTimecourse_ThirdTimescale(nw,args,numberOfPerturbations,timePerturbationStart, writeTimecourse,timecourseFileName,timecourseFileName_2,seed);} 
        double Apofraction1=(double) timecourseResult.get(0);
        double Apofraction2=(double) timecourseResult.get(1);
        double Apofraction3=(double) timecourseResult.get(2);
        double Prolfraction1=(double) timecourseResult.get(3);
        double Prolfraction2=(double) timecourseResult.get(4);
        double Prolfraction3=(double) timecourseResult.get(5);
        double Prolfraction4=(double) timecourseResult.get(6);
        double Apofraction=(double) timecourseResult.get(7);
        double Prolfraction=(double) timecourseResult.get(8);
        System.out.println(PertNodeString1+"="+PertNodeState1+"\t"+PertNodeString2+"="+PertNodeState2+"\t"+PertNodeString3+"="+PertNodeState3+"\t"+Apofraction1+"\t"+Apofraction2+"\t"+Apofraction3+"\t"+Apofraction+"\t"+Prolfraction1+"\t"+Prolfraction2+"\t"+Prolfraction3+"\t"+Prolfraction4+"\t"+Prolfraction);                
                
    }

    /**
     * @param args args[0] is the name of the TXT file where the model is. For the breast cancer model it is "BreastCancerModel_ZanudoEtAl2017.txt".
     * args[1] is the number of initial conditions
     * args[2] is the number of normalized timesteps (number of timesteps equal to the average time needed to update a slow node)
     * args[3] is an option of whether to write to file the timecourse of the simulation
     * args[4] is the node name of the first perturbation, args[5] is the state of the first perturbation
     * args[6] is the node name of the second perturbation, args[7] is the state of the second perturbation
     * args[8] is the node name of the third perturbation, args[9] is the state of the third perturbation
     * args[10] is the node name of the third perturbation, args[11] is the state of the third perturbation
     * @param nw Network object with the model already imported
     * @param ThirdTimescale Boolean variable, whether to use 3 timescales instead of the 2 timescales used throughout the model.
     */ 
    
    public static void QuadruplePerturbationTimecourse(String[] args, Network nw, boolean ThirdTimescale) {
        
        String fileName=args[0]; //This file contains the Boolean rules of the model               
        String PertNodeString1=args[4]; //Node 1 that wil be perturbed
        String PertNodeState1=args[5]; //Node state of the perturbed node 1. Must be 0 or 1
        String PertNodeString2=args[6]; //Node 2 that wil be perturbed
        String PertNodeState2=args[7]; //Node state of the perturbed node 2. Must be 0 or 1        
        String PertNodeString3=args[8]; //Node 3 that wil be perturbed
        String PertNodeState3=args[9]; //Node state of the perturbed node 3. Must be 0 or 1
        String PertNodeString4=args[10]; //Node 3 that wil be perturbed
        String PertNodeState4=args[11]; //Node state of the perturbed node 3. Must be 0 or 1
        int seed=1000;
        int numberOfPerturbations=4;
        int timePerturbationStart=2; 
        String timecourseFileName="timecourse"+fileName.split("\\.")[0]+"_"+PertNodeString1+"="+PertNodeState1+"_"+PertNodeString2+"="+PertNodeState2+"_"+PertNodeString3+"="+PertNodeState3+"_"+PertNodeString4+"="+PertNodeState4+".txt"; 
        String timecourseFileName_2="timecourseSEM"+fileName.split("\\.")[0]+"_"+PertNodeString1+"="+PertNodeState1+"_"+PertNodeString2+"="+PertNodeState2+"_"+PertNodeString3+"="+PertNodeState3+"_"+PertNodeString4+"="+PertNodeState4+".txt"; 
        boolean writeTimecourse=false;
        if("true".equals(args[3])){writeTimecourse=true;}
        if(ThirdTimescale){
            timecourseFileName="timecourse"+fileName.split("\\.")[0]+"_"+PertNodeString1+"="+PertNodeState1+"_"+PertNodeString2+"="+PertNodeState2+"_"+PertNodeString3+"="+PertNodeState3+"_"+PertNodeString4+"="+PertNodeState4+"_ThreeTimeScales.txt"; 
            timecourseFileName_2="timecourseSEM"+fileName.split("\\.")[0]+"_"+PertNodeString1+"="+PertNodeState1+"_"+PertNodeString2+"="+PertNodeState2+"_"+PertNodeString3+"="+PertNodeState3+"_"+PertNodeString4+"="+PertNodeState4+"_ThreeTimeScales.txt";        
        }

        System.out.println("Perturbation1\tPerturbation2\tPerturbation3\tPerturbation4\tApofrac1\tApofrac2\tApofrac3\tApofrac\tProlfrac1\tProlfrac2\tProlfrac3\tProlfrac4\tProlfrac");
        List timecourseResult;
        if(!ThirdTimescale){timecourseResult =runTimecourse(nw,args,numberOfPerturbations,timePerturbationStart, writeTimecourse,timecourseFileName,timecourseFileName_2,seed);}
        else{timecourseResult =runTimecourse_ThirdTimescale(nw,args,numberOfPerturbations,timePerturbationStart, writeTimecourse,timecourseFileName,timecourseFileName_2,seed);} 
        double Apofraction1=(double) timecourseResult.get(0);
        double Apofraction2=(double) timecourseResult.get(1);
        double Apofraction3=(double) timecourseResult.get(2);
        double Prolfraction1=(double) timecourseResult.get(3);
        double Prolfraction2=(double) timecourseResult.get(4);
        double Prolfraction3=(double) timecourseResult.get(5);
        double Prolfraction4=(double) timecourseResult.get(6);
        double Apofraction=(double) timecourseResult.get(7);
        double Prolfraction=(double) timecourseResult.get(8);
        System.out.println(PertNodeString1+"="+PertNodeState1+"\t"+PertNodeString2+"="+PertNodeState2+"\t"+PertNodeString3+"="+PertNodeState3+"\t"+PertNodeString4+"="+PertNodeState4+"\t"+Apofraction1+"\t"+Apofraction2+"\t"+Apofraction3+"\t"+Apofraction+"\t"+Prolfraction1+"\t"+Prolfraction2+"\t"+Prolfraction3+"\t"+Prolfraction4+"\t"+Prolfraction);                
                
    }
    
    public static Network GenerateModel(String fileName) throws ScriptException {
        Network nw = null;        
        File fName = new File(fileName);
        if (fName.isFile()){
            String shortname=fName.getName();
            System.out.println("\nFilename: "+fileName);
            System.out.println("Creating Boolean table directory: "+shortname.split("\\.")[0]);
            ReadWriteFiles.createTablesFromBooleanRules(shortname.split("\\.")[0], fileName);
            System.out.println("Boolean table directory created.");
            System.out.println("Functions and names files created.");
            System.out.println("Creating network model...");
            nw=OtherMethods.RecreateNetwork(shortname.split("\\.")[0]);        
            System.out.println("Model created.");
            nw.findNodeOutputs();
        }
        else{
            System.out.println("File could not be found.");
        }
        return nw;
        
    }

    public static Network GetModel(String fileName) throws ScriptException {
        Network nw = null;        
        File fName = new File(fileName);
        if (fName.isFile()){
            String shortname=fName.getName();
            System.out.println("\nFilename: "+fileName);
            System.out.println("Creating network model based on Boolean tables, functions, and names files...");
            nw=OtherMethods.RecreateNetwork(shortname.split("\\.")[0]);        
            System.out.println("Model created.");
            nw.findNodeOutputs();
        }
        else{
            System.out.println("File could not be found.");
        }
        return nw;
        
    }
   
    public static List<Double> runTimecourse(Network nw,String [] args,int numberOfPerturbations, int timePerturbationStart, boolean writeTimecourse, String timecourseFileName, String timecourseFileName_2, int seed){
        
        String PertNodeString1 = null,PertNodeString2 = null,PertNodeString3 = null,PertNodeString4 = null,PertNodeState1 = null,PertNodeState2 = null,PertNodeState3 = null,PertNodeState4 = null; 
        Random rand = new Random(seed);
        String fileName=args[0].split("\\.")[0];
        if(numberOfPerturbations>0){
                PertNodeString1=args[4]; //Node 1 that wil be perturbed
                PertNodeState1=args[5]; //Node state of the perturbed node 1. Must be 0 or 1
                if(numberOfPerturbations>1){
                    PertNodeString2=args[6]; //Node 2 that wil be perturbed
                    PertNodeState2=args[7]; //Node state of the perturbed node 2. Must be 0 or 1       
                    if(numberOfPerturbations>2){
                        PertNodeString3=args[8]; //Node 3 that wil be perturbed
                        PertNodeState3=args[9]; //Node state of the perturbed node 3. Must be 0 or 1
                        if(numberOfPerturbations>3){
                            PertNodeString4=args[10]; //Node 4 that wil be perturbed
                            PertNodeState4=args[11]; //Node state of the perturbed node 4. Must be 0 or 1        
                        }
                    }
                }
            }
        
        
        int Ntime; //One normalized time step is Ntime updates 
        int IC=Integer.parseInt(args[1]); //Number of initial conditions
        int T=Integer.parseInt(args[2]); //This is the number of normalized time steps 
        
        int Tprint=5*T; //These are used to know how often we will print out to file the timecourse
        int Nprint; 
        
        double p; //This is the probability of updating any of the slow nodes at any update step
        //the update probability of a fast node is 5 times that of a slow node
        int N=nw.getN();
        ArrayList<Integer> fast; //This array stores the indices of the fast nodes
        ArrayList<Integer> slow; //This array stores the indices of the slow nodes
                
        int index;
        boolean steadyState;
        int updateNode,KOnode1 = 0,KOnode2 = 0,KOnode3 = 0,KOnode4 = 0;
        double Apofraction1,Apofraction2,Apofraction3,Prolfraction1,Prolfraction2,Prolfraction3,Prolfraction4,Prolfraction,Apofraction;
        double apo,prol;
        int Apofraction1Ind,Apofraction2Ind,Apofraction3Ind,Prolfraction1Ind,Prolfraction2Ind,Prolfraction3Ind,Prolfraction4Ind;
        int[] nodeStates,pastState;
        double[][] trajectory,trajectory2;
        double[][] trajectory_squared,trajectory2_squared;
        ArrayList<Integer> KOnodes=new ArrayList<Integer>();
        HashMap<String,String> initialCondition;
 
        HashMap namesDictionary=new HashMap<Integer,String>();
        HashMap indexDictionary=new HashMap<String,Integer>();
        for(int i=0;i<N;i++){namesDictionary.put(i, nw.getNames()[i]);indexDictionary.put(nw.getNames()[i],i);}
        Apofraction1Ind=(int) indexDictionary.get("Apoptosis");Apofraction2Ind=(int) indexDictionary.get("Apoptosis_2");Apofraction3Ind=(int) indexDictionary.get("Apoptosis_3");
        Prolfraction1Ind=(int) indexDictionary.get("Proliferation");Prolfraction2Ind=(int) indexDictionary.get("Proliferation_2");Prolfraction3Ind=(int) indexDictionary.get("Proliferation_3");Prolfraction4Ind=(int) indexDictionary.get("Proliferation_4");
        
        List<ArrayList<Integer>> fast_slow_list = SlowFastNodes(indexDictionary,fileName);
        fast = fast_slow_list.get(0);
        slow = fast_slow_list.get(1);
        
        p=1.0*slow.size()/(1.0*slow.size()+5.0*fast.size());//p=pslow, 5*p=pfast, p*slow.size()+pfast*fast.size()= 1 =p(slow.size()+5*fast.size())              
        Ntime=slow.size()+5*fast.size();
        Nprint=(int)(Math.ceil(1.0*Ntime/5.0));
        //p=0;
        //Ntime=1;
        //fast=new ArrayList<Integer>();
        //for(int i=0;i<indexDictionary.size();i++){fast.add(new Integer(i));}
        
        List<ArrayList<Double>> Apoptosis_Proliferation_Weights = ApoptosisProliferationWeights(fileName);
        ArrayList<Double> ApoptosisWeights=Apoptosis_Proliferation_Weights.get(0);
        ArrayList<Double> ProliferationWeights=Apoptosis_Proliferation_Weights.get(1);
        //for(int i=0;i<ApoptosisWeights.size();i++){System.out.println("Apo"+(i+1)+"\t"+ApoptosisWeights.get(i));}
        //for(int i=0;i<ProliferationWeights.size();i++){System.out.println("Prol"+(i+1)+"\t"+ProliferationWeights.get(i));}
                                               
        Apofraction1=0;Apofraction2=0;Apofraction3=0;Prolfraction1=0;Prolfraction2=0;Prolfraction3=0;Prolfraction4=0;Apofraction=0;Prolfraction=0;
        trajectory=new double[Tprint][N];
        trajectory2=new double[Tprint][2];
        trajectory_squared=new double[Tprint][N];
        trajectory2_squared=new double[Tprint][2];
        initialCondition=getInitialCondition(fileName);
        for(int t=0;t<Tprint;t++){for(int i=0;i<N;i++){trajectory[t][i]=0;trajectory_squared[t][i]=0;}}
        for(int t=0;t<Tprint;t++){for(int i=0;i<2;i++){trajectory2[t][i]=0;trajectory2_squared[t][i]=0;}}
        for(int r=0;r<IC;r++){
            nodeStates=setInitialCondition(indexDictionary,initialCondition,rand); 
            if(numberOfPerturbations>0){
                KOnode1=(int) indexDictionary.get(PertNodeString1);
                KOnodes.add(new Integer(KOnode1));
                if(numberOfPerturbations>1){
                    KOnode2=(int) indexDictionary.get(PertNodeString2);
                    KOnodes.add(new Integer(KOnode2));
                    if(numberOfPerturbations>2){
                        KOnode3=(int) indexDictionary.get(PertNodeString3);
                        KOnodes.add(new Integer(KOnode3));
                        if(numberOfPerturbations>3){
                            KOnode3=(int) indexDictionary.get(PertNodeString4);
                            KOnodes.add(new Integer(KOnode4));
                        }
                    }
                }
            }
            if(nodeStates[Apofraction3Ind]==1){apo=ApoptosisWeights.get(2);}else if(nodeStates[Apofraction2Ind]==1){apo=ApoptosisWeights.get(1);}else if(nodeStates[Apofraction1Ind]==1){apo=ApoptosisWeights.get(0);}else{apo=0;}
            if(nodeStates[Prolfraction4Ind]==1){prol=ProliferationWeights.get(3);}else if(nodeStates[Prolfraction3Ind]==1){prol=ProliferationWeights.get(2);}else if(nodeStates[Prolfraction2Ind]==1){prol=ProliferationWeights.get(1);}else if(nodeStates[Prolfraction1Ind]==1){prol=ProliferationWeights.get(0);}else{prol=0;}
            steadyState=false;
            for(int t=0;t<Tprint;t++){
                if(!steadyState){
                    if(t==timePerturbationStart){
                        if(numberOfPerturbations>0){
                            nodeStates[KOnode1]=Integer.parseInt(PertNodeState1);
                            if(numberOfPerturbations>1){
                                nodeStates[KOnode2]=Integer.parseInt(PertNodeState2);
                                if(numberOfPerturbations>2){
                                    nodeStates[KOnode3]=Integer.parseInt(PertNodeState3);
                                    if(numberOfPerturbations>3){
                                        nodeStates[KOnode4]=Integer.parseInt(PertNodeState4);
                                    }
                                }
                            }
                        }    
                    }

                    for(int i=0;i<N;i++){trajectory[t][i]+=1.0*nodeStates[i];trajectory_squared[t][i]+=1.0*nodeStates[i]*nodeStates[i];}
                    trajectory2[t][0]+=1.0*prol;
                    trajectory2[t][1]+=1.0*apo;
                    trajectory2_squared[t][0]+=1.0*prol*prol;
                    trajectory2_squared[t][1]+=1.0*apo*apo;
                    for(int n=0;n<Nprint;n++){
                        if(rand.nextDouble()>=p){index=(int)(rand.nextDouble()*fast.size());updateNode=fast.get(index);}
                        else{index=(int)(rand.nextDouble()*slow.size());updateNode=slow.get(index);}
                        pastState=Arrays.copyOf(nodeStates, nodeStates.length);
                        nodeStates[updateNode]=UpdateMethods.updateSingleNodeBoolean(nw, pastState, updateNode);
                        if(numberOfPerturbations>0){
                            if(updateNode==KOnode1&&t>=timePerturbationStart){nodeStates[KOnode1]=Integer.parseInt(PertNodeState1);}
                            if(numberOfPerturbations>1){
                                if(updateNode==KOnode2&&t>=timePerturbationStart){nodeStates[KOnode2]=Integer.parseInt(PertNodeState2);}
                                if(numberOfPerturbations>2){
                                    if(updateNode==KOnode3&&t>=timePerturbationStart){nodeStates[KOnode3]=Integer.parseInt(PertNodeState3);}
                                    if(numberOfPerturbations>3){
                                        if(updateNode==KOnode4&&t>=timePerturbationStart){nodeStates[KOnode4]=Integer.parseInt(PertNodeState4);}
                                    }
                                }
                            }
                        }                      
                    }

                    if(nodeStates[Apofraction3Ind]==1){apo=ApoptosisWeights.get(2);}else if(nodeStates[Apofraction2Ind]==1){apo=ApoptosisWeights.get(1);}else if(nodeStates[Apofraction1Ind]==1){apo=ApoptosisWeights.get(0);}else{apo=0;}
                    if(nodeStates[Prolfraction4Ind]==1){prol=ProliferationWeights.get(3);}else if(nodeStates[Prolfraction3Ind]==1){prol=ProliferationWeights.get(2);}else if(nodeStates[Prolfraction2Ind]==1){prol=ProliferationWeights.get(1);}else if(nodeStates[Prolfraction1Ind]==1){prol=ProliferationWeights.get(0);}else{prol=0;}
                    if(t>=timePerturbationStart){
                         steadyState=checkSteadyState(nw,nodeStates,KOnodes);
                    }
                }
                else{
                    for(int i=0;i<N;i++){trajectory[t][i]+=1.0*nodeStates[i];trajectory_squared[t][i]+=1.0*nodeStates[i]*nodeStates[i];}
                    trajectory2[t][0]+=1.0*prol;
                    trajectory2[t][1]+=1.0*apo;
                    trajectory2_squared[t][0]+=1.0*prol*prol;
                    trajectory2_squared[t][1]+=1.0*apo*apo;  
                }
            }

            if(nodeStates[(int) indexDictionary.get("Apoptosis")]==1){Apofraction1=Apofraction1+1;}
            if(nodeStates[(int) indexDictionary.get("Apoptosis_2")]==1){Apofraction2=Apofraction2+1;}
            if(nodeStates[(int) indexDictionary.get("Apoptosis_3")]==1){Apofraction3=Apofraction3+1;}
            if(nodeStates[(int) indexDictionary.get("Proliferation")]==1){Prolfraction1=Prolfraction1+1;}
            if(nodeStates[(int) indexDictionary.get("Proliferation_2")]==1){Prolfraction2=Prolfraction2+1;}
            if(nodeStates[(int) indexDictionary.get("Proliferation_3")]==1){Prolfraction3=Prolfraction3+1;}
            if(nodeStates[(int) indexDictionary.get("Proliferation_4")]==1){Prolfraction4=Prolfraction4+1;}
            
            if(nodeStates[Apofraction3Ind]==1){Apofraction=Apofraction+ApoptosisWeights.get(2);}else if(nodeStates[Apofraction2Ind]==1){Apofraction=Apofraction+ApoptosisWeights.get(1);}else if(nodeStates[Apofraction1Ind]==1){Apofraction=Apofraction+ApoptosisWeights.get(0);}else{}
            if(nodeStates[Prolfraction4Ind]==1){Prolfraction=Prolfraction+ProliferationWeights.get(3);}else if(nodeStates[Prolfraction3Ind]==1){Prolfraction=Prolfraction+ProliferationWeights.get(2);}else if(nodeStates[Prolfraction2Ind]==1){Prolfraction=Prolfraction+ProliferationWeights.get(1);}else if(nodeStates[Prolfraction1Ind]==1){Prolfraction=Prolfraction+ProliferationWeights.get(0);}else{}
            
        }
            Apofraction1=Apofraction1/IC;
            Apofraction2=Apofraction2/IC;
            Apofraction3=Apofraction3/IC;
            Prolfraction1=Prolfraction1/IC;
            Prolfraction2=Prolfraction2/IC;
            Prolfraction3=Prolfraction3/IC;
            Prolfraction4=Prolfraction4/IC;
            Apofraction=Apofraction/IC;
            Prolfraction=Prolfraction/IC;
            
            if(writeTimecourse){
                writeTrajectory(nw,N,IC,Tprint,trajectory,trajectory2,trajectory_squared,trajectory2_squared,(double)(Nprint)/(double)(Ntime),timecourseFileName,timecourseFileName_2);
            }
            
            return Arrays.asList(Apofraction1,Apofraction2,Apofraction3,Prolfraction1,Prolfraction2,Prolfraction3,Prolfraction4,Apofraction,Prolfraction);
            
            }
       
    public static List<ArrayList<Integer>> SlowFastNodes(HashMap<String,Integer> indexDictionary, String filename){
        
        ArrayList<Integer> fast=new ArrayList<>(); //This array stores the indices of the fast nodes
        ArrayList<Integer> slow=new ArrayList<>(); //This array stores the indices of the slow nodes
        FileToRead fr=new FileToRead(filename+"_SlowFastNodes.txt");
        HashMap<String,String> slowFastNodes=new HashMap();
        int ranState;
        String line;
        String node;
        String state;        
        
        //Slow nodes for the simulations. The nodes not added to the "slow" list are the fast nodes and will stay in the "fast" list
        for(int i=0;i<indexDictionary.size();i++){fast.add(new Integer(i));}
        while(fr.hasNext()){
            line=fr.nextLine();
            //System.out.println(line);
            fast.remove(new Integer((int) indexDictionary.get(line)));
            slow.add(new Integer((int) indexDictionary.get(line)));           
        }  
        fr.close();
        
        return Arrays.asList(fast, slow);
    
    }
    
    public static List<Double> runTimecourse_ThirdTimescale(Network nw,String [] args,int numberOfPerturbations, int timePerturbationStart, boolean writeTimecourse, String timecourseFileName, String timecourseFileName_2, int seed){
        
        String PertNodeString1 = null,PertNodeString2 = null,PertNodeString3 = null,PertNodeString4 = null,PertNodeState1 = null,PertNodeState2 = null,PertNodeState3 = null,PertNodeState4 = null; 
        Random rand = new Random(seed);
        
        String fileName=args[0].split("\\.")[0];
        if(numberOfPerturbations>0){
                PertNodeString1=args[4]; //Node 1 that wil be perturbed
                PertNodeState1=args[5]; //Node state of the perturbed node 1. Must be 0 or 1
                if(numberOfPerturbations>1){
                    PertNodeString2=args[6]; //Node 2 that wil be perturbed
                    PertNodeState2=args[7]; //Node state of the perturbed node 2. Must be 0 or 1       
                    if(numberOfPerturbations>2){
                        PertNodeString3=args[8]; //Node 3 that wil be perturbed
                        PertNodeState3=args[9]; //Node state of the perturbed node 3. Must be 0 or 1
                        if(numberOfPerturbations>3){
                            PertNodeString4=args[10]; //Node 4 that wil be perturbed
                            PertNodeState4=args[11]; //Node state of the perturbed node 4. Must be 0 or 1        
                        }
                    }
                }
            }
        
        
        double Ntime; //One normalized time step is Ntime updates 
        int IC=Integer.parseInt(args[1]); //Number of initial conditions
        int T=Integer.parseInt(args[2]); //This is the number of normalized time steps 
        
        int Tprint=5*T; //These are used to know how often we will print out to file the timecourse
        int Nprint; 
        
        double p; //This is the probability of updating any of the slow nodes at any update step
        //the update probability of a fast node is slowTimeScale times that of a slow node
        double p_slowest; //This is the probability of updating any of the slowest nodes at any update step
        //the update probability of a slowest node is 1/slowestTimeScale times that of a slow node
        int N=nw.getN();
        ArrayList<Integer> fast; //This array stores the indices of the fast nodes
        ArrayList<Integer> slow; //This array stores the indices of the slow nodes
        ArrayList<Integer> slowest; //This array stores the indices of the slowest nodes
                
        int index;
        boolean steadyState;
        int updateNode,KOnode1 = 0,KOnode2 = 0,KOnode3 = 0,KOnode4 = 0;
        double Apofraction1,Apofraction2,Apofraction3,Prolfraction1,Prolfraction2,Prolfraction3,Prolfraction4,Prolfraction,Apofraction;
        double apo,prol;
        int Apofraction1Ind,Apofraction2Ind,Apofraction3Ind,Prolfraction1Ind,Prolfraction2Ind,Prolfraction3Ind,Prolfraction4Ind;
        int[] nodeStates,pastState;
        double rand_temp, slowTimeScale, slowestTimeScale;
        double[][] trajectory,trajectory2;
        double[][] trajectory_squared,trajectory2_squared;
        ArrayList<Integer> KOnodes=new ArrayList<Integer>();
        HashMap<String,String> initialCondition;
 
        HashMap namesDictionary=new HashMap<Integer,String>();
        HashMap indexDictionary=new HashMap<String,Integer>();
        for(int i=0;i<N;i++){namesDictionary.put(i, nw.getNames()[i]);indexDictionary.put(nw.getNames()[i],i);}
        Apofraction1Ind=(int) indexDictionary.get("Apoptosis");Apofraction2Ind=(int) indexDictionary.get("Apoptosis_2");Apofraction3Ind=(int) indexDictionary.get("Apoptosis_3");
        Prolfraction1Ind=(int) indexDictionary.get("Proliferation");Prolfraction2Ind=(int) indexDictionary.get("Proliferation_2");Prolfraction3Ind=(int) indexDictionary.get("Proliferation_3");Prolfraction4Ind=(int) indexDictionary.get("Proliferation_4");
        
        List<ArrayList<Integer>> fast_slow_list = SlowFastNodes_ThirdTimescale(indexDictionary,fileName);
        fast = fast_slow_list.get(0);
        slow = fast_slow_list.get(1);
        slowest = fast_slow_list.get(2);
        
        double[] timescale_array = TimeScales(fileName);
        slowTimeScale = timescale_array[0];
        slowestTimeScale = timescale_array[1];
        
        p=1.0*slow.size()/(1.0*slow.size()+slowTimeScale*fast.size()+(1.0/slowestTimeScale)*slowest.size());
        p_slowest=(1.0/slowestTimeScale)*slowest.size()/(1.0*slow.size()+slowTimeScale*fast.size()+(1.0/slowestTimeScale)*slowest.size());
        //p=pslow, 5*p=pfast, p/5=pslowest; p*slow.size()+pfast*fast.size()+pslowest*slowest.size()= 1 =p(slow.size()+5*fast.size()+slowest.size()/5)              
        Ntime=slow.size()+slowTimeScale*fast.size()+(1.0/slowestTimeScale)*slowest.size();
        Nprint=(int)(Math.ceil(1.0*Ntime/slowTimeScale));
        
        List<ArrayList<Double>> Apoptosis_Proliferation_Weights = ApoptosisProliferationWeights(fileName);
        ArrayList<Double> ApoptosisWeights=Apoptosis_Proliferation_Weights.get(0);
        ArrayList<Double> ProliferationWeights=Apoptosis_Proliferation_Weights.get(1);
                              
        Apofraction1=0;Apofraction2=0;Apofraction3=0;Prolfraction1=0;Prolfraction2=0;Prolfraction3=0;Prolfraction4=0;Apofraction=0;Prolfraction=0;
        trajectory=new double[Tprint][N];
        trajectory2=new double[Tprint][2];
        trajectory_squared=new double[Tprint][N];
        trajectory2_squared=new double[Tprint][2];
        initialCondition=getInitialCondition(fileName);
        for(int t=0;t<Tprint;t++){for(int i=0;i<N;i++){trajectory[t][i]=0;trajectory_squared[t][i]=0;}}
        for(int t=0;t<Tprint;t++){for(int i=0;i<2;i++){trajectory2[t][i]=0;trajectory2_squared[t][i]=0;}}
        for(int r=0;r<IC;r++){
            nodeStates=setInitialCondition(indexDictionary,initialCondition,rand); 
            if(numberOfPerturbations>0){
                KOnode1=(int) indexDictionary.get(PertNodeString1);
                KOnodes.add(new Integer(KOnode1));
                if(numberOfPerturbations>1){
                    KOnode2=(int) indexDictionary.get(PertNodeString2);
                    KOnodes.add(new Integer(KOnode2));
                    if(numberOfPerturbations>2){
                        KOnode3=(int) indexDictionary.get(PertNodeString3);
                        KOnodes.add(new Integer(KOnode3));
                        if(numberOfPerturbations>3){
                            KOnode3=(int) indexDictionary.get(PertNodeString4);
                            KOnodes.add(new Integer(KOnode4));
                        }
                    }
                }
            }
            if(nodeStates[Apofraction3Ind]==1){apo=ApoptosisWeights.get(2);}else if(nodeStates[Apofraction2Ind]==1){apo=ApoptosisWeights.get(1);}else if(nodeStates[Apofraction1Ind]==1){apo=ApoptosisWeights.get(0);}else{apo=0;}
            if(nodeStates[Prolfraction4Ind]==1){prol=ProliferationWeights.get(3);}else if(nodeStates[Prolfraction3Ind]==1){prol=ProliferationWeights.get(2);}else if(nodeStates[Prolfraction2Ind]==1){prol=ProliferationWeights.get(1);}else if(nodeStates[Prolfraction1Ind]==1){prol=ProliferationWeights.get(0);}else{prol=0;}
            steadyState=false;
            for(int t=0;t<Tprint;t++){
                if(!steadyState){
                    if(t==timePerturbationStart){
                        if(numberOfPerturbations>0){
                            nodeStates[KOnode1]=Integer.parseInt(PertNodeState1);
                            if(numberOfPerturbations>1){
                                nodeStates[KOnode2]=Integer.parseInt(PertNodeState2);
                                if(numberOfPerturbations>2){
                                    nodeStates[KOnode3]=Integer.parseInt(PertNodeState3);
                                    if(numberOfPerturbations>3){
                                        nodeStates[KOnode4]=Integer.parseInt(PertNodeState4);
                                    }
                                }
                            }
                        }    
                    }

                    for(int i=0;i<N;i++){trajectory[t][i]+=1.0*nodeStates[i];trajectory_squared[t][i]+=1.0*nodeStates[i]*nodeStates[i];}
                    trajectory2[t][0]+=1.0*prol;
                    trajectory2[t][1]+=1.0*apo;
                    trajectory2_squared[t][0]+=1.0*prol*prol;
                    trajectory2_squared[t][1]+=1.0*apo*apo;
                    for(int n=0;n<Nprint;n++){
                        rand_temp=rand.nextDouble();
                        if(rand_temp>=p){
                            if(rand_temp>=1.0-p_slowest){index=(int)(rand.nextDouble()*slowest.size());updateNode=slowest.get(index);}
                            else{index=(int)(rand.nextDouble()*fast.size());updateNode=fast.get(index);} 
                        }
                        else{index=(int)(rand.nextDouble()*slow.size());updateNode=slow.get(index);}
                        pastState=Arrays.copyOf(nodeStates, nodeStates.length);
                        nodeStates[updateNode]=UpdateMethods.updateSingleNodeBoolean(nw, pastState, updateNode);
                        if(numberOfPerturbations>0){
                            if(updateNode==KOnode1&&t>=timePerturbationStart){nodeStates[KOnode1]=Integer.parseInt(PertNodeState1);}
                            if(numberOfPerturbations>1){
                                if(updateNode==KOnode2&&t>=timePerturbationStart){nodeStates[KOnode2]=Integer.parseInt(PertNodeState2);}
                                if(numberOfPerturbations>2){
                                    if(updateNode==KOnode3&&t>=timePerturbationStart){nodeStates[KOnode3]=Integer.parseInt(PertNodeState3);}
                                    if(numberOfPerturbations>3){
                                        if(updateNode==KOnode4&&t>=timePerturbationStart){nodeStates[KOnode4]=Integer.parseInt(PertNodeState4);}
                                    }
                                }
                            }
                        }                      
                    }

                    if(nodeStates[Apofraction3Ind]==1){apo=ApoptosisWeights.get(2);}else if(nodeStates[Apofraction2Ind]==1){apo=ApoptosisWeights.get(1);}else if(nodeStates[Apofraction1Ind]==1){apo=ApoptosisWeights.get(0);}else{apo=0;}
                    if(nodeStates[Prolfraction4Ind]==1){prol=ProliferationWeights.get(3);}else if(nodeStates[Prolfraction3Ind]==1){prol=ProliferationWeights.get(2);}else if(nodeStates[Prolfraction2Ind]==1){prol=ProliferationWeights.get(1);}else if(nodeStates[Prolfraction1Ind]==1){prol=ProliferationWeights.get(0);}else{prol=0;}
                    if(t>=timePerturbationStart){
                         steadyState=checkSteadyState(nw,nodeStates,KOnodes);
                    }
                }
                else{
                    for(int i=0;i<N;i++){trajectory[t][i]+=1.0*nodeStates[i];trajectory_squared[t][i]+=1.0*nodeStates[i]*nodeStates[i];}
                    trajectory2[t][0]+=1.0*prol;
                    trajectory2[t][1]+=1.0*apo;
                    trajectory2_squared[t][0]+=1.0*prol*prol;
                    trajectory2_squared[t][1]+=1.0*apo*apo;  
                }
            }

            if(nodeStates[(int) indexDictionary.get("Apoptosis")]==1){Apofraction1=Apofraction1+1;}
            if(nodeStates[(int) indexDictionary.get("Apoptosis_2")]==1){Apofraction2=Apofraction2+1;}
            if(nodeStates[(int) indexDictionary.get("Apoptosis_3")]==1){Apofraction3=Apofraction3+1;}
            if(nodeStates[(int) indexDictionary.get("Proliferation")]==1){Prolfraction1=Prolfraction1+1;}
            if(nodeStates[(int) indexDictionary.get("Proliferation_2")]==1){Prolfraction2=Prolfraction2+1;}
            if(nodeStates[(int) indexDictionary.get("Proliferation_3")]==1){Prolfraction3=Prolfraction3+1;}
            if(nodeStates[(int) indexDictionary.get("Proliferation_4")]==1){Prolfraction4=Prolfraction4+1;}
            
            if(nodeStates[Apofraction3Ind]==1){Apofraction=Apofraction+ApoptosisWeights.get(2);}else if(nodeStates[Apofraction2Ind]==1){Apofraction=Apofraction+ApoptosisWeights.get(1);}else if(nodeStates[Apofraction1Ind]==1){Apofraction=Apofraction+ApoptosisWeights.get(0);}else{}
            if(nodeStates[Prolfraction4Ind]==1){Prolfraction=Prolfraction+ProliferationWeights.get(3);}else if(nodeStates[Prolfraction3Ind]==1){Prolfraction=Prolfraction+ProliferationWeights.get(2);}else if(nodeStates[Prolfraction2Ind]==1){Prolfraction=Prolfraction+ProliferationWeights.get(1);}else if(nodeStates[Prolfraction1Ind]==1){Prolfraction=Prolfraction+ProliferationWeights.get(0);}else{}
            
        }
            Apofraction1=Apofraction1/IC;
            Apofraction2=Apofraction2/IC;
            Apofraction3=Apofraction3/IC;
            Prolfraction1=Prolfraction1/IC;
            Prolfraction2=Prolfraction2/IC;
            Prolfraction3=Prolfraction3/IC;
            Prolfraction4=Prolfraction4/IC;
            Apofraction=Apofraction/IC;
            Prolfraction=Prolfraction/IC;
            
            if(writeTimecourse){
                writeTrajectory(nw,N,IC,Tprint,trajectory,trajectory2,trajectory_squared,trajectory2_squared,(double)(Nprint)/(double)(Ntime),timecourseFileName,timecourseFileName_2);
            }
            
            return Arrays.asList(Apofraction1,Apofraction2,Apofraction3,Prolfraction1,Prolfraction2,Prolfraction3,Prolfraction4,Apofraction,Prolfraction);
            
            }
       
    public static List<ArrayList<Integer>> SlowFastNodes_ThirdTimescale(HashMap<String,Integer> indexDictionary, String filename){
        
        ArrayList<Integer> fast=new ArrayList<>(); //This array stores the indices of the fast nodes
        ArrayList<Integer> slow=new ArrayList<>(); //This array stores the indices of the slow nodes
        ArrayList<Integer> slowest=new ArrayList<>(); //This array stores the indices of the slow nodes
        FileToRead fr=new FileToRead(filename+"_SlowFastNodes.txt");
        FileToRead fr2=new FileToRead(filename+"_SlowFastNodes_ThirdTimescale.txt");
        HashMap<String,String> slowFastNodes=new HashMap();
        int ranState;
        String line;
        String node;
        String state;        
        
        //Slow nodes for the simulations. The nodes not added to the "slow" and "slowest" list are the fast nodes and will stay in the "fast" list
        for(int i=0;i<indexDictionary.size();i++){fast.add(new Integer(i));}
        while(fr.hasNext()){
            line=fr.nextLine();
            //System.out.println(line);
            fast.remove(new Integer((int) indexDictionary.get(line)));
            slow.add(new Integer((int) indexDictionary.get(line)));           
        }
        
        fr.close();
        
        while(fr2.hasNext()){
            line=fr2.nextLine();
            //System.out.println(line);
            slow.remove(new Integer((int) indexDictionary.get(line)));
            slowest.add(new Integer((int) indexDictionary.get(line)));           
        }
        
        fr2.close();
        
        return Arrays.asList(fast, slow, slowest);
    
    }
  
    public static double[] TimeScales(String filename){
        
        double[] timescale_array=new double[2]; //This array stores the slowTimeScale and slowestTimeScale
        
        FileToRead fr=new FileToRead(filename+"_TimeScales_ThirdTimescale.txt");
        //System.out.println(filename+"_TimeScales_ThirdTimescale.txt");
        timescale_array[0]=Double.parseDouble(fr.nextLine()); //slowTimeScale = timescale_list[0];
        //System.out.println(timescale_array[0]);
        timescale_array[1]=Double.parseDouble(fr.nextLine()); //slowestTimeScale = timescale_list[1];
        //System.out.println(timescale_array[1]);
        
        return timescale_array;
    
    }
    
    
    public static List<ArrayList<Double>> ApoptosisProliferationWeights(String filename){
        
        ArrayList<Double> ApoptosisWeights=new ArrayList<>(); //This array stores the weights for Apoptosis1-Apoptosis3
        ArrayList<Double> ProliferationWeights=new ArrayList<>(); //This array stores the weights for Proliferation1-Proliferation4
        FileToRead fr=new FileToRead(filename+"_ApoptosisProliferationWeights.txt");
        String line;
        String[] line_split;
        int counter=0;
        
        //The order of the files is taken to be 
        while(fr.hasNext()){
            line=fr.nextLine();
            line_split=line.split("\t");
            if(counter==0){
                ApoptosisWeights.add(Double.parseDouble(line_split[0])); //Apoptosis1
                ApoptosisWeights.add(Double.parseDouble(line_split[1])); //Apoptosis2
                ApoptosisWeights.add(Double.parseDouble(line_split[2])); //Apoptosis3
            }
            if(counter==1){
                ProliferationWeights.add(Double.parseDouble(line_split[0])); //Proliferation1
                ProliferationWeights.add(Double.parseDouble(line_split[1])); //Proliferation2
                ProliferationWeights.add(Double.parseDouble(line_split[2])); //Proliferation3
                ProliferationWeights.add(Double.parseDouble(line_split[3])); //Proliferation4
            }
            counter++;
        }
                           
        return Arrays.asList(ApoptosisWeights, ProliferationWeights);
    
    }

    public static HashMap<String,String> getInitialCondition(String filename){
            HashMap<String,String> initialCondition=new HashMap();
            int ranState;
            String line;
            String node;
            String state;
            FileToRead fr=new FileToRead(filename+"_InitialConditions.txt");
            while(fr.hasNext()){
                line=fr.nextLine();
                node=line.split("\t")[0];
                state=line.split("\t")[1];        
                initialCondition.put(node, state);            
            }
            fr.close();
            return initialCondition;
    }

    
    public static int[] setInitialCondition(HashMap<String,Integer> indexDictionary, HashMap<String,String> initialCondition, Random rand){
            int[] nodeStates = new int[indexDictionary.size()];
            int ranState;
            int ranStateCorrelated = -1;
            boolean bool_ranState_correlated=false;

            for(int i=0;i<indexDictionary.size();i++){nodeStates[i]=0;}
            
            for(HashMap.Entry<String,String> entry : initialCondition.entrySet()){
                //System.out.println(entry.getKey());
                switch (entry.getValue()) {
                    case "ranStateCorrelated":
                        if(!bool_ranState_correlated){
                            ranStateCorrelated=(int)(1.5*rand.nextDouble());
                            bool_ranState_correlated=true;
                        }   nodeStates[(int) indexDictionary.get(entry.getKey())]=ranStateCorrelated;
                        break;
                    case "ranState":
                        ranState=rand.nextInt(2);
                        nodeStates[(int) indexDictionary.get(entry.getKey())]=ranState;
                        break;
                    default:
                        nodeStates[(int) indexDictionary.get(entry.getKey())]=Integer.parseInt(entry.getValue());
                        break;
                }
                
            }
                
            return nodeStates;
    }
 
    
     public static void writeTrajectory(Network nw, int N,int IC,int T,double[][] trajectory,double[][] trajectory2,double[][] trajectory_squared,double[][] trajectory2_squared,double timeUnit, String filename, String filename2){
    
        //This writes out the timecourse of the average activity in the TXT file
        FileToWrite fw=new FileToWrite(filename); //The average time course is stored in this
        FileToWrite fw2=new FileToWrite(filename2); //The SD of the average time course is stored in this
        double moment_2,moment_1_squared,variance_estimate;
        //tabseparated file. Every row is the average state of a node while every column is the time step
        String line;
        
        line="\t";
        for(int t=0;t<T;t++){
            line=line+timeUnit*t+"\t";
            
        }
        fw.writeLine(line);
        
        for(int i=0;i<N;i++){
            line=nw.getNames()[i]+"\t";
            for(int t=0;t<T;t++){
                line=line+trajectory[t][i]/IC+"\t";
            }
            fw.writeLine(line);
        }
        for(int i=0;i<2;i++){
            line="";
            if(i==0){line="Proliferation_norm\t";}
            if(i==1){line="Apoptosis_norm\t";}
            for(int t=0;t<T;t++){
                line=line+trajectory2[t][i]/IC+"\t";
            }
            fw.writeLine(line);
        }
        
        fw.close();
        
        line="\t";
        for(int t=0;t<T;t++){
            line=line+timeUnit*t+"\t";
            
        }
        fw2.writeLine(line);
        
        for(int i=0;i<N;i++){
            line=nw.getNames()[i]+"\t";
            for(int t=0;t<T;t++){
                moment_2=trajectory_squared[t][i]/IC;
                moment_1_squared=(trajectory[t][i]/IC)*(trajectory[t][i]/IC);
                variance_estimate=(IC/(IC-1))*(moment_2-moment_1_squared);
                line=line+(1.0/Math.sqrt(IC))*Math.sqrt(variance_estimate)+"\t";
            }
            fw2.writeLine(line);
        }
        for(int i=0;i<2;i++){
            line="";
            if(i==0){line="Proliferation_norm\t";}
            if(i==1){line="Apoptosis_norm\t";}
            for(int t=0;t<T;t++){
                moment_2=trajectory2_squared[t][i]/IC;
                moment_1_squared=(trajectory2[t][i]/IC)*(trajectory2[t][i]/IC);
                variance_estimate=(IC/(IC-1))*(moment_2-moment_1_squared);
                line=line+(1.0/Math.sqrt(IC))*Math.sqrt(variance_estimate)+"\t";
            }
            fw2.writeLine(line);
        }
        
        fw2.close();
        
    }
    
    public static boolean checkSteadyState(Network nw, int[] nodeStates,ArrayList<Integer> KOnodes){
        
        int[] pastState=Arrays.copyOf(nodeStates, nodeStates.length);
        int state=0;
        boolean steadyState=true;
        for(int i=0;i<nodeStates.length;i++){
                if(!KOnodes.contains(new Integer(i))){
                    state=UpdateMethods.updateSingleNodeBoolean(nw, pastState, i);
                    if(state!=pastState[i]){steadyState=false;break;}
                }
        }
        
        return steadyState;
    }
    
        public static ArrayList<String> GetNotSteadyState(Network nw, int[] nodeStates,ArrayList<Integer> KOnodes, HashMap<Integer,String> namesDictionary){
        
        int[] pastState=Arrays.copyOf(nodeStates, nodeStates.length);
        int state=0;
        ArrayList<String> notSteadyState=new ArrayList<String>();
        boolean steadyState=true;
        for(int i=0;i<nodeStates.length;i++){
                if(!KOnodes.contains(new Integer(i))){
                    state=UpdateMethods.updateSingleNodeBoolean(nw, pastState, i);
                    if(state!=pastState[i]){notSteadyState.add(namesDictionary.get(new Integer(i)));}
                }
        }
        
        return notSteadyState;
    }
    
}

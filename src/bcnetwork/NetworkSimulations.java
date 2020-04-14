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
     * @param args args[0] is the name of the TXT file where the model is. For the breast cancer model it is "BreastCancerModel_ZanudoEtAl2017.txt".
     * args[1] is the number of initial conditions
     * args[2] is the number of normalized timesteps (number of timesteps equal to the average time needed to update a slow node)
     * args[3] is an option of whether to write to file the timecourse of the simulation
     * @param nw Network model that will be simulated
     */

    public static void BaselineTimecourse(String[] args, Network nw) {
        
        String fileName=args[0]; //This file contains the Boolean rules of the model              
        String timecourseFileName="timecourse"+fileName.split("\\.")[0]+".txt";
        int numberOfPerturbations=0;
        int timePerturbationStart=2;
        boolean writeTimecourse=false;
        if("true".equals(args[3])){writeTimecourse=true;}
        
        System.out.println("Perturbation\tApofrac1\tApofrac2\tApofrac3\tApofrac\tProlfrac1\tProlfrac2\tProlfrac3\tProlfrac4\tProlfrac");                
        List timecourseResult=runTimecourse(nw,args,numberOfPerturbations,timePerturbationStart, writeTimecourse,timecourseFileName);
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
     * @param nw
     */
    
    public static void SinglePerturbationTimecourse(String[] args, Network nw) {
        
        String fileName=args[0]; //This file contains the Boolean rules of the model       
        String PertNodeString=args[4]; //Node that wil be perturbed
        String PertNodeState=args[5]; //Node state of the perturbed node. Must be 0 or 1
        String timecourseFileName="timecourse"+fileName.split("\\.")[0]+"_"+PertNodeString+"="+PertNodeState+".txt"; 
        int numberOfPerturbations=1;
        int timePerturbationStart=2; 
        boolean writeTimecourse=false;
        if("true".equals(args[3])){writeTimecourse=true;}     
        
        System.out.println("Perturbation\tApofrac1\tApofrac2\tApofrac3\tApofrac\tProlfrac1\tProlfrac2\tProlfrac3\tProlfrac4\tProlfrac");                
        List timecourseResult=runTimecourse(nw,args,numberOfPerturbations,timePerturbationStart, writeTimecourse,timecourseFileName);
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
     * @param nw Network model that will be simulated
     */
    
    public static void DoublePerturbationTimecourse(String[] args, Network nw) {
        
        String fileName=args[0]; //This file contains the Boolean rules of the model               
        String PertNodeString1=args[4]; //Node 1 that wil be perturbed
        String PertNodeState1=args[5]; //Node state of the perturbed node 1. Must be 0 or 1
        String PertNodeString2=args[6]; //Node 2 that wil be perturbed
        String PertNodeState2=args[7]; //Node state of the perturbed node 2. Must be 0 or 1        
        String timecourseFileName="timecourse"+fileName.split("\\.")[0]+"_"+PertNodeString1+"="+PertNodeState1+"_"+PertNodeString2+"="+PertNodeState2+".txt";
        int numberOfPerturbations=2;
        int timePerturbationStart=2; 
        boolean writeTimecourse=false;
        if("true".equals(args[3])){writeTimecourse=true;}
        
        System.out.println("Perturbation1\tPerturbation2\tApofrac1\tApofrac2\tApofrac3\tApofrac\tProlfrac1\tProlfrac2\tProlfrac3\tProlfrac4\tProlfrac");                
        List timecourseResult=runTimecourse(nw,args,numberOfPerturbations,timePerturbationStart, writeTimecourse,timecourseFileName);
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
     * args[8] is the node name of the second perturbation, args[9] is the state of the second perturbation
     * @param nw Network model that will be simulated
     */    
    
    public static void TriplePerturbationTimecourse(String[] args, Network nw) {
        
        String fileName=args[0]; //This file contains the Boolean rules of the model               
        String PertNodeString1=args[4]; //Node 1 that wil be perturbed
        String PertNodeState1=args[5]; //Node state of the perturbed node 1. Must be 0 or 1
        String PertNodeString2=args[6]; //Node 2 that wil be perturbed
        String PertNodeState2=args[7]; //Node state of the perturbed node 2. Must be 0 or 1        
        String PertNodeString3=args[8]; //Node 3 that wil be perturbed
        String PertNodeState3=args[9]; //Node state of the perturbed node 3. Must be 0 or 1  
        int numberOfPerturbations=3;
        int timePerturbationStart=2; 
        String timecourseFileName="timecourse"+fileName.split("\\.")[0]+"_"+PertNodeString1+"="+PertNodeState1+"_"+PertNodeString2+"="+PertNodeState2+"_"+PertNodeString3+"="+PertNodeState3+".txt"; 
        boolean writeTimecourse=false;
        if("true".equals(args[3])){writeTimecourse=true;}

        System.out.println("Perturbation1\tPerturbation2\tPerturbation3\tApofrac1\tApofrac2\tApofrac3\tApofrac\tProlfrac1\tProlfrac2\tProlfrac3\tProlfrac4\tProlfrac");
        List timecourseResult=runTimecourse(nw,args,numberOfPerturbations,timePerturbationStart, writeTimecourse,timecourseFileName);
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

    public static Network GenerateModel(String fileName) throws ScriptException {
        Network nw = null;        
        File fName = new File(fileName);
        if (fName.isFile()){
            String shortname=fName.getName();
            System.out.println("\nFilename: "+fileName);
            System.out.println("Creating Boolean table directory: "+shortname.split("\\.")[0]);
            ReadWriteFiles.createTablesFromBooleanRules(shortname.split("\\.")[0], fileName);
            System.out.println("Boolean table directory created.");
            System.out.println("Creating functions and names files.");
            nw=OtherMethods.RecreateNetwork(shortname.split("\\.")[0]);        
            System.out.println("Functions and names files created.");
            nw.findNodeOutputs();
        }
        else{
            System.out.println("File could not be found.");
        }
        return nw;
        
    }
    
    public static List<Double> runTimecourse(Network nw,String [] args,int numberOfPerturbations, int timePerturbationStart, boolean writeTimecourse, String timecourseFileName){
        
        String PertNodeString1 = null,PertNodeString2 = null,PertNodeString3 = null,PertNodeState1 = null,PertNodeState2 = null,PertNodeState3 = null; 
        if(numberOfPerturbations>0){
                PertNodeString1=args[4]; //Node 1 that wil be perturbed
                PertNodeState1=args[5]; //Node state of the perturbed node 1. Must be 0 or 1
                if(numberOfPerturbations>1){
                    PertNodeString2=args[6]; //Node 2 that wil be perturbed
                    PertNodeState2=args[7]; //Node state of the perturbed node 2. Must be 0 or 1       
                    if(numberOfPerturbations>2){
                        PertNodeString3=args[8]; //Node 2 that wil be perturbed
                        PertNodeState3=args[9]; //Node state of the perturbed node 2. Must be 0 or 1        
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
        int updateNode,KOnode1 = 0,KOnode2 = 0,KOnode3 = 0;
        double Apofraction1,Apofraction2,Apofraction3,Prolfraction1,Prolfraction2,Prolfraction3,Prolfraction4,Prolfraction,Apofraction;
        double apo,prol;
        int Apofraction1Ind,Apofraction2Ind,Apofraction3Ind,Prolfraction1Ind,Prolfraction2Ind,Prolfraction3Ind,Prolfraction4Ind;
        int[] nodeStates,pastState;
        double[][] trajectory,trajectory2;
        ArrayList<Integer> KOnodes=new ArrayList<Integer>();
 
        HashMap namesDictionary=new HashMap<Integer,String>();
        HashMap indexDictionary=new HashMap<String,Integer>();
        for(int i=0;i<N;i++){namesDictionary.put(i, nw.getNames()[i]);indexDictionary.put(nw.getNames()[i],i);}
        Apofraction1Ind=(int) indexDictionary.get("Apoptosis");Apofraction2Ind=(int) indexDictionary.get("Apoptosis_2");Apofraction3Ind=(int) indexDictionary.get("Apoptosis_3");
        Prolfraction1Ind=(int) indexDictionary.get("Proliferation");Prolfraction2Ind=(int) indexDictionary.get("Proliferation_2");Prolfraction3Ind=(int) indexDictionary.get("Proliferation_3");Prolfraction4Ind=(int) indexDictionary.get("Proliferation_4");
        
        List<ArrayList<Integer>> fast_slow_list = SlowFastNodes(indexDictionary);
        fast = fast_slow_list.get(0);
        slow = fast_slow_list.get(1);
        
        p=1.0*slow.size()/(1.0*slow.size()+5.0*fast.size());//p=pslow, 5*p=pfast, p*slow.size()+pfast*fast.size()= 1 =p(slow.size()+5*fast.size())              
        Ntime=slow.size()+5*fast.size();
        Nprint=(int)(Math.ceil(1.0*Ntime/5.0));
        //p=0;
        //Ntime=1;
        //fast=new ArrayList<Integer>();
        //for(int i=0;i<indexDictionary.size();i++){fast.add(new Integer(i));}
                                         
        Apofraction1=0;Apofraction2=0;Apofraction3=0;Prolfraction1=0;Prolfraction2=0;Prolfraction3=0;Prolfraction4=0;Apofraction=0;Prolfraction=0;
        trajectory=new double[Tprint][N];
        trajectory2=new double[Tprint][2];
        for(int t=0;t<Tprint;t++){for(int i=0;i<N;i++){trajectory[t][i]=0;}}
        for(int t=0;t<Tprint;t++){for(int i=0;i<2;i++){trajectory2[t][i]=0;}}
        for(int r=0;r<IC;r++){
            nodeStates=setInitialCondition(indexDictionary); 
            if(numberOfPerturbations>0){
                KOnode1=(int) indexDictionary.get(PertNodeString1);
                KOnodes.add(new Integer(KOnode1));
                if(numberOfPerturbations>1){
                    KOnode2=(int) indexDictionary.get(PertNodeString2);
                    KOnodes.add(new Integer(KOnode2));
                    if(numberOfPerturbations>2){
                        KOnode3=(int) indexDictionary.get(PertNodeString3);
                        KOnodes.add(new Integer(KOnode3));
                    }
                }
            }
            if(nodeStates[Apofraction3Ind]==1){apo=1;}else if(nodeStates[Apofraction2Ind]==1){apo=0.5;}else if(nodeStates[Apofraction1Ind]==1){apo=0.25;}else{apo=0;}
            if(nodeStates[Prolfraction4Ind]==1){prol=1;}else if(nodeStates[Prolfraction3Ind]==1){prol=0.5;}else if(nodeStates[Prolfraction2Ind]==1){prol=0.25;}else if(nodeStates[Prolfraction1Ind]==1){prol=0.125;}else{prol=0;}
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
                                }
                            }
                        }    
                    }

                    for(int i=0;i<N;i++){trajectory[t][i]+=1.0*nodeStates[i];}
                    trajectory2[t][0]+=1.0*prol;
                    trajectory2[t][1]+=1.0*apo;
                    for(int n=0;n<Nprint;n++){
                        if(Math.random()>=p){index=(int)(Math.random()*fast.size());updateNode=fast.get(index);}
                        else{index=(int)(Math.random()*slow.size());updateNode=slow.get(index);}
                        pastState=Arrays.copyOf(nodeStates, nodeStates.length);
                        nodeStates[updateNode]=UpdateMethods.updateSingleNodeBoolean(nw, pastState, updateNode);
                        if(numberOfPerturbations>0){
                            if(updateNode==KOnode1&&t>=timePerturbationStart){nodeStates[KOnode1]=Integer.parseInt(PertNodeState1);}
                            if(numberOfPerturbations>1){
                                if(updateNode==KOnode2&&t>=timePerturbationStart){nodeStates[KOnode2]=Integer.parseInt(PertNodeState2);}
                                if(numberOfPerturbations>2){
                                    if(updateNode==KOnode3&&t>=timePerturbationStart){nodeStates[KOnode3]=Integer.parseInt(PertNodeState3);}
                                }
                            }
                        }                      
                    }

                    if(nodeStates[Apofraction3Ind]==1){apo=1;}else if(nodeStates[Apofraction2Ind]==1){apo=0.5;}else if(nodeStates[Apofraction1Ind]==1){apo=0.25;}else{apo=0;}
                    if(nodeStates[Prolfraction4Ind]==1){prol=1;}else if(nodeStates[Prolfraction3Ind]==1){prol=0.5;}else if(nodeStates[Prolfraction2Ind]==1){prol=0.25;}else if(nodeStates[Prolfraction1Ind]==1){prol=0.125;}else{prol=0;}            
                    if(t>=timePerturbationStart){
                         steadyState=checkSteadyState(nw,nodeStates,KOnodes);
                    }
                }
                else{
                    for(int i=0;i<N;i++){trajectory[t][i]+=1.0*nodeStates[i];}
                    trajectory2[t][0]+=1.0*prol;
                    trajectory2[t][1]+=1.0*apo;   
                }
            }

            if(nodeStates[(int) indexDictionary.get("Apoptosis")]==1){Apofraction1=Apofraction1+1;}
            if(nodeStates[(int) indexDictionary.get("Apoptosis_2")]==1){Apofraction2=Apofraction2+1;}
            if(nodeStates[(int) indexDictionary.get("Apoptosis_3")]==1){Apofraction3=Apofraction3+1;}
            if(nodeStates[(int) indexDictionary.get("Proliferation")]==1){Prolfraction1=Prolfraction1+1;}
            if(nodeStates[(int) indexDictionary.get("Proliferation_2")]==1){Prolfraction2=Prolfraction2+1;}
            if(nodeStates[(int) indexDictionary.get("Proliferation_3")]==1){Prolfraction3=Prolfraction3+1;}
            if(nodeStates[(int) indexDictionary.get("Proliferation_4")]==1){Prolfraction4=Prolfraction4+1;}
            if(nodeStates[Apofraction3Ind]==1){Apofraction=Apofraction+1;}else if(nodeStates[Apofraction2Ind]==1){Apofraction=Apofraction+0.5;}else if(nodeStates[Apofraction1Ind]==1){Apofraction=Apofraction+0.25;}else{}
            if(nodeStates[Prolfraction4Ind]==1){Prolfraction=Prolfraction+1;}else if(nodeStates[Prolfraction3Ind]==1){Prolfraction=Prolfraction+0.5;}else if(nodeStates[Prolfraction2Ind]==1){Prolfraction=Prolfraction+0.25;}else if(nodeStates[Prolfraction1Ind]==1){Prolfraction=Prolfraction+0.125;}else{}
            
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
                writeTrajectory(nw,N,IC,Tprint,trajectory,trajectory2,(double)(Nprint)/(double)(Ntime),timecourseFileName);
            }
            
            return Arrays.asList(Apofraction1,Apofraction2,Apofraction3,Prolfraction1,Prolfraction2,Prolfraction3,Prolfraction4,Apofraction,Prolfraction);
            
            }
    
    public static List<ArrayList<Integer>> SlowFastNodes(HashMap<String,Integer> indexDictionary){
        
        ArrayList<Integer> fast=new ArrayList<>(); //This array stores the indices of the fast nodes
        ArrayList<Integer> slow=new ArrayList<>(); //This array stores the indices of the slow nodes
        
        //Slow nodes for the simulations. The nodes not added to the "slow" list are the fast nodes and will stay in the "fast" list
        for(int i=0;i<indexDictionary.size();i++){fast.add(new Integer(i));}
        fast.remove(new Integer((int)indexDictionary.get("HER3")));slow.add(new Integer((int)indexDictionary.get("HER3")));
        fast.remove(new Integer((int)indexDictionary.get("HER3_2")));slow.add(new Integer((int)indexDictionary.get("HER3_2")));
        fast.remove(new Integer((int)indexDictionary.get("BIM")));slow.add(new Integer((int)indexDictionary.get("BIM")));
        fast.remove(new Integer((int)indexDictionary.get("BCL2")));slow.add(new Integer((int)indexDictionary.get("BCL2")));
        fast.remove(new Integer((int)indexDictionary.get("MCL1")));slow.add(new Integer((int)indexDictionary.get("MCL1")));
        fast.remove(new Integer((int)indexDictionary.get("HER2")));slow.add(new Integer((int)indexDictionary.get("HER2")));
        fast.remove(new Integer((int)indexDictionary.get("HER3_T")));slow.add(new Integer((int)indexDictionary.get("HER3_T")));
        fast.remove(new Integer((int)indexDictionary.get("cycE_CDK2_T")));slow.add(new Integer((int)indexDictionary.get("cycE_CDK2_T")));
        fast.remove(new Integer((int)indexDictionary.get("IGF1R_T")));slow.add(new Integer((int)indexDictionary.get("IGF1R_T")));
        fast.remove(new Integer((int)indexDictionary.get("BCL2_T")));slow.add(new Integer((int)indexDictionary.get("BCL2_T")));
        fast.remove(new Integer((int)indexDictionary.get("BIM_T")));slow.add(new Integer((int)indexDictionary.get("BIM_T")));
        fast.remove(new Integer((int)indexDictionary.get("ER")));slow.add(new Integer((int)indexDictionary.get("ER")));
        fast.remove(new Integer((int)indexDictionary.get("ESR1")));slow.add(new Integer((int)indexDictionary.get("ESR1")));
        fast.remove(new Integer((int)indexDictionary.get("ESR1_2")));slow.add(new Integer((int)indexDictionary.get("ESR1_2")));
        fast.remove(new Integer((int)indexDictionary.get("FOXA1")));slow.add(new Integer((int)indexDictionary.get("FOXA1")));
        fast.remove(new Integer((int)indexDictionary.get("PBX1")));slow.add(new Integer((int)indexDictionary.get("PBX1")));
        fast.remove(new Integer((int)indexDictionary.get("ER_transcription")));slow.add(new Integer((int)indexDictionary.get("ER_transcription")));
        fast.remove(new Integer((int)indexDictionary.get("ER_transcription_2")));slow.add(new Integer((int)indexDictionary.get("ER_transcription_2")));
        fast.remove(new Integer((int)indexDictionary.get("cyclinD")));slow.add(new Integer((int)indexDictionary.get("cyclinD")));
        fast.remove(new Integer((int)indexDictionary.get("cyclinD_2")));slow.add(new Integer((int)indexDictionary.get("cyclinD_2")));
        fast.remove(new Integer((int)indexDictionary.get("CDK46")));slow.add(new Integer((int)indexDictionary.get("CDK46")));
        fast.remove(new Integer((int)indexDictionary.get("E2F")));slow.add(new Integer((int)indexDictionary.get("E2F")));
        fast.remove(new Integer((int)indexDictionary.get("E2F_2")));slow.add(new Integer((int)indexDictionary.get("E2F_2")));
        fast.remove(new Integer((int)indexDictionary.get("E2F_3")));slow.add(new Integer((int)indexDictionary.get("E2F_3")));
        fast.remove(new Integer((int)indexDictionary.get("p21_p27_T")));slow.add(new Integer((int)indexDictionary.get("p21_p27_T")));
        fast.remove(new Integer((int)indexDictionary.get("SGK1_T")));slow.add(new Integer((int)indexDictionary.get("SGK1_T")));
        fast.remove(new Integer((int)indexDictionary.get("PDK1")));slow.add(new Integer((int)indexDictionary.get("PDK1")));
        fast.remove(new Integer((int)indexDictionary.get("ER")));slow.add(new Integer((int)indexDictionary.get("ER")));
        fast.remove(new Integer((int)indexDictionary.get("mTORC2")));slow.add(new Integer((int)indexDictionary.get("mTORC2")));
        fast.remove(new Integer((int)indexDictionary.get("FOXO3_Ub")));slow.add(new Integer((int)indexDictionary.get("FOXO3_Ub")));
        fast.remove(new Integer((int)indexDictionary.get("IGF1R")));slow.add(new Integer((int)indexDictionary.get("IGF1R")));
        fast.remove(new Integer((int)indexDictionary.get("IGF1R_2")));slow.add(new Integer((int)indexDictionary.get("IGF1R_2")));
        fast.remove(new Integer((int) indexDictionary.get("Apoptosis")));slow.add(new Integer((int) indexDictionary.get("Apoptosis")));
        fast.remove(new Integer((int) indexDictionary.get("Apoptosis_2")));slow.add(new Integer((int) indexDictionary.get("Apoptosis_2")));
        fast.remove(new Integer((int) indexDictionary.get("Apoptosis_3")));slow.add(new Integer((int) indexDictionary.get("Apoptosis_3")));
        fast.remove(new Integer((int) indexDictionary.get("Proliferation")));slow.add(new Integer((int) indexDictionary.get("Proliferation")));
        fast.remove(new Integer((int) indexDictionary.get("Proliferation_2")));slow.add(new Integer((int) indexDictionary.get("Proliferation_2")));
        fast.remove(new Integer((int) indexDictionary.get("Proliferation_3")));slow.add(new Integer((int) indexDictionary.get("Proliferation_3")));
        fast.remove(new Integer((int) indexDictionary.get("Proliferation_4")));slow.add(new Integer((int) indexDictionary.get("Proliferation_4")));
        fast.remove(new Integer((int)indexDictionary.get("MYC")));slow.add(new Integer((int)indexDictionary.get("MYC")));
        fast.remove(new Integer((int)indexDictionary.get("MYC_2")));slow.add(new Integer((int)indexDictionary.get("MYC_2")));
        
        
        return Arrays.asList(fast, slow);
    
    }
    
    public static int[] setInitialCondition(HashMap<String,Integer> indexDictionary){
            int[] nodeStates = new int[indexDictionary.size()];
            int ranState;
            
            for(int i=0;i<indexDictionary.size();i++){nodeStates[i]=0;}
            
            nodeStates[(int) indexDictionary.get("IGF1R_T")]=1;
            nodeStates[(int) indexDictionary.get("HER2")]=0;
            nodeStates[(int) indexDictionary.get("HER3_T")]=0;
            nodeStates[(int) indexDictionary.get("PDK1")]=0;
            nodeStates[(int) indexDictionary.get("SGK1_T")]=0;
            nodeStates[(int) indexDictionary.get("mTORC2")]=1;
            nodeStates[(int) indexDictionary.get("PIM")]=0;
            nodeStates[(int) indexDictionary.get("PTEN")]=0;

            nodeStates[(int) indexDictionary.get("Fulvestrant")]=0;
            nodeStates[(int) indexDictionary.get("Alpelisib")]=0;
            nodeStates[(int) indexDictionary.get("Everolimus")]=0;
            nodeStates[(int) indexDictionary.get("Palbociclib")]=0;
            nodeStates[(int) indexDictionary.get("Trametinib")]=0;
                        
            ranState=(int)(1.5*Math.random());
            nodeStates[(int) indexDictionary.get("BIM")]=ranState;
            nodeStates[(int) indexDictionary.get("BIM_T")]=ranState;
            nodeStates[(int) indexDictionary.get("BAD")]=0;
            nodeStates[(int) indexDictionary.get("MCL1")]=1;           
            nodeStates[(int) indexDictionary.get("BCL2_T")]=ranState;
            nodeStates[(int) indexDictionary.get("BCL2")]=ranState;
            
            //Nodes state of cancer attractor            
            nodeStates[(int) indexDictionary.get("IGF1R")]=1;
            nodeStates[(int) indexDictionary.get("IGF1R_2")]=0;
            nodeStates[(int) indexDictionary.get("HER2_3")]=0;
            nodeStates[(int) indexDictionary.get("SGK1")]=0;
            nodeStates[(int) indexDictionary.get("RAS")]=1;
            nodeStates[(int) indexDictionary.get("RAS_2")]=0;
            nodeStates[(int) indexDictionary.get("MAPK")]=1;
            nodeStates[(int) indexDictionary.get("MAPK_2")]=0;
            nodeStates[(int) indexDictionary.get("PI3K")]=1;
            nodeStates[(int) indexDictionary.get("PIP3")]=1;
            nodeStates[(int) indexDictionary.get("PDK1_pm")]=1;
            nodeStates[(int) indexDictionary.get("mTORC2_pm")]=1;
            nodeStates[(int) indexDictionary.get("AKT")]=1;
            nodeStates[(int) indexDictionary.get("p21_p27_T")]=0;
            nodeStates[(int) indexDictionary.get("p21_p27")]=0;
            nodeStates[(int) indexDictionary.get("cycE_CDK2")]=1;
            nodeStates[(int) indexDictionary.get("cycE_CDK2_T")]=1;            
            nodeStates[(int) indexDictionary.get("KMT2D")]=0;
            nodeStates[(int) indexDictionary.get("TSC")]=0;
            nodeStates[(int) indexDictionary.get("PRAS40")]=0;
            nodeStates[(int) indexDictionary.get("mTORC1")]=1;
            nodeStates[(int) indexDictionary.get("FOXO3")]=0;
            nodeStates[(int) indexDictionary.get("FOXO3_Ub")]=0;
            nodeStates[(int) indexDictionary.get("EIF4F")]=1;
            nodeStates[(int) indexDictionary.get("S6K")]=1;
            nodeStates[(int) indexDictionary.get("Translation")]=1;
            nodeStates[(int) indexDictionary.get("ER")]=1;
            nodeStates[(int) indexDictionary.get("ESR1")]=1;
            nodeStates[(int) indexDictionary.get("ESR1_2")]=0;
            nodeStates[(int) indexDictionary.get("FOXA1")]=0;
            nodeStates[(int) indexDictionary.get("PBX1")]=1;
            nodeStates[(int) indexDictionary.get("ER_transcription")]=1;
            nodeStates[(int) indexDictionary.get("ER_transcription_2")]=0;
            nodeStates[(int) indexDictionary.get("cyclinD")]=1;
            nodeStates[(int) indexDictionary.get("cyclinD_2")]=0;
            nodeStates[(int) indexDictionary.get("CDK46")]=1;
            nodeStates[(int) indexDictionary.get("cycD_CDK46")]=1;
            nodeStates[(int) indexDictionary.get("cycD_CDK46_2")]=0;
            nodeStates[(int) indexDictionary.get("pRb")]=1;
            nodeStates[(int) indexDictionary.get("pRb_2")]=1;
            nodeStates[(int) indexDictionary.get("pRb_3")]=0;
            nodeStates[(int) indexDictionary.get("E2F")]=1;
            nodeStates[(int) indexDictionary.get("E2F_2")]=1;
            nodeStates[(int) indexDictionary.get("E2F_3")]=0;
            nodeStates[(int) indexDictionary.get("Proliferation")]=1;
            nodeStates[(int) indexDictionary.get("Proliferation_2")]=1;
            nodeStates[(int) indexDictionary.get("Proliferation_3")]=1;
            nodeStates[(int) indexDictionary.get("Proliferation_4")]=0;
            nodeStates[(int) indexDictionary.get("Apoptosis")]=0;
            nodeStates[(int) indexDictionary.get("Apoptosis_2")]=0;
            nodeStates[(int) indexDictionary.get("Apoptosis_3")]=0;
            nodeStates[(int) indexDictionary.get("MYC")]=1;
            nodeStates[(int) indexDictionary.get("MYC_2")]=0;
            
            return nodeStates;
    }
    
    public static void writeTrajectory(Network nw, int N,int IC,int T,double[][] trajectory,double[][] trajectory2,double timeUnit, String filename){
    
        //This writes out the timecourse of the average activity in the TXT file
        FileToWrite fw=new FileToWrite(filename); //The average time course is stored in this
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

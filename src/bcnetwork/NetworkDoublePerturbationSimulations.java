/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bcnetwork;

import booleandynamicmodeling.Network;
import fileOperations.FileToWrite;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jorge G. T. Zañudo
 * @date November 2020
 */
public class NetworkDoublePerturbationSimulations {

    //This program simulates the ER+ breast cancer network model in the presence of each possible
    //double node perturbation, starting from the cancer steady state initial condition. 
    
    //The regulatory functions of the bare model given in the TXT file "BreastCancerModel_ZanudoEtAl2017.txt".
    //The model dynamics are governed by the stochastic general asynchronous updating
    //scheme. Every time step corresponds to several (Ntimes) updates, with one time
    //step corresponding to the average number of updates needed to update a slow node. 

     /**
     * @param args args[0] is the name of the TXT file where the model rules are. For the breast cancer model it is "BreastCancerModel_ZanudoEtAl2017.txt".
     * args[1] is the number of initial conditions
     * args[2] is the number of normalized timesteps (number of timesteps equal to the average time needed to update a slow node)
     * @param nw Network object with the model already imported
     */
    
        public static void main(String[] args, Network nw) {
        
        String fileName=args[0]; //This file contains the Boolean rules of the model              
        String outputFileName="BreastCancerDoublePerturbations.txt";
        String IC=args[1]; //Number of initial conditions
        String T=args[2]; //This is the number of time steps
        int seed=1000;
        String line;
        int numberOfPerturbations=3; //Alpelisib + 2 perturbations
        int timePerturbationStart=0;

        ArrayList<Integer> state1=new ArrayList<Integer>();state1.add(0);state1.add(1);
        FileToWrite fw=new FileToWrite(outputFileName);
        //The average apoptosis and proliferation at the end of each simulation for each
        //single perturbation will be saved in this tab-separated file
                
        System.out.println("Perturbation\tApofrac1\tApofrac2\tApofrac3\tApofrac\tProlfrac1\tProlfrac2\tProlfrac3\tProlfrac4\tProlfrac");                
        fw.writeLine("Perturbation\tApofrac1\tApofrac2\tApofrac3\tApofrac\tProlfrac1\tProlfrac2\tProlfrac3\tProlfrac4\tProlfrac");
        
        String[] test=new String[10];
        test[0]=fileName;
        test[1]=IC;
        test[2]=T;
        test[4]="Alpelisib";test[5]="1";
        
        for(Integer s0: state1){
            for(int i0=0;i0<nw.getN();i0++){
                test[6]=nw.getNames()[i0];test[7]=""+s0;
                for(Integer s1: state1){
                    for(int i1=0;i1<i0;i1++){
                        test[8]=nw.getNames()[i1];test[9]=""+s1;
                        List timecourseResult=NetworkSimulations.runTimecourse(nw,test,numberOfPerturbations,timePerturbationStart, false,"",seed);
                        double Apofraction1=(double) timecourseResult.get(0);
                        double Apofraction2=(double) timecourseResult.get(1);
                        double Apofraction3=(double) timecourseResult.get(2);
                        double Prolfraction1=(double) timecourseResult.get(3);
                        double Prolfraction2=(double) timecourseResult.get(4);
                        double Prolfraction3=(double) timecourseResult.get(5);
                        double Prolfraction4=(double) timecourseResult.get(6);
                        double Apofraction=(double) timecourseResult.get(7);
                        double Prolfraction=(double) timecourseResult.get(8);
                        line=nw.getNames()[i0]+"="+s0+"-"+nw.getNames()[i1]+"="+s1+"\t"+Apofraction1+"\t"+Apofraction2+"\t"+Apofraction3+"\t"+Apofraction+"\t"+Prolfraction1+"\t"+Prolfraction2+"\t"+Prolfraction3+"\t"+Prolfraction4+"\t"+Prolfraction;
                        System.out.println(line);
                        fw.writeLine(line);
                    }
                }
            }
        }
        fw.close();
        
    }
            
    
}


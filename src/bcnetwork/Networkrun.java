/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bcnetwork;

import booleandynamicmodeling.Network;
import static bcnetwork.NetworkSimulations.GenerateModel;
import javax.script.ScriptException;

/**
 *
 * @author JGTZ
 * @date November 2020
 */
public class Networkrun {

    //This program simulates the ER+ breast cancer network model, starting from the
    //cancer steady state initial condition. The different java classes perform different
    //simulations:
    //NetworkSimulations - simulates the model under specified single, double, triple and no perturbations 
    //NetworkSinglePerturbationSimulations - simulates the model under all possible single node perturbations
    //NetworkDoublePerturbationSimulations - simulated the model under all possible double node perturbations
    
    /**
     * @param args 
     * args[0] is the name of the TXT file where the model is.
     * For the breast cancer model of Zanudo et al. 2017 it is "BreastCancerModel_ZanudoEtAl2017.txt". If we use this fileName, the
     * program will run the analysis of the Zanudo et al. 2017 manuscript.
     * args[1] is the number of initial conditions to run.
     * args[2] is the number of normalized time steps (number of timesteps equal to the average time needed to update a slow node) to run.
     * args[3] Whether to write to file the . Takes the values (without quotes) "true" or "false".
     * args[4] is the node name of the first perturbation, args[5] is the state of the first perturbation
     * args[6] is the node name of the second perturbation, args[7] is the state of the second perturbation
     * args[8] is the node name of the second perturbation, args[9] is the state of the second perturbation
     */
    
    public static void main(String[] args) throws ScriptException {

        String fileName=args[0]; //This file contains the Boolean rules of the model
        //String fileName="BreastCancerModel_ZanudoEtAl2017.booleannet";
        if("BreastCancerModel_ZanudoEtAl2017.booleannet".equals(fileName)){BreastCancerModelZanudoEtAl2017(args);}
        else{
            Network nw=GenerateModel(fileName);
            if(args.length==4){NetworkSimulations.BaselineTimecourse(args,nw);}
            if(args.length==6){NetworkSimulations.SinglePerturbationTimecourse(args,nw);} 
            if(args.length==8){NetworkSimulations.DoublePerturbationTimecourse(args,nw);}
            if(args.length==10){NetworkSimulations.TriplePerturbationTimecourse(args,nw);} 
        
        }

        
    }
    
    public static void BreastCancerModelZanudoEtAl2017(String[] args) throws ScriptException {

        String fileName="BreastCancerModel_ZanudoEtAl2017.booleannet";
        int IC=1000; //Number of initial conditions
        int T=25; //This is the number of time steps
        
        String[] test=new String[10];
        test[0]=fileName;
        test[1]=""+IC;
        test[2]=""+T;
        test[3]="true";
        
        //Generating network model from filename
        Network nw=GenerateModel(fileName);
        System.out.println("");
                
        //Simulates the model under no perturbations and outputs the timecourse
        //of the average activity of each node
        System.out.println("Baseline timecourse");
        NetworkSimulations.BaselineTimecourse(test,nw);
        System.out.println("");
        
        //Simulates the model in the presence of Alpelisib and outputs the timecourse
        //of the average activity of each node
        System.out.println("Alpelisib=1 timecourse");
        test[4]="Alpelisib";test[5]="1";
        NetworkSimulations.SinglePerturbationTimecourse(test,nw);
        System.out.println("");
        
        //Simulates the model in the presence of Alpelisib and PIM, and outputs
        //the timecourse of the average activity of each node        
        System.out.println("Alpelisib=1 + PIM=1 timecourse");
        test[4]="Alpelisib";test[5]="1";
        test[6]="PIM";test[7]="1";
        NetworkSimulations.DoublePerturbationTimecourse(test,nw);
        System.out.println("");
        
        //Simulates the model in the presence of Alpelisib and MCL1 inhibition,
        //and outputs the timecourse of the average activity of each node
        System.out.println("Alpelisib=1 + MCL1=0 timecourse");        
        test[4]="Alpelisib";test[5]="1";
        test[6]="MCL1";test[7]="0";
        NetworkSimulations.DoublePerturbationTimecourse(test,nw);
        System.out.println("");

        //Simulates the model in the presence of Alpelisib and Everolimus,
        //and outputs the timecourse of the average activity of each node
        System.out.println("Alpelisib=1 + Everolimus=1 timecourse");
        test[4]="Alpelisib";test[5]="1";
        test[6]="Everolimus";test[7]="1";
        NetworkSimulations.DoublePerturbationTimecourse(test,nw);
        System.out.println("");
                        
        System.out.println("Alpelisib=1 + Palbociclib=1 + Fulvestrant=1 timecourse");
        test[4]="Alpelisib";test[5]="1";
        test[6]="Palbociclib";test[7]="1";
        test[8]="Fulvestrant";test[9]="1";        
        NetworkSimulations.TriplePerturbationTimecourse(test,nw);
        System.out.println("");
        
        //Simulates the model in the presence of Alpelisib and each of 
        //every possible single node perturbation
        System.out.println("Alpelisib + every possible single node perturbation");
        NetworkSinglePerturbationSimulations.main(test,nw);
        System.out.println("");
        
        //Simulates the model in the presence of Alpelisib and each of
        //every possible double node perturbation                
        System.out.println("Alpelisib + every possible doubble node perturbation");
        NetworkDoublePerturbationSimulations.main(test,nw);
        
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bcnetwork;

import booleandynamicmodeling.Network;
import static bcnetwork.NetworkSimulations.GenerateModel;
import static bcnetwork.NetworkSimulations.GetModel;
import java.io.File;
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
     * args[0] is used to specify the mode in which to run the program.
     * - "Generate" - Will generate the Boolean tables, names, and functions files based on the model specified by args[1] 
     * - "Run" - Will run (but not generate) the Boolean tables, names, and functions files based on the model specified by args[1]
     * - "Run_ThirdTimescale" - Like "Run", but will use 3 timescales instead of the usual 2 timescales (slow, fast nodes)
     * - "GenerateAndRun" - Will run and generate the Boolean tables, names, and functions files based on the model specified by args[1]
     * - "BreastCancerModel_ZanudoEtAl2017" - Will run the simulations of the breast cancer model of the Zanudo et al. 2017 manuscript.
     * args[1] is the name of the TXT file where the model rules are.
     * args[2] is the number of initial conditions to run.
     * args[3] is the number of normalized time steps (number of timesteps equal to the average time needed to update a slow node) to run.
     * args[4] Whether to write to file the timecourse. Takes the values (without quotes) "true" or "false".
     * args[5] is the node name of the first perturbation, args[6] is the state of the first perturbation
     * args[7] is the node name of the second perturbation, args[8] is the state of the second perturbation
     * args[9] is the node name of the third perturbation, args[10] is the state of the third perturbation
     */
    
    public static void main(String[] args) throws ScriptException {
        
        String mode=args[0];
        String[] test;
        String fileName; 
        File fName;
        Network nw=null;

        if("Generate".equalsIgnoreCase(mode)| "Run".equalsIgnoreCase(mode)| "GenerateAndRun".equalsIgnoreCase(mode) | "Run_ThirdTimescale".equalsIgnoreCase(mode)){
            fileName=args[1]; //This file contains the Boolean rules of the model     
            fName = new File(args[1]);
            test=new String[args.length-1];
            test[0]=fName.getName();
            for(int i=1;i<args.length-1;i++){test[i]=args[i+1];}
            if("GenerateAndRun".equalsIgnoreCase(mode) | "Generate".equalsIgnoreCase(mode)){
                nw=GenerateModel(fileName);
            }
            else{
                nw=GetModel(fileName);
            } 
            if("GenerateAndRun".equalsIgnoreCase(mode) | "Run".equalsIgnoreCase(mode) | "Run_ThirdTimescale".equalsIgnoreCase(mode)){
                
                if("GenerateAndRun".equalsIgnoreCase(mode) | "Run".equalsIgnoreCase(mode)){
                    if(test.length==4){NetworkSimulations.BaselineTimecourse(test,nw,false);}
                    if(test.length==6){NetworkSimulations.SinglePerturbationTimecourse(test,nw,false);} 
                    if(test.length==8){NetworkSimulations.DoublePerturbationTimecourse(test,nw,false);}
                    if(test.length==10){NetworkSimulations.TriplePerturbationTimecourse(test,nw,false);}
                    if(test.length==12){NetworkSimulations.QuadruplePerturbationTimecourse(test,nw,false);}                 
                }
                else{
                    if(test.length==4){NetworkSimulations.BaselineTimecourse(test,nw,true);}
                    if(test.length==6){NetworkSimulations.SinglePerturbationTimecourse(test,nw,true);} 
                    if(test.length==8){NetworkSimulations.DoublePerturbationTimecourse(test,nw,true);}
                    if(test.length==10){NetworkSimulations.TriplePerturbationTimecourse(test,nw,true);}
                    if(test.length==12){NetworkSimulations.QuadruplePerturbationTimecourse(test,nw,true);}  
                }                
            }
            else{
                System.out.println("Exiting program");
            }
        }
        else if("BreastCancerModel_ZanudoEtAl2017".equalsIgnoreCase(mode)){
            test=new String[args.length-1];
            test[0]="BreastCancerModel_ZanudoEtAl2017.booleannet";
            for(int i=1;i<args.length-1;i++){test[i]=args[i+1];}
            BreastCancerModelZanudoEtAl2017(test);      
        }
        else{
            System.out.println("Only modes \"Generate\", \"Run\", \"GenerateAndRun\", \"BreastCancerModel_ZanudoEtAl2017\" are allowed.");     
        }

        
    }
    
    public static void BreastCancerModelZanudoEtAl2017(String[] args) throws ScriptException {

        String fileName=args[0];
        File fName = new File(fileName);
        String shortName=fName.getName();
        int IC=1000; //Number of initial conditions
        int T=25; //This is the number of time steps
        
        String[] test=new String[10];
        test[0]=shortName;
        test[1]=""+IC;
        test[2]=""+T;
        test[3]="true";
        
        //Generating network model from filename
        Network nw=GenerateModel(fileName);
        System.out.println("");
                
        //Simulates the model under no perturbations and outputs the timecourse
        //of the average activity of each node
        System.out.println("Baseline timecourse");
        NetworkSimulations.BaselineTimecourse(test,nw,false);
        System.out.println("");
        
        //Simulates the model in the presence of Alpelisib and outputs the timecourse
        //of the average activity of each node
        System.out.println("Alpelisib=1 timecourse");
        test[4]="Alpelisib";test[5]="1";
        NetworkSimulations.SinglePerturbationTimecourse(test,nw,false);
        System.out.println("");
        
        //Simulates the model in the presence of Alpelisib and PIM, and outputs
        //the timecourse of the average activity of each node        
        System.out.println("Alpelisib=1 + PIM=1 timecourse");
        test[4]="Alpelisib";test[5]="1";
        test[6]="PIM";test[7]="1";
        NetworkSimulations.DoublePerturbationTimecourse(test,nw,false);
        System.out.println("");
        
        //Simulates the model in the presence of Alpelisib and MCL1 inhibition,
        //and outputs the timecourse of the average activity of each node
        System.out.println("Alpelisib=1 + MCL1=0 timecourse");        
        test[4]="Alpelisib";test[5]="1";
        test[6]="MCL1";test[7]="0";
        NetworkSimulations.DoublePerturbationTimecourse(test,nw,false);
        System.out.println("");

        //Simulates the model in the presence of Alpelisib and Everolimus,
        //and outputs the timecourse of the average activity of each node
        System.out.println("Alpelisib=1 + Everolimus=1 timecourse");
        test[4]="Alpelisib";test[5]="1";
        test[6]="Everolimus";test[7]="1";
        NetworkSimulations.DoublePerturbationTimecourse(test,nw,false);
        System.out.println("");
                        
        System.out.println("Alpelisib=1 + Palbociclib=1 + Fulvestrant=1 timecourse");
        test[4]="Alpelisib";test[5]="1";
        test[6]="Palbociclib";test[7]="1";
        test[8]="Fulvestrant";test[9]="1";        
        NetworkSimulations.TriplePerturbationTimecourse(test,nw,false);
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

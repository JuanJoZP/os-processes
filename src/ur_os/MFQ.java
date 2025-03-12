/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author prestamour
 */
public class MFQ extends Scheduler{

    int currentScheduler;
    private ArrayList<Scheduler> schedulers;

    MFQ(OS os){
        super(os);
        currentScheduler = -1;
        schedulers = new ArrayList();
    }
    
    MFQ(OS os, Scheduler... s){ //Received multiple arrays
        this(os);
        schedulers.addAll(Arrays.asList(s));
        if(s.length > 0) currentScheduler = 0;
    }
        
    @Override
    public void addProcess(Process p){
       // incoming process goes into first queue allways 
       schedulers.get(0).addProcess(p);
    }
    
    void defineCurrentScheduler(){
        // set currentScheduler to the first non empty scheduler
        for (int i = 0; i < schedulers.size(); i++) {
            if (!schedulers.get(i).isEmpty()) {
                currentScheduler = i;
                return;
            }
        }
    }
    
    boolean nonEmptySchedulerBefore(int stop) {
        for (int i = 0; i < stop; i++) {
            if(!schedulers.get(i).isEmpty()) {
                return true;
            }
        }
        return false;
    }
    
   
    @Override
    public void getNext(boolean cpuEmpty) {
        if (!cpuEmpty) {
            Process prev_process = os.cpu.getProcess();
            schedulers.get(currentScheduler).getNext(false); // if preemptive this changes the process in cpu 
            Process next_process = os.cpu.getProcess();

            // if a process is preempted, send it to the next queue
            if (prev_process != next_process) {
                // si son de la misma cola
                Process preempted_p = schedulers.get(0).processes.removeLast(); // preempted p got appended into first queue
                schedulers.get(currentScheduler+1).addProcess(preempted_p);
                
                // aca un if next_process es null, significa que saca el proceso por quantum
                // pero no tenia que mas meter asi que saco y dejó null
                if (next_process == null) {
                    getNext(true); // mete el que este en cola de mas prioridad
                } else if (nonEmptySchedulerBefore(currentScheduler)) {
                    // puso un proceso distinto, pero puede que de la misma cola,
                    // eso podria ser un error, porque una cola de mayor prioridad puede que tuviera procesos
                    // esperando
                    os.interrupt(InterruptType.SCHEDULER_CPU_TO_RQ, null); // this adds the process to the last position of the first queue
                    Process wrong_p = schedulers.get(0).processes.removeLast();  // the p that was incorrectly put on CPU
                    schedulers.get(currentScheduler).processes.add(0, wrong_p);
                    getNext(true);
                    // if alguna cola hasta currentScheduler tenia algo
                // sacar de cpu el proceso que metio, devolverlo al primer puesto de la
                // cola en la que estaba
                // llamar getNext(true)
                }
            }
        } else {
            defineCurrentScheduler();
            schedulers.get(currentScheduler).getNext(true);
        }
        
    }
    
    @Override
    public void newProcess(boolean cpuEmpty) {} //Non-preemtive in this event

    @Override
    public void IOReturningProcess(boolean cpuEmpty) {} //Non-preemtive in this event
    
}

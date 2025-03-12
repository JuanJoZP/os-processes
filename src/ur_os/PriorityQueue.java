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
public class PriorityQueue extends Scheduler{

    int currentScheduler;
    private ArrayList<Scheduler> schedulers;
    
    PriorityQueue(OS os){
        super(os);
        currentScheduler = -1;
        schedulers = new ArrayList();
    }
    
    PriorityQueue(OS os, Scheduler... s){ //Received multiple arrays
        this(os);
        schedulers.addAll(Arrays.asList(s));
        if(s.length > 0) currentScheduler = 0;
    }
    
    @Override
    public void addProcess(Process p){
       int priority = p.getPriority();  
       int idx = Math.min(priority, schedulers.size() - 1); 
       schedulers.get(idx).addProcess(p);     
    }
    
    void defineCurrentScheduler(){
        for (int i = 0; i < schedulers.size(); i++) {
            Scheduler s = schedulers.get(i);
            if (!s.isEmpty()) {
                currentScheduler = i;
                return;
            }
        }   
    }
   
    @Override
    public void getNext(boolean cpuEmpty) {
        if (cpuEmpty) {
            defineCurrentScheduler();
        }
        schedulers.get(currentScheduler).getNext(cpuEmpty);

        // the line before migth put a process on CPU so "cpuEmpty" is no longer correct
        if (os.isCPUEmpty()) {
            defineCurrentScheduler();
            schedulers.get(currentScheduler).getNext(true);
        }
    }
    
    @Override
    public void newProcess(boolean cpuEmpty) {} //Non-preemtive in this event

    @Override
    public void IOReturningProcess(boolean cpuEmpty) {} //Non-preemtive in this event
}

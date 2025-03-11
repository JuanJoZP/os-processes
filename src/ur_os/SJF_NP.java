/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author prestamour
 */
public class SJF_NP extends Scheduler{

    SJF_NP(OS os){
        super(os);
    }    
   
    @Override
    public void getNext(boolean cpuEmpty) {
        if (!processes.isEmpty() && cpuEmpty) {
            Process next = null;
            int shortest_process_size = Integer.MAX_VALUE;
            for (int i = 0; i < processes.size(); i++) {
                int process_size = processes.get(i).getRemainingTimeInCurrentBurst();
                if (process_size < shortest_process_size) {
                    next = processes.get(i);
                    shortest_process_size = next.getRemainingTimeInCurrentBurst();
                } else if (process_size == shortest_process_size) {
                    next = tieBreaker(next, processes.get(i));
                }
            }
            
            if (next != null) {
                processes.remove(next);
                os.interrupt(InterruptType.SCHEDULER_RQ_TO_CPU, next);
            }
        }
    }
    
    @Override
    public void newProcess(boolean cpuEmpty) {} //Non-preemtive

    @Override
    public void IOReturningProcess(boolean cpuEmpty) {} //Non-preemtive
}

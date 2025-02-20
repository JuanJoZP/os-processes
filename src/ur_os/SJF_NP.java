/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os;

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
       if(!processes.isEmpty() && cpuEmpty) {
            int shortest_process_index = -1;
            int shortest_process_size = Integer.MAX_VALUE;
            for (int i = 0; i < processes.size(); i++) {
                int process_size = processes.get(i).getRemainingTimeInCurrentBurst();
                if (process_size <= shortest_process_size) {
                    shortest_process_index = i;
                    shortest_process_size = process_size;
                }
            }
            
            Process next_p = processes.get(shortest_process_index);
            processes.remove(shortest_process_index);
            os.interrupt(InterruptType.SCHEDULER_RQ_TO_CPU, next_p);
        }
    }
    
    @Override
    public void newProcess(boolean cpuEmpty) {} //Non-preemtive

    @Override
    public void IOReturningProcess(boolean cpuEmpty) {} //Non-preemtive
    
}

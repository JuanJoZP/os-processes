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
public class SJF_P extends Scheduler{

    
    SJF_P(OS os){
        super(os);
    }
    
    @Override
    public void newProcess(boolean cpuEmpty){// When a NEW process enters the queue, process in CPU, if any, is extracted to compete with the rest
    	if(cpuEmpty == false) {
			
			os.interrupt(InterruptType.SCHEDULER_CPU_TO_RQ,null);
		} else {
			//addContextSwitch();
		}
    } 

    @Override
    public void IOReturningProcess(boolean cpuEmpty){// When a process return from IO and enters the queue, process in CPU, if any, is extracted to compete with the rest
		if(cpuEmpty == false) {
			os.interrupt(InterruptType.SCHEDULER_CPU_TO_RQ,null);					
		} else {
			//addContextSwitch();
		}
    } 
   
    @Override
    public void getNext(boolean cpuEmpty) {
        if(!processes.isEmpty() && cpuEmpty) {
        	int cont = 0;
        	List<Process> shortest_processes = new ArrayList<>();
            int shortest_process_size = Integer.MAX_VALUE;
            for (int i = 0; i < processes.size(); i++) {
                int process_size = processes.get(i).getRemainingTimeInCurrentBurst();
                if (process_size == shortest_process_size){
                	shortest_processes.add(processes.get(i));
                }
                else if (process_size < shortest_process_size) {
                	shortest_processes.clear();
                	shortest_processes.add(processes.get(i)); 
                	shortest_process_size = process_size;
                }
            }
            Process next = shortest_processes.getFirst() ;
            for(int i = 0; i < shortest_processes.size()-1 ;i++) {
            	next = tieBreaker(next,shortest_processes.get(i+1));
            }
            processes.remove(next);
            os.interrupt(InterruptType.SCHEDULER_RQ_TO_CPU, next);
        }	

    }
}

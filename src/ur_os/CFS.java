/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os;

import java.util.Comparator;

public class CFS extends Scheduler {

    public RedBlackTree processQueue;
    
    private int q;
    
    public CFS(OS os, int quantum) {
        super(os);
        processQueue = new RedBlackTree();
        this.q = quantum;
    }
    
    @Override
    public void addProcess(Process p) {
             //The order in which process will be addedd to the ReadyQueue, based on the execution of the
        //simulator, is: New processes, Interrupted processed from CPU, Incomming process from I/O
        if(p.getState() == ProcessState.NEW){
            newProcess(os.isCPUEmpty()); //If the process is NEW, let the scheduler defines what it will do to update the queue to select the next
        }else if(p.getState() == ProcessState.IO){
            IOReturningProcess(os.isCPUEmpty()); //If the process is returning from IO, let the scheduler defines what it will do to update the queue to select the next
        }
        p.setState(ProcessState.READY);
        processQueue.insert(p);
    }
    
    
    

    @Override
    public void getNext(boolean cpuEmpty) {
        // MAXVRUNTIME SOLO SE DEBE CALCULAR AL INICIO DE CADA QUANTUM
        // CUANDO TODOS LOS VRUNTIMES LLEGUEN A MAX ENTONCES SE REINICIAN A 0
        // Y AHI SI SE RECALCULA EL MAXVRUNTIME
        // CREO QUE EN NEWPROCESS E IORETURNING DEBERIA RECALCULAR MAXVRUNTIME
        
        // PERO PONIENDO CUIDADO DE QUE SI EL RECALCULO HACE QUE SE PASEN TODOS REINICIARLOS
        if(processQueue.size > 0){ 
            if(processQueue.GetAllRuntime(getMaxVruntime())){
                processQueue.restartVruntime();
            }
        }
        if (!processQueue.isEmpty() && cpuEmpty) {
            Node leftmostNode = processQueue.getLeftmostNode();
            if (leftmostNode != null) {  
                Process nextProcess = leftmostNode.GetProcess();
                if (os.isCPUEmpty() || preempt()) {
                    processQueue.deleteLeftmostNode();
                    os.interrupt(InterruptType.SCHEDULER_RQ_TO_CPU, nextProcess);
                }
            }
        }
        
        
        
        if (!os.isCPUEmpty()) {
            Process p = os.getProcessInCPU();
            p.setVruntime(p.getVruntime()+1);
        }
    }

    @Override
    public void newProcess(boolean cpuEmpty) {
        if (!cpuEmpty) {
            os.interrupt(InterruptType.SCHEDULER_CPU_TO_RQ, null);
        }
    }

    @Override
    public void IOReturningProcess(boolean cpuEmpty) {
        if (!cpuEmpty) {
            os.interrupt(InterruptType.SCHEDULER_CPU_TO_RQ, null);
        }
    }
    
    public double getMaxVruntime(){
        return q/processQueue.size;
    }

    public boolean preempt() {
        Process currentP = os.getProcessInCPU();
        return currentP.vruntime >= getMaxVruntime() ;   
    }

}

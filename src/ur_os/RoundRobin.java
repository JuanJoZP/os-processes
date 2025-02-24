package ur_os;

/**
 *
 * @author prestamour
 */
public class RoundRobin extends Scheduler {

    int q;
    int cont;
    boolean multiqueue;

    // Constructor con valores por defecto
    RoundRobin(OS os) {
        super(os);
        this.q = 5;
        this.cont = 0;
        this.multiqueue = false;
    }

    // Constructor con quantum personalizado
    RoundRobin(OS os, int q) {
        super(os);
        this.q = q;
        this.cont = 0;
        this.multiqueue = false;
    }

    // Constructor con quantum y soporte para multiqueue
    RoundRobin(OS os, int q, boolean multiqueue) {
        super(os);
        this.q = q;
        this.cont = 0;
        this.multiqueue = multiqueue;
    }

    // Método para reiniciar el contador
    void resetCounter() {
        cont = 0;
    }

    @Override
    public void getNext(boolean cpuEmpty) {
        if (!cpuEmpty) {
            cont += 1;
            if (cont > q) {
                Process p = null;
                if (!processes.isEmpty()) {
                    p = processes.remove();
                }
                os.interrupt(InterruptType.SCHEDULER_CPU_TO_RQ, p);
                cont = 1;
            }
        } else if (!processes.isEmpty()) {
            Process p = processes.remove();
            os.interrupt(InterruptType.SCHEDULER_RQ_TO_CPU, p);
            cont = 1;
        }
    }

    @Override
    public void newProcess(boolean cpuEmpty) {
        // Non-preemptive in this event
    }

    @Override
    public void IOReturningProcess(boolean cpuEmpty) {
        // Non-preemptive in this event
    }
}

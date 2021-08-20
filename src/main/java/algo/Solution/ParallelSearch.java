package algo.Solution;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class ParallelSearch{

    public BestSchedule bestSchedule;
    private int states = 0;
    private long prune = 0;
    private IntGraph graph;
    private int numProcessors;
    private BranchAndBound bb;

    public ParallelSearch(IntGraph graph, int processors){
        this.graph = graph;
        this.numProcessors = processors;
        bestSchedule = new BestSchedule();
        bb = new BranchAndBound(graph, processors, true);
    }

    public void run() {
        boolean[] startTasks = bb.getOrder();
        int candidateTask = 0;
        int candidateProcessor = 0;
        int commCost = 0;
        for (int i = 0; i < bb.numTasks; i++) {
            if (startTasks[i]) {
                for (int j = 0; j < numProcessors; j++) {
                    candidateTask = i;
                    candidateProcessor = j;
                    commCost = bb.commCost(candidateTask, candidateProcessor);
                    break;
                }
                break;
            }
        }
        ForkJoinPool pool = new ForkJoinPool();
        RecursiveSearch re = new RecursiveSearch(bb, candidateTask, candidateProcessor, commCost);
        pool.invoke(re);
        //branchBound(candidateTask, candidateProcessor, commCost);
    }

    private class RecursiveSearch extends RecursiveAction{

        private BranchAndBound branchAndBound;
        private int task;
        private int processor;
        private int cost;

        public RecursiveSearch(BranchAndBound branchAndBound, int task, int processor, int cost){
            this.branchAndBound = branchAndBound;
            this.task = task;
            this.processor = processor;
            this.cost = cost;
        }

        @Override
        protected void compute() {
            int bWeight = branchAndBound.bottomLevel[task] + cost + branchAndBound.processorTimes[processor];
            int loadBalance = branchAndBound.loadBalance(cost);
            int candidateTime = Math.max(branchAndBound.time.peek(), Math.max(bWeight, loadBalance));


                if (bestSchedule.bestTime <= candidateTime) {
                    prune += 1;
                    return;
                } else {
                    branchAndBound.addTask(task, processor, cost);
                }




                if (branchAndBound.unscheduledTasks.isEmpty()) {
                    int candidateBest = branchAndBound.time.peek();
                    synchronized (RecursiveSearch.class) {
                        if (candidateBest < bestSchedule.bestTime) {
                            bestSchedule.makeCopy(candidateBest, branchAndBound.taskProcessors, branchAndBound.taskInformation);
                        }
                    }
                }

            boolean[] candidateTasks = branchAndBound.getOrder();
            PriorityQueue<DSL> lowestCost = new PriorityQueue<>();
            boolean[] candidateProcessors = branchAndBound.normalise();
            for (int i = 0; i < branchAndBound.numTasks; i++) {
                if (candidateTasks[i]) {
                    for (int j = 0; j < numProcessors; j++) {
                        if (candidateProcessors[j]) {
                            int commCost = branchAndBound.commCost(i,j);
                            boolean seen = branchAndBound.checkSeen(i,j,commCost);

                            if (!seen) {
                                DSL dsl = new DSL(branchAndBound.bottomLevel[i],commCost,branchAndBound.processorTimes[j],i,j);
                                lowestCost.add(dsl);
                            }
                        }
                    }
                }
            }

            List<RecursiveSearch> list = new ArrayList<>();
            while (!lowestCost.isEmpty()) {
                DSL candidate = lowestCost.poll();
                int candidateTask = candidate.task;
                int processorID = candidate.processor;
                int candidateCost = candidate.cost;
                BranchAndBound c_branchAndBound = branchAndBound.deepCopy();
                RecursiveSearch re = new RecursiveSearch(c_branchAndBound, candidateTask, processorID, candidateCost);
                list.add(re);
            }
            invokeAll(list);
            branchAndBound.removeTask(task,processor,cost);
            states += 1;

        }
    }
    public int done() {
//        bestSchedule.printTasks();
        System.out.println(bestSchedule.bestTime);
        System.out.println(states);
//        System.out.println(prune);
        return bestSchedule.bestTime;
    }

}

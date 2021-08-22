package algo.helpers.costFunctions;

public class LoadBalancer {
    static int graphWeight ;
    public static void initLoadBalancer(int[] weights) {
        graphWeight = 0;
        for (int i = 0; i < weights.length; i++) {
            graphWeight += weights[i];
        }
    }

    public static int calculateLoadBalance(int idle, int currentCost, int numProcessors) {
        return (int) Math.ceil((graphWeight + idle + currentCost) / numProcessors);
    }
}

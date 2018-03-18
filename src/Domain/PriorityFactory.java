package Domain;

public class PriorityFactory {

    public IPriorityDecider getPriority(boolean highPriority) {
        IPriorityDecider priority = null;

        if (highPriority) {
            priority = new HighPriority();
        } else {
            priority = new NoPriority();
        }

        return priority;
    }
}

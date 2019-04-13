import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GrammarStateManager {
    private static GrammarStateManager self;
    public ArrayList<GrammarState> states = new ArrayList<GrammarState>();
    public ArrayList<GrammarState> compressedStates = new ArrayList<GrammarState>();
    public HashMap<GrammarState, HashMap<Tag, GrammarState>> transitionMap = new HashMap<>();
    // grammarStateID, inputTag, action:shift to next grammarState/reduce by a production
    public HashMap<Integer, HashMap<Tag, Integer>> lrStateTable = new HashMap<>();
    // compress or not
    private boolean isCompressed = false;

    private GrammarStateManager(){}

    public static GrammarStateManager getInstance() {
        if (self == null) {
            self = new GrammarStateManager();
        }
        return self;
    }

    public void buildTransitionStateMechine() {
        ProductionManager productionManager = ProductionManager.getInstance();
        GrammarState state = getGrammarState(productionManager.getProductionsByLeft(Tag.Program));
        state.startTransition();
    }

    public GrammarState getGrammarState(int stateNum) {
        Iterator iterator = null;
        if (isCompressed) {
            iterator = compressedStates.iterator();
        } else {
            iterator = states.iterator();
        }

        while (iterator.hasNext()) {
            GrammarState state = (GrammarState)iterator.next();
            if (state.stateNum == stateNum) {
                return state;
            }
        }
        return null;
    }

    public GrammarState getGrammarState(ArrayList<Production> productions) {
        // return grammar state contain productions
        // if not exist, then create a new grammar state with those productions

        GrammarState state = new GrammarState(productions);

        if (!states.contains(state)) {
            states.add(state);
            GrammarState.increaseStateNum();
            return state;
        }

        for (int i = 0; i < states.size(); i++) {
            if (states.get(i).equals(state)) {
                state = states.get(i);
            }
        }
        return state;

    }

    public void addTransition(GrammarState from, GrammarState to, Tag on) {
        if (isCompressed) {
            from = getAndMergeSimilarStates(from);
            to = getAndMergeSimilarStates(to);
        }

        HashMap<Tag, GrammarState> map = transitionMap.get(from);
        if (map == null) {
            map = new HashMap<>();
            transitionMap.put(from, map);
        }
        map.put(on, to);
    }

    private GrammarState getAndMergeSimilarStates(GrammarState state) {
        Iterator iterator = states.iterator();
        GrammarState currentState;
        GrammarState returnState = state;

        while(iterator.hasNext()) {
            currentState = (GrammarState)iterator.next();

            if (!currentState.equals(state) && currentState.productionEquals(state, true)) {
                if (currentState.stateNum < state.stateNum) {
                    currentState.stateMerge(state);
                    returnState = currentState;
                } else {
                    state.stateMerge(currentState);
                    returnState = state;
                }
                break;
            }
        }
        if (!compressedStates.contains(returnState)) {
            compressedStates.add(returnState);
        }
        return returnState;
    }

    public HashMap<Integer, HashMap<Tag, Integer>> getLRStateTable() {
        Iterator iterator = null;
        if (isCompressed) {
            iterator = compressedStates.iterator();
        } else {
            iterator = states.iterator();
        }

        while (iterator.hasNext()) {
            GrammarState state = (GrammarState)iterator.next();
            HashMap<Tag, GrammarState> map = transitionMap.get(state);
            HashMap<Tag, Integer> action = new HashMap<>();

            if (map != null) {
                for (Map.Entry<Tag, GrammarState> item: map.entrySet()) {
                    action.put(item.getKey(), item.getValue().stateNum);
                }
            }

            HashMap<Tag, Integer> reduceMap = state.makeReduce();
            if (reduceMap.size() > 0) {
                for (Map.Entry<Tag, Integer> item : reduceMap.entrySet()) {
                    // reduce action id < 0
                    action.put(item.getKey(), - item.getValue());
                }
            }

            lrStateTable.put(state.stateNum, action);
        }

        return lrStateTable;
    }
}

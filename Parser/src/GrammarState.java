import java.util.*;

public class GrammarState {
    public static int totalStatesCount = 0;
    private boolean transitionDone = false;
    public int stateNum;
    private GrammarStateManager stateManager = GrammarStateManager.getInstance();
    private ArrayList<Production> productions = new ArrayList<>();
    private HashMap<Tag, GrammarState> transition = new HashMap<>();
    private ArrayList<Production> closureSet = new ArrayList<Production>();
    private ProductionManager productionManager = ProductionManager.getInstance();
    // map of list with same next symbol
    private HashMap<Tag, ArrayList<Production>> partition = new HashMap<>();
    private ArrayList<Production> mergedProduction = new ArrayList<>();

    public static void increaseStateNum() {
        totalStatesCount++;
    }

    public boolean isTransitionDone() {
        return transitionDone;
    }

    public GrammarState(ArrayList<Production> productions) {
        this.stateNum = totalStatesCount;
        this.productions = productions;
        this.closureSet.addAll(this.productions);

//        System.out.println(stateNum);
    }

    public void startTransition() {
        if (transitionDone) {
            return;
        }
        transitionDone = true;

        // make closure
        makeClosure();
        // partition
        makePartition();
        // make transition
        makeTransition();
    }

    private void makeClosure() {
        Stack<Production> productionStack = new Stack<Production>();
        for (int i = 0; i < productions.size(); i++) {
            productionStack.push(productions.get(i));
        }

        while (!productionStack.empty()) {
            Production production = productionStack.pop();

            if (Tag.isTerminal(production.getDotSymbol())) {
                continue;
            }
            Tag symbolTag = production.getDotSymbol();


            ArrayList<Production> symbolProductions = productionManager.getProductionsByLeft(symbolTag);
            ArrayList<Tag> lookAhead = production.computeDotSymbolFirstSetOfBetaAndA();


            Iterator iterator = symbolProductions.iterator();
            while (iterator.hasNext()) {
                // look ahead may be difference
                Production oldProduction = (Production)iterator.next();
                Production newProduction = oldProduction.cloneSelf();

                newProduction.addLookAheadSet(lookAhead);

                if (!closureSet.contains(newProduction)) {
                    closureSet.add(newProduction);
                    productionStack.push(newProduction);
                    removeRedundantProduction(newProduction);
                }
            }
        }
    }

    private void removeRedundantProduction(Production production) {
        boolean remove = true;
        while (remove) {
            remove = false;
            Iterator iterator = closureSet.iterator();
            while (iterator.hasNext()) {
                Production item = (Production) iterator.next();
                if (production.coverUp(item)) {
                    remove = true;
                    closureSet.remove(item);
                    break;
                }
            }
        }
    }

    private void makePartition() {
        for (int i = 0; i < closureSet.size(); i++) {
            Tag symbolTag = closureSet.get(i).getDotSymbol();
            if (symbolTag == Tag.Unknown) {
                continue;
            }
            ArrayList<Production> productions = partition.get(symbolTag);
            if (productions == null) {
                productions = new ArrayList<Production>();
                partition.put(symbolTag, productions);
            }
            if (!productions.contains(closureSet.get(i))) {
                productions.add(closureSet.get(i));
            }
        }
    }

    private GrammarState buildNextGrammarState(Tag tag) {
        ArrayList<Production> partitionProductions = partition.get(tag);
        ArrayList<Production> nextStateProductions = new ArrayList<Production>();

        for (int i = 0; i < partitionProductions.size(); i++) {
            Production production =  partitionProductions.get(i);
            nextStateProductions.add(production.dotForward());
        }
        return stateManager.getGrammarState(nextStateProductions);
    }

    private void makeTransition() {
        for (Map.Entry<Tag, ArrayList<Production>> entry : partition.entrySet()) {
            GrammarState nextState = buildNextGrammarState(entry.getKey());
            transition.put(entry.getKey(), nextState);
            stateManager.addTransition(this, nextState, entry.getKey());
        }
        extendFollowingTransition();
    }

    private void extendFollowingTransition() {
        for (Map.Entry<Tag, GrammarState> entry: transition.entrySet()) {
            GrammarState state = entry.getValue();
            if (!state.isTransitionDone()) {
                state.startTransition();
            }
        }
    }

    @Override
    public boolean equals(Object object) {
        return productionEquals(object, false);
    }

    public boolean productionEquals(Object object, boolean isPartial) {
        // isPartial[false]: check look ahead
        GrammarState state = (GrammarState)object;

        if (state.productions.size() != this.productions.size()) {
            return false;
        }

        int equalCount = 0;
        for (int i = 0; i < state.productions.size(); i++) {
            for (int j = 0; j < this.productions.size(); j++) {
                if (!isPartial) {
                    if (state.productions.get(i).equals(this.productions.get(j))) {
                        equalCount++;
                        break;
                    }
                } else {
                    if (state.productions.get(i).productionEquals(this.productions.get(j))) {
                        equalCount++;
                        break;
                    }
                }
            }
        }
        return equalCount == state.productions.size();
    }

    public void stateMerge(GrammarState state) {
        if (!this.productions.containsAll(state.productions)) {
            for (int i = 0; i < state.productions.size(); i++) {
                if (!this.productions.contains(state.productions.get(i)) && !mergedProduction.contains(state.productions.get(i))) {
                    mergedProduction.add(state.productions.get(i));
                }
            }
        }
    }
    public HashMap<Tag, Integer> makeReduce() {
        HashMap<Tag, Integer> map = new HashMap<>();
        reduce(map, this.productions);
        reduce(map, this.mergedProduction);
        return map;
    }

    private void reduce(HashMap<Tag, Integer> map, ArrayList<Production> productions) {
        for (int i = 0; i < productions.size(); i++) {
            if (productions.get(i).canBeReduce()) {
                ArrayList<Tag> lookAhead = productions.get(i).lookAhead;
                for (int j = 0; j < lookAhead.size(); j++) {
                    map.put(lookAhead.get(j), productions.get(i).getProductionNum());
                }
            }
        }
    }

}

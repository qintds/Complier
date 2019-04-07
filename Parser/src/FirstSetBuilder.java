import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class FirstSetBuilder {
    private HashMap<Tag, Symbol> symbolMap;
    private ArrayList<Symbol> symbols;
    // if there are some new changes, we need to run the process again
    private boolean needMoreRound = true;
    private static FirstSetBuilder self;

    public static FirstSetBuilder getInstance() {
        if (self == null) {
            self = new FirstSetBuilder();
        }
        return self;
    }

    private FirstSetBuilder() {
        symbolMap = GrammarInitializer.getInstance().symbolMap;
        symbols = GrammarInitializer.getInstance().symbols;
    }

    public boolean isSymbolNullable(Tag tag) {
        Symbol symbol = symbolMap.get(tag);
        if (symbol == null) {
            return false;
        }
        return symbol.isNullable;
    }

    public boolean isSymbolTerminal(Symbol symbol) {
        return Tag.isTerminal(symbol.value);
    }

    public boolean isSymbolTerminal(Tag tag) {
        return Tag.isTerminal(tag);
    }

    public void runFirstSet() {
        while (needMoreRound) {
            needMoreRound = false;

            Iterator<Symbol> iterator = symbols.iterator();
            while (iterator.hasNext()) {
                Symbol symbol = iterator.next();
                addSymbolFirstSet(symbol);
            }

        }
    }

    private void addSymbolFirstSet(Symbol symbol) {
        // if the left side is a terminal, the first set of a terminal is itself and has already finish this process when create this object
        if (isSymbolTerminal(symbol)) {
            return;
        }

        // left side is not a terminal, than we need to compute its right side
        // an non terminal may have a couple of productions
        for (int i = 0; i < symbol.productions.size(); i++) {
            // getLine every production in order
            ArrayList<Tag> production = symbol.productions.get(i);
            if (production.size() == 0) continue;

            // getLine symbol of this production in order
            for (int j = 0; j < production.size(); j++) {
                Tag currentTag = production.get(j);
                Symbol current = symbolMap.get(currentTag);
                if (!symbol.firstSet.containsAll(current.firstSet)) {
                    needMoreRound = true;

                    for (int k = 0; k < current.firstSet.size(); k++) {
                        if (!symbol.firstSet.contains(current.firstSet.get(k))) {
                            symbol.firstSet.add(current.firstSet.get(k));
                        }
                    }
                }
                if (!current.isNullable) break;
            }
        }
    }

    public ArrayList<Tag> getFirstSet(Tag tag) {
        return symbolMap.get(tag).firstSet;
    }
}

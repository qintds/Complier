import java.util.ArrayList;
// the productions of a non terminal
public class Symbol {
    public Tag value;
    public ArrayList<ArrayList<Tag>> productions;
    public ArrayList<Tag> firstSet = new ArrayList<Tag>();
    // public  ArrayList<Tag> followSet = new ArrayList<Tag>();
    // public  ArrayList<Tag> selectionSet = new ArrayList<Tag>();

    public boolean isNullable;

    public Symbol(boolean nullable, Tag symbolValue, ArrayList<ArrayList<Tag>> productions) {
        this.value = symbolValue;
        this.isNullable = nullable;
        this.productions = productions;

        if (Tag.isTerminal(symbolValue)) {
            firstSet.add(symbolValue);
        }
    }

    public void addProduction(ArrayList<Tag> production) {
        if (!productions.contains(production)) {
            productions.add(production);
        }
    }
}

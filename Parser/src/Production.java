import java.util.ArrayList;

// Single line of a production, support dot move
public class Production {
    public int dosPos = 0;
    public Tag left = Tag.NonTerminal;
    public ArrayList<Tag> right = null;
    public ArrayList<Tag> lookAhead = new ArrayList<Tag>();
    public int productionNum = -1;

    public int getProductionNum() {
        return productionNum;
    }

    public Production(int productionNum, Tag left, int dot, ArrayList<Tag> right) {
        this.productionNum = productionNum;
        this.left = left;
        this.dosPos = dot >= right.size() ? right.size() : dot;
        this.right = right;

        this.lookAhead.add(Tag.End);
    }

    private Production dotMove(int step) {
        Production production = new Production(productionNum, this.left, dosPos + step, this.right);

        production.lookAhead = new ArrayList<Tag>();
        for (int i = 0; i < this.lookAhead.size(); i++) {
            production.lookAhead.add(this.lookAhead.get(i));
        }

        return production;
    }
    public Production dotForward() {
        return dotMove(1);
    }

    public Production cloneSelf() {
        return dotMove(0);
    }

    public ArrayList<Tag> computeDotSymbolFirstSetOfBetaAndA() {
        // [A -> α.Bβ, a] [ B -> .γ, b]  b ∈ First(βa)
        ArrayList<Tag> betaSet = new ArrayList<>();
        for (int i = dosPos + 1; i < right.size(); i++) {
            betaSet.add(right.get(i));
        }

        //compute
        ArrayList<Tag> firstSet = new ArrayList<Tag>();

        if (betaSet.size() > 0) {
            for (int i = 0; i < betaSet.size(); i++) {
                ArrayList<Tag> firstSetBeta = FirstSetBuilder.getInstance().getFirstSet(betaSet.get(i));

                for (int j = 0; j < firstSetBeta.size(); j++) {
                    if (!firstSet.contains(firstSetBeta.get(j))) {
                        firstSet.add(firstSetBeta.get(j));
                    }
                }

                if (!FirstSetBuilder.getInstance().isSymbolNullable(betaSet.get(i))) {
                    break;
                }

                // all productions of beta contain nullable
                if (i == betaSet.size() -1) {
                    firstSet.addAll(lookAhead);
                }
            }
        } else {
            firstSet.addAll(lookAhead);
        }

        return firstSet;
    }

    public void addLookAheadSet(ArrayList<Tag> list) {
        lookAhead = list;
    }

    public Tag getDotSymbol() {
        if (dosPos >= right.size()) {
            return Tag.Unknown;
        }
        return right.get(dosPos);
    }

    @Override
    public boolean equals(Object object) {
        // production and look ahead set are both equal
        Production production = (Production)object;
         if (this.productionEquals(production) && this.lookAheadSetComparing(production) == 0) {
             return true;
         }
        return false;
    }

    public boolean coverUp(Production production) {
        // production is equal and look ahead set contain all the other one
        if (this.productionEquals(production) && this.lookAheadSetComparing(production) > 0) {
            return true;
        }
        return false;
    }

    public boolean productionEquals(Production production) {
        if (this.left != production.left) {
            return false;
        }
        if (!this.right.equals(production.right)){
            return false;
        }
        if (this.dosPos != production.dosPos) {
            return false;
        }
        return true;
    }

    public int lookAheadSetComparing(Production production) {
        // not equal will return -1
        // cover will return 1
        // same will return 0
        if (this.lookAhead.size() > production.lookAhead.size()) {
            for (int i = 0; i < production.lookAhead.size(); i++) {
                if (!this.lookAhead.contains(production.lookAhead.get(i))) {
                    return -1;
                }
            }
            return 1;
        }
        if (this.lookAhead.size() < production.lookAhead.size()) {
            return -1;
        }

        if (this.lookAhead.size() == production.lookAhead.size()) {
            for (int i = 0; i < this.lookAhead.size(); i++) {
                if (this.lookAhead.get(i) != production.lookAhead.get(i)){
                    return -1;
                }
            }
        }
        return 0;
    }

    public boolean canBeReduce() {
        return dosPos >= right.size();
    }

    public void printProductionOnly() {
        System.out.print(left.name()+" -> ");
        for (int i = 0; i < right.size(); i++) {
            System.out.print(right.get(i).name() + " ");
        }
        System.out.println();
    }
}

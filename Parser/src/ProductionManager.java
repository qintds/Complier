import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class ProductionManager {
    private static ProductionManager self = null;

    public HashMap<Tag, ArrayList<Production>> productionMap;

    private ProductionManager() {
        productionMap = GrammarInitializer.getInstance().productionMap;
    }

    public static ProductionManager getInstance() {
        if (self == null) {
            self = new ProductionManager();
        }

        return self;
    }

    public void runFirstSet() {
        FirstSetBuilder.getInstance().runFirstSet();
    }

    public Production getProductionByIndex(int index) {
        for (Entry<Tag, ArrayList<Production>> item : productionMap.entrySet()) {
            ArrayList<Production> productionList = item.getValue();
            for (int i = 0; i < productionList.size(); i++) {
                if (productionList.get(i).getProductionNum() == index) {
                    return productionList.get(i);
                }
            }
        }
        return null;
    }

    public ArrayList<Production> getProductionsByLeft(Tag left) {
        return productionMap.get(left);
    }

    public void printAllProductionOnly() {
        for (int i = 0; i < GrammarInitializer.getInstance().productionNum; i++) {
            getProductionByIndex(i).printProductionOnly();
        }
    }
}

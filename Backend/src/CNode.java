import java.util.ArrayList;
import java.util.Collections;

// 常量折叠 单对单文法收缩(会丢失信息)
public class CNode {
    public Tag tag;
    public CNode parent;
    public ArrayList<CNode> children;
    public GrammarEnum production;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setIdentifier(Word word) {
        if (word.tag == Tag.Identifier) {
            this.identifier = word.toString();
        }
    }

    public XObject getObject() {
        return object;
    }

    public void setObject(XObject object) {
        this.object = object;
    }

    public boolean isChildrenReverse() {
        return isChildrenReverse;
    }

    public void setChildrenReverse(boolean childrenReverse) {
        isChildrenReverse = childrenReverse;
    }

    // if tag is IDENTIFIER, set identifier
    private String identifier;
    private XObject object;
    private boolean isChildrenReverse = false;

    public CNode(Tag tag) {
        this.tag = tag;
        this.parent = null;
        this.children = new ArrayList<>();
    }

    public void addChild(CNode node) {
        if (node != null) {
            children.add(node);
            node.parent = this;
        }
    }

    public void reverseChildren() {
        if (isChildrenReverse == true) {
            return;
        }

        Collections.reverse(children);
        isChildrenReverse = true;
    }



}

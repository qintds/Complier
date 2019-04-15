import java.util.ArrayList;
import java.util.Collections;

// 常量折叠 单对单文法收缩(会丢失信息)
public class CNode {
    public Tag tag;
    public CNode parent;
    public ArrayList<CNode> children;
    public GrammarEnum production;

    // if tag is IDENTIFIER, set identifier
    private String identifier;
    private XObject xObject;
    private boolean isChildrenReverse = false;

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

    public XObject getXObject() {
        return xObject;
    }

    public void setXObject(XObject object) {
        this.xObject = object;
    }

    public boolean isChildrenReverse() {
        return isChildrenReverse;
    }

    public void setChildrenReverse(boolean childrenReverse) {
        isChildrenReverse = childrenReverse;
    }



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

    public CNode getChild(int i) {
        return children.get(i);
    }

    public void reverseChildren() {
        if (isChildrenReverse == true) {
            return;
        }

        Collections.reverse(children);
        isChildrenReverse = true;
    }



}

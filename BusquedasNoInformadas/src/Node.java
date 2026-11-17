public class Node {
    private String state;
    private Node parent;
    private int depth;
    private int cost;

    public Node(String state, Node parent) {
        this.state = state;
        this.parent = parent;
        this.depth = (parent == null) ? 0 : parent.getDepth() + 1;
        this.cost = (parent == null) ? 0 : parent.getCost() + 1;
    }

    public Node(String state, Node parent, int stepCost) {
        this.state = state;
        this.parent = parent;
        this.depth = (parent == null) ? 0 : parent.getDepth() + 1;
        this.cost = (parent == null) ? 0 : parent.getCost() + stepCost;
    }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public Node getParent() { return parent; }
    public void setParent(Node parent) { this.parent = parent; }
    public int getDepth() { return depth; }
    public void setDepth(int depth) { this.depth = depth; }
    public int getCost() { return cost; }
    public void setCost(int cost) { this.cost = cost; }
}
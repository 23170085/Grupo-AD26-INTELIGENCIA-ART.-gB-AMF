
import java.util.List;

public class App {
    public static void main(String[] args) throws Exception {
        
        // 3x3 = 9
        String initialState = "7621 3458"; // Random Initial State
        String goalState = "12345678"; // Goal State
        SearchTree searchTree = new SearchTree(initialState, goalState);
        searchTree.breadthFirstSearch();
        System.out.println("Solution found!");

        System.out.println("Initial State " + initialState);

        List<Node> children = NodeUtils.generateChildren(new Node(initialState, null));

        for (Node node : children) {
            System.out.println(node.getState());
        }

    }

}
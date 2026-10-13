
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class SearchTree {
    
    Node root;
    String initialState;
    String goalState;

    public SearchTree(String initialState, String goalState) {
        this.initialState = initialState;
        this.goalState = goalState;
        this.root = new Node(initialState, null);
    }

    public void breadthFirstSearch() {
        // Set to keep track of visited states to avoid cycles
        Set<String> visited = new HashSet<String>();
        // 1. Buscar el nodo raiz y agregarlo a la cola
        Node currentNode = root;
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        // 2. Mientras la cola no este vacia, hacer lo siguiente:
        while (!queue.isEmpty()) {
            // 3. Sacar el primer nodo de la cola y verificar si es el nodo objetivo. Si es asi, terminar la busqueda.
            currentNode = queue.poll();
            visited.add(currentNode.getState());
            System.out.println(NodeUtils.formatState(currentNode.getState()));
            if (currentNode.getState().equals(goalState)) {
                System.out.println("Goal state found: " + currentNode.getState());
                // print the path from the root to the goal state
                return;
            }
            // 4. Si no es el nodo objetivo, generar sus hijos y agregarlos a la cola.  
            List<Node> children = NodeUtils.generateChildren(currentNode);
            for (Node child : children) {
                if (!visited.contains(child.getState())) {
                    queue.add(child);
                }
            }
        }
    }
}

import java.util.LinkedList;
import java.util.Queue;

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
        // 1. Buscar el nodo raiz y agregarlo a la cola
        Node currentNode = root;
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        // 2. Mientras la cola no este vacia, hacer lo siguiente:
        while (!queue.isEmpty()) {
            // 3. Sacar el primer nodo de la cola y verificar si es el nodo objetivo. Si es asi, terminar la busqueda.
            currentNode = queue.poll();
            if (currentNode.getState().equals(goalState)) {
                System.out.println("Goal state found: " + currentNode.getState());
                return;
            }
            // 4. Si no es el nodo objetivo, generar sus hijos y agregarlos a la cola.  
        }
    }
}
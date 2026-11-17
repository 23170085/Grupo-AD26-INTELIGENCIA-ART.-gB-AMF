import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

public class SearchTree {
    
    Node root;
    String initialState;
    String goalState;
    private int dlsNodesExpanded; // Contador auxiliar para DLS/IDDFS

    public SearchTree(String initialState, String goalState) {
        this.initialState = initialState;
        this.goalState = goalState;
        this.root = new Node(initialState, null);
    }

    // 1. BUSQUEDA EN ANCHURA (BFS)
    public Node breadthFirstSearch() {
        long startTime = System.nanoTime();
        int nodesExpanded = 0;
        Set<String> visited = new HashSet<>();
        Queue<Node> queue = new LinkedList<>();
        
        queue.add(root);
        visited.add(root.getState());

        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();
            nodesExpanded++;

            if (currentNode.getState().equals(goalState)) {
                long duration = (System.nanoTime() - startTime) / 1_000_000;
                System.out.println("[BFS] Solucion encontrada | Nodos expandidos: " + nodesExpanded + " | Profundidad: " + currentNode.getDepth() + " | Tiempo: " + duration + " ms");
                return currentNode;
            }

            for (Node child : NodeUtils.generateChildren(currentNode)) {
                if (!visited.contains(child.getState())) {
                    visited.add(child.getState());
                    queue.add(child);
                }
            }
        }
        return null;
    }

    // 2. BUSQUEDA EN PROFUNDIDAD (DFS)
    public Node depthFirstSearch() {
        long startTime = System.nanoTime();
        int nodesExpanded = 0;
        Set<String> visited = new HashSet<>();
        Stack<Node> stack = new Stack<>();

        stack.push(root);

        while (!stack.isEmpty()) {
            Node currentNode = stack.pop();

            if (visited.contains(currentNode.getState())) {
                continue;
            }
            visited.add(currentNode.getState());
            nodesExpanded++;

            if (currentNode.getState().equals(goalState)) {
                long duration = (System.nanoTime() - startTime) / 1_000_000;
                System.out.println("[DFS] Solucion encontrada | Nodos expandidos: " + nodesExpanded + " | Profundidad: " + currentNode.getDepth() + " | Tiempo: " + duration + " ms");
                return currentNode;
            }

            List<Node> children = NodeUtils.generateChildren(currentNode);
            for (int i = children.size() - 1; i >= 0; i--) {
                Node child = children.get(i);
                if (!visited.contains(child.getState())) {
                    stack.push(child);
                }
            }
        }
        return null;
    }

    // 3. BUSQUEDA DE COSTO UNIFORME (UCS)
    public Node uniformCostSearch() {
        long startTime = System.nanoTime();
        int nodesExpanded = 0;
        Set<String> visited = new HashSet<>();
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(Node::getCost));

        priorityQueue.add(root);

        while (!priorityQueue.isEmpty()) {
            Node currentNode = priorityQueue.poll();

            if (visited.contains(currentNode.getState())) {
                continue;
            }
            visited.add(currentNode.getState());
            nodesExpanded++;

            if (currentNode.getState().equals(goalState)) {
                long duration = (System.nanoTime() - startTime) / 1_000_000;
                System.out.println("[UCS] Solucion encontrada | Nodos expandidos: " + nodesExpanded + " | Costo: " + currentNode.getCost() + " | Profundidad: " + currentNode.getDepth() + " | Tiempo: " + duration + " ms");
                return currentNode;
            }

            for (Node child : NodeUtils.generateChildren(currentNode)) {
                if (!visited.contains(child.getState())) {
                    priorityQueue.add(child);
                }
            }
        }
        return null;
    }

    // 4A. BUSQUEDA EN PROFUNDIDAD LIMITADA (DLS)
    public Node depthLimitedSearch(int limit) {
        this.dlsNodesExpanded = 0;
        Set<String> visitedInBranch = new HashSet<>();
        return recursiveDLS(root, limit, visitedInBranch);
    }

    private Node recursiveDLS(Node currentNode, int limit, Set<String> visitedInBranch) {
        dlsNodesExpanded++;

        if (currentNode.getState().equals(goalState)) {
            return currentNode;
        }

        if (currentNode.getDepth() >= limit) {
            return null;
        }

        visitedInBranch.add(currentNode.getState());

        for (Node child : NodeUtils.generateChildren(currentNode)) {
            if (!visitedInBranch.contains(child.getState())) {
                Node result = recursiveDLS(child, limit, visitedInBranch);
                if (result != null) {
                    return result;
                }
            }
        }

        visitedInBranch.remove(currentNode.getState());
        return null;
    }

    // 4B. BUSQUEDA ITERATIVA (IDDFS - Combina con Limitada)
    public Node iterativeDeepeningSearch() {
        long startTime = System.nanoTime();
        int totalNodesExpanded = 0;
        int maxDepth = 50; // Limite maximo de seguridad

        for (int limit = 0; limit <= maxDepth; limit++) {
            Node result = depthLimitedSearch(limit);
            totalNodesExpanded += this.dlsNodesExpanded;

            if (result != null) {
                long duration = (System.nanoTime() - startTime) / 1_000_000;
                System.out.println("[IDDFS] Solucion encontrada | Limite alcanzado: " + limit + " | Nodos expandidos totales: " + totalNodesExpanded + " | Profundidad: " + result.getDepth() + " | Tiempo: " + duration + " ms");
                return result;
            }
        }
        System.out.println("[IDDFS] No se encontro solucion dentro del limite maximo.");
        return null;
    }
}
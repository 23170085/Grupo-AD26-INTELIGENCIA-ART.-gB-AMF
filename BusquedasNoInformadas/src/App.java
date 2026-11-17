public class App {
    public static void main(String[] args) {
        
        // Estado inicial y objetivo (9 caracteres: 8 numeros + 1 espacio)
        String initialState = "7621 3458"; 
        String goalState = "12345678 "; // Corregido con el espacio al final

        System.out.println("=== 8-PUZZLE: BUSQUEDAS NO INFORMADAS ===");
        System.out.println("Estado Inicial:\n" + NodeUtils.formatState(initialState));
        System.out.println("Estado Objetivo:\n" + NodeUtils.formatState(goalState));
        System.out.println("Ejecutando algoritmos...\n");

        SearchTree searchTree = new SearchTree(initialState, goalState);

        // 1. Anchura (BFS)
        Node bfsResult = searchTree.breadthFirstSearch();

        // 2. Profundidad (DFS)
        Node dfsResult = searchTree.depthFirstSearch();

        // 3. Costo Uniforme (UCS)
        Node ucsResult = searchTree.uniformCostSearch();

        // 4. Iterativa combinada con Limitada (IDDFS)
        Node iddfsResult = searchTree.iterativeDeepeningSearch();

        // Imprimir solucion optima encontrada (usando BFS o UCS)
        if (bfsResult != null) {
            System.out.println("\n=== CAMINO DE LA SOLUCION OPTIMA (BFS / UCS) ===");
            NodeUtils.printSolutionPath(bfsResult);
        }

        // 6. TABLA COMPARATIVA DE COMPLEJIDAD ALGORITMICA
        printComplexityTable(bfsResult, dfsResult, ucsResult, iddfsResult);
    }

    private static void printComplexityTable(Node bfs, Node dfs, Node ucs, Node iddfs) {
        System.out.println("\n=========================================================================================================");
        System.out.println("                         TABLA COMPARATIVA DE COMPLEJIDAD ALGORITMICA (***IMPORTANTE)                    ");
        System.out.println("=========================================================================================================");
        System.out.printf("%-28s | %-12s | %-12s | %-12s | %-12s | %-15s%n", 
                "Algoritmo", "Tiempo", "Espacio", "Completo", "Optimo", "Pasos (Prof.)");
        System.out.println("---------------------------------------------------------------------------------------------------------");
        
        System.out.printf("%-28s | %-12s | %-12s | %-12s | %-12s | %-15s%n", 
                "1. Anchura (BFS)", "O(b^d)", "O(b^d)", "Si*", "Si**", 
                (bfs != null ? bfs.getDepth() : "N/A"));

        System.out.printf("%-28s | %-12s | %-12s | %-12s | %-12s | %-15s%n", 
                "2. Profundidad (DFS)", "O(b^m)", "O(b * m)", "No", "No", 
                (dfs != null ? dfs.getDepth() : "N/A"));

        System.out.printf("%-28s | %-12s | %-12s | %-12s | %-12s | %-15s%n", 
                "3. Costo Uniforme (UCS)", "O(b^(1+C*/e))", "O(b^(1+C*/e))", "Si*", "Si", 
                (ucs != null ? ucs.getDepth() : "N/A"));

        System.out.printf("%-28s | %-12s | %-12s | %-12s | %-12s | %-15s%n", 
                "4. Limitada (DLS)", "O(b^l)", "O(b * l)", "No (si l<d)", "No", 
                "Depende de l");

        System.out.printf("%-28s | %-12s | %-12s | %-12s | %-12s | %-15s%n", 
                "4. Iterativa (IDDFS)", "O(b^d)", "O(b * d)", "Si*", "Si**", 
                (iddfs != null ? iddfs.getDepth() : "N/A"));
        
        System.out.println("=========================================================================================================");
        System.out.println("Nomenclatura:");
        System.out.println("  b = Factor de ramificacion (~2.67 en el 8-puzzle)");
        System.out.println("  d = Profundidad de la solucion menos costosa");
        System.out.println("  m = Profundidad maxima del arbol de estados (puede ser infinita)");
        System.out.println("  l = Limite de profundidad establecido en DLS");
        System.out.println("  C* = Costo de la solucion optima | e = Costo minimo de cada paso (> 0)");
        System.out.println("  * Completo si b es finito. | ** Optimo si todos los pasos tienen el mismo costo.");
        System.out.println("=========================================================================================================");
    }
}
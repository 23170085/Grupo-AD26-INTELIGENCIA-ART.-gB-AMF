import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NodeUtils {

    private static String swapPositions(String state, int pos1, int pos2) {
        char[] arr = state.toCharArray();
        char temp = arr[pos1];
        arr[pos1] = arr[pos2];
        arr[pos2] = temp;
        return new String(arr);
    }

    public static List<Node> generateChildren(Node parentNode) {
        List<Node> successors = new ArrayList<>();
        int zeroPos = parentNode.getState().indexOf(" ");

        int[][] adjacentPositions = {
            {1, 3},           // pos 0
            {0, 2, 4},        // pos 1
            {1, 5},           // pos 2
            {0, 4, 6},        // pos 3
            {1, 3, 5, 7},     // pos 4
            {2, 4, 8},        // pos 5
            {3, 7},           // pos 6
            {4, 6, 8},        // pos 7
            {5, 7}            // pos 8
        };

        for (int adjPos : adjacentPositions[zeroPos]) {
            String newState = swapPositions(parentNode.getState(), zeroPos, adjPos);
            successors.add(new Node(newState, parentNode, 1));
        }

        return successors;
    }

    public static String formatState(String state) {
        StringBuilder formattedState = new StringBuilder();
        for (int i = 0; i < state.length(); i++) {
            formattedState.append(state.charAt(i)).append(" ");
            if ((i + 1) % 3 == 0) {
                formattedState.append("\n");
            }
        }
        return formattedState.toString();
    }

    public static void printSolutionPath(Node goalNode) {
        List<Node> path = new ArrayList<>();
        Node current = goalNode;
        while (current != null) {
            path.add(current);
            current = current.getParent();
        }
        Collections.reverse(path);
        System.out.println("Pasos totales: " + (path.size() - 1));
        for (Node step : path) {
            System.out.println(formatState(step.getState()));
        }
    }
}
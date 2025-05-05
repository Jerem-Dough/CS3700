import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class router {

    static final int MAX = Integer.MAX_VALUE;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int n = 0;

        while (n < 2) {
            System.out.println("Enter number of routers: ");
            try {
                n = Integer.parseInt(input.nextLine().trim());
                if (n < 2) System.out.println("Enter a value greater than or equal to 2!");
            } catch (Exception e) {
                System.out.println("Input is not an Integer.");
            }
        }

        int[][] costMatrix = new int[n][n];
        for (int[] row : costMatrix) Arrays.fill(row, MAX);

        while (true) {
            System.out.println("Enter cost input file (e.g., topo.txt): ");
            String file = input.nextLine().trim();

            try (Scanner sc = new Scanner(new File(file))) {
                int line = 0;
                while (sc.hasNextLine()) {
                    line++;
                    String[] parts = sc.nextLine().trim().split("\\s+");
                    if (parts.length != 3) throw new Exception();

                    int u = Integer.parseInt(parts[0]);
                    int v = Integer.parseInt(parts[1]);
                    int cost = Integer.parseInt(parts[2]);

                    if (u < 0 || u >= n || v < 0 || v >= n || cost <= 0) {
                        System.out.println("Invalid on line " + line);
                        throw new Exception();
                    }

                    System.out.println("Parsed: " + u + " -> " + v + " cost: " + cost);
                    costMatrix[u][v] = cost;
                }
                break;
            } catch (Exception e) {
                System.out.println("Invalid file or format. Try again.");
            }
        }

        boolean[] visited = new boolean[n];
        int[] distance = new int[n];
        int[] previous = new int[n];

        Arrays.fill(distance, MAX);
        Arrays.fill(previous, -1);
        distance[0] = 0;

        Map<Integer, Integer> visitedMap = new LinkedHashMap<>();
        visitedMap.put(0, 0);
        showStatus(visitedMap, distance, previous);

        for (int count = 0; count < n - 1; count++) {
            int u = -1, min = MAX;

            for (int i = 0; i < n; i++) {
                if (!visited[i] && distance[i] < min) {
                    min = distance[i];
                    u = i;
                }
            }

            if (u == -1) break;
            visited[u] = true;
            visitedMap.put(u, distance[u]);

            for (int v = 0; v < n; v++) {
                if (!visited[v] && costMatrix[u][v] != MAX && distance[u] + costMatrix[u][v] < distance[v]) {
                    distance[v] = distance[u] + costMatrix[u][v];
                    previous[v] = u;
                }
            }

            showStatus(visitedMap, distance, previous);
        }

        System.out.println("\nForwarding Table (from V0):");
        System.out.println("Destination\tLink");

        for (int dest = 1; dest < n; dest++) {
            int hop = dest;
            while (previous[hop] != 0 && previous[hop] != -1) {
                hop = previous[hop];
            }
            if (previous[dest] == -1) {
                System.out.printf("V%d\t\tUnreachable\n", dest);
            } else {
                System.out.printf("V%d\t\t(V0,V%d)\n", dest, hop);
            }
        }
    }

    private static void showStatus(Map<Integer, Integer> visitedMap, int[] distance, int[] previous) {
        System.out.println("\nN': " + getVisitedNodes(visitedMap));
        System.out.print("D: ");
        for (int i = 1; i < distance.length; i++) {
            String d = (distance[i] == MAX) ? "INF" : Integer.toString(distance[i]);
            System.out.print("D(V" + i + ")=" + d + " ");
        }
        System.out.print("\nP: ");
        for (int i = 1; i < previous.length; i++) {
            String p = (previous[i] == -1) ? "-" : "V" + previous[i];
            System.out.print("P(V" + i + ")=" + p + " ");
        }
        System.out.println();
    }

    private static String getVisitedNodes(Map<Integer, Integer> visitedMap) {
        List<String> list = new ArrayList<>();
        for (int i : visitedMap.keySet()) list.add("V" + i);
        return list.toString();
    }
}

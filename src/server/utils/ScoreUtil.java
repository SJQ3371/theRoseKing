package server.utils;

import java.util.ArrayList;
import java.util.List;

public class ScoreUtil {

    public static int getScore(int[][] array, int value) {
        List<List<Integer>> list = findConnectedIndices(array, value);
        int score = 0;
        for (List<Integer> integers : list) {
            int linkCount = integers.size() / 2;
            score += linkCount * linkCount;
        }
        return score;
    }

    public static void main(String[] args) {
        System.out.println(findConnectedIndices(new int[][]{
                {2, 1, 1},
                {2, 1, 2}
        }, 2));
    }

    private static List<List<Integer>> findConnectedIndices(int[][] array, int value) {
        int rows = array.length;
        int cols = array[0].length;
        boolean[][] visited = new boolean[rows][cols];
        List<List<Integer>> connectedIndices = new ArrayList<>();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (array[i][j] == value && !visited[i][j]) {
                    List<Integer> indices = new ArrayList<>();
                    dfs(array, visited, i, j, indices,value);
                    connectedIndices.add(indices);
                }
            }
        }

        return connectedIndices;
    }

    private static void dfs(int[][] array, boolean[][] visited, int row, int col, List<Integer> indices, int val) {
        int rows = array.length;
        int cols = array[0].length;

        if (row < 0 || row >= rows || col < 0 || col >= cols || array[row][col] != val || visited[row][col]) {
            return;
        }

        visited[row][col] = true;
        indices.add(row);
        indices.add(col);

        dfs(array, visited, row - 1, col, indices, val); // 上
        dfs(array, visited, row + 1, col, indices, val); // 下
        dfs(array, visited, row, col - 1, indices, val); // 左
        dfs(array, visited, row, col + 1, indices, val); // 右
    }

}

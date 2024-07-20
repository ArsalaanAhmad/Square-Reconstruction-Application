package org.example.magicsquare;

import java.util.Random;

public class MagicSquareGenerator {
    public static int[][] generateMagicSquare(int n) {
        int[][] magicArray = new int[n][n];
        int x = 0;
        int y = n / 2;
        magicArray[x][y] = 1;

        for (int i = 2; i <= n * n; i++) {
            int nextX = (x - 1 + n) % n;
            int nextY = (y - 1 + n) % n;

            if (magicArray[nextX][nextY] != 0) {
                x = (x + 1) % n;
            } else {
                x = nextX;
                y = nextY;
            }
            magicArray[x][y] = i;
        }
        return magicArray;
    }

    public static void shuffleMagicSquare(int[][] magicArray) {
        int n = magicArray.length;
        Random random = new Random();

        for (int count = 0; count < n * n; count++) {
            int randomX1 = random.nextInt(n);
            int randomY1 = random.nextInt(n);

            int randomX2, randomY2;
            do {
                randomX2 = random.nextInt(n);
                randomY2 = random.nextInt(n);
            } while (isOnDiagonal(randomX1, randomY1, randomX2, randomY2));

            int temp = magicArray[randomX1][randomY1];
            magicArray[randomX1][randomY1] = magicArray[randomX2][randomY2];
            magicArray[randomX2][randomY2] = temp;
        }
    }

    private static boolean isOnDiagonal(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) == Math.abs(y1 - y2);
    }

    public static void swap(int[][] array, int i1, int j1, int i2, int j2) {
        if (i2 >= 0 && i2 < array.length && j2 >= 0 && j2 < array[0].length) {
            int temp = array[i1][j1];
            array[i1][j1] = array[i2][j2];
            array[i2][j2] = temp;
        }
    }

    public static boolean isValidMagicSquare(int[][] matrix) {
        int n = matrix.length;
        int magicSum = n * (n * n + 1) / 2;

        // Check rows and columns
        for (int i = 0; i < n; i++) {
            int rowSum = 0;
            int colSum = 0;
            for (int j = 0; j < n; j++) {
                rowSum += matrix[i][j];
                colSum += matrix[j][i];
            }
            if (rowSum != magicSum || colSum != magicSum) {
                return false;
            }
        }

        // Check diagonals
        int diag1Sum = 0;
        int diag2Sum = 0;
        for (int i = 0; i < n; i++) {
            diag1Sum += matrix[i][i];
            diag2Sum += matrix[i][n - 1 - i];
        }
        if (diag1Sum != magicSum || diag2Sum != magicSum) {
            return false;
        }

        return true;
    }

    public static int[] getSums(int[][] matrix) {
        int n = matrix.length;
        int[] sums = new int[2 * n + 2];

        // Calculate row sums and column sums
        for (int i = 0; i < n; i++) {
            int rowSum = 0;
            int colSum = 0;
            for (int j = 0; j < n; j++) {
                rowSum += matrix[i][j];
                colSum += matrix[j][i];
            }
            sums[i] = rowSum;
            sums[n + i] = colSum;
        }

        // Calculate diagonal sums
        int diag1Sum = 0;
        int diag2Sum = 0;
        for (int i = 0; i < n; i++) {
            diag1Sum += matrix[i][i];
            diag2Sum += matrix[i][n - 1 - i];
        }
        sums[2 * n] = diag1Sum;
        sums[2 * n + 1] = diag2Sum;

        return sums;
    }
}

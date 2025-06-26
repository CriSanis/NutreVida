package com.nutrevida.app;

import java.util.Random;

public class TetrisPiece {
    private int[][] shape;
    private int color;

    // Definir las formas de las piezas Tetris
    private static final int[][][] PIECES = {
            // Cuadrado
            {{1, 1}, {1, 1}},

            // Línea horizontal
            {{1, 1, 1}},

            // Línea vertical
            {{1}, {1}, {1}},

            // L shape
            {{1, 0}, {1, 0}, {1, 1}},

            // L invertida
            {{0, 1}, {0, 1}, {1, 1}},

            // T shape
            {{1, 1, 1}, {0, 1, 0}},

            // Z shape
            {{1, 1, 0}, {0, 1, 1}},

            // S shape
            {{0, 1, 1}, {1, 1, 0}},

            // Línea larga
            {{1, 1, 1, 1}},

            // Línea vertical larga
            {{1}, {1}, {1}, {1}},

            // Cuadrado grande
            {{1, 1, 1}, {1, 1, 1}, {1, 1, 1}},

            // Cruz
            {{0, 1, 0}, {1, 1, 1}, {0, 1, 0}},

            // Esquina
            {{1, 0}, {1, 1}},

            // Esquina invertida
            {{0, 1}, {1, 1}},

            // Pieza en L grande
            {{1, 0, 0}, {1, 0, 0}, {1, 1, 1}},

            // Pieza en L invertida grande
            {{0, 0, 1}, {0, 0, 1}, {1, 1, 1}},

            // Pieza individual
            {{1}},

            // Línea de 2
            {{1, 1}},

            // Línea vertical de 2
            {{1}, {1}},

            // Pieza en T invertida
            {{0, 1, 0}, {1, 1, 1}},

            // Pieza zigzag pequeña
            {{1, 0}, {1, 1}},

            // Pieza zigzag invertida pequeña
            {{0, 1}, {1, 1}}
    };

    public TetrisPiece(int[][] shape, int color) {
        this.shape = new int[shape.length][shape[0].length];
        for (int i = 0; i < shape.length; i++) {
            System.arraycopy(shape[i], 0, this.shape[i], 0, shape[i].length);
        }
        this.color = color;
    }

    public static TetrisPiece generateRandomPiece(Random random) {
        int pieceIndex = random.nextInt(PIECES.length);
        int color = random.nextInt(7) + 1; // Colores del 1 al 7
        return new TetrisPiece(PIECES[pieceIndex], color);
    }

    public int[][] getShape() {
        return shape;
    }

    public int getColor() {
        return color;
    }

    public TetrisPiece rotate() {
        int rows = shape.length;
        int cols = shape[0].length;
        int[][] rotated = new int[cols][rows];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                rotated[j][rows - 1 - i] = shape[i][j];
            }
        }

        return new TetrisPiece(rotated, color);
    }

    public int getWidth() {
        return shape[0].length;
    }

    public int getHeight() {
        return shape.length;
    }

    public boolean isEmpty() {
        for (int[] row : shape) {
            for (int cell : row) {
                if (cell == 1) {
                    return false;
                }
            }
        }
        return true;
    }
}
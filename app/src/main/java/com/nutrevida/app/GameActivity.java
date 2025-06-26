package com.nutrevida.app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    private static final int GRID_SIZE = 10;
    private static final int PREVIEW_SIZE = 4;

    private GridLayout gameGrid;
    private GridLayout previewGrid1, previewGrid2, previewGrid3;
    private TextView scoreText;
    private Button resetButton;

    private int[][] gameBoard = new int[GRID_SIZE][GRID_SIZE];
    private List<TetrisPiece> currentPieces = new ArrayList<>();
    private int score = 0;
    private Random random = new Random();

    private TetrisPiece selectedPiece = null;
    private int selectedPieceIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        initializeViews();
        initializeGame();
        generateNewPieces();
        updateDisplay();
    }

    private void initializeViews() {
        gameGrid = findViewById(R.id.gameGrid);
        previewGrid1 = findViewById(R.id.previewGrid1);
        previewGrid2 = findViewById(R.id.previewGrid2);
        previewGrid3 = findViewById(R.id.previewGrid3);
        scoreText = findViewById(R.id.scoreText);
        resetButton = findViewById(R.id.resetButton);

        setupGameGrid();
        setupPreviewGrids();

        resetButton.setOnClickListener(v -> resetGame());
    }

    private void setupGameGrid() {
        gameGrid.setColumnCount(GRID_SIZE);
        gameGrid.setRowCount(GRID_SIZE);

        for (int i = 0; i < GRID_SIZE * GRID_SIZE; i++) {
            View cell = new View(this);
            cell.setBackgroundResource(R.drawable.grid_cell_empty);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 100;
            params.height = 100;
            params.setMargins(2, 2, 2, 2);
            cell.setLayoutParams(params);

            final int row = i / GRID_SIZE;
            final int col = i % GRID_SIZE;

            cell.setOnClickListener(v -> onGridCellClick(row, col));
            gameGrid.addView(cell);
        }
    }

    private void setupPreviewGrids() {
        setupSinglePreviewGrid(previewGrid1, 0);
        setupSinglePreviewGrid(previewGrid2, 1);
        setupSinglePreviewGrid(previewGrid3, 2);
    }

    private void setupSinglePreviewGrid(GridLayout grid, final int pieceIndex) {
        grid.setColumnCount(PREVIEW_SIZE);
        grid.setRowCount(PREVIEW_SIZE);

        for (int i = 0; i < PREVIEW_SIZE * PREVIEW_SIZE; i++) {
            View cell = new View(this);
            cell.setBackgroundResource(R.drawable.grid_cell_empty);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 60;
            params.height = 60;
            params.setMargins(1, 1, 1, 1);
            cell.setLayoutParams(params);

            grid.addView(cell);
        }

        grid.setOnClickListener(v -> onPreviewPieceClick(pieceIndex));
    }

    private void initializeGame() {
        // Inicializar tablero vacío
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                gameBoard[i][j] = 0;
            }
        }
        score = 0;
    }

    private void generateNewPieces() {
        currentPieces.clear();

        for (int i = 0; i < 3; i++) {
            currentPieces.add(TetrisPiece.generateRandomPiece(random));
        }
    }

    private void onPreviewPieceClick(int pieceIndex) {
        if (pieceIndex < currentPieces.size()) {
            selectedPiece = currentPieces.get(pieceIndex);
            selectedPieceIndex = pieceIndex;

            // Actualizar visualización para mostrar pieza seleccionada
            updatePreviewGrids();
            Toast.makeText(this, "Pieza seleccionada. Toca el tablero para colocarla.", Toast.LENGTH_SHORT).show();
        }
    }

    private void onGridCellClick(int row, int col) {
        if (selectedPiece == null) {
            Toast.makeText(this, "Selecciona una pieza primero", Toast.LENGTH_SHORT).show();
            return;
        }

        if (canPlacePiece(selectedPiece, row, col)) {
            placePiece(selectedPiece, row, col);
            currentPieces.remove(selectedPieceIndex);
            selectedPiece = null;
            selectedPieceIndex = -1;

            checkAndClearLines();
            updateDisplay();

            if (currentPieces.isEmpty()) {
                generateNewPieces();
                updatePreviewGrids();
            }

            if (isGameOver()) {
                Toast.makeText(this, "¡Juego terminado! Puntuación: " + score, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "No se puede colocar la pieza aquí", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean canPlacePiece(TetrisPiece piece, int startRow, int startCol) {
        int[][] shape = piece.getShape();

        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] == 1) {
                    int newRow = startRow + i;
                    int newCol = startCol + j;

                    if (newRow < 0 || newRow >= GRID_SIZE || newCol < 0 || newCol >= GRID_SIZE) {
                        return false;
                    }

                    if (gameBoard[newRow][newCol] != 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void placePiece(TetrisPiece piece, int startRow, int startCol) {
        int[][] shape = piece.getShape();

        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] == 1) {
                    gameBoard[startRow + i][startCol + j] = piece.getColor();
                }
            }
        }
    }

    private void checkAndClearLines() {
        List<Integer> linesToClear = new ArrayList<>();

        // Verificar filas
        for (int i = 0; i < GRID_SIZE; i++) {
            boolean fullRow = true;
            for (int j = 0; j < GRID_SIZE; j++) {
                if (gameBoard[i][j] == 0) {
                    fullRow = false;
                    break;
                }
            }
            if (fullRow) {
                linesToClear.add(i);
            }
        }

        // Verificar columnas
        for (int j = 0; j < GRID_SIZE; j++) {
            boolean fullCol = true;
            for (int i = 0; i < GRID_SIZE; i++) {
                if (gameBoard[i][j] == 0) {
                    fullCol = false;
                    break;
                }
            }
            if (fullCol) {
                // Limpiar columna
                for (int i = 0; i < GRID_SIZE; i++) {
                    gameBoard[i][j] = 0;
                }
                score += 100;
            }
        }

        // Limpiar filas
        for (int row : linesToClear) {
            for (int j = 0; j < GRID_SIZE; j++) {
                gameBoard[row][j] = 0;
            }
            score += 100;
        }

        if (!linesToClear.isEmpty()) {
            Toast.makeText(this, "¡Línea(s) completada(s)! +" + (linesToClear.size() * 100) + " puntos", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isGameOver() {
        // El juego termina si no se puede colocar ninguna de las piezas actuales
        for (TetrisPiece piece : currentPieces) {
            for (int i = 0; i < GRID_SIZE; i++) {
                for (int j = 0; j < GRID_SIZE; j++) {
                    if (canPlacePiece(piece, i, j)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void updateDisplay() {
        updateGameGrid();
        updateScoreText();
    }

    private void updateGameGrid() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                View cell = gameGrid.getChildAt(i * GRID_SIZE + j);
                if (gameBoard[i][j] == 0) {
                    cell.setBackgroundResource(R.drawable.grid_cell_empty);
                } else {
                    cell.setBackgroundResource(getColorResource(gameBoard[i][j]));
                }
            }
        }
    }

    private void updatePreviewGrids() {
        updateSinglePreviewGrid(previewGrid1, 0);
        updateSinglePreviewGrid(previewGrid2, 1);
        updateSinglePreviewGrid(previewGrid3, 2);
    }

    private void updateSinglePreviewGrid(GridLayout grid, int pieceIndex) {
        // Limpiar grid
        for (int i = 0; i < PREVIEW_SIZE * PREVIEW_SIZE; i++) {
            View cell = grid.getChildAt(i);
            cell.setBackgroundResource(R.drawable.grid_cell_empty);
        }

        if (pieceIndex < currentPieces.size()) {
            TetrisPiece piece = currentPieces.get(pieceIndex);
            int[][] shape = piece.getShape();

            for (int i = 0; i < shape.length; i++) {
                for (int j = 0; j < shape[i].length; j++) {
                    if (shape[i][j] == 1) {
                        int cellIndex = i * PREVIEW_SIZE + j;
                        if (cellIndex < PREVIEW_SIZE * PREVIEW_SIZE) {
                            View cell = grid.getChildAt(cellIndex);
                            cell.setBackgroundResource(getColorResource(piece.getColor()));
                        }
                    }
                }
            }

            // Resaltar si está seleccionada
            if (selectedPieceIndex == pieceIndex) {
                grid.setBackgroundResource(R.drawable.selected_piece_background);
            } else {
                grid.setBackground(null);
            }
        }
    }

    private void updateScoreText() {
        scoreText.setText("Puntuación: " + score);
    }

    private int getColorResource(int colorCode) {
        switch (colorCode) {
            case 1: return R.drawable.grid_cell_blue;
            case 2: return R.drawable.grid_cell_red;
            case 3: return R.drawable.grid_cell_green;
            case 4: return R.drawable.grid_cell_yellow;
            case 5: return R.drawable.grid_cell_purple;
            case 6: return R.drawable.grid_cell_orange;
            case 7: return R.drawable.grid_cell_cyan;
            default: return R.drawable.grid_cell_empty;
        }
    }

    private void resetGame() {
        initializeGame();
        generateNewPieces();
        selectedPiece = null;
        selectedPieceIndex = -1;
        updateDisplay();
        updatePreviewGrids();
        Toast.makeText(this, "Juego reiniciado", Toast.LENGTH_SHORT).show();
    }
}
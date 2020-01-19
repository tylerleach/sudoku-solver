package com.mycompany.guisudoku;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;


public class MainApp extends Application {
    public static TextField[][] grid; // Grid of sudoku boxes
    public static int[][] preSolveGrid; // Grid of pre solution sudoku board (Makes it easier to load up automatically)
    
    // Solves the sudoku board
    public boolean solve() {
        int row;
        int column;
        
        int[] blankCell = findBlankSquare();
        row = blankCell[0];
        column = blankCell[1];
        
        if(row == -1)
        {
            return true;
        }
        
        for(int i = 1; i <= 9; i++)
        {
            if(isSafe(row, column, i))
            {
                grid[row][column].setText(String.valueOf(i));
                
                if(solve())
                {
                    return true;
                }
                
                grid[row][column].setText("");
            }
        }
        
        return false;
    }
    
    // Checks to see if it is safe to place a number in a position
    public boolean isSafe(int row, int column, int n) {
        return !usedInRow(row, n) && !usedInColumn(column, n) && !usedInBox(row - row % 3, column - column %3, n);
    }
    
    // Check if n is already used in row
    public boolean usedInRow(int row, int n) {
        for(int i = 0; i < 9; i++)
        {
            if(grid[row][i].getText().equals(String.valueOf(n)))
            {
                return true;
            }
        }
        
        return false;
    }
    
    // Check if n is already used in column
    public boolean usedInColumn(int column, int n) {
        for(int i = 0; i < 9; i++)
        {
            if(grid[i][column].getText().equals(String.valueOf(n)))
            {
                return true;
            }
        }
        
        return false;
    }
    
    // Check if n is already used in box
    public boolean usedInBox(int boxRow, int boxCol, int n) {
        for(int i = 0; i < 3; i++)
        {
            for(int j = 0; j < 3; j++)
            {
                if(grid[boxRow + i][boxCol + j].getText().equals(String.valueOf(n)))
                {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    public int[] findBlankSquare() {
        int[] cell = new int[2];
        
        for(int i = 0; i < 9; i++)
        {
            for(int j = 0; j < 9; j++)
            {
                if(grid[i][j].getText().equals(""))
                {
                    cell[0] = i;
                    cell[1] = j;
                    
                    return cell;
                }
            }
        }
        
        cell[0] = -1; // Means no blank locations
        cell[1] = -1;
        return cell;
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Scene.fxml"));
        
        GridPane gridPane = new GridPane();
        
        preSolveGrid = new int[][] { 
            {0,0,2, 0,1,0, 0,0,0},
            {0,0,0, 9,4,0, 7,0,0},
            {5,0,0, 2,0,8, 0,0,0},
            {6,1,0, 0,0,0, 9,5,0},
            {0,0,3, 0,0,0, 0,0,0},
            {0,4,7, 0,0,0, 0,3,1},
            {0,0,0, 5,0,1, 0,0,7},
            {0,0,6, 0,7,2, 0,0,0},
            {0,0,0, 0,9,0, 8,0,0},
        };
        
        grid = new TextField[9][9];
        
        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                TextField tf = new TextField();
                tf.setAlignment(Pos.CENTER);
                tf.setPrefHeight(60);
                tf.setPrefWidth(60);

                grid[i][j] = tf;
                
                if(preSolveGrid[i][j] != 0)
                    grid[i][j].setText(String.valueOf(preSolveGrid[i][j]));
                
                gridPane.add(tf, j + 1, i);
            }
        }
        
        Button solveButton = new Button("Solve");
        solveButton.setPrefSize(60, 60);
        gridPane.add(solveButton, 10, 0);
        
        solveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                solve();
            }
        });
        
        Scene scene = new Scene(gridPane, 600, 540);
        scene.getStylesheets().add("/styles/Styles.css");
        
        stage.setTitle("JavaFX and Maven");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}

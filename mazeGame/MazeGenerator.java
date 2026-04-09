package mazeGame;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class MazeGenerator
{
    // ------------------------------------------------------------

    private class Cell
    {
        int row;
        int col;
        boolean visited = false;

        boolean top = true;
        boolean right = true;
        boolean bottom = true;
        boolean left = true;

        public Cell(int row, int col)
        {
            this.row = row;
            this.col = col;
        }
    }

    // ------------------------------------------------------------

    private int rows;
    private int cols;
    private int cellSize;
    private int wallThickness;

    private int startX;
    private int startY;

    private Cell[][] grid;
    private ArrayList<Rect> walls = new ArrayList<>();
    private Random rand = new Random();

    // ------------------------------------------------------------

    public MazeGenerator(int rows, int cols, int cellSize, int wallThickness, int startX, int startY)
    {
        this.rows = rows;
        this.cols = cols;
        this.cellSize = cellSize;
        this.wallThickness = wallThickness;
        this.startX = startX;
        this.startY = startY;

        grid = new Cell[rows][cols];

        for (int r = 0; r < rows; r++)
        {
            for (int c = 0; c < cols; c++)
            {
                grid[r][c] = new Cell(r, c);
            }
        }

        generateMaze();
        addExtraConnections(50); // make the maze have more loops
        buildWalls();
    }

    // ------------------------------------------------------------

    private void generateMaze()
    {
        Stack<Cell> stack = new Stack<>();

        Cell current = grid[0][0];
        current.visited = true;

        while (true)
        {
            Cell next = getRandomUnvisitedNeighbor(current);

            if (next != null)
            {
                next.visited = true;
                stack.push(current);

                removeWalls(current, next);

                current = next;
            }
            else if (!stack.isEmpty())
            {
                current = stack.pop();
            }
            else
            {
                break;
            }
        }

        // entrance / exit
        grid[0][0].left = false;
        grid[rows - 1][cols - 1].right = false;
    }

    // ------------------------------------------------------------

    private void addExtraConnections(int attempts)
    {
        for (int i = 0; i < attempts; i++)
        {
            int r = rand.nextInt(rows);
            int c = rand.nextInt(cols);

            Cell cell = grid[r][c];

            int dir = rand.nextInt(2); // 0 = right, 1 = down

            if (dir == 0)
            {
                if (c < cols - 1)
                {
                    Cell right = grid[r][c + 1];
                    cell.right = false;
                    right.left = false;
                }
            }
            else
            {
                if (r < rows - 1)
                {
                    Cell down = grid[r + 1][c];
                    cell.bottom = false;
                    down.top = false;
                }
            }
        }
    }

    // ------------------------------------------------------------

    private Cell getRandomUnvisitedNeighbor(Cell cell)
    {
        ArrayList<Cell> neighbors = new ArrayList<>();

        int r = cell.row;
        int c = cell.col;

        // up
        if (r > 0 && !grid[r - 1][c].visited)
            neighbors.add(grid[r - 1][c]);

        // right
        if (c < cols - 1 && !grid[r][c + 1].visited)
            neighbors.add(grid[r][c + 1]);

        // down
        if (r < rows - 1 && !grid[r + 1][c].visited)
            neighbors.add(grid[r + 1][c]);

        // left
        if (c > 0 && !grid[r][c - 1].visited)
            neighbors.add(grid[r][c - 1]);

        if (neighbors.size() == 0)
            return null;

        return neighbors.get(rand.nextInt(neighbors.size()));
    }

    // ------------------------------------------------------------

    private void removeWalls(Cell a, Cell b)
    {
        int rowDiff = a.row - b.row;
        int colDiff = a.col - b.col;

        if (rowDiff == 1)
        {
            a.top = false;
            b.bottom = false;
        }
        else if (rowDiff == -1)
        {
            a.bottom = false;
            b.top = false;
        }
        else if (colDiff == 1)
        {
            a.left = false;
            b.right = false;
        }
        else if (colDiff == -1)
        {
            a.right = false;
            b.left = false;
        }
    }

    // ------------------------------------------------------------

    private void buildWalls()
    {
        walls.clear();

        for (int r = 0; r < rows; r++)
        {
            for (int c = 0; c < cols; c++)
            {
                Cell cell = grid[r][c];

                int x = startX + c * cellSize;
                int y = startY + r * cellSize;

                // top wall
                if (cell.top)
                {
                    walls.add(new Rect(x, y, cellSize, wallThickness));
                }

                // left wall
                if (cell.left)
                {
                    walls.add(new Rect(x, y, wallThickness, cellSize));
                }

                // bottom border
                if (r == rows - 1 && cell.bottom)
                {
                    walls.add(new Rect(x, y + cellSize, cellSize, wallThickness));
                }

                // right border
                if (c == cols - 1 && cell.right)
                {
                    walls.add(new Rect(x + cellSize, y, wallThickness, cellSize));
                }
            }
        }
    }

    // ------------------------------------------------------------

    public ArrayList<Rect> getWalls()
    {
        return walls;
    }

    // ------------------------------------------------------------

    public int getPixelWidth()
    {
        return cols * cellSize + wallThickness;
    }

    // ------------------------------------------------------------

    public int getPixelHeight()
    {
        return rows * cellSize + wallThickness;
    }

    // ------------------------------------------------------------

    public int getStartX()
    {
        return startX;
    }

    // ------------------------------------------------------------

    public int getStartY()
    {
        return startY;
    }

    // ------------------------------------------------------------

    public int getCellSize()
    {
        return cellSize;
    }

    // ------------------------------------------------------------
}
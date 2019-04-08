import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

//Rafael Chaves

public class GridClicking extends JPanel {
    private int rows = 16;
    private int cols = 30;
    private int[][] board;
    private boolean[][]revealed;
    private boolean[][]flags = new boolean[rows][cols];
    private boolean [][] wrong = new boolean[rows][cols];
    private int size;
    private int score, time, clicks = 0;
    private int x, y;
    private int numMines = 99;


    public GridClicking(int width, int height) {
        setSize(width, height);
        board = new int[rows][cols];
        revealed = new boolean[rows][cols];
        for (int r = 0; r < revealed.length; r++) {
            for (int c = 0; c < revealed[0].length; c++) {
                revealed[r][c] = false;
            }
        }
        size = 20;
        plantMines(numMines);
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[0].length; c++) {
                if (board[r][c] != -1)
                    board[r][c] = numMinesAround(r, c);
            }
        }
        setupMouseListener();
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                time++;
                repaint();
            }
        });
        timer.start();
    }

    public void plantMines(int num){
        for (int i = 0; i < num; i++) {
            int row = (int)(Math.random()*rows);
            int col = (int)(Math.random()*cols);
            if (board[row][col] != -1)
                board[row][col] = -1;
            else
                i--;
        }
    }
    int numMinesAround(int row, int col){
        int count = 0;
        if (row - 1 >= 0 && col - 1 > 0 && board[row - 1][col - 1] == -1) {
            count++;
        }
        if (row - 1 >= 0 && col >= 0 && board[row - 1][col] == -1) {
            count++;
        }
        if (row - 1 >= 0 && col + 1 < board[0].length && board[row - 1][col + 1] == -1) {
            count++;
        }
        if (row >= 0 && col + 1 < board[0].length && board[row][col + 1] == -1) {
            count++;
        }
        if (row + 1 < board.length && col + 1 < board[0].length && board[row + 1][col + 1] == -1) {
            count++;
        }
        if (row + 1 < board.length && col >= 0 && board[row + 1][col] == -1) {
            count++;
        }
        if (row + 1 < board.length && col - 1 >= 0 && board[row + 1][col - 1] == -1) {
            count++;
        }
        if (row >= 0 && col - 1 >= 0 && board[row][col - 1] == -1) {
            count++;
        }
        return count;
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[0].length; c++) {

                if (board[r][c] > 0)
                    g2.drawString("" + board[r][c], c * size + size / 3, r * size + size / 2);
                else if (board[r][c] == -1) {
                    g2.setColor(Color.red);
                    g2.fillRect(c * size, r * size, size, size);
                    g2.setColor(Color.black);
                }


                if (!revealed[r][c]) {
                    g2.setColor(Color.GRAY);
                    g2.fillRect(c * size, r * size, size, size);
                    g2.setColor(Color.BLACK);
                }
                g2.drawRect(c * size, r * size, size, size);
                if (flags[r][c]){
                    g2.setColor(Color.CYAN);
                    g2.fillRect(c * size, r * size, size, size);
                    g2.setColor(Color.BLACK);
                }
                if (wrong[r][c]) {
                    g2.setColor(Color.RED);
                    g2.drawLine(c * size, r * size, c * size + size, r * size + size);
                    g2.drawLine(c * size + size, r * size, c * size, r * size + size);
                    g2.setColor(Color.BLACK);
                }
                g2.setFont(new Font("LucidaGrande", Font.PLAIN, 30));
                g2.drawString("Mines Left: " + numMines, 20, 360);
                g2.drawString("Score: " + score, 250, 360);
                g2.drawString("Time: " + time, 450, 360);

                g2.setFont(new Font("LucidaGrande", Font.PLAIN, 13));

            }
        }

    }
    public void revealCell(int r, int c){
        if (r > -1 && c > -1 && r < board.length && c < board[0].length) {
            if (!revealed[r][c]){
                revealed[r][c] = true;
                if (!flags[r][c])
                    score++;
                if (board[r][c] == -1)
                    revealed[r][c] = false;
                if (board[r][c] == 0){
                    revealCell(r - 1, c - 1); //nw
                    revealCell(r - 1, c); //n
                    revealCell(r - 1, c + 1); //ne
                    revealCell(r, c - 1); //w
                    revealCell(r, c + 1); //e
                    revealCell(r + 1, c - 1); //sw
                    revealCell(r + 1, c); //s
                    revealCell(r + 1, c + 1); //sw

                }

            }

        }




    }


    public void setupMouseListener(){
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                x = e.getX();
                y = e.getY();

                int r = y / size;
                int c = x / size;

                clicks++;
                if (clicks == 1)
                    board[r][c] = numMinesAround(r, c);
                if (board[r][c] == -1 && !flags[r][c]) {
                    for (int j = 0; j < revealed.length; j++) {
                        for (int k = 0; k < revealed[0].length; k++) {
                            revealed[j][k] = true;
                            if (board[j][k] != -1 && flags[j][k]) {
                                wrong[j][k] = true;
                            }
                        }
                    }
                    System.out.println("You Lose!");
                }
                revealCell(r, c);
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {

            }

            @Override
            public void mouseMoved(MouseEvent e) {
                x = e.getX();
                y = e.getY();
            }
        });
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    if (!flags[y / size][x / size] && !revealed[y / size][x / size]) {
                        flags[y / size][x / size] = true;
                        numMines--;
                    } else if (flags[y / size][x / size] && !revealed[y / size][x / size]) {
                        flags[y / size][x / size] = false;
                        numMines++;
                    }
                }
                repaint();
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

    }


    //sets ups the panel and frame.  Probably not much to modify here.
    public static void main(String[] args) {
        JFrame window = new JFrame("Minesweeper");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setBounds(0, 0, 700, 400 + 22); //(x, y, w, h) 22 due to title bar.

        GridClicking panel = new GridClicking(600, 600);
        panel.setFocusable(true);
        panel.grabFocus();

        window.add(panel);
        window.setVisible(true);
        window.setResizable(true);
    }
}
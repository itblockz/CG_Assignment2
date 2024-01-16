import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;

import engine.Database;
import engine.GraphicsEngine;

public class Assignment2_65050434_65050534 extends JPanel {
    private static final int width = 600;
    private static final int height = 600;
    
    public static void main(String[] args) {
        Assignment2_65050434_65050534 m = new Assignment2_65050434_65050534();
        JFrame f = new JFrame();
        f.add(m);
        f.setTitle("Animation");
        f.setSize(width, height);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.BLACK);

        Graphics2D g2 = (Graphics2D) g;
        g2.translate(0, 0); 
        String d = "M0.5 17.5C4.33333 11.6667 15.5 1.49012e-08 17.5 1.49998C20.1833 3.51243 23.3333 15.1666 25.5 22M13 6C14 5 16.3 5.5 15.5 7.5C14.7 9.5 10.1667 14 8 16C7.16667 16.6667 5.6 17.4 6 15C6.4 12.6 10.8333 8 13 6ZM17 13.5C19.0377 13.5 20.1 14.7 18.5 15.5C16.9 16.3 15 16.8 14 17C12.7897 17.2421 11.4 16.2 13 15C14.6 13.8 16 13.5 17 13.5Z";
        String[] tokens = d.split("(?<=[A-Z]*)(?=[A-Z])");
        int x1, y1, x2, y2;
        x1 = y1 = 0;
        for (String token : tokens) {
            int[] arr, xPoints, yPoints;
            char command = token.charAt(0);
            switch (command) {
                case 'M':
                    arr = Arrays.stream(token.substring(1).split(" "))
                        .mapToInt(str -> (int) Math.round(Double.parseDouble(str)))
                        .toArray();
                    x1 = arr[0];
                    y1 = arr[1];
                    break;
                case 'V':
                    y2 = (int) Math.round(Double.parseDouble(token.substring(1)));
                    GraphicsEngine.line(g2, x1, y1, x1, y2);
                    y1 = y2;
                    break;
                case 'H':
                    x2 = (int) Math.round(Double.parseDouble(token.substring(1)));
                    GraphicsEngine.line(g2, x1, y1, x2, y1);
                    x1 = x2;
                    break;
                case 'C':
                    arr = Arrays.stream(token.substring(1).split(" "))
                        .mapToInt(str -> (int) Math.round(Double.parseDouble(str)))
                        .toArray();
                    xPoints = new int[4];
                    yPoints = new int[4];
                    xPoints[0] = x1;
                    yPoints[0] = y1;
                    for (int i = 0; i < 6; i+=2) {
                        xPoints[i/2 + 1] = arr[i];
                        yPoints[i/2 + 1] = arr[i+1];
                    }
                    GraphicsEngine.curve(g2, xPoints, yPoints);
                    x1 = xPoints[3];
                    y1 = yPoints[3];
                    break;
                default:
                    break;
            }
        }
    } // paint
} // class
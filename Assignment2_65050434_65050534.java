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

        String d = "M54.057 29.1724C41.09 2.25889 17.1476 -0.648923 6.79732 1.26136C4.63074 1.24256 0.453318 6.30651 1.07631 26.7127C1.85506 52.2204 28.5579 28.9512 38.7365 66.0409C46.8794 95.7126 36.1775 149.688 29.8086 172.967C30.5595 182.474 36.2106 195.824 52.8077 173.167C73.5542 144.846 70.2657 62.8143 54.057 29.1724Z";
        String[] tokens = d.substring(1).split("C|Z");
        int[] x = new int[4];
        int[] y = new int[4];
        String[] arr = tokens[0].split(" ");
        x[0] = (int)Math.round(Double.parseDouble(arr[0]));
        y[0] = (int)Math.round(Double.parseDouble(arr[1]));
        for (int i = 1; i < tokens.length; i++) {
            arr = tokens[i].split(" ");
            x[1] = (int)Math.round(Double.parseDouble(arr[0]));
            y[1] = (int)Math.round(Double.parseDouble(arr[1]));
            x[2] = (int)Math.round(Double.parseDouble(arr[2]));
            y[2] = (int)Math.round(Double.parseDouble(arr[3]));
            x[3] = (int)Math.round(Double.parseDouble(arr[4]));
            y[3] = (int)Math.round(Double.parseDouble(arr[5]));
            GraphicsEngine.curve((Graphics2D)g, x, y);
            x[0] = x[3];
            y[0] = y[3];
        }
    } // paint

    static void draw(Graphics2D g2, Map<String, String> data) {
        String type = data.get("TYPE");
        int n;
        int[] x, y;
        String color;
        switch (type) {
            case "line":
                int x1 = Integer.parseInt(data.get("X1"));
                int y1 = Integer.parseInt(data.get("Y1"));
                int x2 = Integer.parseInt(data.get("X2"));
                int y2 = Integer.parseInt(data.get("Y2"));
                color = data.get("COLOR_BOUND");
                if (!data.get("COLOR").isEmpty())
                    color = data.get("COLOR");
                g2.setColor(Color.decode(color));
                GraphicsEngine.line(g2, x1, y1, x2, y2);
                break;
            case "curve":
                n = 4;
                x = new int[n];
                y = new int[n];
                for (int i = 0; i < n; i++) {
                    x[i] = Integer.parseInt(data.get("X"+(i+1)));
                    y[i] = Integer.parseInt(data.get("Y"+(i+1)));
                }
                color = data.get("COLOR_BOUND");
                if (!data.get("COLOR").isEmpty())
                    color = data.get("COLOR");
                g2.setColor(Color.decode(color));
                GraphicsEngine.curve(g2, x, y);
                break;
            case "polygon":
                n = 3;
                x = new int[n];
                y = new int[n];
                for (int i = 0; i < n; i++) {
                    x[i] = Integer.parseInt(data.get("X"+(i+1)));
                    y[i] = Integer.parseInt(data.get("Y"+(i+1)));
                }
                color = data.get("COLOR");
                g2.setColor(Color.decode(color));
                GraphicsEngine.polygon(g2, x, y);
                break;
        } // switch
    } // draw
} // class
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;

import engine.Database;
import engine.GraphicsEngine;

public class Assignment2_65050434_65050534 extends JPanel implements Runnable {
    private static final int width = 600;
    private static final int height = 600;
    private static double catFrontLegRotate = 0;
    private static double catBackLegRotate = 0;
    private static double catPacifierMove = 0;
    private static double catMustacheScale = 0.00001;
    private static double catBeardScale = 0.00001;
    private static double catFrontLegVelocity = 20;
    private static double catBackLegVelocity = 20;
    private static double catPacifierVelocity = 0;
    private static double catMustacheVelocity = 1;
    private static double catBeardVelocity = 1;
    private static double catPacifierAccelaration = 200;
    public static void main(String[] args) {
        Assignment2_65050434_65050534 m = new Assignment2_65050434_65050534();
        JFrame f = new JFrame();
        f.add(m);
        f.setTitle("Animation");
        f.setSize(width, height);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);

        (new Thread(m)).start();
    }

    @Override
    public void run() {
        double lastTime = System.currentTimeMillis();
        double currentTime, elapsedTime, elapsedTimeSinceStart;
        double startTime = lastTime;

        while (true) {
            currentTime = System.currentTimeMillis();
            elapsedTime = currentTime - lastTime;
            lastTime = currentTime;
            elapsedTimeSinceStart = (currentTime - startTime) / 1000.0; // second

            catFrontLegRotate += catFrontLegVelocity * elapsedTime / 1000.0;
            catBackLegRotate += catFrontLegVelocity * elapsedTime / 1000.0;
            if (catPacifierMove <= 600)
                catPacifierMove += catPacifierVelocity * elapsedTime / 1000.0;
            double swingLimit = 10;
            // Check for swing limits and reverse direction if necessary
            if (catFrontLegRotate >= swingLimit) {
                catFrontLegRotate = swingLimit;
                catFrontLegVelocity = -catFrontLegVelocity;
            } else if (catFrontLegRotate <= -swingLimit) {
                catFrontLegRotate = -swingLimit;
                catFrontLegVelocity = -catFrontLegVelocity;
            }
            if (catBackLegRotate >= swingLimit) {
                catBackLegRotate = swingLimit;
                catBackLegVelocity = -catBackLegVelocity;
            } else if (catBackLegRotate <= -swingLimit) {
                catBackLegRotate = -swingLimit;
                catBackLegVelocity = -catBackLegVelocity;
            }

            if (elapsedTimeSinceStart > 1.0 && elapsedTimeSinceStart <= 3.0) {
                catPacifierVelocity += catPacifierAccelaration * elapsedTime / 1000.0;
            }
            if (elapsedTimeSinceStart > 3.0 && elapsedTimeSinceStart <= 5.0) {
                catMustacheScale += catMustacheVelocity * elapsedTime / 1000.0;
                catBeardScale += catBeardVelocity * elapsedTime / 1000.0;
            }
            if (catMustacheScale > 1) {
                catMustacheScale = 1;
            }
            if (catBeardScale > 1) {
                catBeardScale = 1;
            }

            // Display
            repaint();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        Graphics2D g2d = (Graphics2D) g;

        Database db = new Database();
        boolean isTableCreated = db.createTable("./data.csv", "Data");
        if (isTableCreated) {
            List<Map<String, String>> table = db.getTable("Data");
            for (Map<String,String> row : table) {
                BufferedImage buffer = new BufferedImage(width+1, height+1, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = buffer.createGraphics();
                String name = row.get("NAME");
                int x = (int) Math.round(Double.parseDouble(row.get("X")));
                int y = (int) Math.round(Double.parseDouble(row.get("Y")));
                int w = (int) Math.round(Double.parseDouble(row.get("WIDTH")));
                int h = (int) Math.round(Double.parseDouble(row.get("HEIGHT")));
                double angle = Double.parseDouble(row.get("ROTATION"));
                Color stroke = Color.decode(row.get("STROKE"));
                String d = row.get("D");
                g2.setColor(stroke);
                draw(g2, d);
                if (!row.get("FILL").isEmpty()) {
                    Color fill = Color.decode(row.get("FILL"));
                    int seedX = (int) Math.round(Double.parseDouble(row.get("SEED_X")));
                    int seedY = (int) Math.round(Double.parseDouble(row.get("SEED_Y")));
                    GraphicsEngine.fill(buffer, seedX, seedY, fill);
                }
                g2d.translate(x, y);
                g2d.rotate(-Math.toRadians(angle), w/2, h/2);
                double originX, originY;
                if (name.startsWith("cat_leg_front")) {
                    originX = 20.5;
                    originY = 11;
                    g2d.rotate(-Math.toRadians(catFrontLegRotate), originX, originY);
                    g2d.drawImage(buffer, 0, 0, null);
                    g2d.rotate(Math.toRadians(catFrontLegRotate), originX, originY);
                } else if (name.startsWith("cat_leg_back")) {
                    originX = 27.58;
                    originY = 13;
                    g2d.rotate(-Math.toRadians(catFrontLegRotate), originX, originY);
                    g2d.drawImage(buffer, 0, 0, null);
                    g2d.rotate(Math.toRadians(catFrontLegRotate), originX, originY);
                } else if (name.startsWith("cat_pacifier")) {
                    g2d.translate(0, catPacifierMove);
                    g2d.drawImage(buffer, 0, 0, null);
                    g2d.translate(0, -catPacifierMove);
                } else if (name.startsWith("cat_mustache")) {
                    g2d.scale(1, catMustacheScale);
                    g2d.drawImage(buffer, 0, 0, null);
                    g2d.scale(1, 1/catMustacheScale);
                } else if (name.startsWith("cat_beard")) {
                    g2d.scale(1, catBeardScale);
                    g2d.drawImage(buffer, 0, 0, null);
                    g2d.scale(1, 1/catBeardScale);
                } else {
                    g2d.drawImage(buffer, 0, 0, null);
                }
                g2d.rotate(Math.toRadians(angle), w/2, h/2);
                g2d.translate(-x, -y);
            } // for
        } // if
    } // paint

    public void draw(Graphics2D g2, String d) {
        String[] tokens = d.split("(?<=[A-Z]*)(?=[A-Z])");
        int x1, y1, x2, y2;
        x1 = y1 = 0;
        for (String token : tokens) {
            int[] arr, xPoints, yPoints;
            char command = token.charAt(0);
            switch (command) {
                case 'M': // Move To
                    arr = Arrays.stream(token.substring(1).split(" "))
                        .mapToInt(str -> (int) Math.round(Double.parseDouble(str)))
                        .toArray();
                    x1 = arr[0];
                    y1 = arr[1];
                    break;
                case 'V': // Vertical Line To
                    y2 = (int) Math.round(Double.parseDouble(token.substring(1)));
                    GraphicsEngine.line(g2, x1, y1, x1, y2);
                    y1 = y2;
                    break;
                case 'H': // Horizontal Line To
                    x2 = (int) Math.round(Double.parseDouble(token.substring(1)));
                    GraphicsEngine.line(g2, x1, y1, x2, y1);
                    x1 = x2;
                    break;
                case 'L': // Line To
                    arr = Arrays.stream(token.substring(1).split(" "))
                        .mapToInt(str -> (int) Math.round(Double.parseDouble(str)))
                        .toArray();
                    x2 = arr[0];
                    y2 = arr[1];
                    GraphicsEngine.line(g2, x1, y1, x2, y2);
                    x1 = x2;
                    y1 = y2;
                    break;
                case 'C': // Cubic Bezier Curve
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
            } // switch
        } // for
    } // draw
} // class
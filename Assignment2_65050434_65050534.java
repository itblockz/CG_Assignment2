import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
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
    private static double catFrontLegRotate = 0; // degrees
    private static double catFrontLegVelocity = 20; // seconds
    private static double catBackLegRotate = 0; // degrees
    private static double catBackLegVelocity = 20; // seconds
    private static double catLegSwingLimit = 10; // degrees
    private static double catPacifierMove = 0; // pixels
    private static double catPacifierVelocity = 0; // seconds
    private static double catPacifierAccelaration = 200; // seconds
    private static double catMustacheScale = 0; // times
    private static double catMustacheVelocity = 1; // seconds
    private static double catBeardScale = 0; // times
    private static double catBeardVelocity = 1; // seconds
    private static double orangeVelocity = -35; // seconds
    private static double orangeAccelaration = 0; // seconds
    private static double flower1Scale = 1; // times
    private static double flower1Velocity = 0.1; // seconds
    private static double orangeRotate = 0; // degrees
    private static double orangeMove = 0; // pixels
    private static double orangeFriction = 5;
    private static double catPosition = -78;
    private static double butterflyTimePeriod = 0.5; // seconds
    private static int butterflyStateNum = 6;
    private static int butterflyState = 0;
    private static BufferedImage buffer;
    private static double xButterfly1 = 0;
    private static double yButterfly1 = 0;
    private static double xButterfly2 = 0;
    private static double yButterfly2 = 0;
    private static double xButterfly3 = 0;
    private static double yButterfly3 = 0;
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
        double elapsedTime, elapsedTimeSinceStart;
        double currentTime, startTime;

        int fps = 60;
        int num = fps * 5;
        elapsedTime = 1000.0 / fps;
        elapsedTimeSinceStart = 0;
        BufferedImage[] images = new BufferedImage[num];
        for (int i = 0; i < num; i++) {
            updateAnimation(elapsedTime, elapsedTimeSinceStart);
            elapsedTimeSinceStart += elapsedTime;
            images[i] = getDrawnBuffer();
            buffer = images[i];
            repaint();
            System.out.printf("Buffer %d was drawn%n", i+1);
        }

        startTime = System.currentTimeMillis();
        while (true) {
            currentTime = System.currentTimeMillis();
            elapsedTimeSinceStart = currentTime - startTime;
            int idx = (int)(elapsedTimeSinceStart * num / 5000.0);
            idx %= num;
            buffer = images[idx];
            repaint();
        }
    }

    public void updateAnimation(double elapsedTime, double elapsedTimeSinceStart) { // millisecond
        elapsedTimeSinceStart /= 1000.0;
        catFrontLegRotate += catFrontLegVelocity * elapsedTime / 1000.0;
        catBackLegRotate += catFrontLegVelocity * elapsedTime / 1000.0;
        // orangeMove += orangeVelocity * elapsedTime / 1000.0;
        catPacifierMove += catPacifierVelocity * elapsedTime / 1000.0;
        butterflyState = (int)(elapsedTimeSinceStart * butterflyStateNum / butterflyTimePeriod) % butterflyStateNum;
        
        if (orangeVelocity > 0) orangeAccelaration = -orangeFriction;
        else if (orangeVelocity < 0) orangeAccelaration = orangeFriction;
        // orangeRotate += (-2 * orangeVelocity) * elapsedTime / 1000.0;
        orangeVelocity += orangeAccelaration * elapsedTime / 1000.0;
        
        // Check for swing limits and reverse direction if necessary
        if (catFrontLegRotate >= catLegSwingLimit) {
            catFrontLegRotate = catLegSwingLimit;
            catFrontLegVelocity = -catFrontLegVelocity;
        } else if (catFrontLegRotate <= -catLegSwingLimit) {
            catFrontLegRotate = -catLegSwingLimit;
            catFrontLegVelocity = -catFrontLegVelocity;
        }
        if (catBackLegRotate >= catLegSwingLimit) {
            catBackLegRotate = catLegSwingLimit;
            catBackLegVelocity = -catBackLegVelocity;
        } else if (catBackLegRotate <= -catLegSwingLimit) {
            catBackLegRotate = -catLegSwingLimit;
            catBackLegVelocity = -catBackLegVelocity;
        }

        if (elapsedTimeSinceStart > 1.0 && elapsedTimeSinceStart <= 3.0) {
            catPacifierVelocity += catPacifierAccelaration * elapsedTime / 1000.0;
        } else if (elapsedTimeSinceStart > 3.0 && elapsedTimeSinceStart <= 5.0) {
            catMustacheScale += catMustacheVelocity * elapsedTime / 1000.0;
            catBeardScale += catBeardVelocity * elapsedTime / 1000.0;
            if (catMustacheScale > 1) {
                catMustacheScale = 1;
            }
            if (catBeardScale > 1) {
                catBeardScale = 1;
            }
        }
        if (elapsedTimeSinceStart > 0 && elapsedTimeSinceStart <=4){
            flower1Scale +=  flower1Velocity * elapsedTime / 1000.0;
        }
        
        // Bounce back if enter cat
        if (orangeMove < catPosition) {
            orangeMove = catPosition;
            orangeVelocity = -orangeVelocity;
        }

        if (elapsedTimeSinceStart > 0 && elapsedTimeSinceStart <= 3){
            xButterfly1 = cubic(elapsedTimeSinceStart/3, 0, 59, 202, 311);
            yButterfly1 = cubic(elapsedTimeSinceStart/3, 0, 21, 94, 54);
        }else if(elapsedTimeSinceStart > 3 && elapsedTimeSinceStart <= 5){
            xButterfly1 = cubic((elapsedTimeSinceStart-3)/2, 311 , 435 , 423 , 428);
            yButterfly1 = cubic((elapsedTimeSinceStart-3)/2, 54 , 8 , -56 , -68);
        }
        if (elapsedTimeSinceStart > 0 && elapsedTimeSinceStart <= 5){
            xButterfly2 = cubic(elapsedTimeSinceStart/5, 0, -28, -164, -475);
            yButterfly2 = cubic(elapsedTimeSinceStart/5, 0, -96, -281, -249);
        }
        if (elapsedTimeSinceStart > 0 && elapsedTimeSinceStart <= 3){
            xButterfly3 = cubic(elapsedTimeSinceStart/3, 0, 24, 118, 306);
            yButterfly3 = cubic(elapsedTimeSinceStart/3, 0, -29, -98, -138);
        }else if (elapsedTimeSinceStart > 3 && elapsedTimeSinceStart <= 5){
            xButterfly3 = cubic((elapsedTimeSinceStart-3)/2, 306, 493, 515, 503);
            yButterfly3 = cubic((elapsedTimeSinceStart-3)/2, -138, -179, -269, -309);
        }

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(buffer, 0, 0, null);
    } // paint

    private BufferedImage getDrawnBuffer() {
        Database db = new Database();
        boolean isTableCreated = db.createTable("./data.csv", "Data");
        if (isTableCreated) {
            List<Map<String, String>> table = db.getTable("Data");
            BufferedImage sub1Buffer = new BufferedImage(width+1, height+1, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = sub1Buffer.createGraphics();
            for (Map<String,String> row : table) {
                BufferedImage sub2Buffer = new BufferedImage(width+1, height+1, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2Sub = sub2Buffer.createGraphics();
                String name = row.get("NAME");
                int x = (int) Math.round(Double.parseDouble(row.get("X")));
                int y = (int) Math.round(Double.parseDouble(row.get("Y")));
                int w = (int) Math.round(Double.parseDouble(row.get("WIDTH")));
                int h = (int) Math.round(Double.parseDouble(row.get("HEIGHT")));
                double angle = Double.parseDouble(row.get("ROTATION"));
                Color stroke = Color.decode(row.get("STROKE"));
                String d = row.get("D");
                g2Sub.setColor(stroke);
                draw(g2Sub, d);
                if (!row.get("FILL").isEmpty()) {
                    Color fill = Color.decode(row.get("FILL"));
                    int seedX = (int) Math.round(Double.parseDouble(row.get("SEED_X")));
                    int seedY = (int) Math.round(Double.parseDouble(row.get("SEED_Y")));
                    GraphicsEngine.fill(sub2Buffer, seedX, seedY, fill);
                }
                g2.setTransform(new AffineTransform());
                g2.translate(x, y);
                g2.rotate(-Math.toRadians(angle), w/2, h/2);
                double originX, originY;
                AffineTransform Tx = g2.getTransform();
                if (name.startsWith("cat_leg_front")) {
                    originX = 20.5;
                    originY = 11;
                    g2.rotate(-Math.toRadians(catFrontLegRotate), originX, originY);
                } else if (name.startsWith("cat_leg_back")) {
                    originX = 27.58;
                    originY = 13;
                    g2.rotate(-Math.toRadians(catFrontLegRotate), originX, originY);
                } else if (name.startsWith("cat_pacifier")) {
                    g2.translate(0, catPacifierMove);
                } else if (name.startsWith("cat_mustache")) {
                    g2.scale(1, catMustacheScale);
                } else if (name.startsWith("cat_beard")) {
                    g2.scale(1, catBeardScale);
                } else if (name.startsWith("orange")) {
                    originX = 32;
                    originY = 27;
                    g2.translate(orangeMove, 0);
                    g2.rotate(-Math.toRadians(orangeRotate), originX, originY);
                } else if (name.startsWith("flower_up")){
                    g2.translate(w/2, 0);
                    g2.scale(flower1Scale, flower1Scale);
                    g2.translate(-w/2, 0);
                } else if (name.startsWith("flower_down")){
                    g2.translate(w/2, h);
                    g2.scale(flower1Scale, flower1Scale);
                    g2.translate(-w/2, -h);
                } else if (name.startsWith("butterfly")){
                    int nameState = Integer.parseInt(name.split("(?<=_)(?=\\d+)")[1]);
                    if (nameState != butterflyState) continue;
                }
                if (name.startsWith("butterfly1")){
                    g2.translate(xButterfly1, yButterfly1);
                }
                else if (name.startsWith("butterfly2")){
                    g2.translate(xButterfly2, yButterfly2);
                    g2.scale(-1, 1);
                }else if (name.startsWith("butterfly3")){
                    g2.translate(xButterfly3, yButterfly3);
                }
                g2.drawImage(sub2Buffer, 0, 0, null);
                g2.setTransform(Tx);
            } // for
            return sub1Buffer;
        } // if
        return null;
    }

    public void draw(Graphics2D g2, String d) {
        String[] tokens = d.split("(?<=[A-Z]*)(?=[A-Z])");
        int x1, y1, x2, y2, r, a, b;
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
                case 'O': // Circle "not in SVG"
                    r = (int) Math.round(Double.parseDouble(token.substring(1)));
                    GraphicsEngine.circle(g2, x1, y1, r);
                    break;
                case 'E': // Ellipse "not in SVG"
                    arr = Arrays.stream(token.substring(1).split(" "))
                    .mapToInt(str -> (int) Math.round(Double.parseDouble(str)))
                    .toArray();
                    a = arr[0];
                    b = arr[1];
                    GraphicsEngine.ellipse(g2, x1, y1, a, b);
                    break;
                default:
                    break;
            } // switch
        } // for
    } // draw

    private double cubic(double t, int x1, int x2, int x3, int x4) {
        return (1-t)*(1-t)*(1-t)*x1 + 3*t*(1-t)*(1-t)*x2 + 3*t*t*(1-t)*x3 + t*t*t*x4;
    }
} // class
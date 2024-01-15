package engine;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

public class GraphicsEngine {
    public static void line(Graphics2D g2, int x1, int y1, int x2, int y2) {
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);

        int sx = (x1 < x2) ? 1 : -1;
        int sy = (y1 < y2) ? 1 : -1;
        boolean isSwap = false;

        if (dy > dx) {
            int tmp = dx;
            dx = dy;
            dy = tmp;
            isSwap = true;
        }

        int D = 2 * dy - dx;

        int x = x1;
        int y = y1;
        for (int i = 1; i <= dx; i++) {
            plot(g2, x, y);

            if (D >= 0) {
                if (isSwap)
                    x += sx;
                else
                    y += sy;
                D -= 2 * dx;
            }
            if (isSwap)
                y += sy;
            else
                x += sx;

            D += 2 * dy;
        }
    }

    public static void curve(Graphics2D g2, int[] xPoints, int[] yPoints) {
        int n = 1000;
        double d = 1.0 / n;

        double t = 0;
        for (int i = 0; i < n; i++) {
            int x = (int) Math.round(cubic(t, xPoints[0], xPoints[1], xPoints[2], xPoints[3]));
            int y = (int) Math.round(cubic(t, yPoints[0], yPoints[1], yPoints[2], yPoints[3]));
            plot(g2, x, y);
            t += d;
        }
    }

    private static double cubic(double t, int x1, int x2, int x3, int x4) {
        return (1-t)*(1-t)*(1-t)*x1 + 3*t*(1-t)*(1-t)*x2 + 3*t*t*(1-t)*x3 + t*t*t*x4;
    }

    public static void polygon(Graphics2D g2, int[] x, int[] y) {
        g2.fillPolygon(x, y, x.length);
    }

    public static void fill(BufferedImage m, int x, int y, Color replacementColour) {
        int targetColour = m.getRGB(x, y);
        if (targetColour == replacementColour.getRGB()) return;

        List<Integer> xQ = new LinkedList<>();
        List<Integer> yQ = new LinkedList<>();
        xQ.add(x); yQ.add(y);

        while (!xQ.isEmpty()) {
            int curX = xQ.remove(0);
            int curY = yQ.remove(0);

            if (m.getRGB(curX, curY) == targetColour) {
                m.setRGB(curX, curY, replacementColour.getRGB());
                xQ.add(curX); yQ.add(curY-1);
                xQ.add(curX); yQ.add(curY+1);
                xQ.add(curX-1); yQ.add(curY);
                xQ.add(curX+1); yQ.add(curY);
            }
        }
    }

    private static void plot(Graphics2D g, int x, int y) {
        g.fillRect(x, y, 1, 1);
    }
}

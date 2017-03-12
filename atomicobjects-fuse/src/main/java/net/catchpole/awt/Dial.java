package net.catchpole.awt;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Dial {
    private final static Font font = new Font("Adore64", Font.PLAIN, 10);;

    private final int startX;
    private final int startY;
    private final int size;
    private final Color faceColor;
    private final String label;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    public Dial(int x, int y, int size, Color faceColor, String label) {
        this.startX = x;
        this.startY = y;
        this.size = size;
        this.faceColor = faceColor;
        this.label = label;
    }

    public void drawClockFace(Graphics2D g, Calendar calendar) {
        drawFace(g);

        // render hour markers
        for (int x = 0; x < 12; x++) {
            drawMarker(g, 1.0f / 12.0f * x);
        }

        // hours hand - position creeps with minutes as well
        drawHand(g, (1.0f / (12.0f * 60.0f)) * (
                        ((double) calendar.get(Calendar.HOUR)) * 60 + (double) calendar.get(Calendar.MINUTE)), 55,
                Color.WHITE);
        // minute hand
        drawHand(g, (1.0f / 60.0f) * ((double) calendar.get(Calendar.MINUTE)), 75, Color.WHITE);
        // second hand
        drawHand(g, (1.0f / 60.0f) * ((double) calendar.get(Calendar.SECOND)), 75, Color.RED);

        drawLabels(g, dateFormat.format(calendar.getTime()));
    }

    public void drawBearing(Graphics2D g, double bearing, double heading) {
        drawFace(g);
        drawHand(g, bearing/360.0d, 75, Color.RED);
        drawHand(g, heading/360.0d, 75, Color.WHITE);
        drawLabels(g, Integer.toString((int)bearing) + " " + Integer.toString((int)heading));
    }

    public void drawMeter(Graphics2D g, double value) {
        drawMeter(g, value, String.format("%.3f", value));
    }

    public void drawMeter(Graphics2D g, double scale, double value) {
        drawMeter(g, value * scale, String.format("%.3f", value));
    }

    public void drawMeter(Graphics2D g, double value, String valueString) {
        drawFace(g);
        drawMarker(g, (1.0d/360.0d)*135.0d);
        drawMarker(g, (1.0d/360.0d)*225.0d);
        drawHand(g, normaliseFraction((value * 0.75d) + ((1.0d / 360.d) * 225.0)), 75, Color.WHITE);
        drawLabels(g, valueString);
    }

    public void drawCenteredMeter(Graphics2D g, double value) {
        drawFace(g);
        drawMarker(g, 0.0d);
        drawMarker(g, (1.0d/360.0d)*135.0d);
        drawMarker(g, (1.0d/360.0d)*225.0d);
        drawHand(g, normaliseFraction((((value + 1.0d) / 2.0d) * 0.75d) + ((1.0d / 360.d) * 225.0)), 75, Color.WHITE);
        drawLabels(g, String.format( "%.3f", value ));
    }

    public void drawCenteredMeterPercent(Graphics2D g, double value) {
        drawFace(g);
        drawMarker(g, 0.0d);
        drawMarker(g, (1.0d/360.0d)*135.0d);
        drawMarker(g, (1.0d/360.0d)*225.0d);
        drawHand(g, normaliseFraction((((value + 1.0d) / 2.0d) * 0.75d) + ((1.0d / 360.d) * 225.0)), 75, Color.WHITE);
        drawLabels(g, Integer.toString((int)(value * 100.0D)));
    }

    private void drawFace(Graphics2D g) {
        g.setColor(faceColor);
        int inset = size / 20;
        g.fillOval(startX + inset, startY + inset, size - inset - inset, size - inset - inset);
    }

    private void drawMarker(Graphics2D g, double degree) {
        int rad = (int) getRadialSize(5);
        int center = size / 2;
        double rotation = degree * 2.0f * Math.PI;
        double radialSize = getRadialSize(85);
        g.setColor(Color.WHITE);
        g.fillOval(
                this.startX + ((int) (((double) center) + (Math.sin(rotation) * radialSize))) - rad,
                this.startY + (int) (((double) center) - (Math.cos(rotation) * radialSize)) - rad,
                rad + rad,
                rad + rad);
    }

    private void drawLabels(Graphics2D g, String detail) {
        int center = size / 2;
        int y = (int) (((double) size) * .70);
        if (detail != null) {
            drawCenteredText(g, detail, center, y);
        }
        if (label != null) {
            drawCenteredText(g, label, center, y + 16);
        }
    }

    private void drawHand(Graphics g, double rotation, int percent, Color color) {
        g.setColor(color);
        Polygon polygon = new Polygon();
        addPoint(polygon, rotation, percent);
        addPoint(polygon, rotation - 0.78f, 5);
        addPoint(polygon, rotation + 0.78f, 5);
        g.fillPolygon(polygon);
    }

    private void addPoint(Polygon polygon, double rotation, int percent) {
        rotation *= 2 * Math.PI;
        int center = size / 2;
        polygon.addPoint(
                this.startX + (int) (((double) center) + (Math.sin(rotation) * getRadialSize(percent))),
                this.startY + (int) (((double) center) - (Math.cos(rotation) * getRadialSize(percent))));

    }

    private void drawCenteredText(Graphics g, String string, int x, int y) {
        g.setFont(font);
        x -= (g.getFontMetrics().stringWidth(string) / 2);
        // drop shaddow
        g.setColor(Color.BLACK);
        g.drawString(string, this.startX + x + 1, this.startY + y + 1);
        // real text
        g.setColor(Color.WHITE);
        g.drawString(string, this.startX + x, this.startY + y);
    }

    private double getRadialSize(int percent) {
        return ((double) size / 2) / 100.0f * percent;
    }

    public String toString() {
        return this.getClass().getSimpleName() + ' ' + size + ' ' + Integer.toHexString(faceColor.getRGB());
    }

    private static double normaliseFraction(double value) {
        int intValue = (int)value;
        value -= intValue;
        return value < 0 ? value+1.0d : value;
    }
}
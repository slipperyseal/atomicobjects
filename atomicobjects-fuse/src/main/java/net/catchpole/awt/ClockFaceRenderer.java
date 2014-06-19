package net.catchpole.awt;

//   Copyright 2014 catchpole.net
//
//   Licensed under the Apache License, Version 2.0 (the "License");
//   you may not use this file except in compliance with the License.
//   You may obtain a copy of the License at
//
//       http://www.apache.org/licenses/LICENSE-2.0
//
//   Unless required by applicable law or agreed to in writing, software
//   distributed under the License is distributed on an "AS IS" BASIS,
//   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//   See the License for the specific language governing permissions and
//   limitations under the License.

import net.catchpole.fuse.annotation.Fuse;
import net.catchpole.fuse.annotation.Transform;
import net.catchpole.lang.Transformation;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Fuse
public class ClockFaceRenderer implements Serializable, Transformation<Date, BufferedImage> {
    private static final long serialVersionUID = 42L;

    private final int size;
    private final Color faceColor;
    private final boolean secondHand;
    private final String label;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");

    public ClockFaceRenderer(int size, Color faceColor, boolean secondHand) {
        this.size = size;
        this.faceColor = faceColor;
        this.secondHand = secondHand;
        this.label = null;
    }

    public ClockFaceRenderer(int size, Color faceColor, boolean secondHand, String label) {
        this.size = size;
        this.faceColor = faceColor;
        this.secondHand = secondHand;
        this.label = label;
    }

    @Transform
    public BufferedImage transform(Date date) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        BufferedImage bufferedImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        drawClockFace(bufferedImage, calendar);
        return bufferedImage;
    }

    private void drawClockFace(BufferedImage bufferedImage, Calendar calendar) {
        Graphics g = bufferedImage.getGraphics();

        if (g instanceof Graphics2D) {
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        }

        g.setColor(faceColor);
        int inset = size / 20;
        int center = size / 2;

        // draw clock
        g.fillOval(inset, inset, size - inset - inset, size - inset - inset);

        // draw hour markers
        g.setColor(Color.WHITE);
        int rad = (int) getRadialSize(2);
        for (int x = 0; x < 12; x++) {
            float rotation = 1.0f / 12.0f * x * 2.0f * (float) Math.PI;
            float radialSize = getRadialSize(85);
            g.fillOval(((int) (((float) center) + (Math.sin(rotation) * radialSize))) - rad,
                    (int) (((float) center) - (Math.cos(rotation) * radialSize)) - rad,
                    rad + rad, rad + rad);
        }

        // hours hand - position creeps with minutes as well
        drawHand(g, (1.0f / (12.0f * 60.0f)) * (
                ((float) calendar.get(Calendar.HOUR)) * 60 + (float) calendar.get(Calendar.MINUTE)), 55);
        // minute hand
        drawHand(g, (1.0f / 60.0f) * ((float) calendar.get(Calendar.MINUTE)), 75);

        if (secondHand) {
            g.setColor(Color.RED);
            drawHand(g, (1.0f / 60.0f) * ((float) calendar.get(Calendar.SECOND)), 75);
        }

        int y = (int) (((float) size) * .75);
        drawCenteredText(g, dateFormat.format(calendar.getTime()), center, y);

        if (label != null) {
            drawCenteredText(g, label, center, y + 20);
        }
    }

    private void drawHand(Graphics g, float rotation, int percent) {
        Polygon polygon = new Polygon();
        addPoint(polygon, rotation, percent);
        addPoint(polygon, rotation - 0.58f, 5);
        addPoint(polygon, rotation + 0.58f, 5);
        g.fillPolygon(polygon);
    }

    private void addPoint(Polygon polygon, float rotation, int percent) {
        rotation *= 2 * (float) Math.PI;
        int center = size / 2;
        polygon.addPoint(
                (int) (((float) center) + (Math.sin(rotation) * getRadialSize(percent))),
                (int) (((float) center) - (Math.cos(rotation) * getRadialSize(percent))));

    }

    private void drawCenteredText(Graphics g, String string, int x, int y) {
        x -= (g.getFontMetrics().stringWidth(string) / 2);
        // drop shaddow
        g.setColor(Color.BLACK);
        g.drawString(string, x + 1, y + 1);
        // real text
        g.setColor(Color.WHITE);
        g.drawString(string, x, y);
    }

    private float getRadialSize(int percent) {
        return ((float) size / 2) / 100.0f * percent;
    }

    public String toString() {
        return this.getClass().getSimpleName() + ' ' + size + ' ' + Integer.toHexString(faceColor.getRGB()) + ' ' + secondHand;
    }
}

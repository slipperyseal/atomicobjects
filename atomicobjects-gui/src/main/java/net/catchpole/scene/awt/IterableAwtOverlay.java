package net.catchpole.scene.awt;

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

import net.catchpole.trace.Core;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class IterableAwtOverlay implements AwtOverlay {
    private final List<Item> list = new ArrayList<Item>();
    private final Color color;
    private final int border = 8;
    private int width;
    private int height;

    public IterableAwtOverlay(Color color) {
        this.color = color;
    }

    public void addIterable(Iterable iterable) {
        for (Object item : iterable) {
            this.addItem(item);
        }
    }

    public void addItem(Object item) {
        Core.getTrace().info(item);
        Item i = new Item();
        i.text = item.toString();
        list.add(i);
    }

    public void render(Graphics g, int xpos, int ypos) {
        calculateSize(g);

        Graphics2D g2d = (Graphics2D) g;

        g.setColor(color);

        // transparent overlay box
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2F));
        try {
            g.fillRoundRect(xpos - border, ypos - border, this.width + border + border, this.height + border + border,
                    border, border);
        } finally {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0F));
        }

        for (Item item : list) {
            ypos += item.y;
            g.drawString(item.text, xpos, ypos);
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getBorder() {
        return border;
    }

    public void calculateSize(Graphics g) {
        FontMetrics fm = g.getFontMetrics();

        this.width = 0;
        this.height = 0;

        for (Item item : list) {
            Rectangle2D r = fm.getStringBounds(item.text, g);
            item.x = (int) r.getWidth();
            item.y = (int) r.getHeight();

            if (this.width < item.x) {
                this.width = item.x;
            }
            this.height += item.y;
        }
    }

    class Item {
        public String text;
        public int x;
        public int y;
    }
}

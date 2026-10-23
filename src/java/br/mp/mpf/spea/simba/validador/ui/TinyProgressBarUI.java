package br.mp.mpf.spea.simba.validador.ui;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicProgressBarUI;

public class TinyProgressBarUI extends BasicProgressBarUI {
   protected void paintDeterminate(Graphics g, JComponent c) {
      boolean vertical = this.progressBar.getOrientation() == 1;
      int barRectWidth = this.progressBar.getWidth();
      int barRectHeight = this.progressBar.getHeight() - 1;
      int amountFull = this.getAmountFull((Insets)null, barRectWidth, barRectHeight);
      g.setColor(this.progressBar.getForeground());
      barRectHeight -= 2;
      barRectWidth -= 2;
      if (barRectWidth > 0 && barRectHeight > 0) {
         Graphics2D g2 = (Graphics2D)g;
         g2.setStroke(new BasicStroke((float)(vertical ? barRectWidth : barRectHeight), 0, 2));
         if (!vertical) {
            g2.drawLine(2, barRectHeight / 2 + 1, amountFull - 2, barRectHeight / 2 + 1);
         } else {
            g2.drawLine(barRectWidth / 2 + 1, barRectHeight + 1, barRectWidth / 2 + 1, barRectHeight + 1 - amountFull + 2);
         }

      }
   }
}

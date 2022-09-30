//
//  Name: Triance, Nick
//  Due: 03 Dec. 2021
//  Description:
//              Print dialog for notepad

import java.awt.print.*;
import java.awt.*;

public class Printer implements Printable {

    /**
     * Sets up printer dialog.
     */
    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (pageIndex > 0) {
            return 0;
        }

        Graphics2D g2d = (Graphics2D)graphics;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

        graphics.drawString("", 100, 100);
        return PAGE_EXISTS;
    }
    
}

//
//  Name: Triance, Nick
//  Due: 03 Dec. 2021
//  Description:
//              Holds default constants for notepad

import java.awt.Font;
import java.awt.Color;

public final class Defaults {

    /** Amount by which the font size changes while zooming */
    public static final int ZOOM_INCREMENT = 2;

    /** Name of the default font */
    public static final String DEFAULT_FONT_NAME = "Courier New";

    /** Style of the default font */
    public static final int DEFAULT_FONT_STYLE = Font.PLAIN;

    /** Size of the default font */
    public static final int DEFAULT_FONT_SIZE = 12;

    /** The default font */
    public static final Font DEFAULT_FONT = new Font(DEFAULT_FONT_NAME, DEFAULT_FONT_STYLE, DEFAULT_FONT_SIZE);

    /** Defaults to YELLOW, the default color for the highlighter, used by Find and Find Next... */
    public static final Color HIGHLIGHT_COLOR = Color.YELLOW;
}

package utils;

import java.awt.Color;
import java.awt.Font;

public class UITheme {
	// Colors
	public static final Color PRIMARY = new Color(99, 102, 241);
	public static final Color PRIMARY_DARK = new Color(79, 70, 229);
	public static final Color SECONDARY = new Color(139, 92, 246);
	public static final Color SUCCESS = new Color(16, 185, 129);
	public static final Color WARNING = new Color(245, 158, 11);
	public static final Color DANGER = new Color(239, 68, 68);
	public static final Color INFO = new Color(59, 130, 246);

	public static final Color SIDEBAR_BG = new Color(30, 41, 59);
	public static final Color SIDEBAR_HOVER = new Color(51, 65, 85);
	public static final Color SIDEBAR_ACTIVE = new Color(99, 102, 241);

	public static final Color BG_LIGHT = new Color(248, 250, 252);
	public static final Color CARD_BG = Color.WHITE;
	public static final Color BORDER = new Color(226, 232, 240);

	public static final Color TEXT_DARK = new Color(15, 23, 42);
	public static final Color TEXT_GRAY = new Color(100, 116, 139);
	public static final Color TEXT_LIGHT = new Color(148, 163, 184);
	public static final Color TEXT_WHITE = Color.WHITE;

	// Fonts - using fallback chain for emoji support
	public static final String FONT_FAMILY = "Segoe UI";
	public static final String EMOJI_FONT = "Segoe UI Emoji";

	public static final Font TITLE_LARGE = new Font(FONT_FAMILY, Font.BOLD, 28);
	public static final Font TITLE = new Font(FONT_FAMILY, Font.BOLD, 22);
	public static final Font SUBTITLE = new Font(FONT_FAMILY, Font.BOLD, 16);
	public static final Font HEADING = new Font(FONT_FAMILY, Font.BOLD, 14);
	public static final Font BODY = new Font(FONT_FAMILY, Font.PLAIN, 13);
	public static final Font BODY_BOLD = new Font(FONT_FAMILY, Font.BOLD, 13);
	public static final Font SMALL = new Font(FONT_FAMILY, Font.PLAIN, 12);
	public static final Font CAPTION = new Font(FONT_FAMILY, Font.PLAIN, 11);

	// For text with emojis - use this font
	public static final Font EMOJI_TITLE = new Font(EMOJI_FONT, Font.BOLD, 22);
	public static final Font EMOJI_BODY = new Font(EMOJI_FONT, Font.PLAIN, 13);
	public static final Font EMOJI_HEADING = new Font(EMOJI_FONT, Font.BOLD, 14);
}

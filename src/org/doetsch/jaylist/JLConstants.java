package org.doetsch.jaylist;

import java.awt.Color;
import java.awt.Font;

import javax.swing.Icon;
import javax.swing.ImageIcon;

class JLConstants {

	public static final int FRAME_DEFAULT_WIDTH = 266;
	public static final int FRAME_DEFAULT_HEIGHT = 500;
	
	public static final int ITEMPANEL_WIDTH = 256;
	public static final int ITEMPANEL_HEIGHT = 32;
	public static final int HEADER_HEIGHT = 32;
	public static final int ITEMPANEL_HEIGHT_EXPANDED = 90;

	public static final Color COLOR_BUTTON_BG = new Color(27, 125, 255);
	public static final Color COLOR_BUTTON_FG = Color.WHITE;
	public static final Color COLOR_HEADER_BG = new Color(251, 251, 251);
	public static final Color COLOR_HIGHLIGHT = new Color(228, 241, 254);
	public static final Color COLOR_HIGHLIGHT2 = new Color(228, 241, 254);
	public static final Color COLOR_GRID = new Color(218, 230, 242);
	
	public static final ImageIcon ICON_APP = new ImageIcon(
			ItemPanel.class.getResource("resources/icon_32.png"));
	
	public static final int ICON_FLAG_COUNT = 2;
	public static final ImageIcon ICON_CHECKED = new ImageIcon(
			ItemPanel.class.getResource("resources/set3/checked.png"));
	public static final ImageIcon ICON_UNCHECKED = new ImageIcon(
			JLConstants.class.getResource("resources/set3/unchecked.png"));
	public static final ImageIcon ICON_URGENT = new ImageIcon(
			JLConstants.class.getResource("resources/set2/urgent_32x32.png"));
	public static final ImageIcon ICON_ISSUE = new ImageIcon(
			JLConstants.class.getResource("resources/set2/question_32x32.png"));
	public static final ImageIcon ICON_EXPAND = new ImageIcon(
			JLConstants.class.getResource("resources/set2/expand.png"));
	public static final ImageIcon ICON_COLLAPSE = new ImageIcon(
			JLConstants.class.getResource("resources/set2/collapse.png"));
	public static final Icon ICON_BIRD = new ImageIcon(
			JLConstants.class.getResource("resources/set3/bird.png"));
	
	public static final Font FONT_ITEM_TITLE = new Font("Arial", Font.PLAIN, 14);
	public static final Font FONT_HEADER = new Font("Arial", Font.PLAIN, 16);
	public static final Font FONT_ITEM_DESCRIPTIOn = new Font("Arial", Font.PLAIN, 12);
	

}
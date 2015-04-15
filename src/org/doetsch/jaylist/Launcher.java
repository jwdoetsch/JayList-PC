package org.doetsch.jaylist;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Component;

import javax.swing.SwingConstants;

import java.awt.AWTException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Font;
import java.awt.Color;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.AbstractCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.plaf.ScrollBarUI;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.plaf.metal.MetalScrollBarUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.UIManager;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.AttributeList;
import org.xml.sax.SAXException;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.Window.Type;

import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.JLabel;

import java.awt.ComponentOrientation;

public class Launcher extends JFrame {

	class MagicRenderer implements TableCellRenderer {

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			
			if (value != null)
				return new LauncherPanel((LauncherModel)value, false);
			else
				return new LauncherPanel(new LauncherModel("", null), true);
		}
		
	}
	
	class MagicEditor extends AbstractCellEditor implements TableCellEditor {

		LauncherModel model;
		
		@Override
		public Object getCellEditorValue() {
			return model;
		}

		@Override
		public Component getTableCellEditorComponent(JTable arg0, Object arg1,
				boolean arg2, int arg3, int arg4) {
			
			model = (LauncherModel)arg1;
			if (arg1 != null)
				return new LauncherPanel(model, false);
			else
				return new LauncherPanel(new LauncherModel("", null), true);
		}
		
	}
	
	private JPanel contentPane;
	private JPanel panel;
	private JButton btnNew;
	private JPanel panel_1;
	private JPanel panel_2;
	private JButton btnOpen;
	private JScrollPane scrollPane;
	private JTable table;
	private ArrayList<LauncherModel> models;
	private TrayIcon trayIcon;
	private SystemTray systemTray;
	private JPanel panel_3;
	private JPanel panel_4;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Launcher frame = new Launcher();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Launcher() {
		initComponents();
		initSystemTray();
	}
	
	private void initSystemTray() {
		systemTray = SystemTray.getSystemTray();
		if (!SystemTray.isSupported()) {
			return;
		}
		
		trayIcon = new TrayIcon(
				new ImageIcon(Launcher.class.
						getResource("resources/tray.png")).getImage(), 
					
				"JayList");
		
		PopupMenu menu = new PopupMenu();
		trayIcon.setPopupMenu(menu);
		MenuItem itemNew = new MenuItem("New");
		MenuItem itemOpen = new MenuItem("Open");
		MenuItem itemExit = new MenuItem("Exit");
		menu.add(itemNew);
		menu.add(itemOpen);
		menu.add(itemExit);
		itemNew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				newList();
			}
		});
		itemOpen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				openList();
			}
			
		});
		
		
		try {
			systemTray.add(trayIcon);
			
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		trayIcon.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!Launcher.this.isActive()) {
					Launcher.this.setVisible(true);
					Launcher.this.requestFocus();
				}
			}
		});
		
		
	}

	private void initComponents() {
		
		models = new ArrayList<LauncherModel>();
		setTitle("Pinboard - JayList");
		//setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//setDefaultCloseOperation()
		//setBounds(100, 100, 358, 368);
		setBounds(368, 128, 64, 64);
		this.contentPane = new JPanel();
		this.contentPane.setBackground(Constants.LAUNCHER_COLOR_BG);
		this.contentPane.setPreferredSize(new Dimension(440, 320));
		this.contentPane.setBorder(new EmptyBorder(4, 4, 4, 4));
		this.contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(this.contentPane);
		this.panel = new JPanel();
		this.panel.setOpaque(false);
		this.contentPane.add(this.panel, BorderLayout.NORTH);
		this.panel.setLayout(new BoxLayout(this.panel, BoxLayout.X_AXIS));
		this.panel_1 = new JPanel();
		this.panel_1.setBorder(new EmptyBorder(2, 2, 2, 2));
		this.panel_1.setOpaque(false);
		//this.panel_1.setBorder(new EmptyBorder(4, 4, 4, 4));
		this.panel_1.setMaximumSize(new Dimension(104, 104));
		this.panel_1.setMinimumSize(new Dimension(104, 104));
		this.panel_1.setPreferredSize(new Dimension(104, 104));
		this.panel_1.setSize(new Dimension(104, 104));
		this.panel.add(this.panel_1);
		this.panel_1.setLayout(new BorderLayout(0, 0));
		
		this.btnNew = new JButton("New");
		this.btnNew.setIcon(new ImageIcon(Launcher.class.getResource("/org/doetsch/jaylist/resources/new_16x16.png")));
		//this.btnNew.setContentAreaFilled(false);
		this.btnNew.setMnemonic('N');
		this.btnNew.setBorder(new EmptyBorder(8, 8, 8, 8));
		this.btnNew.setVerticalTextPosition(SwingConstants.TOP);
		this.btnNew.setBackground(Constants.LAUNCHER_COLOR_BUTTON);
		this.btnNew.setForeground(Color.WHITE);
		this.btnNew.setFont(Constants.LAUNCHER_FONT);
		this.btnNew.setHorizontalAlignment(SwingConstants.LEADING);
		this.btnNew.setVerticalAlignment(SwingConstants.TOP);
		this.panel_1.add(this.btnNew);
		this.btnNew.setMinimumSize(new Dimension(104, 104));
		this.btnNew.setMaximumSize(new Dimension(104, 104));
		this.btnNew.setPreferredSize(new Dimension(104, 104));
		this.btnNew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				newList();
			}
		});
		
		this.panel_2 = new JPanel();
		this.panel_2.setOpaque(false);
		this.panel_2.setSize(new Dimension(104, 104));
		this.panel_2.setPreferredSize(new Dimension(104, 104));
		this.panel_2.setMinimumSize(new Dimension(104, 104));
		this.panel_2.setMaximumSize(new Dimension(104, 104));
		this.panel_2.setBorder(new EmptyBorder(2, 2, 2, 2));
		this.panel.add(this.panel_2);
		this.panel_2.setLayout(new BorderLayout(0, 0));
		
		this.btnOpen = new JButton("Open");
		this.btnOpen.setVerticalAlignment(SwingConstants.TOP);
		this.btnOpen.setVerticalTextPosition(SwingConstants.BOTTOM);
		this.btnOpen.setIcon(new ImageIcon(Launcher.class.getResource("/org/doetsch/jaylist/resources/new_16x16.png")));
		this.btnOpen.setBorder(new EmptyBorder(8, 8, 8, 8));
		this.btnOpen.setMnemonic('O');
		this.btnOpen.setBackground(Constants.LAUNCHER_COLOR_BUTTON);
		this.btnOpen.setForeground(Color.WHITE);
		this.btnOpen.setPreferredSize(new Dimension(104, 104));
		this.btnOpen.setMinimumSize(new Dimension(104, 104));
		this.btnOpen.setMaximumSize(new Dimension(104, 104));
		this.btnOpen.setMargin(new Insets(0, 0, 0, 0));
		this.btnOpen.setHorizontalAlignment(SwingConstants.LEADING);
		this.btnOpen.setFont(new Font("Arial", Font.PLAIN, 16));
		this.btnOpen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openList();
			}
		});
		
		this.panel_2.add(this.btnOpen, BorderLayout.CENTER);
		this.panel_3 = new JPanel();
		this.panel_3.setSize(new Dimension(104, 104));
		this.panel_3.setPreferredSize(new Dimension(104, 104));
		this.panel_3.setOpaque(false);
		this.panel_3.setMinimumSize(new Dimension(104, 104));
		this.panel_3.setMaximumSize(new Dimension(104, 104));
		this.panel_3.setBorder(new EmptyBorder(2, 2, 2, 2));
		this.panel.add(this.panel_3);
		this.panel_3.setLayout(null);
		this.panel_4 = new JPanel();
		this.panel_4.setSize(new Dimension(104, 104));
		this.panel_4.setPreferredSize(new Dimension(104, 104));
		this.panel_4.setOpaque(false);
		this.panel_4.setMinimumSize(new Dimension(104, 104));
		this.panel_4.setMaximumSize(new Dimension(104, 104));
		this.panel_4.setBorder(new EmptyBorder(2, 2, 2, 2));
		this.panel.add(this.panel_4);
		this.panel_4.setLayout(null);
		this.scrollPane = new JScrollPane();
		this.scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		this.scrollPane.setBackground(Constants.LAUNCHER_COLOR_BG);
		this.scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		this.scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		this.contentPane.add(this.scrollPane, BorderLayout.CENTER);
		this.table = new JTable();
		this.table.setBackground(Constants.LAUNCHER_COLOR_BG);
		this.table.setBorder(new EmptyBorder(0, 0, 0, 0));
		this.table.setShowVerticalLines(false);
		this.table.setShowHorizontalLines(false);
		this.table.setShowGrid(false);
		this.table.setRowSelectionAllowed(false);
		this.table.setColumnSelectionAllowed(false);

		this.table.setModel(new DefaultTableModel(
				new LauncherModel[][] {
						{},
					},
					new String[] {
						
					}
				));
		this.table.setCellSelectionEnabled(true);
		this.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.table.setFillsViewportHeight(true);
		this.scrollPane.setViewportView(this.table);
		this.table.setTableHeader(null);
		this.table.setRowHeight(104);
		this.table.setDefaultRenderer(Object.class, new MagicRenderer());
		this.table.setDefaultEditor(Object.class, new MagicEditor());
		this.table.setIntercellSpacing(new Dimension(0, 0));

		
		pack();

		models = syncHistory();		
		syncTableModel();
		
		
	}

   
	private ArrayList<LauncherModel> syncHistory() {
		XMLResourceAdapter xmlRsrc = new XMLResourceAdapter();
		ArrayList<LauncherModel> resources = new ArrayList<LauncherModel>();
		Node n, root, child;
		NodeList children;
		NamedNodeMap atts;
		
		try {
			n = xmlRsrc.getRootNode(Launcher.class.getResource("xml/history.xml"), 
					Launcher.class.getResource("xml/history.xsd"));
		} catch (IOException | SAXException | ParserConfigurationException e) {
			e.printStackTrace();
			return resources;
		}
		
		root = n.getFirstChild();
		children = root.getChildNodes();
		
		for (int i = 0; i < children.getLength(); i++) {
			child = children.item(i);
			if (child.getNodeName().equals("entry")) {
				atts = child.getAttributes();
				try {
					resources.add(new LauncherModel(
							atts.getNamedItem("title").getNodeValue(),
							new File(atts.getNamedItem("path").getNodeValue()).toURI().toURL()));
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
			
			
		}
		
		for (LauncherModel m : resources) {
			System.out.println(m.title + ": " + m.path);
		}
		
		return resources;		
	}

	
	
	


	
	/*
	 * Patterns the table's model after the contents of
	 * the history array. Renders the linear list of 
	 * LauncherModel objects to the n x 3 table, where
	 * n is the number of rows necessary to accommodate
	 * all the LauncherModels.
	 */
	void syncTableModel () {
		
		int col, row;
		int cols = 4;
		
		int rows = (models.size() / cols)
				//if there are only 1 or 2 cells in the row then count the 
				//row as a whole row
				+ (models.size() % cols > 0 ? 1 : 0) ;
		
		DefaultTableModel model = new DefaultTableModel(new LauncherPanel[rows][cols], new Object[cols]);
		
		
		for (int i = 0; i < models.size(); i++) {
			col = i % cols;
			row = i / cols;
			model.setValueAt(models.get(i), row, col);
		}
		
		table.setModel(model);
				
	}
	


	public void newList() {
		ListMarshall marshall = new ListMarshall();
			ListFrame newFrame;
		try {
			newFrame = new ListFrame(this, marshall.unmarshall(Constants.XMl_NEW_LIST), false);
			newFrame.setVisible(true);
			newFrame.setLocation(this.getLocation().x + 64,
					this.getLocation().y + 64);
		} catch (IOException | SAXException | ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
				
	}
	
	void openList () {
		JFileChooser fc = new JFileChooser();

		int state = fc.showOpenDialog(this);
		if (state == JFileChooser.APPROVE_OPTION) {
			ListMarshall marshall = new ListMarshall();
			try {
				ListFrame newList;
				newList = new ListFrame(this, marshall.unmarshall(fc.getSelectedFile().toURI().toURL()), true);
				newList.setVisible(true);
				
			/*
			 * catch XML decode and file IO exceptions and notify the
			 * user via dialog
			 */
			} catch (IOException | SAXException
					| ParserConfigurationException e1) {
				JOptionPane.showMessageDialog(this, "Can't open the selected file.");
			}
		}
	}
}
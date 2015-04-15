package org.doetsch.jaylist;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.AbstractCellEditor;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import javax.swing.DropMode;


/**
 * ListFrame is the main UI window that contains the necessary UI
 * components to represent a usable JayList. 
 *  
 * @author Jacob Wesley Doetsch
 */
class ListFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1516813645310303053L;
	
	private JPanel contentPane;
	
	//UI components
	private JScrollPane uiScrollPane;
	private JTable uiTable;
	private JTextPane uiTextPane;
	private JPopupMenu uiPopupMenu;
	private JMenuItem uiMenuItemSave;
	private JMenuItem uiMenuItemSaveAs;
	private JMenuItem uiMenuItemOpen;
	private JMenuItem uiMenuItemAdd;
	private JMenuItem uiMenuItemRemove;
	private JMenuItem uiMenuItemNew;


	//determines save behavior according to whether the list 
	//is a new list or an opened list
	private Launcher launcher; 
	private boolean hasPath;
	private URL path;


//	/**
//	 * Launch a ListFrame instance that opens a new list.
//	 * Uses the Metal look and feel.
//	 */
//	public static void main (String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				
//				try {
//					UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
//				} catch (ClassNotFoundException | InstantiationException
//						| IllegalAccessException
//						| UnsupportedLookAndFeelException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//				
//				try {
//					//pass the new list template to the new frame
//					ListMarshall marshall = new ListMarshall();
//					ListFrame frame = new ListFrame(marshall.unmarshall(
//							Constants.XMl_NEW_LIST), false);
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Instantiates a ListFrame that patterns itself after the
	 * given ListModel.
	 * @param listModel the ListModel to pattern
	 */
	public ListFrame (Launcher launcher, ListFrameModel listModel, boolean hasPath) {
		this.launcher = launcher;
		this.hasPath = hasPath;
		
		initComponents();
		injectListModel(listModel);
	}
	
	/**
	 * Applies the given ListModel field values to the ListFrame.
	 *  
	 * @param listModel
	 */
	private void injectListModel (ListFrameModel listModel) {
		
		path = listModel.getPath();
		
		setTitle();
		
		//set header text
		this.uiTextPane.setText(listModel.getHeader());
		
		//build the table model
		DefaultTableModel tableModel = new DefaultTableModel(
				new ItemPanelModel[][] {
				},
				new String[] {
					"Items"
				}
			);
		for (ItemPanelModel itemModel : listModel.getItemModels()) {
			tableModel.addRow(new ItemPanelModel[] {itemModel});
		}

		//select the header text of new lists
		if (hasPath) {
			this.uiTextPane.setSelectionStart(0);
			this.uiTextPane.setSelectionEnd(25);
		}
		
		//inject the new TableModel
		this.uiTable.setModel(tableModel);
		
		this.setBounds(this.getBounds().x, this.getBounds().y, 
				listModel.getFrameSize().width, listModel.getFrameSize().height);
		
	}
	
	private void setTitle () {
		//set title bar
		if (hasPath) {
			File f = new File(path.getFile());
			this.setTitle(f.getName().replace("%20", " ") + " - JayList");
			
		} else {
			
			this.setTitle("new* - JayList");
		}		
	}
	
	private void newItem () {
		DefaultTableModel tableModel =
				(DefaultTableModel)ListFrame.this.uiTable.getModel();
		tableModel.addRow(new ItemPanelModel[] {new ItemPanelModel("<add a title>", "<add a description>", 0, false)});
		
		if (uiTable.isEditing()) {
			uiTable.getCellEditor().stopCellEditing();
		}
		uiTable.getSelectionModel().clearSelection();
		uiTable.changeSelection(uiTable.getRowCount() - 1, 0, true, false);
	}
	
	private void saveAs () {
		JFileChooser fc = new JFileChooser();
		
		int state = fc.showSaveDialog(ListFrame.this);
		if (state == JFileChooser.APPROVE_OPTION) {
			ListMarshall marshall = new ListMarshall();
			
			try {
				path = fc.getSelectedFile().toURI().toURL();
				marshall.marshall(ListFrame.this.extractListModel(), path);
				hasPath = true;
				setTitle();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}			
	}
	
//	private void open () {
////		JFileChooser fc = new JFileChooser();
//////		try {
//////			fc.setCurrentDirectory(new File(ClassLoader.getSystemClassLoader().getResource("/lists").toURI()));
//////		} catch (URISyntaxException e) {
//////			// TODO Auto-generated catch block
//////			e.printStackTrace();
//////		}
////		
////		if (uiTable.isEditing()) {
////			ListFrame.this.uiTable.getCellEditor().stopCellEditing();
////		}
////		ListFrame.this.uiTable.getSelectionModel().clearSelection();
////		
////		int state = fc.showOpenDialog(ListFrame.this);
////		if (state == JFileChooser.APPROVE_OPTION) {
////			ListMarshall marshall = new ListMarshall();
////			try {
////				ListFrame newList;
////				newList = new ListFrame(marshall.unmarshall(fc.getSelectedFile().toURI().toURL()), true);
////				newList.setVisible(true);
////				
////			/*
////			 * catch XML decode and file IO exceptions and notify the
////			 * user via dialog
////			 */
////			} catch (IOException | SAXException
////					| ParserConfigurationException e1) {
////				JOptionPane.showMessageDialog(ListFrame.this, "Can't open the selected file.");
////			}
////		}
//	}

	/**
	 * Creates and returns a  ListModel instance that models the
	 * field values and table item contents. 
	 * 
	 * @return the ListModel
	 */
	private ListFrameModel extractListModel () {
		ListFrameModel listModel = new ListFrameModel();
		listModel.setHeader(this.uiTextPane.getText());
		listModel.setFrameSize(new Dimension(
				this.getBounds().width, this.getBounds().height));
		
		for (int i = 0; i < uiTable.getRowCount(); i++) {
			ItemPanelModel itemModel = (ItemPanelModel)uiTable.getValueAt(i, 0);
			listModel.addItemModels(itemModel);
		}
		
		
		
		return listModel;
	}

	/**
	 * Initialize UI components
	 */
	private void initComponents () {
		
		//set up the JFrame
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100,
				Constants.FRAME_WIDTH, Constants.FRAME_HEIGHT);
//		this.addComponentListener(new ComponentListener() {
//
//			@Override
//			public void componentHidden(ComponentEvent arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void componentMoved(ComponentEvent arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void componentResized(ComponentEvent arg0) {
//				uiTable.repaint();
//			}
//
//			@Override
//			public void componentShown(ComponentEvent arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//		});
		
		
		
		this.setIconImage(Constants.ICON_APP.getImage());
		this.setMinimumSize(new Dimension(Constants.FRAME_WIDTH, 192));
		
		//set up content pane
		this.contentPane = new JPanel();
		//this.contentPane.setBackground(JLConstants.COLOR_GRID);
		this.contentPane.addMouseListener(new ContentPaneMouseListener());
		this.contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		this.contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(this.contentPane);

		//set up the menu items
		this.uiMenuItemSave = new JMenuItem("Save");
		this.uiMenuItemSave.addActionListener(new UIMenuSaveAction());
		this.uiMenuItemNew = new JMenuItem("New");
		this.uiMenuItemNew.addActionListener(new UIMenuNewAction());
		this.uiMenuItemNew.setIcon(new ImageIcon(Constants.ICON_MENU_NEW));
		this.uiMenuItemOpen = new JMenuItem("Open");
		this.uiMenuItemOpen.addActionListener(new UIMenuOpenAction());
		this.uiMenuItemOpen.setIcon(new ImageIcon(Constants.ICON_MENU_OPEN));
		this.uiMenuItemSave.setIcon(new ImageIcon(Constants.ICON_MENU_SAVE));
		
		//set up the header text pane
		this.uiTextPane = new JTextPane();
		this.contentPane.add(this.uiTextPane, BorderLayout.NORTH);
		//center the header text horizontally and vertically
		StyledDocument doc = uiTextPane.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		StyleConstants.setSpaceAbove(center, 4f);
		StyleConstants.setSpaceBelow(center, 4f);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		this.uiTextPane.addFocusListener(new FocusAdapter() {
			
			//stop table cell editing and deselect item when the header
			//gains focus
			@Override
			public void focusGained(FocusEvent arg0) {
				if (uiTable.isEditing()) {
					ListFrame.this.uiTable.getCellEditor().stopCellEditing();
				}
				ListFrame.this.uiTable.getSelectionModel().clearSelection();
			}
			
		});
		this.uiTextPane.setFont(Constants.FONT_HEADER);
		this.uiTextPane.setBackground(Constants.COLOR_HEADER_BG);
		this.uiTextPane.setMinimumSize(new Dimension(6, Constants.HEADER_HEIGHT));
		
		//set up table scroll pane
		this.uiScrollPane = new JScrollPane();
		this.uiScrollPane.setBorder(new EmptyBorder(5, 0, 0, 0));
		this.uiScrollPane.setBackground(Constants.COLOR_GRID);
		this.contentPane.add(this.uiScrollPane, BorderLayout.CENTER);
		
		//set up the table
		this.uiTable = new JTable();
		this.uiTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.uiTable.setFillsViewportHeight(true);
		this.uiTable.setGridColor(Constants.COLOR_GRID);
		this.uiTable.setShowVerticalLines(false);
		this.uiTable.setBorder(new EmptyBorder(0, 0, 0, 0));
		this.uiTable.setTableHeader(null);
		//give the table a single row/column so the renderer and editor can
		//be added
		this.uiTable.setModel(new DefaultTableModel(
			new ItemPanelModel[][] {
				{null},
				{},
			},
			new String[] {
				"Items"
			}
		));
		this.uiScrollPane.setViewportView(this.uiTable);
		this.uiTable.setDefaultRenderer(uiTable.getColumnClass(0), new MagicRenderer());
		this.uiTable.setDefaultEditor(uiTable.getColumnClass(0), new MagicEditor());

		{ //disable the escape keystroke when the jtable is focused
			KeyStroke escapeKey = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
			
			uiTable.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(
					escapeKey, "ESCAPE");
		}
		
		this.uiMenuItemAdd = new JMenuItem("Add Item");
		this.uiMenuItemAdd.addActionListener(new UIItemAddAction());
		this.uiMenuItemAdd.setIcon(Constants.ICON_ADD);
		
		
		this.uiMenuItemRemove = new JMenuItem("Remove Item");
		this.uiMenuItemRemove.setIcon(Constants.ICON_REMOVE);
		this.uiMenuItemRemove.addActionListener(new UIBItemRemoveAction());
		
		this.uiMenuItemSaveAs = new JMenuItem("Save As...");
		this.uiMenuItemSaveAs.addActionListener(new UIMenuSaveAsAction());
		this.uiMenuItemSaveAs.setIcon(Constants.ICON_SAVEAS);
		//this.uiMenuItemSaveAs.addActionListener(new UIBtnRemoveActionListener()); 
		
		//set up the popup menu
		this.uiPopupMenu = new JPopupMenu();
		this.uiPopupMenu.add(uiMenuItemAdd);
		this.uiPopupMenu.add(uiMenuItemRemove);
		this.uiPopupMenu.add(new JSeparator());
		this.uiPopupMenu.add(uiMenuItemNew);
		this.uiPopupMenu.add(uiMenuItemOpen);
		this.uiPopupMenu.add(uiMenuItemSave);
		this.uiPopupMenu.add(uiMenuItemSaveAs);

		this.uiTable.addMouseListener(new PopupListener(uiPopupMenu, new Point(-56, -12)));
		
		this.uiTable.setModel(new DefaultTableModel( ));
		
		initUIKeyBindings();
		
	}
	
	void initUIKeyBindings () {
		
		InputMap tableInputMap = this.uiTable.getInputMap(
				JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		ActionMap actionMap = this.uiTable.getActionMap();
		
		tableInputMap.put(KeyStroke.getKeyStroke(
				KeyEvent.VK_UP, InputEvent.SHIFT_DOWN_MASK), "UP_ARROW");
		actionMap.put("UP_ARROW", new UIRowShiftUpAction());

		tableInputMap.put(KeyStroke.getKeyStroke(
				KeyEvent.VK_DOWN, InputEvent.SHIFT_DOWN_MASK), "DOWN_ARROW");
		actionMap.put("DOWN_ARROW", new UIRowShiftDownAction());

		
	}
	
	void swapListItems (int index1, int index2) {
		DefaultTableModel tableModel = 
				(DefaultTableModel) this.uiTable.getModel();
		
		ItemPanelModel item1 =
				(ItemPanelModel) tableModel.getValueAt(index1, 0);

		ItemPanelModel item2 =
				(ItemPanelModel) tableModel.getValueAt(index2, 0);

		tableModel.setValueAt(item1, index2, 0);
		tableModel.setValueAt(item2, index1, 0);
		
		
		
	}

	@SuppressWarnings("serial")
	class UIRowShiftUpAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println(uiTable.getSelectedRow() + ": UP");
			int rowIndex = uiTable.getSelectedRow();
			if ((rowIndex < 1) || (uiTable.getRowCount() < 2)) {
				return;
			}
			
			uiTable.getSelectionModel().clearSelection();
			
			//swap the selected item with the one above it
			stopEditingClearSelection();
			swapListItems (rowIndex, rowIndex - 1);
			uiTable.getSelectionModel().setSelectionInterval(rowIndex - 1, rowIndex - 1);
		}
	}

	@SuppressWarnings("serial")
	class UIRowShiftDownAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println(uiTable.getSelectedRow() + ": DOWN");
			int rowIndex = uiTable.getSelectedRow();
			if ((rowIndex == -1)
					|| (rowIndex == uiTable.getRowCount() - 1)
					|| (uiTable.getRowCount() < 2)) {
				return;
			}
			
			//swap the selected row with the one below
			stopEditingClearSelection();
			swapListItems (rowIndex, rowIndex + 1);	
			uiTable.getSelectionModel().setSelectionInterval(rowIndex + 1, rowIndex + 1);
		}
	}
	
	/*
	 * Instantiates and renders an ItemPanel based on ItemModel
	 * instances encapsulated in the table model.
	 */
	class MagicRenderer implements TableCellRenderer {

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {

			ItemPanel itemPanel = new ItemPanel((ItemPanelModel)value, isSelected,
					uiTable, row, uiPopupMenu);
			return itemPanel;
		}
		
	}
	

	/*
	 * 
	 */
	@SuppressWarnings("serial")
	class MagicEditor extends AbstractCellEditor implements TableCellEditor {

		//the itempanel that is currently being added
		ItemPanel itemPanel;
		
		/*
		 * forget the changes and deselect the editing item
		 */
		@Override
		public void cancelCellEditing () {
			ListFrame.this.uiTable.getSelectionModel().clearSelection();
			fireEditingCanceled();			
		}
		
		/*
		 * deselect the editing item and apply the changes
		 */
		@Override
		public boolean stopCellEditing () {
			ListFrame.this.uiTable.getSelectionModel().clearSelection();
			fireEditingStopped();
			return true;
		}
		
		/*
		 * return a new ItemModel instance patterned after the 
		 * field values of the currently editing ItemPanel
		 */
		@Override
		public Object getCellEditorValue () {
			return itemPanel.toItemModel();
		}

		/*
		 * return a new ItemPanel instance representing the
		 * selected table ItemModel
		 */
		@Override
		public Component getTableCellEditorComponent (JTable table, Object value,
				boolean isSelected, int row, int column) {

			itemPanel = new ItemPanel((ItemPanelModel)value, true, uiTable,
					row, uiPopupMenu);
			return itemPanel;
		}
		
	}

	private class UIItemAddAction implements ActionListener {

		/*
		 * add a new ItemModel to the list and select the new
		 * item for editing
		 */
		public void actionPerformed(ActionEvent arg0) {
			newItem();
		}
	}
	
	/*
	 * deselect and stop editing when the content pane is clicked
	 */
	private class ContentPaneMouseListener extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent arg0) {
			stopEditingClearSelection();
		}
	}
	
	
	void stopEditingClearSelection () {
		if (uiTable.isEditing()) {
			ListFrame.this.uiTable.getCellEditor().stopCellEditing();
		}
		ListFrame.this.uiTable.getSelectionModel().clearSelection();
	}
	
	private class UIMenuSaveAction implements ActionListener {
		
		/*
		 * instantiates a JFileChooser, builds a ListModel from the
		 * and uses the ListMarshal to save an XML representation
		 * of the ListModel
		 */
		public void actionPerformed(ActionEvent arg0) {
			
			URL savePath;
			
			stopEditingClearSelection();
			
			/*
			 * if the list is new then trigger the Save As save action 
			 */
			if (hasPath) {
				
				savePath = path;
				ListMarshall marshall = new ListMarshall();
				marshall.marshall(ListFrame.this.extractListModel(), savePath);
				
			} else {
				
				saveAs();
			
			}
			
			
			
			
			
		}

	
	}
	
	/*
	 * 
	 */
	private class UIMenuOpenAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
			stopEditingClearSelection();
			
			launcher.openList();
			
		}
	}
	
	
	private class UIMenuNewAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
			stopEditingClearSelection();
			
			launcher.newList();
			
		}
	}
	
	private class UIMenuSaveAsAction implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			saveAs();
		}
	}
	
	/*
	 * 
	 */
	private class UIBItemRemoveAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
			DefaultTableModel tableModel = (DefaultTableModel)uiTable.getModel();
			int row = uiTable.getSelectedRow();
			
			
			if (row != -1) {
				int choice = JOptionPane.showConfirmDialog(
						ListFrame.this,
						"Are you sure you want to remove this item?",
						"Remove Item Confirmation",
						JOptionPane.YES_NO_OPTION, 
						JOptionPane.WARNING_MESSAGE);
				
				if (choice == JOptionPane.OK_OPTION) {
				
					stopEditingClearSelection();
					
					tableModel.removeRow(row);
					
				}
			}
		}
	}	
}

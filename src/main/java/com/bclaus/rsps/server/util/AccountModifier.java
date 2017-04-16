package com.bclaus.rsps.server.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.bclaus.rsps.server.vd.items.Item;
import com.bclaus.rsps.server.vd.items.ItemAssistant;

/**
 * 
 * @author Jason MacKeigan http://www.rune-server.org/members/jason
 * @date Jul 1, 2014, 8:40:32 AM
 */
public class AccountModifier extends JFrame implements ActionListener, ListSelectionListener {

	private JTextArea outputArea;

	private JList<String> accountList;

	private DefaultListModel<String> accountListModel;

	private JScrollPane accountListScrollpane, outputScrollpane;

	private JPanel accountSettingsPanel, itemRemovalPanel, searchResultsPanel, searchResultsSubPanel1, searchResultsSubPanel2, searchResultsSubPanel3, searchResultsSubPanel4;

	private FileOperations fileOperations;

	private JTabbedPane tabbedPane;

	private JToggleButton accountSettingsToggle, bankToggleButton, inventoryToggleButton;

	private ImageIcon onIcon = new ImageIcon("./sprites/ON.PNG");

	private ImageIcon offIcon = new ImageIcon("./sprites/OFF.PNG");

	private JTextField itemRemovalIdField, itemRemovalAmountField, specificStringField, searchItemIdField, searchItemAmountField, searchValueField, searchAmountOfItemsField;

	private JButton deleteItemButton, searchResultsButton1, searchResultsButton2, searchResultsButton3, searchResultsButton4;

	private JLabel inventoryLabel, bankLabel, accountSettingsLabel, specificStringLabel, searchItemIdLabel, searchItemAmountLabel, searchValueLabel, searchAmountOfItemsLabel;

	private String selectedListName = "";

	private boolean contentExists;

	public AccountModifier(boolean visible) {
		super("Character Modifier");
		try {
			setSize(900, 510);
			setLayout(null);
			setPreferredSize(new Dimension(900, 510));
			setResizable(false);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			initialize();
		} catch (Exception e) {
			e.printStackTrace();
		}
		setVisible(visible);
	}

	private void initialize() {
		fileOperations = new FileOperations();
		fileOperations.load();
		outputArea = new JTextArea();
		outputScrollpane = new JScrollPane(outputArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		accountListModel = new DefaultListModel<String>();
		accountList = new JList<String>(accountListModel);
		accountSettingsPanel = new JPanel(null);
		itemRemovalPanel = new JPanel(null);
		searchResultsPanel = new JPanel(null);
		searchResultsSubPanel1 = new JPanel(null);
		searchResultsSubPanel2 = new JPanel(null);
		searchResultsSubPanel3 = new JPanel(null);
		searchResultsSubPanel4 = new JPanel(null);
		tabbedPane = new JTabbedPane();
		accountSettingsToggle = new JToggleButton();
		accountListScrollpane = new JScrollPane(accountList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		itemRemovalIdField = new JTextField();
		itemRemovalAmountField = new JTextField("0");
		specificStringField = new JTextField();
		searchItemIdField = new JTextField();
		searchItemAmountField = new JTextField();
		searchValueField = new JTextField();
		searchAmountOfItemsField = new JTextField();
		deleteItemButton = new JButton("Remove Amount");
		searchResultsButton1 = new JButton("Search");
		searchResultsButton2 = new JButton("Search");
		searchResultsButton3 = new JButton("Search");
		searchResultsButton4 = new JButton("Search");
		bankToggleButton = new JToggleButton();
		inventoryToggleButton = new JToggleButton();
		inventoryLabel = new JLabel("Check Inventory:");
		bankLabel = new JLabel("Check Bank:");
		accountSettingsLabel = new JLabel("Target All Accounts:");
		specificStringLabel = new JLabel("String To Search:");
		searchItemIdLabel = new JLabel("Item Id:");
		searchItemAmountLabel = new JLabel("Item Amount:");
		searchValueLabel = new JLabel("Minimum Total Item Value:");
		searchAmountOfItemsLabel = new JLabel("Minimum Amount Of Items:");
		addListeners();
		addComponents();
	}

	private void addListeners() {
		this.accountList.addListSelectionListener(this);
		this.deleteItemButton.addActionListener(this);
		this.searchResultsButton1.addActionListener(this);
		this.searchResultsButton2.addActionListener(this);
		this.searchResultsButton3.addActionListener(this);
		this.searchResultsButton4.addActionListener(this);
	}

	private void addComponents() {
		add(accountSettingsPanel);
		add(outputScrollpane);
		add(tabbedPane);
		accountSettingsPanel.add(accountListScrollpane);
		accountSettingsPanel.add(accountSettingsToggle);
		accountSettingsPanel.add(accountSettingsLabel);
		itemRemovalPanel.add(itemRemovalIdField);
		itemRemovalPanel.add(itemRemovalAmountField);
		itemRemovalPanel.add(deleteItemButton);
		accountSettingsPanel.add(bankToggleButton);
		accountSettingsPanel.add(inventoryToggleButton);
		accountSettingsPanel.add(bankLabel);
		accountSettingsPanel.add(inventoryLabel);
		searchResultsPanel.add(searchResultsSubPanel1);
		searchResultsPanel.add(searchResultsSubPanel2);
		searchResultsPanel.add(searchResultsSubPanel3);
		searchResultsPanel.add(searchResultsSubPanel4);
		searchResultsSubPanel1.add(searchResultsButton1);
		searchResultsSubPanel1.add(specificStringField);
		searchResultsSubPanel1.add(specificStringLabel);
		searchResultsSubPanel2.add(searchResultsButton2);
		searchResultsSubPanel2.add(searchItemAmountField);
		searchResultsSubPanel2.add(searchItemAmountLabel);
		searchResultsSubPanel2.add(searchItemIdField);
		searchResultsSubPanel2.add(searchItemIdLabel);
		searchResultsSubPanel3.add(searchResultsButton3);
		searchResultsSubPanel3.add(searchValueLabel);
		searchResultsSubPanel3.add(searchValueField);
		searchResultsSubPanel4.add(searchResultsButton4);
		searchResultsSubPanel4.add(searchAmountOfItemsField);
		searchResultsSubPanel4.add(searchAmountOfItemsLabel);
		updateComponents();
	}

	private void updateComponents() {
		outputArea.setForeground(new Color(30, 160, 0));
		outputArea.setBackground(Color.BLACK);
		outputArea.setRows(20000);
		outputArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
		outputArea.validate();
		outputScrollpane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED), "Output"));
		outputScrollpane.setOpaque(false);
		outputScrollpane.setBounds(20, 205, 860, 270);
		populateComboBox();
		accountListScrollpane.setBounds(0, 0, 150, 155);
		accountList.setOpaque(false);
		tabbedPane.setBounds(20, 5, 860, 200);
		tabbedPane.addTab("Account Settings", accountSettingsPanel);
		tabbedPane.addTab("Item Removal", itemRemovalPanel);
		tabbedPane.addTab("Search Results", searchResultsPanel);
		accountSettingsToggle.setBounds(289, 7, 40, 15);
		accountSettingsToggle.setIcon(offIcon);
		accountSettingsToggle.setSelectedIcon(onIcon);
		accountSettingsToggle.setToolTipText("Toggles on and off.");
		accountSettingsToggle.setBorder(null);
		accountSettingsLabel.setBounds(160, 2, 200, 20);
		itemRemovalIdField.setBounds(5, 5, 100, 38);
		itemRemovalIdField.setBorder(BorderFactory.createTitledBorder("Item Id"));
		itemRemovalIdField.setOpaque(false);
		itemRemovalIdField.setToolTipText("The identification value for the item.");
		itemRemovalAmountField.setBounds(120, 5, 180, 38);
		itemRemovalAmountField.setBorder(BorderFactory.createTitledBorder("Removal Amount"));
		itemRemovalAmountField.setOpaque(false);
		itemRemovalAmountField.setToolTipText("The amount subtracted from the total sum of items in each character file. Leave at 0 if deleting all.");
		deleteItemButton.setBounds(0, 45, 160, 30);
		inventoryToggleButton.setBounds(267, 26, 40, 15);
		inventoryToggleButton.setIcon(offIcon);
		inventoryToggleButton.setSelectedIcon(onIcon);
		inventoryToggleButton.setToolTipText("Toggles on and off.");
		inventoryToggleButton.setBorder(null);
		bankToggleButton.setBounds(237, 46, 40, 15);
		bankToggleButton.setIcon(offIcon);
		bankToggleButton.setSelectedIcon(onIcon);
		bankToggleButton.setToolTipText("Toggles on and off.");
		bankToggleButton.setBorder(null);
		inventoryLabel.setBounds(160, 22, 200, 20);
		bankLabel.setBounds(160, 42, 200, 20);
		searchResultsSubPanel1.setBounds(4, 0, 200, 150);
		searchResultsSubPanel1.setBorder(BorderFactory.createTitledBorder("Search Specific String"));
		searchResultsSubPanel2.setBounds(212, 0, 200, 150);
		searchResultsSubPanel2.setBorder(BorderFactory.createTitledBorder("Search Item Id & Amount"));
		searchResultsSubPanel3.setBounds(422, 0, 200, 150);
		searchResultsSubPanel3.setBorder(BorderFactory.createTitledBorder("Search Total Value"));
		searchResultsSubPanel4.setBounds(630, 0, 200, 150);
		searchResultsSubPanel4.setBorder(BorderFactory.createTitledBorder("Search Amount of Item"));

		searchResultsButton1.setBounds(0, 120, 200, 30);
		searchResultsButton2.setBounds(0, 120, 200, 30);
		searchResultsButton3.setBounds(0, 120, 200, 30);
		searchResultsButton4.setBounds(0, 120, 200, 30);

		specificStringLabel.setBounds(9, 20, 200, 20);
		specificStringField.setBounds(7, 43, 186, 20);

		searchItemIdLabel.setBounds(9, 20, 200, 20);
		searchItemIdField.setBounds(7, 43, 186, 20);
		searchItemAmountLabel.setBounds(9, 70, 200, 20);
		searchItemAmountField.setBounds(7, 93, 186, 20);

		searchValueLabel.setBounds(9, 20, 200, 20);
		searchValueField.setBounds(7, 43, 186, 20);

		searchAmountOfItemsLabel.setBounds(9, 20, 200, 20);
		searchAmountOfItemsField.setBounds(7, 43, 186, 20);
	}

	private void populateComboBox() {
		for (File account : fileOperations.getAccounts()) {
			if (!account.isDirectory())
				accountListModel.addElement(account.getName());
		}
	}

	private void searchItemAmount() {
		boolean checkInventory = this.inventoryToggleButton.isSelected() ? true : false;
		boolean checkBank = this.bankToggleButton.isSelected() ? true : false;
		if (!checkBank && !checkInventory) {
			sendOutputLn("You must choose to check the inventory, or bank.");
			return;
		}
		if (this.searchAmountOfItemsField.getText().length() < 1) {
			sendOutputLn("You must enter atleast one character into the required field.");
			return;
		}
		Pattern pattern = Pattern.compile("\\d");
		Matcher match = null;
		for (char c : searchAmountOfItemsField.getText().toCharArray()) {
			match = pattern.matcher(Character.toString(c));
			if (!match.matches()) {
				sendOutputLn("The value you have entered is illegal.");
				return;
			}
		}
		contentExists = false;
		long textValue = Long.parseLong(searchAmountOfItemsField.getText());
		if (textValue > Integer.MAX_VALUE)
			searchAmountOfItemsField.setText(Integer.toString(Integer.MAX_VALUE));
		int itemId = -1;
		int itemAmount = Integer.parseInt(searchAmountOfItemsField.getText());
		if (itemAmount == 0)
			itemAmount = Integer.MAX_VALUE;
		if (!accountSettingsToggle.isSelected() && !selectedListName.isEmpty()) {
			File character = fileOperations.getFile(selectedListName);
			if (character == null) {
				sendOutputLn("The character file '" + selectedListName + "' cannot be found.");
				return;
			}
			fileOperations.read(character, checkBank, checkInventory, itemId, itemAmount, "search_amount");
		} else if (accountSettingsToggle.isSelected()) {
			int reviews = 0;
			for (File file : fileOperations.getAccounts()) {
				if (file == null)
					continue;
				reviews++;
				fileOperations.read(file, checkBank, checkInventory, itemId, itemAmount, "search_amount");
			}
		}
		if (!contentExists)
			sendOutputLn("[SEARCH] No character account could be found with any item with an amount of " + itemAmount + ".");
	}

	private void searchTotalValue() {
		boolean checkInventory = this.inventoryToggleButton.isSelected() ? true : false;
		boolean checkBank = this.bankToggleButton.isSelected() ? true : false;
		if (!checkBank && !checkInventory) {
			sendOutputLn("You must choose to check the inventory, or bank.");
			return;
		}
		if (this.searchValueField.getText().length() < 1) {
			sendOutputLn("You must enter atleast one character into the value field.");
			return;
		}
		Pattern pattern = Pattern.compile("\\d");
		Matcher match = null;
		for (char c : searchValueField.getText().toCharArray()) {
			match = pattern.matcher(Character.toString(c));
			if (!match.matches()) {
				sendOutputLn("The value you have entered is illegal.");
				return;
			}
		}
		contentExists = false;
		long textValue = Long.parseLong(this.searchValueField.getText());
		if (textValue > Integer.MAX_VALUE)
			this.searchValueField.setText(Integer.toString(Integer.MAX_VALUE));
		int itemId = -1;
		int itemAmount = Integer.parseInt(searchValueField.getText());
		if (itemAmount == 0)
			itemAmount = Integer.MAX_VALUE;
		if (!accountSettingsToggle.isSelected() && !selectedListName.isEmpty()) {
			File character = fileOperations.getFile(selectedListName);
			if (character == null) {
				sendOutputLn("The character file '" + selectedListName + "' cannot be found.");
				return;
			}
			fileOperations.read(character, checkBank, checkInventory, itemId, itemAmount, "search_value");
		} else if (accountSettingsToggle.isSelected()) {
			for (File file : fileOperations.getAccounts()) {
				if (file == null)
					continue;
				fileOperations.read(file, checkBank, checkInventory, itemId, itemAmount, "search_value");
			}
		}
		if (!contentExists)
			sendOutputLn("[SEARCH] No character account could be found with any items valued over " + itemAmount + ".");
	}

	public void searchItemAndAmount() {
		boolean checkInventory = this.inventoryToggleButton.isSelected() ? true : false;
		boolean checkBank = this.bankToggleButton.isSelected() ? true : false;
		if (!checkBank && !checkInventory) {
			sendOutputLn("You must choose to check the inventory, or bank.");
			return;
		}
		if (this.searchItemIdField.getText().length() < 1) {
			sendOutputLn("You must enter atleast one character into the item id field.");
			return;
		}
		if (this.searchItemAmountField.getText().length() < 1) {
			sendOutputLn("You must enter atleast one character into the item amount field.");
			return;
		}
		Pattern pattern = Pattern.compile("\\d");
		Matcher match = null;
		for (char c : searchItemIdField.getText().toCharArray()) {
			match = pattern.matcher(Character.toString(c));
			if (!match.matches()) {
				sendOutputLn("The value you have entered for the item id is illegal.");
				return;
			}
		}
		for (char c : searchItemAmountField.getText().toCharArray()) {
			match = pattern.matcher(Character.toString(c));
			if (!match.matches()) {
				sendOutputLn("The value you have entered for the item amount is illegal.");
				return;
			}
		}
		long textValue = Long.parseLong(this.searchItemIdField.getText());
		if (textValue > Integer.MAX_VALUE)
			this.searchItemIdField.setText(Integer.toString(Integer.MAX_VALUE));
		textValue = Long.parseLong(this.searchItemIdField.getText());
		if (textValue > Integer.MAX_VALUE)
			this.searchItemAmountField.setText(Integer.toString(Integer.MAX_VALUE));
		int itemId = Integer.parseInt(this.searchItemIdField.getText()) + 1;
		int itemAmount = Integer.parseInt(this.searchItemAmountField.getText());
		if (itemAmount == 0)
			itemAmount = Integer.MAX_VALUE;
		if (!accountSettingsToggle.isSelected() && !selectedListName.isEmpty()) {
			File character = fileOperations.getFile(selectedListName);
			if (character == null) {
				sendOutputLn("The character file '" + selectedListName + "' cannot be found.");
				return;
			}
			fileOperations.read(character, checkBank, checkInventory, itemId, itemAmount, "search_item_amount");
		} else if (accountSettingsToggle.isSelected()) {
			for (File file : fileOperations.getAccounts()) {
				if (file == null)
					continue;
				fileOperations.read(file, checkBank, checkInventory, itemId, itemAmount, "search_item_amount");
			}
		}
	}

	private void searchString() {
		if (this.specificStringField.getText().length() < 2) {
			sendOutputLn("You must enter atleast two characters into the text field.");
			return;
		}
		contentExists = false;
		if (!accountSettingsToggle.isSelected() && !selectedListName.isEmpty()) {
			File character = fileOperations.getFile(selectedListName);
			if (character == null) {
				sendOutputLn("The character file '" + selectedListName + "' cannot be found.");
				return;
			}
			fileOperations.read(character, "search_string");
		} else if (accountSettingsToggle.isSelected()) {
			for (File file : fileOperations.getAccounts()) {
				if (file == null)
					continue;
				fileOperations.read(file, "search_string");
			}
		}
		if (!contentExists)
			sendOutputLn("[SEARCH] The string (" + specificStringField.getText() + ") could not be found in the file(s).");
	}

	private void removeItem() {
		boolean checkInventory = this.inventoryToggleButton.isSelected() ? true : false;
		boolean checkBank = this.bankToggleButton.isSelected() ? true : false;
		if (!checkBank && !checkInventory) {
			sendOutputLn("You must choose to check the inventory, or bank.");
			return;
		}
		if (this.itemRemovalIdField.getText().isEmpty()) {
			sendOutputLn("Enter a feasable item id in the item id field.");
			return;
		}
		if (this.itemRemovalAmountField.getText().isEmpty()) {
			sendOutputLn("Enter a feasable item amount in the item amount field.");
			return;
		}
		Pattern pattern = Pattern.compile("\\d");
		Matcher match = null;
		for (char c : itemRemovalIdField.getText().toCharArray()) {
			match = pattern.matcher(Character.toString(c));
			if (!match.matches()) {
				sendOutputLn("The value you have entered for the item id is illegal.");
				return;
			}
		}
		for (char c : itemRemovalAmountField.getText().toCharArray()) {
			match = pattern.matcher(Character.toString(c));
			if (!match.matches()) {
				sendOutputLn("The value you have entered for the item amount is illegal.");
				return;
			}
		}
		long textValue = Long.parseLong(this.itemRemovalAmountField.getText());
		if (textValue > Integer.MAX_VALUE)
			this.itemRemovalAmountField.setText(Integer.toString(Integer.MAX_VALUE));
		textValue = Long.parseLong(this.itemRemovalIdField.getText());
		if (textValue > Integer.MAX_VALUE)
			this.itemRemovalIdField.setText(Integer.toString(Integer.MAX_VALUE));
		int itemId = Integer.parseInt(this.itemRemovalIdField.getText()) + 1;
		int itemAmount = Integer.parseInt(this.itemRemovalAmountField.getText());
		if (itemAmount == 0)
			itemAmount = Integer.MAX_VALUE;
		contentExists = false;
		if (!accountSettingsToggle.isSelected() && !selectedListName.isEmpty()) {
			File character = fileOperations.getFile(selectedListName);
			if (character == null) {
				sendOutputLn("The character file '" + selectedListName + "' cannot be found.");
				return;
			}
			fileOperations.read(character, checkBank, checkInventory, itemId, itemAmount, "delete");
		} else if (accountSettingsToggle.isSelected()) {
			for (File file : fileOperations.getAccounts()) {
				if (file == null)
					continue;
				fileOperations.read(file, checkBank, checkInventory, itemId, itemAmount, "delete");
			}
		}
		if (!contentExists)
			sendOutputLn("[REMOVE] The item " + itemId + " was not found in the file(s).");
	}

	private void sendOutput(String output) {
		this.outputArea.setText(this.outputArea.getText() + output);
	}

	private void sendOutputLn(String output) {
		if (this.outputArea.getText().isEmpty())
			this.outputArea.setText(output);
		else
			this.outputArea.setText(this.outputArea.getText() + "\n" + output);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JComponent component = (JComponent) e.getSource();
		if (component == this.deleteItemButton)
			removeItem();
		else if (component == this.searchResultsButton1)
			searchString();
		else if (component == this.searchResultsButton2)
			searchItemAndAmount();
		else if (component == this.searchResultsButton3)
			searchTotalValue();
		else if (component == this.searchResultsButton4)
			searchItemAmount();
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		JComponent component = (JComponent) e.getSource();
		if (component == accountList) {
			selectedListName = accountList.getSelectedValue();
		}
	}

	public boolean isContentExists() {
		return contentExists;
	}

	public void setContentExists(boolean contentExists) {
		this.contentExists = contentExists;
	}

	class FileOperations {
		File[] accounts = null;

		void load() {
			File[] folder = new File("./Data/characters/").listFiles();
			accounts = new File[folder.length];
			for (int i = 0; i < folder.length; i++)
				accounts[i] = folder[i];
		}

		File[] getAccounts() {
			return accounts;
		}

		File getFile(String name) {
			if (!name.contains(".txt"))
				name = name + ".txt";
			for (File file : accounts) {
				if (file == null)
					continue;
				if (file.getName().equalsIgnoreCase(name))
					return file;
			}
			return null;
		}

		void write(File character, ArrayList<String> lines) {
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(character))) {
				for (String line : lines) {
					if (line != null) {
						writer.write(line);
						writer.newLine();
					}
				}
				writer.close();
			} catch (Exception e) {
				sendOutputLn("[ERROR] Unable to write to file, check stack.");
				e.printStackTrace();
			}
		}

		void read(File character, String type) {
			if (character.getName().contains(".backup"))
				return;
			try (BufferedReader reader = new BufferedReader(new FileReader(character))) {
				String line;
				while ((line = reader.readLine()) != null) {
					if (type.equals("search_string")) {
						if (line.toLowerCase().contains(specificStringField.getText().toLowerCase())) {
							sendOutputLn("[SEARCH] " + character.getName() + ", match found: " + line);
							contentExists = true;
						}
					}
				}
				reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		void read(File character, boolean checkBank, boolean checkInventory, int itemId, int itemAmount, String type) {
			if (character.getName().contains(".backup"))
				return;
			boolean characterModified = false;
			ArrayList<String> characterLineList = new ArrayList<String>();
			try (BufferedReader reader = new BufferedReader(new FileReader(character))) {
				String line;
				while ((line = reader.readLine()) != null) {
					String[] contents;
					boolean skipLine = false;
					if (checkInventory) {
						if (line.startsWith("character-item")) {
							String line2 = line.substring(17, line.length());
							contents = line2.split("\t");
							int fileItemSlot = Integer.parseInt(contents[0]);
							int fileItemId = Integer.parseInt(contents[1]);
							int fileItemAmount = Integer.parseInt(contents[2]);
							if (fileItemId == itemId) {
								if (type.equals("delete")) {
									sendOutputLn("[INV][" + character.getName() + "] Contains " + fileItemAmount + " x " + Item.getItemName(itemId - 1) + ". " + (fileItemAmount - itemAmount <= 0 ? fileItemAmount : itemAmount) + " sucessfully removed.");
									contentExists = true;
									if (fileItemAmount - itemAmount <= 0)
										fileItemAmount = 0;
									else
										fileItemAmount -= itemAmount;
									if (fileItemAmount <= 0) {
										skipLine = true;
									} else {
										line = "character-item = " + fileItemSlot + "\t" + fileItemId + "\t" + fileItemAmount;
									}
									characterModified = true;
								} else if (type.equals("search_item_amount")) {
									if (fileItemAmount >= itemAmount) {
										sendOutputLn("[INV][" + character.getName() + "] Contains " + fileItemAmount + " x " + Item.getItemName(itemId - 1) + ".");
										characterModified = false;
										contentExists = true;
									}
								} else if (type.equals("search_value")) {
									long value = (long) (fileItemAmount * ItemAssistant.getItemDef(fileItemId - 1).get().ShopValue);
									if (value > itemAmount) {
										sendOutputLn("[INV][" + character.getName() + "] Contains " + fileItemAmount + " x " + Item.getItemName(fileItemId - 1) + ", Value: " + value);
										characterModified = false;
										contentExists = true;
									}
								} else if (type.equals("search_amount")) {
									if (fileItemAmount >= itemAmount) {
										sendOutputLn("[INV][" + character.getName() + "] Contains " + fileItemAmount + " x " + Item.getItemName(fileItemId - 1));
										characterModified = false;
										contentExists = true;
									}
								}
							}
						}
					}
					if (checkBank) {
						if (line.startsWith("bank-tab")) {
							String line2 = line.substring(11, line.length());
							contents = line2.split("\t");
							int fileItemSlot = Integer.parseInt(contents[0]);
							int fileItemId = Integer.parseInt(contents[1]);
							int fileItemAmount = Integer.parseInt(contents[2]);
							if (type.equals("delete")) {
								if (fileItemId == itemId) {
									sendOutputLn("[BANK][" + character.getName() + "] Contains " + fileItemAmount + " x " + Item.getItemName(itemId - 1) + ". " + (fileItemAmount - itemAmount <= 0 ? fileItemAmount : itemAmount) + " sucessfully removed.");
									contentExists = true;
									if (fileItemAmount - itemAmount <= 0)
										fileItemAmount = 0;
									else
										fileItemAmount -= itemAmount;
									if (fileItemAmount <= 0) {
										skipLine = true;
									} else {
										line = "bank-tab = " + fileItemSlot + "\t" + fileItemId + "\t" + fileItemAmount;
									}
									characterModified = true;
								}
							} else if (type.equals("search_item_amount")) {
								if (fileItemId == itemId) {
									if (fileItemAmount >= itemAmount) {
										sendOutputLn("[BANK][" + character.getName() + "] Contains " + fileItemAmount + " x " + Item.getItemName(itemId - 1) + ".");
										characterModified = false;
										contentExists = true;
									}
								}
							} else if (type.equals("search_value")) {
								long value = (long) (fileItemAmount * ItemAssistant.getItemDef(fileItemId - 1).get().ShopValue);
								if (value > itemAmount) {
									sendOutputLn("[BANK][" + character.getName() + "] Contains " + fileItemAmount + " x " + Item.getItemName(fileItemId - 1) + ", Value: " + value);
									characterModified = false;
									contentExists = true;
								}
							} else if (type.equals("search_amount")) {
								if (fileItemAmount >= itemAmount) {
									sendOutputLn("[BANK][" + character.getName() + "] Contains " + fileItemAmount + " x " + Item.getItemName(fileItemId - 1));
									characterModified = false;
									contentExists = true;
								}
							}
						}
					}
					if (!skipLine)
						characterLineList.add(line);
				}
				reader.close();
			} catch (Exception e) {
				sendOutput("[ERROR] Unable to read character file, check stack.");
				e.printStackTrace();
			}
			if (characterModified)
				write(character, characterLineList);
		}

	}

}

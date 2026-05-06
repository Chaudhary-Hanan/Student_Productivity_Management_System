package gui;

import models.Goal;
import services.AuthService;
import services.FileManager;
import utils.UITheme;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class GoalsFrame extends JFrame {

	private ArrayList<Goal> goals;
	private JPanel goalsContainer;
	private String goalFile;
	private JComboBox<String> cmbFilter;

	public GoalsFrame() {
		goalFile = "goals_" + AuthService.getCurrentUser().getUsername() + ".dat";
		goals = FileManager.loadObjects(goalFile);
		if (goals == null) goals = new ArrayList<>();

		initComponents();
		refreshGoals("All");
	}

	private void initComponents() {
		getContentPane().setLayout(new BorderLayout());
		getContentPane().setBackground(UITheme.BG_LIGHT);

		// Top bar
		JPanel topBar = new JPanel(null);
		topBar.setBackground(Color.WHITE);
		topBar.setPreferredSize(new Dimension(1020, 70));
		topBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UITheme.BORDER));

		JLabel lblPageTitle = new JLabel("Goals");
		lblPageTitle.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 22));
		lblPageTitle.setForeground(UITheme.TEXT_DARK);
		lblPageTitle.setBounds(30, 20, 400, 30);
		topBar.add(lblPageTitle);

		getContentPane().add(topBar, BorderLayout.NORTH);

		// Main panel
		JPanel mainPanel = new JPanel(null);
		mainPanel.setBackground(UITheme.BG_LIGHT);

		JLabel lblHeading = new JLabel("Track Your Academic Goals");
		lblHeading.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 20));
		lblHeading.setForeground(UITheme.TEXT_DARK);
		lblHeading.setBounds(30, 25, 400, 28);
		mainPanel.add(lblHeading);

		JLabel lblSub = new JLabel("Set weekly or monthly study targets and watch your progress");
		lblSub.setFont(new Font(UITheme.FONT_FAMILY, Font.PLAIN, 13));
		lblSub.setForeground(UITheme.TEXT_GRAY);
		lblSub.setBounds(30, 55, 500, 20);
		mainPanel.add(lblSub);

		// Filter
		JLabel lblFilter = new JLabel("Show:");
		lblFilter.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 12));
		lblFilter.setForeground(UITheme.TEXT_GRAY);
		lblFilter.setBounds(30, 95, 50, 25);
		mainPanel.add(lblFilter);

		cmbFilter = new JComboBox<>(new String[]{"All", "Weekly", "Monthly", "Active", "Achieved"});
		cmbFilter.setFont(new Font(UITheme.FONT_FAMILY, Font.PLAIN, 12));
		cmbFilter.setBounds(80, 92, 150, 32);
		cmbFilter.setBackground(Color.WHITE);
		cmbFilter.addActionListener(e -> refreshGoals((String) cmbFilter.getSelectedItem()));
		mainPanel.add(cmbFilter);

		// Add button
		JButton btnAdd = new JButton("+  Add New Goal");
		btnAdd.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 13));
		btnAdd.setBackground(UITheme.PRIMARY);
		btnAdd.setForeground(Color.WHITE);
		btnAdd.setFocusPainted(false);
		btnAdd.setBorderPainted(false);
		btnAdd.setBounds(810, 88, 160, 40);
		btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnAdd.addActionListener(e -> showGoalDialog(null));
		mainPanel.add(btnAdd);

		// Scrollable goals container
		goalsContainer = new JPanel();
		goalsContainer.setLayout(new BoxLayout(goalsContainer, BoxLayout.Y_AXIS));
		goalsContainer.setBackground(UITheme.BG_LIGHT);

		JScrollPane scroll = new JScrollPane(goalsContainer);
		scroll.setBounds(30, 145, 940, 480);
		scroll.setBorder(null);
		scroll.getVerticalScrollBar().setUnitIncrement(16);
		scroll.setBackground(UITheme.BG_LIGHT);
		scroll.getViewport().setBackground(UITheme.BG_LIGHT);
		mainPanel.add(scroll);

		getContentPane().add(mainPanel, BorderLayout.CENTER);
	}

	private void refreshGoals(String filter) {
		goalsContainer.removeAll();

		ArrayList<Goal> filtered = new ArrayList<>();
		for (Goal g : goals) {
			if (filter.equals("Weekly") && !g.getType().equals("WEEKLY")) continue;
			if (filter.equals("Monthly") && !g.getType().equals("MONTHLY")) continue;
			if (filter.equals("Active") && g.isAchieved()) continue;
			if (filter.equals("Achieved") && !g.isAchieved()) continue;
			filtered.add(g);
		}

		if (filtered.isEmpty()) {
			JPanel empty = new JPanel(new BorderLayout());
			empty.setBackground(Color.WHITE);
			empty.setBorder(BorderFactory.createLineBorder(UITheme.BORDER));
			empty.setMaximumSize(new Dimension(920, 200));
			empty.setPreferredSize(new Dimension(920, 200));

			JLabel lbl = new JLabel("<html><center>No goals to show.<br><br>" +
					"Click 'Add New Goal' to set your first target!</center></html>",
					SwingConstants.CENTER);
			lbl.setFont(new Font(UITheme.FONT_FAMILY, Font.PLAIN, 14));
			lbl.setForeground(UITheme.TEXT_GRAY);
			empty.add(lbl);
			goalsContainer.add(empty);
		} else {
			for (Goal g : filtered) {
				goalsContainer.add(createGoalCard(g));
				goalsContainer.add(Box.createRigidArea(new Dimension(0, 15)));
			}
		}

		goalsContainer.revalidate();
		goalsContainer.repaint();
	}

	private JPanel createGoalCard(Goal goal) {
		JPanel card = new JPanel(null);
		card.setBackground(Color.WHITE);
		card.setBorder(BorderFactory.createLineBorder(UITheme.BORDER, 1));
		card.setPreferredSize(new Dimension(920, 150));
		card.setMaximumSize(new Dimension(920, 150));

		// Color strip on left
		Color stripColor = goal.isAchieved() ? UITheme.SUCCESS :
				(goal.getType().equals("WEEKLY") ? UITheme.PRIMARY : UITheme.SECONDARY);
		JPanel strip = new JPanel();
		strip.setBackground(stripColor);
		strip.setBounds(0, 0, 5, 150);
		card.add(strip);

		// Title
		JLabel lblTitle = new JLabel(goal.getTitle());
		lblTitle.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 17));
		lblTitle.setForeground(UITheme.TEXT_DARK);
		lblTitle.setBounds(25, 18, 500, 25);
		card.add(lblTitle);

		// Type badge
		JLabel lblType = new JLabel(goal.getType(), SwingConstants.CENTER);
		lblType.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 10));
		lblType.setForeground(Color.WHITE);
		lblType.setBackground(stripColor);
		lblType.setOpaque(true);
		lblType.setBounds(25, 48, 75, 22);
		card.add(lblType);

		// Achieved badge
		if (goal.isAchieved()) {
			JLabel lblAch = new JLabel("ACHIEVED", SwingConstants.CENTER);
			lblAch.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 10));
			lblAch.setForeground(Color.WHITE);
			lblAch.setBackground(UITheme.SUCCESS);
			lblAch.setOpaque(true);
			lblAch.setBounds(110, 48, 85, 22);
			card.add(lblAch);
		}

		// Description
		JLabel lblDesc = new JLabel("<html>" + goal.getDescription() + "</html>");
		lblDesc.setFont(new Font(UITheme.FONT_FAMILY, Font.PLAIN, 12));
		lblDesc.setForeground(UITheme.TEXT_GRAY);
		lblDesc.setBounds(25, 75, 500, 20);
		card.add(lblDesc);

		// Date range
		JLabel lblDates = new JLabel(goal.getStartDate() + "  to  " + goal.getEndDate());
		lblDates.setFont(new Font(UITheme.FONT_FAMILY, Font.PLAIN, 11));
		lblDates.setForeground(UITheme.TEXT_LIGHT);
		lblDates.setBounds(25, 95, 300, 18);
		card.add(lblDates);

		// Progress
		JLabel lblProgressLbl = new JLabel("Progress");
		lblProgressLbl.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 11));
		lblProgressLbl.setForeground(UITheme.TEXT_GRAY);
		lblProgressLbl.setBounds(550, 18, 100, 18);
		card.add(lblProgressLbl);

		JLabel lblHours = new JLabel(goal.getCompletedHours() + " / " + goal.getTargetHours() + " hours",
				SwingConstants.RIGHT);
		lblHours.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 13));
		lblHours.setForeground(UITheme.TEXT_DARK);
		lblHours.setBounds(750, 18, 145, 18);
		card.add(lblHours);

		// Progress bar
		JProgressBar pb = new JProgressBar(0, 100);
		pb.setValue((int) goal.getProgress());
		pb.setStringPainted(true);
		pb.setString(String.format("%.0f%%", goal.getProgress()));
		pb.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 11));
		pb.setForeground(stripColor);
		pb.setBackground(new Color(241, 245, 249));
		pb.setBorderPainted(false);
		pb.setBounds(550, 45, 345, 22);
		card.add(pb);

		// Action buttons
		JButton btnLog = new JButton("+ Hours");
		btnLog.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 11));
		btnLog.setBackground(UITheme.SUCCESS);
		btnLog.setForeground(Color.WHITE);
		btnLog.setFocusPainted(false);
		btnLog.setBorderPainted(false);
		btnLog.setBounds(550, 105, 90, 30);
		btnLog.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnLog.addActionListener(e -> logHours(goal));
		card.add(btnLog);

		JButton btnEdit = new JButton("Edit");
		btnEdit.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 11));
		btnEdit.setBackground(UITheme.WARNING);
		btnEdit.setForeground(Color.WHITE);
		btnEdit.setFocusPainted(false);
		btnEdit.setBorderPainted(false);
		btnEdit.setBounds(650, 105, 80, 30);
		btnEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnEdit.addActionListener(e -> showGoalDialog(goal));
		card.add(btnEdit);

		JButton btnDelete = new JButton("Delete");
		btnDelete.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 11));
		btnDelete.setBackground(UITheme.DANGER);
		btnDelete.setForeground(Color.WHITE);
		btnDelete.setFocusPainted(false);
		btnDelete.setBorderPainted(false);
		btnDelete.setBounds(740, 105, 80, 30);
		btnDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnDelete.addActionListener(e -> deleteGoal(goal));
		card.add(btnDelete);

		return card;
	}

	private void logHours(Goal goal) {
		String input = JOptionPane.showInputDialog(this, "How many hours did you study?", "1");
		if (input != null && !input.trim().isEmpty()) {
			try {
				int hours = Integer.parseInt(input.trim());
				if (hours <= 0) {
					JOptionPane.showMessageDialog(this, "Please enter a positive number!");
					return;
				}
				boolean wasAchieved = goal.isAchieved();
				goal.addHours(hours);
				FileManager.saveObjects(goals, goalFile);

				if (!wasAchieved && goal.isAchieved()) {
					AuthService.getCurrentUser().addPoints(50);
					AuthService.updateUser(AuthService.getCurrentUser());
					JOptionPane.showMessageDialog(this,
							"Congratulations! Goal achieved! +50 bonus points awarded.");
				}
				refreshGoals((String) cmbFilter.getSelectedItem());
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "Please enter a valid number!");
			}
		}
	}

	private void deleteGoal(Goal goal) {
		int confirm = JOptionPane.showConfirmDialog(this, "Delete this goal?",
				"Confirm Delete", JOptionPane.YES_NO_OPTION);
		if (confirm == JOptionPane.YES_OPTION) {
			goals.remove(goal);
			FileManager.saveObjects(goals, goalFile);
			refreshGoals((String) cmbFilter.getSelectedItem());
		}
	}

	private void showGoalDialog(Goal existing) {
		JDialog dialog = new JDialog((JFrame) null,
				(existing == null ? "Add Goal" : "Edit Goal"), true);
		dialog.setSize(500, 540);
		dialog.setLocationRelativeTo(null);
		dialog.setLayout(null);
		dialog.getContentPane().setBackground(Color.WHITE);

		// Header
		JPanel header = new JPanel(null);
		header.setBackground(UITheme.PRIMARY);
		header.setBounds(0, 0, 500, 70);

		JLabel lblTitle = new JLabel(existing == null ? "Set New Goal" : "Edit Goal");
		lblTitle.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 18));
		lblTitle.setForeground(Color.WHITE);
		lblTitle.setBounds(25, 20, 400, 30);
		header.add(lblTitle);
		dialog.add(header);

		int y = 95;

		addLabel(dialog, "TITLE", 30, y);
		JTextField txtTitle = createField();
		txtTitle.setBounds(30, y + 22, 440, 36);
		dialog.add(txtTitle);
		y += 70;

		addLabel(dialog, "DESCRIPTION", 30, y);
		JTextArea txtDesc = new JTextArea();
		txtDesc.setFont(new Font(UITheme.FONT_FAMILY, Font.PLAIN, 13));
		txtDesc.setLineWrap(true);
		txtDesc.setWrapStyleWord(true);
		JScrollPane descScroll = new JScrollPane(txtDesc);
		descScroll.setBounds(30, y + 22, 440, 60);
		descScroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER));
		dialog.add(descScroll);
		y += 95;

		addLabel(dialog, "TYPE", 30, y);
		JComboBox<String> cmbType = new JComboBox<>(new String[]{"WEEKLY", "MONTHLY"});
		cmbType.setFont(new Font(UITheme.FONT_FAMILY, Font.PLAIN, 13));
		cmbType.setBounds(30, y + 22, 210, 36);
		cmbType.setBackground(Color.WHITE);
		dialog.add(cmbType);

		addLabel(dialog, "TARGET HOURS", 260, y);
		JTextField txtHours = createField();
		txtHours.setText("10");
		txtHours.setBounds(260, y + 22, 210, 36);
		dialog.add(txtHours);
		y += 70;

		addLabel(dialog, "START DATE (YYYY-MM-DD)", 30, y);
		JTextField txtStart = createField();
		txtStart.setText(LocalDate.now().toString());
		txtStart.setBounds(30, y + 22, 210, 36);
		dialog.add(txtStart);

		addLabel(dialog, "END DATE (YYYY-MM-DD)", 260, y);
		JTextField txtEnd = createField();
		txtEnd.setText(LocalDate.now().plusDays(7).toString());
		txtEnd.setBounds(260, y + 22, 210, 36);
		dialog.add(txtEnd);
		y += 80;

		// Pre-fill
		if (existing != null) {
			txtTitle.setText(existing.getTitle());
			txtDesc.setText(existing.getDescription());
			cmbType.setSelectedItem(existing.getType());
			txtHours.setText(String.valueOf(existing.getTargetHours()));
			txtStart.setText(existing.getStartDate().toString());
			txtEnd.setText(existing.getEndDate().toString());
		}

		// Auto-update end date based on type
		cmbType.addActionListener(e -> {
			if (existing == null) {
				if (cmbType.getSelectedItem().equals("WEEKLY")) {
					txtEnd.setText(LocalDate.now().plusDays(7).toString());
				} else {
					txtEnd.setText(LocalDate.now().plusDays(30).toString());
				}
			}
		});

		// Buttons
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 12));
		btnCancel.setBackground(new Color(108, 117, 125));
		btnCancel.setForeground(Color.WHITE);
		btnCancel.setFocusPainted(false);
		btnCancel.setBorderPainted(false);
		btnCancel.setBounds(260, y, 100, 40);
		btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnCancel.addActionListener(e -> dialog.dispose());
		dialog.add(btnCancel);

		JButton btnSave = new JButton(existing == null ? "Save Goal" : "Update");
		btnSave.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 12));
		btnSave.setBackground(UITheme.SUCCESS);
		btnSave.setForeground(Color.WHITE);
		btnSave.setFocusPainted(false);
		btnSave.setBorderPainted(false);
		btnSave.setBounds(370, y, 100, 40);
		btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
		dialog.add(btnSave);

		btnSave.addActionListener(e -> {
			try {
				String title = txtTitle.getText().trim();
				String desc = txtDesc.getText().trim();
				String type = (String) cmbType.getSelectedItem();
				int hours = Integer.parseInt(txtHours.getText().trim());
				LocalDate start = LocalDate.parse(txtStart.getText().trim());
				LocalDate end = LocalDate.parse(txtEnd.getText().trim());

				if (title.isEmpty()) {
					JOptionPane.showMessageDialog(dialog, "Title is required!");
					return;
				}
				if (hours <= 0) {
					JOptionPane.showMessageDialog(dialog, "Target hours must be positive!");
					return;
				}

				if (existing != null) {
					existing.setTitle(title);
					existing.setDescription(desc);
					existing.setType(type);
					existing.setTargetHours(hours);
					existing.setStartDate(start);
					existing.setEndDate(end);
				} else {
					Goal g = new Goal(title, desc, type, start, end, hours);
					goals.add(g);
				}

				FileManager.saveObjects(goals, goalFile);
				refreshGoals((String) cmbFilter.getSelectedItem());
				dialog.dispose();

			} catch (Exception ex) {
				JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage() +
						"\nCheck date format (YYYY-MM-DD) and number fields.");
			}
		});

		dialog.setVisible(true);
	}

	private JTextField createField() {
		JTextField tf = new JTextField();
		tf.setFont(new Font(UITheme.FONT_FAMILY, Font.PLAIN, 13));
		tf.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(UITheme.BORDER, 1),
				new EmptyBorder(5, 10, 5, 10)));
		return tf;
	}

	private void addLabel(JDialog d, String text, int x, int y) {
		JLabel lbl = new JLabel(text);
		lbl.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 11));
		lbl.setForeground(UITheme.TEXT_GRAY);
		lbl.setBounds(x, y, 250, 18);
		d.add(lbl);
	}
}

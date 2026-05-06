package gui;

import models.Task;
import models.Assignment;
import models.Exam;
import services.AuthService;
import services.FileManager;
import utils.UITheme;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class TaskFrame extends JFrame {

	private ArrayList<Task> tasks;
	private JTable taskTable;
	private DefaultTableModel tableModel;
	private JComboBox<String> cmbFilter;
	private String taskFile;

	public TaskFrame() {
		taskFile = "tasks_" + AuthService.getCurrentUser().getUsername() + ".dat";
		tasks = FileManager.loadObjects(taskFile);
		if (tasks == null) tasks = new ArrayList<>();

		initComponents();
		loadTasksToTable("All");
	}

	private void initComponents() {
		getContentPane().setLayout(new BorderLayout());
		getContentPane().setBackground(UITheme.BG_LIGHT);

		// ===== TOP BAR =====
		JPanel topBar = new JPanel(null);
		topBar.setBackground(Color.WHITE);
		topBar.setPreferredSize(new Dimension(1020, 70));
		topBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UITheme.BORDER));

		JLabel lblPageTitle = new JLabel("Tasks & Assignments");
		lblPageTitle.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 22));
		lblPageTitle.setForeground(UITheme.TEXT_DARK);
		lblPageTitle.setBounds(30, 20, 400, 30);
		topBar.add(lblPageTitle);

		getContentPane().add(topBar, BorderLayout.NORTH);

		// ===== MAIN CONTENT =====
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(null);
		mainPanel.setBackground(UITheme.BG_LIGHT);

		// Header section
		JLabel lblHeading = new JLabel("Manage Your Tasks");
		lblHeading.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 20));
		lblHeading.setForeground(UITheme.TEXT_DARK);
		lblHeading.setBounds(30, 25, 400, 28);
		mainPanel.add(lblHeading);

		JLabel lblSub = new JLabel("Keep track of assignments and exams in one place");
		lblSub.setFont(new Font(UITheme.FONT_FAMILY, Font.PLAIN, 13));
		lblSub.setForeground(UITheme.TEXT_GRAY);
		lblSub.setBounds(30, 55, 500, 20);
		mainPanel.add(lblSub);

		// Action buttons
		JButton btnAddAssignment = createPrimaryButton("+  Add Assignment", UITheme.PRIMARY);
		btnAddAssignment.setBounds(620, 30, 180, 42);
		btnAddAssignment.addActionListener(e -> showTaskDialog("Assignment", null));
		mainPanel.add(btnAddAssignment);

		JButton btnAddExam = createPrimaryButton("+  Add Exam", UITheme.SECONDARY);
		btnAddExam.setBounds(810, 30, 160, 42);
		btnAddExam.addActionListener(e -> showTaskDialog("Exam", null));
		mainPanel.add(btnAddExam);

		// ===== TABLE CARD =====
		JPanel tableCard = new JPanel(null);
		tableCard.setBackground(Color.WHITE);
		tableCard.setBorder(BorderFactory.createLineBorder(UITheme.BORDER, 1));
		tableCard.setBounds(30, 95, 940, 530);

		// Filter section
		JLabel lblFilter = new JLabel("Filter By:");
		lblFilter.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 12));
		lblFilter.setForeground(UITheme.TEXT_GRAY);
		lblFilter.setBounds(20, 20, 80, 25);
		tableCard.add(lblFilter);

		cmbFilter = new JComboBox<>(new String[]{"All", "Assignments", "Exams", "Pending", "Completed"});
		cmbFilter.setFont(new Font(UITheme.FONT_FAMILY, Font.PLAIN, 12));
		cmbFilter.setBounds(95, 18, 160, 32);
		cmbFilter.setBackground(Color.WHITE);
		cmbFilter.addActionListener(e -> loadTasksToTable((String) cmbFilter.getSelectedItem()));
		tableCard.add(cmbFilter);

		// Table
		String[] columns = {"Type", "Title", "Subject", "Deadline", "Days Left", "Priority", "Alert", "Status"};
		tableModel = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) { return false; }
		};

		taskTable = new JTable(tableModel);
		taskTable.setFont(new Font(UITheme.FONT_FAMILY, Font.PLAIN, 13));
		taskTable.setRowHeight(40);
		taskTable.setShowVerticalLines(false);
		taskTable.setGridColor(UITheme.BORDER);
		taskTable.setSelectionBackground(new Color(238, 242, 255));
		taskTable.setSelectionForeground(UITheme.TEXT_DARK);
		taskTable.setIntercellSpacing(new Dimension(0, 0));

		// Header styling
		JTableHeader header = taskTable.getTableHeader();
		header.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 12));
		header.setBackground(new Color(248, 250, 252));
		header.setForeground(UITheme.TEXT_GRAY);
		header.setPreferredSize(new Dimension(0, 42));
		header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UITheme.BORDER));

		// Custom renderer for alert column
		taskTable.getColumnModel().getColumn(6).setCellRenderer(new AlertRenderer());
		taskTable.getColumnModel().getColumn(7).setCellRenderer(new StatusRenderer());
		taskTable.getColumnModel().getColumn(5).setCellRenderer(new PriorityRenderer());

		// Column widths
		taskTable.getColumnModel().getColumn(0).setPreferredWidth(110);
		taskTable.getColumnModel().getColumn(1).setPreferredWidth(180);
		taskTable.getColumnModel().getColumn(2).setPreferredWidth(120);
		taskTable.getColumnModel().getColumn(3).setPreferredWidth(100);
		taskTable.getColumnModel().getColumn(4).setPreferredWidth(85);
		taskTable.getColumnModel().getColumn(5).setPreferredWidth(90);
		taskTable.getColumnModel().getColumn(6).setPreferredWidth(140);
		taskTable.getColumnModel().getColumn(7).setPreferredWidth(100);

		JScrollPane scroll = new JScrollPane(taskTable);
		scroll.setBounds(20, 65, 900, 390);
		scroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER, 1));
		scroll.getViewport().setBackground(Color.WHITE);
		tableCard.add(scroll);

		// Action buttons
		JButton btnComplete = createSecondaryButton("Mark Complete", UITheme.SUCCESS);
		btnComplete.setBounds(550, 470, 140, 38);
		btnComplete.addActionListener(e -> markComplete());
		tableCard.add(btnComplete);

		JButton btnEdit = createSecondaryButton("Edit", UITheme.WARNING);
		btnEdit.setBounds(700, 470, 100, 38);
		btnEdit.addActionListener(e -> editTask());
		tableCard.add(btnEdit);

		JButton btnDelete = createSecondaryButton("Delete", UITheme.DANGER);
		btnDelete.setBounds(810, 470, 110, 38);
		btnDelete.addActionListener(e -> deleteTask());
		tableCard.add(btnDelete);

		mainPanel.add(tableCard);

		getContentPane().add(mainPanel, BorderLayout.CENTER);
	}

	private JButton createPrimaryButton(String text, Color bg) {
		JButton btn = new JButton(text);
		btn.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 13));
		btn.setBackground(bg);
		btn.setForeground(Color.WHITE);
		btn.setFocusPainted(false);
		btn.setBorderPainted(false);
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		return btn;
	}

	private JButton createSecondaryButton(String text, Color bg) {
		JButton btn = new JButton(text);
		btn.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 12));
		btn.setBackground(bg);
		btn.setForeground(Color.WHITE);
		btn.setFocusPainted(false);
		btn.setBorderPainted(false);
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		return btn;
	}

	private void loadTasksToTable(String filter) {
		tableModel.setRowCount(0);

		for (Task t : tasks) {
			if (filter.equals("Assignments") && !(t instanceof Assignment)) continue;
			if (filter.equals("Exams") && !(t instanceof Exam)) continue;
			if (filter.equals("Pending") && t.isCompleted()) continue;
			if (filter.equals("Completed") && !t.isCompleted()) continue;

			String type = (t instanceof Assignment) ? "Assignment" : "Exam";
			String status = t.isCompleted() ? "Done" : "Pending";

			tableModel.addRow(new Object[]{
					type, t.getTitle(), t.getSubject(), t.getDeadline().toString(),
					t.getDaysLeft(), t.getPriority(), t.getAlertLevel(), status
			});
		}
	}

	private Task getTaskAtRow(int row) {
		String filter = (String) cmbFilter.getSelectedItem();
		int count = 0;
		for (Task t : tasks) {
			if (filter.equals("Assignments") && !(t instanceof Assignment)) continue;
			if (filter.equals("Exams") && !(t instanceof Exam)) continue;
			if (filter.equals("Pending") && t.isCompleted()) continue;
			if (filter.equals("Completed") && !t.isCompleted()) continue;

			if (count == row) return t;
			count++;
		}
		return null;
	}

	private void editTask() {
		int row = taskTable.getSelectedRow();
		if (row == -1) {
			JOptionPane.showMessageDialog(this, "Please select a task to edit!");
			return;
		}
		Task t = getTaskAtRow(row);
		if (t != null) {
			String type = (t instanceof Assignment) ? "Assignment" : "Exam";
			showTaskDialog(type, t);
		}
	}

	private void showTaskDialog(String type, Task existing) {
		JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this) instanceof JFrame ?
				(JFrame) SwingUtilities.getWindowAncestor(this) : null,
				(existing == null ? "Add " : "Edit ") + type, Dialog.ModalityType.APPLICATION_MODAL);
		dialog.setSize(500, type.equals("Assignment") ? 580 : 640);
		dialog.setLocationRelativeTo(null);
		dialog.setLayout(null);
		dialog.getContentPane().setBackground(Color.WHITE);

		// Header
		JPanel headerPanel = new JPanel(null);
		headerPanel.setBackground(UITheme.PRIMARY);
		headerPanel.setBounds(0, 0, 500, 70);

		JLabel lblTitle = new JLabel((existing == null ? "Add New " : "Edit ") + type);
		lblTitle.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 18));
		lblTitle.setForeground(Color.WHITE);
		lblTitle.setBounds(25, 20, 400, 30);
		headerPanel.add(lblTitle);
		dialog.add(headerPanel);

		int y = 95;

		addDialogLabel(dialog, "TITLE", 30, y);
		JTextField txtTitle = createDialogField();
		txtTitle.setBounds(30, y + 22, 440, 36);
		dialog.add(txtTitle);
		y += 70;

		addDialogLabel(dialog, "SUBJECT", 30, y);
		JTextField txtSubject = createDialogField();
		txtSubject.setBounds(30, y + 22, 440, 36);
		dialog.add(txtSubject);
		y += 70;

		addDialogLabel(dialog, "DESCRIPTION", 30, y);
		JTextArea txtDesc = new JTextArea();
		txtDesc.setFont(new Font(UITheme.FONT_FAMILY, Font.PLAIN, 13));
		txtDesc.setLineWrap(true);
		txtDesc.setWrapStyleWord(true);
		JScrollPane descScroll = new JScrollPane(txtDesc);
		descScroll.setBounds(30, y + 22, 440, 60);
		descScroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER));
		dialog.add(descScroll);
		y += 95;

		addDialogLabel(dialog, "DEADLINE (YYYY-MM-DD)", 30, y);
		JTextField txtDeadline = createDialogField();
		txtDeadline.setText(LocalDate.now().plusDays(7).toString());
		txtDeadline.setBounds(30, y + 22, 210, 36);
		dialog.add(txtDeadline);

		addDialogLabel(dialog, "PRIORITY", 260, y);
		JComboBox<String> cmbPriority = new JComboBox<>(new String[]{"HIGH", "MEDIUM", "LOW"});
		cmbPriority.setFont(new Font(UITheme.FONT_FAMILY, Font.PLAIN, 13));
		cmbPriority.setBounds(260, y + 22, 210, 36);
		cmbPriority.setBackground(Color.WHITE);
		dialog.add(cmbPriority);
		y += 70;

		// Type-specific fields
		JTextField txtMarks = null, txtVenue = null, txtDuration = null;
		JComboBox<String> cmbType = null, cmbExamType = null;

		if (type.equals("Assignment")) {
			addDialogLabel(dialog, "TOTAL MARKS", 30, y);
			txtMarks = createDialogField();
			txtMarks.setText("100");
			txtMarks.setBounds(30, y + 22, 210, 36);
			dialog.add(txtMarks);

			addDialogLabel(dialog, "SUBMISSION TYPE", 260, y);
			cmbType = new JComboBox<>(new String[]{"Online", "Physical"});
			cmbType.setFont(new Font(UITheme.FONT_FAMILY, Font.PLAIN, 13));
			cmbType.setBounds(260, y + 22, 210, 36);
			cmbType.setBackground(Color.WHITE);
			dialog.add(cmbType);
			y += 70;
		} else {
			addDialogLabel(dialog, "EXAM TYPE", 30, y);
			cmbExamType = new JComboBox<>(new String[]{"Midterm", "Final", "Quiz"});
			cmbExamType.setFont(new Font(UITheme.FONT_FAMILY, Font.PLAIN, 13));
			cmbExamType.setBounds(30, y + 22, 210, 36);
			cmbExamType.setBackground(Color.WHITE);
			dialog.add(cmbExamType);

			addDialogLabel(dialog, "DURATION (MINS)", 260, y);
			txtDuration = createDialogField();
			txtDuration.setText("60");
			txtDuration.setBounds(260, y + 22, 210, 36);
			dialog.add(txtDuration);
			y += 70;

			addDialogLabel(dialog, "VENUE", 30, y);
			txtVenue = createDialogField();
			txtVenue.setBounds(30, y + 22, 440, 36);
			dialog.add(txtVenue);
			y += 70;
		}

		// Pre-fill if editing
		if (existing != null) {
			txtTitle.setText(existing.getTitle());
			txtSubject.setText(existing.getSubject());
			txtDesc.setText(existing.getDescription());
			txtDeadline.setText(existing.getDeadline().toString());
			cmbPriority.setSelectedItem(existing.getPriority());

			if (existing instanceof Assignment) {
				Assignment a = (Assignment) existing;
				txtMarks.setText(String.valueOf(a.getTotalMarks()));
				cmbType.setSelectedItem(a.getSubmissionType());
			} else if (existing instanceof Exam) {
				Exam ex = (Exam) existing;
				cmbExamType.setSelectedItem(ex.getExamType());
				txtDuration.setText(String.valueOf(ex.getDuration()));
				txtVenue.setText(ex.getVenue());
			}
		}

		// Buttons
		JButton btnCancel = createSecondaryButton("Cancel", new Color(108, 117, 125));
		btnCancel.setBounds(260, y + 10, 100, 40);
		btnCancel.addActionListener(e -> dialog.dispose());
		dialog.add(btnCancel);

		JButton btnSave = createPrimaryButton(existing == null ? "Save" : "Update", UITheme.SUCCESS);
		btnSave.setBounds(370, y + 10, 100, 40);
		dialog.add(btnSave);

		final JTextField fMarks = txtMarks;
		final JTextField fVenue = txtVenue;
		final JTextField fDuration = txtDuration;
		final JComboBox<String> fSubType = cmbType;
		final JComboBox<String> fExamType = cmbExamType;

		btnSave.addActionListener(e -> {
			try {
				String title = txtTitle.getText().trim();
				String subject = txtSubject.getText().trim();
				String desc = txtDesc.getText().trim();
				LocalDate deadline = LocalDate.parse(txtDeadline.getText().trim());
				String priority = (String) cmbPriority.getSelectedItem();

				if (title.isEmpty() || subject.isEmpty()) {
					JOptionPane.showMessageDialog(dialog, "Title and Subject are required!");
					return;
				}

				if (existing != null) {
					existing.setTitle(title);
					existing.setSubject(subject);
					existing.setDescription(desc);
					existing.setDeadline(deadline);
					existing.setPriority(priority);

					if (existing instanceof Assignment) {
						Assignment a = (Assignment) existing;
						a.setTotalMarks(Integer.parseInt(fMarks.getText().trim()));
						a.setSubmissionType((String) fSubType.getSelectedItem());
					} else if (existing instanceof Exam) {
						Exam ex = (Exam) existing;
						ex.setExamType((String) fExamType.getSelectedItem());
						ex.setDuration(Integer.parseInt(fDuration.getText().trim()));
						ex.setVenue(fVenue.getText().trim());
					}
				} else {
					Task newTask;
					if (type.equals("Assignment")) {
						int marks = Integer.parseInt(fMarks.getText().trim());
						newTask = new Assignment(title, desc, deadline, priority, subject,
								marks, (String) fSubType.getSelectedItem());
					} else {
						int duration = Integer.parseInt(fDuration.getText().trim());
						newTask = new Exam(title, desc, deadline, priority, subject,
								(String) fExamType.getSelectedItem(),
								fVenue.getText().trim(), duration);
					}
					tasks.add(newTask);
				}

				FileManager.saveObjects(tasks, taskFile);
				loadTasksToTable((String) cmbFilter.getSelectedItem());
				dialog.dispose();

			} catch (Exception ex) {
				JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage() +
						"\n(Check date format YYYY-MM-DD and numeric fields)");
			}
		});

		dialog.setVisible(true);
	}

	private JTextField createDialogField() {
		JTextField tf = new JTextField();
		tf.setFont(new Font(UITheme.FONT_FAMILY, Font.PLAIN, 13));
		tf.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(UITheme.BORDER, 1),
				new EmptyBorder(5, 10, 5, 10)));
		return tf;
	}

	private void addDialogLabel(JDialog dialog, String text, int x, int y) {
		JLabel lbl = new JLabel(text);
		lbl.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 11));
		lbl.setForeground(UITheme.TEXT_GRAY);
		lbl.setBounds(x, y, 250, 18);
		dialog.add(lbl);
	}

	private void markComplete() {
		int row = taskTable.getSelectedRow();
		if (row == -1) {
			JOptionPane.showMessageDialog(this, "Please select a task!");
			return;
		}
		Task t = getTaskAtRow(row);
		if (t != null && !t.isCompleted()) {
			t.setCompleted(true);
			AuthService.getCurrentUser().addPoints(10);
			AuthService.updateUser(AuthService.getCurrentUser());

			FileManager.saveObjects(tasks, taskFile);
			loadTasksToTable((String) cmbFilter.getSelectedItem());
			JOptionPane.showMessageDialog(this, "Task completed! +10 points awarded.");
		}
	}

	private void deleteTask() {
		int row = taskTable.getSelectedRow();
		if (row == -1) {
			JOptionPane.showMessageDialog(this, "Please select a task!");
			return;
		}
		int confirm = JOptionPane.showConfirmDialog(this, "Delete this task?",
				"Confirm Delete", JOptionPane.YES_NO_OPTION);
		if (confirm == JOptionPane.YES_OPTION) {
			Task t = getTaskAtRow(row);
			tasks.remove(t);
			FileManager.saveObjects(tasks, taskFile);
			loadTasksToTable((String) cmbFilter.getSelectedItem());
		}
	}

	// ===== CUSTOM RENDERERS =====

	class AlertRenderer extends DefaultTableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value,
													   boolean isSelected, boolean hasFocus, int row, int column) {
			JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value,
					isSelected, hasFocus, row, column);
			lbl.setHorizontalAlignment(SwingConstants.CENTER);
			lbl.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 11));
			lbl.setBorder(new EmptyBorder(5, 8, 5, 8));

			if (!isSelected && value != null) {
				String alert = value.toString();
				Color bg, fg = Color.WHITE;
				switch (alert) {
					case "OVERDUE":            bg = new Color(220, 38, 38); break;
					case "URGENT - DUE TODAY": bg = new Color(239, 68, 68); break;
					case "CRITICAL":           bg = new Color(245, 158, 11); break;
					case "HIGH":               bg = new Color(251, 191, 36); fg = new Color(120, 53, 15); break;
					case "MEDIUM":             bg = new Color(34, 197, 94); break;
					default:                   bg = new Color(148, 163, 184);
				}
				lbl.setBackground(bg);
				lbl.setForeground(fg);
				lbl.setOpaque(true);
			}
			return lbl;
		}
	}

	class StatusRenderer extends DefaultTableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value,
													   boolean isSelected, boolean hasFocus, int row, int column) {
			JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value,
					isSelected, hasFocus, row, column);
			lbl.setHorizontalAlignment(SwingConstants.CENTER);
			lbl.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 11));

			if (!isSelected && value != null) {
				if (value.toString().equals("Done")) {
					lbl.setForeground(UITheme.SUCCESS);
				} else {
					lbl.setForeground(UITheme.WARNING);
				}
			}
			return lbl;
		}
	}

	class PriorityRenderer extends DefaultTableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value,
													   boolean isSelected, boolean hasFocus, int row, int column) {
			JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value,
					isSelected, hasFocus, row, column);
			lbl.setHorizontalAlignment(SwingConstants.CENTER);
			lbl.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 11));

			if (!isSelected && value != null) {
				String p = value.toString();
				if (p.equals("HIGH")) lbl.setForeground(UITheme.DANGER);
				else if (p.equals("MEDIUM")) lbl.setForeground(UITheme.WARNING);
				else lbl.setForeground(UITheme.SUCCESS);
			}
			return lbl;
		}
	}
}

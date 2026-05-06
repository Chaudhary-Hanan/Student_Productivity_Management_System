package gui;

import models.Note;
import services.AuthService;
import services.FileManager;
import utils.UITheme;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class NotesFrame extends JFrame {

	private ArrayList<Note> notes;
	private JList<Note> notesList;
	private DefaultListModel<Note> listModel;
	private JTextField txtSearch;
	private JTextArea txtPreview;
	private JLabel lblPreviewTitle, lblPreviewMeta, lblPreviewTags;
	private String notesFile;
	private Note currentNote;

	public NotesFrame() {
		notesFile = "notes_" + AuthService.getCurrentUser().getUsername() + ".dat";
		notes = FileManager.loadObjects(notesFile);
		if (notes == null) notes = new ArrayList<>();

		initComponents();
		refreshList("");
	}

	private void initComponents() {
		getContentPane().setLayout(new BorderLayout());
		getContentPane().setBackground(UITheme.BG_LIGHT);

		// Top bar
		JPanel topBar = new JPanel(null);
		topBar.setBackground(Color.WHITE);
		topBar.setPreferredSize(new Dimension(1020, 70));
		topBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UITheme.BORDER));

		JLabel lblPageTitle = new JLabel("Digital Notes");
		lblPageTitle.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 22));
		lblPageTitle.setForeground(UITheme.TEXT_DARK);
		lblPageTitle.setBounds(30, 20, 400, 30);
		topBar.add(lblPageTitle);

		getContentPane().add(topBar, BorderLayout.NORTH);

		// Main split layout
		JPanel mainPanel = new JPanel(null);
		mainPanel.setBackground(UITheme.BG_LIGHT);

		// ===== LEFT PANEL - Notes List =====
		JPanel leftPanel = new JPanel(null);
		leftPanel.setBackground(Color.WHITE);
		leftPanel.setBorder(BorderFactory.createLineBorder(UITheme.BORDER, 1));
		leftPanel.setBounds(20, 20, 360, 605);

		JLabel lblYourNotes = new JLabel("Your Notes");
		lblYourNotes.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 16));
		lblYourNotes.setForeground(UITheme.TEXT_DARK);
		lblYourNotes.setBounds(20, 18, 200, 25);
		leftPanel.add(lblYourNotes);

		JButton btnNew = new JButton("+  New");
		btnNew.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 12));
		btnNew.setBackground(UITheme.PRIMARY);
		btnNew.setForeground(Color.WHITE);
		btnNew.setFocusPainted(false);
		btnNew.setBorderPainted(false);
		btnNew.setBounds(265, 15, 80, 32);
		btnNew.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnNew.addActionListener(e -> showNoteDialog(null));
		leftPanel.add(btnNew);

		// Search
		txtSearch = new JTextField();
		txtSearch.setFont(new Font(UITheme.FONT_FAMILY, Font.PLAIN, 12));
		txtSearch.setBounds(20, 58, 325, 32);
		txtSearch.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(UITheme.BORDER, 1),
				new EmptyBorder(5, 10, 5, 10)));
		txtSearch.putClientProperty("JTextField.placeholderText", "Search notes or tags...");
		leftPanel.add(txtSearch);

		JButton btnSearch = new JButton("Search");
		btnSearch.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 11));
		btnSearch.setBackground(new Color(241, 245, 249));
		btnSearch.setForeground(UITheme.TEXT_DARK);
		btnSearch.setFocusPainted(false);
		btnSearch.setBorderPainted(false);
		btnSearch.setBounds(20, 100, 100, 28);
		btnSearch.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnSearch.addActionListener(e -> refreshList(txtSearch.getText().trim()));
		leftPanel.add(btnSearch);

		JButton btnClear = new JButton("Clear");
		btnClear.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 11));
		btnClear.setBackground(new Color(241, 245, 249));
		btnClear.setForeground(UITheme.TEXT_DARK);
		btnClear.setFocusPainted(false);
		btnClear.setBorderPainted(false);
		btnClear.setBounds(125, 100, 80, 28);
		btnClear.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnClear.addActionListener(e -> { txtSearch.setText(""); refreshList(""); });
		leftPanel.add(btnClear);

		// Notes list
		listModel = new DefaultListModel<>();
		notesList = new JList<>(listModel);
		notesList.setFont(new Font(UITheme.FONT_FAMILY, Font.PLAIN, 13));
		notesList.setFixedCellHeight(70);
		notesList.setSelectionBackground(new Color(238, 242, 255));
		notesList.setSelectionForeground(UITheme.TEXT_DARK);
		notesList.setCellRenderer(new NoteCellRenderer());
		notesList.addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting() && notesList.getSelectedValue() != null) {
				showNotePreview(notesList.getSelectedValue());
			}
		});

		JScrollPane listScroll = new JScrollPane(notesList);
		listScroll.setBounds(20, 140, 325, 450);
		listScroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER));
		leftPanel.add(listScroll);

		mainPanel.add(leftPanel);

		// ===== RIGHT PANEL - Note Preview =====
		JPanel rightPanel = new JPanel(null);
		rightPanel.setBackground(Color.WHITE);
		rightPanel.setBorder(BorderFactory.createLineBorder(UITheme.BORDER, 1));
		rightPanel.setBounds(395, 20, 580, 605);

		lblPreviewTitle = new JLabel("Select a note to preview");
		lblPreviewTitle.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 20));
		lblPreviewTitle.setForeground(UITheme.TEXT_DARK);
		lblPreviewTitle.setBounds(25, 20, 540, 30);
		rightPanel.add(lblPreviewTitle);

		lblPreviewMeta = new JLabel("");
		lblPreviewMeta.setFont(new Font(UITheme.FONT_FAMILY, Font.PLAIN, 12));
		lblPreviewMeta.setForeground(UITheme.TEXT_GRAY);
		lblPreviewMeta.setBounds(25, 55, 540, 20);
		rightPanel.add(lblPreviewMeta);

		lblPreviewTags = new JLabel("");
		lblPreviewTags.setFont(new Font(UITheme.FONT_FAMILY, Font.PLAIN, 12));
		lblPreviewTags.setForeground(UITheme.PRIMARY);
		lblPreviewTags.setBounds(25, 78, 540, 20);
		rightPanel.add(lblPreviewTags);

		txtPreview = new JTextArea();
		txtPreview.setFont(new Font(UITheme.FONT_FAMILY, Font.PLAIN, 14));
		txtPreview.setLineWrap(true);
		txtPreview.setWrapStyleWord(true);
		txtPreview.setEditable(false);
		txtPreview.setBackground(new Color(250, 251, 253));
		txtPreview.setBorder(new EmptyBorder(15, 15, 15, 15));

		JScrollPane previewScroll = new JScrollPane(txtPreview);
		previewScroll.setBounds(25, 110, 530, 425);
		previewScroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER));
		rightPanel.add(previewScroll);

		// Action buttons
		JButton btnEdit = new JButton("Edit");
		btnEdit.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 12));
		btnEdit.setBackground(UITheme.WARNING);
		btnEdit.setForeground(Color.WHITE);
		btnEdit.setFocusPainted(false);
		btnEdit.setBorderPainted(false);
		btnEdit.setBounds(355, 555, 95, 35);
		btnEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnEdit.addActionListener(e -> {
			if (currentNote != null) showNoteDialog(currentNote);
			else JOptionPane.showMessageDialog(this, "Select a note first!");
		});
		rightPanel.add(btnEdit);

		JButton btnDelete = new JButton("Delete");
		btnDelete.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 12));
		btnDelete.setBackground(UITheme.DANGER);
		btnDelete.setForeground(Color.WHITE);
		btnDelete.setFocusPainted(false);
		btnDelete.setBorderPainted(false);
		btnDelete.setBounds(460, 555, 95, 35);
		btnDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnDelete.addActionListener(e -> deleteNote());
		rightPanel.add(btnDelete);

		mainPanel.add(rightPanel);

		getContentPane().add(mainPanel, BorderLayout.CENTER);
	}

	private void refreshList(String search) {
		listModel.clear();
		String q = search.toLowerCase();
		for (Note n : notes) {
			if (q.isEmpty() ||
					n.getTitle().toLowerCase().contains(q) ||
					n.getSubject().toLowerCase().contains(q) ||
					n.getContent().toLowerCase().contains(q) ||
					n.getTagsAsString().toLowerCase().contains(q)) {
				listModel.addElement(n);
			}
		}
	}

	private void showNotePreview(Note n) {
		currentNote = n;
		lblPreviewTitle.setText(n.getTitle());
		lblPreviewMeta.setText("Subject: " + n.getSubject() + "  |  Updated: " +
				n.getUpdatedAt().toLocalDate());
		lblPreviewTags.setText(n.getTags().isEmpty() ? "" : "Tags: " + n.getTagsAsString());
		txtPreview.setText(n.getContent());
		txtPreview.setCaretPosition(0);
	}

	private void deleteNote() {
		if (currentNote == null) {
			JOptionPane.showMessageDialog(this, "Select a note first!");
			return;
		}
		int confirm = JOptionPane.showConfirmDialog(this, "Delete this note?",
				"Confirm Delete", JOptionPane.YES_NO_OPTION);
		if (confirm == JOptionPane.YES_OPTION) {
			notes.remove(currentNote);
			FileManager.saveObjects(notes, notesFile);
			currentNote = null;
			lblPreviewTitle.setText("Select a note to preview");
			lblPreviewMeta.setText("");
			lblPreviewTags.setText("");
			txtPreview.setText("");
			refreshList(txtSearch.getText().trim());
		}
	}

	private void showNoteDialog(Note existing) {
		JDialog dialog = new JDialog((JFrame) null,
				existing == null ? "New Note" : "Edit Note", true);
		dialog.setSize(600, 600);
		dialog.setLocationRelativeTo(null);
		dialog.setLayout(null);
		dialog.getContentPane().setBackground(Color.WHITE);

		JPanel header = new JPanel(null);
		header.setBackground(UITheme.PRIMARY);
		header.setBounds(0, 0, 600, 70);

		JLabel lblH = new JLabel(existing == null ? "Create New Note" : "Edit Note");
		lblH.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 18));
		lblH.setForeground(Color.WHITE);
		lblH.setBounds(25, 20, 400, 30);
		header.add(lblH);
		dialog.add(header);

		int y = 95;

		addLabel(dialog, "TITLE", 30, y);
		JTextField txtTitle = createField();
		txtTitle.setBounds(30, y + 22, 540, 36);
		dialog.add(txtTitle);
		y += 70;

		addLabel(dialog, "SUBJECT", 30, y);
		JTextField txtSubj = createField();
		txtSubj.setBounds(30, y + 22, 260, 36);
		dialog.add(txtSubj);

		addLabel(dialog, "TAGS (comma separated)", 310, y);
		JTextField txtTags = createField();
		txtTags.setBounds(310, y + 22, 260, 36);
		dialog.add(txtTags);
		y += 70;

		addLabel(dialog, "CONTENT", 30, y);
		JTextArea txtContent = new JTextArea();
		txtContent.setFont(new Font(UITheme.FONT_FAMILY, Font.PLAIN, 13));
		txtContent.setLineWrap(true);
		txtContent.setWrapStyleWord(true);
		JScrollPane sc = new JScrollPane(txtContent);
		sc.setBounds(30, y + 22, 540, 280);
		sc.setBorder(BorderFactory.createLineBorder(UITheme.BORDER));
		dialog.add(sc);
		y += 320;

		if (existing != null) {
			txtTitle.setText(existing.getTitle());
			txtSubj.setText(existing.getSubject());
			txtTags.setText(existing.getTagsAsString());
			txtContent.setText(existing.getContent());
		}

		JButton btnCancel = new JButton("Cancel");
		btnCancel.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 12));
		btnCancel.setBackground(new Color(108, 117, 125));
		btnCancel.setForeground(Color.WHITE);
		btnCancel.setFocusPainted(false);
		btnCancel.setBorderPainted(false);
		btnCancel.setBounds(360, y, 100, 40);
		btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnCancel.addActionListener(e -> dialog.dispose());
		dialog.add(btnCancel);

		JButton btnSave = new JButton("Save");
		btnSave.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 12));
		btnSave.setBackground(UITheme.SUCCESS);
		btnSave.setForeground(Color.WHITE);
		btnSave.setFocusPainted(false);
		btnSave.setBorderPainted(false);
		btnSave.setBounds(470, y, 100, 40);
		btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
		dialog.add(btnSave);

		btnSave.addActionListener(e -> {
			String title = txtTitle.getText().trim();
			String subj = txtSubj.getText().trim();
			String content = txtContent.getText().trim();

			if (title.isEmpty() || subj.isEmpty()) {
				JOptionPane.showMessageDialog(dialog, "Title and Subject required!");
				return;
			}

			ArrayList<String> tags = new ArrayList<>();
			for (String tag : txtTags.getText().split(",")) {
				String t = tag.trim();
				if (!t.isEmpty()) tags.add(t);
			}

			if (existing != null) {
				existing.setTitle(title);
				existing.setSubject(subj);
				existing.setContent(content);
				existing.setTags(tags);
			} else {
				Note n = new Note(title, content, subj, tags);
				notes.add(n);
			}

			FileManager.saveObjects(notes, notesFile);
			refreshList(txtSearch.getText().trim());
			dialog.dispose();
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

	// Custom cell renderer for notes list
	class NoteCellRenderer extends JPanel implements ListCellRenderer<Note> {
		private JLabel lblTitle, lblSubject, lblPreview;

		public NoteCellRenderer() {
			setLayout(null);
			setPreferredSize(new Dimension(300, 70));

			lblTitle = new JLabel();
			lblTitle.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 13));
			lblTitle.setBounds(12, 8, 280, 18);
			add(lblTitle);

			lblSubject = new JLabel();
			lblSubject.setFont(new Font(UITheme.FONT_FAMILY, Font.PLAIN, 11));
			lblSubject.setBounds(12, 28, 280, 16);
			add(lblSubject);

			lblPreview = new JLabel();
			lblPreview.setFont(new Font(UITheme.FONT_FAMILY, Font.PLAIN, 11));
			lblPreview.setBounds(12, 46, 280, 16);
			add(lblPreview);
		}

		@Override
		public Component getListCellRendererComponent(JList<? extends Note> list,
													  Note n, int index, boolean isSelected, boolean cellHasFocus) {
			lblTitle.setText(n.getTitle());
			lblSubject.setText(n.getSubject() + (n.getTags().isEmpty() ? "" : "  -  " + n.getTagsAsString()));

			String preview = n.getContent().replace("\n", " ");
			if (preview.length() > 50) preview = preview.substring(0, 50) + "...";
			lblPreview.setText(preview);

			if (isSelected) {
				setBackground(new Color(238, 242, 255));
				lblTitle.setForeground(UITheme.PRIMARY);
			} else {
				setBackground(Color.WHITE);
				lblTitle.setForeground(UITheme.TEXT_DARK);
			}
			lblSubject.setForeground(UITheme.TEXT_GRAY);
			lblPreview.setForeground(UITheme.TEXT_LIGHT);

			setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UITheme.BORDER));
			return this;
		}
	}
}

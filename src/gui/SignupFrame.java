package gui;

import models.Student;
import services.AuthService;
import utils.UITheme;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SignupFrame extends JFrame {

	private JTextField txtUsername, txtEmail, txtStudentId, txtProgram;
	private JPasswordField txtPassword, txtConfirmPassword;
	private JSpinner spnSemester;

	public SignupFrame() {
		setTitle("Smart Study Planner - Sign Up");
		setSize(900, 680);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);

		initComponents();
	}

	private void initComponents() {
		setLayout(new BorderLayout());

		// Left branding panel (same gradient style)
		JPanel leftPanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				GradientPaint gp = new GradientPaint(0, 0, UITheme.PRIMARY,
						getWidth(), getHeight(), UITheme.SECONDARY);
				g2.setPaint(gp);
				g2.fillRect(0, 0, getWidth(), getHeight());
			}
		};
		leftPanel.setLayout(null);
		leftPanel.setPreferredSize(new Dimension(380, 680));

		JLabel lblIcon = new JLabel("◆", SwingConstants.CENTER);
		lblIcon.setFont(new Font("Arial", Font.BOLD, 70));
		lblIcon.setForeground(Color.WHITE);
		lblIcon.setBounds(0, 180, 380, 80);
		leftPanel.add(lblIcon);

		JLabel lblBrand = new JLabel("Join Us Today!", SwingConstants.CENTER);
		lblBrand.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 28));
		lblBrand.setForeground(Color.WHITE);
		lblBrand.setBounds(0, 270, 380, 40);
		leftPanel.add(lblBrand);

		JLabel lblTag = new JLabel("Start your productivity journey", SwingConstants.CENTER);
		lblTag.setFont(new Font(UITheme.FONT_FAMILY, Font.PLAIN, 14));
		lblTag.setForeground(new Color(230, 230, 255));
		lblTag.setBounds(0, 315, 380, 25);
		leftPanel.add(lblTag);

		add(leftPanel, BorderLayout.WEST);

		// Right form panel
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(null);
		rightPanel.setBackground(Color.WHITE);
		rightPanel.setPreferredSize(new Dimension(520, 680));

		JLabel lblTitle = new JLabel("Create Account");
		lblTitle.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 26));
		lblTitle.setForeground(UITheme.TEXT_DARK);
		lblTitle.setBounds(40, 35, 400, 35);
		rightPanel.add(lblTitle);

		JLabel lblSub = new JLabel("Fill in your details to get started");
		lblSub.setFont(new Font(UITheme.FONT_FAMILY, Font.PLAIN, 13));
		lblSub.setForeground(UITheme.TEXT_GRAY);
		lblSub.setBounds(40, 70, 400, 20);
		rightPanel.add(lblSub);

		int y = 110;
		int gap = 70;

		// Username
		addFieldLabel(rightPanel, "USERNAME", 40, y);
		txtUsername = createStyledTextField();
		txtUsername.setBounds(40, y + 22, 440, 38);
		rightPanel.add(txtUsername);
		y += gap;

		// Email
		addFieldLabel(rightPanel, "EMAIL", 40, y);
		txtEmail = createStyledTextField();
		txtEmail.setBounds(40, y + 22, 440, 38);
		rightPanel.add(txtEmail);
		y += gap;

		// Student ID + Semester (side by side)
		addFieldLabel(rightPanel, "STUDENT ID", 40, y);
		txtStudentId = createStyledTextField();
		txtStudentId.setBounds(40, y + 22, 280, 38);
		rightPanel.add(txtStudentId);

		addFieldLabel(rightPanel, "SEMESTER", 335, y);
		spnSemester = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1));
		spnSemester.setFont(new Font(UITheme.FONT_FAMILY, Font.PLAIN, 14));
		spnSemester.setBounds(335, y + 22, 145, 38);
		rightPanel.add(spnSemester);
		y += gap;

		// Program
		addFieldLabel(rightPanel, "PROGRAM", 40, y);
		txtProgram = createStyledTextField();
		txtProgram.setBounds(40, y + 22, 440, 38);
		rightPanel.add(txtProgram);
		y += gap;

		// Password + Confirm
		addFieldLabel(rightPanel, "PASSWORD", 40, y);
		txtPassword = new JPasswordField();
		txtPassword.setFont(new Font(UITheme.FONT_FAMILY, Font.PLAIN, 14));
		txtPassword.setBounds(40, y + 22, 215, 38);
		txtPassword.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(UITheme.BORDER, 1),
				new EmptyBorder(5, 12, 5, 12)));
		rightPanel.add(txtPassword);

		addFieldLabel(rightPanel, "CONFIRM PASSWORD", 270, y);
		txtConfirmPassword = new JPasswordField();
		txtConfirmPassword.setFont(new Font(UITheme.FONT_FAMILY, Font.PLAIN, 14));
		txtConfirmPassword.setBounds(270, y + 22, 210, 38);
		txtConfirmPassword.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(UITheme.BORDER, 1),
				new EmptyBorder(5, 12, 5, 12)));
		rightPanel.add(txtConfirmPassword);
		y += gap + 5;

		// Sign up button
		JButton btnSignup = new JButton("CREATE ACCOUNT");
		btnSignup.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 14));
		btnSignup.setBackground(UITheme.PRIMARY);
		btnSignup.setForeground(Color.WHITE);
		btnSignup.setFocusPainted(false);
		btnSignup.setBorderPainted(false);
		btnSignup.setBounds(40, y, 440, 45);
		btnSignup.setCursor(new Cursor(Cursor.HAND_CURSOR));
		rightPanel.add(btnSignup);
		y += 55;

		// Back to login link
		JButton btnBack = new JButton("Already have an account? Sign In");
		btnBack.setFont(new Font(UITheme.FONT_FAMILY, Font.PLAIN, 13));
		btnBack.setForeground(UITheme.PRIMARY);
		btnBack.setBackground(Color.WHITE);
		btnBack.setBorderPainted(false);
		btnBack.setFocusPainted(false);
		btnBack.setBounds(40, y, 440, 30);
		btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
		rightPanel.add(btnBack);

		add(rightPanel, BorderLayout.CENTER);

		// Listeners
		btnSignup.addActionListener(e -> handleSignup());
		btnBack.addActionListener(e -> {
			new LoginFrame().setVisible(true);
			dispose();
		});
	}

	private JTextField createStyledTextField() {
		JTextField tf = new JTextField();
		tf.setFont(new Font(UITheme.FONT_FAMILY, Font.PLAIN, 14));
		tf.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(UITheme.BORDER, 1),
				new EmptyBorder(5, 12, 5, 12)));
		return tf;
	}

	private void addFieldLabel(JPanel panel, String text, int x, int y) {
		JLabel lbl = new JLabel(text);
		lbl.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 11));
		lbl.setForeground(UITheme.TEXT_GRAY);
		lbl.setBounds(x, y, 250, 18);
		panel.add(lbl);
	}

	private void handleSignup() {
		String username = txtUsername.getText().trim();
		String email = txtEmail.getText().trim();
		String studentId = txtStudentId.getText().trim();
		String program = txtProgram.getText().trim();
		int semester = (int) spnSemester.getValue();
		String password = new String(txtPassword.getPassword());
		String confirmPassword = new String(txtConfirmPassword.getPassword());

		if (username.isEmpty() || email.isEmpty() || studentId.isEmpty() ||
				program.isEmpty() || password.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please fill in all fields!"); return;
		}
		if (!password.equals(confirmPassword)) {
			JOptionPane.showMessageDialog(this, "Passwords do not match!"); return;
		}
		if (password.length() < 4) {
			JOptionPane.showMessageDialog(this, "Password must be at least 4 characters!"); return;
		}
		if (!email.contains("@")) {
			JOptionPane.showMessageDialog(this, "Please enter a valid email!"); return;
		}

		Student student = new Student(username, password, email, studentId, program, semester);

		if (AuthService.signup(student)) {
			JOptionPane.showMessageDialog(this, "Account created successfully!");
			new LoginFrame().setVisible(true);
			dispose();
		} else {
			JOptionPane.showMessageDialog(this, "Username already taken!");
		}
	}
}

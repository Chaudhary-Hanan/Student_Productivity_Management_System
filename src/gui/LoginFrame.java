package gui;

import models.Student;
import services.AuthService;
import utils.UITheme;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginFrame extends JFrame {

	private JTextField txtUsername;
	private JPasswordField txtPassword;

	public LoginFrame() {
		setTitle("Smart Study Planner");
		setSize(900, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);

		initComponents();
	}

	private void initComponents() {
		setLayout(new BorderLayout());

		// ===== LEFT SIDE - Branding Panel =====
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
		leftPanel.setPreferredSize(new Dimension(450, 600));

		// Big icon (using text symbol that works everywhere)
		JLabel lblIcon = new JLabel("◆", SwingConstants.CENTER);
		lblIcon.setFont(new Font("Arial", Font.BOLD, 80));
		lblIcon.setForeground(Color.WHITE);
		lblIcon.setBounds(0, 130, 450, 90);
		leftPanel.add(lblIcon);

		JLabel lblBrand = new JLabel("Smart Study Planner", SwingConstants.CENTER);
		lblBrand.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 30));
		lblBrand.setForeground(Color.WHITE);
		lblBrand.setBounds(0, 230, 450, 40);
		leftPanel.add(lblBrand);

		JLabel lblTagline = new JLabel("Boost your productivity. Achieve your goals.", SwingConstants.CENTER);
		lblTagline.setFont(new Font(UITheme.FONT_FAMILY, Font.PLAIN, 14));
		lblTagline.setForeground(new Color(230, 230, 255));
		lblTagline.setBounds(0, 275, 450, 25);
		leftPanel.add(lblTagline);

		// Feature highlights
		String[] features = {
				"*  Smart Study Planner",
				"*  Pomodoro Focus Timer",
				"*  Goal Tracking & Analytics",
				"*  Achievements & Streaks"
		};

		int fy = 340;
		for (String feature : features) {
			JLabel lbl = new JLabel(feature);
			lbl.setFont(new Font(UITheme.FONT_FAMILY, Font.PLAIN, 14));
			lbl.setForeground(new Color(230, 230, 255));
			lbl.setBounds(110, fy, 300, 25);
			leftPanel.add(lbl);
			fy += 30;
		}

		add(leftPanel, BorderLayout.WEST);

		// ===== RIGHT SIDE - Login Form =====
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(null);
		rightPanel.setBackground(Color.WHITE);
		rightPanel.setPreferredSize(new Dimension(450, 600));

		JLabel lblWelcome = new JLabel("Welcome Back");
		lblWelcome.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 28));
		lblWelcome.setForeground(UITheme.TEXT_DARK);
		lblWelcome.setBounds(50, 100, 350, 35);
		rightPanel.add(lblWelcome);

		JLabel lblSubtitle = new JLabel("Please sign in to your account");
		lblSubtitle.setFont(new Font(UITheme.FONT_FAMILY, Font.PLAIN, 13));
		lblSubtitle.setForeground(UITheme.TEXT_GRAY);
		lblSubtitle.setBounds(50, 138, 350, 20);
		rightPanel.add(lblSubtitle);

		// Username
		JLabel lblUser = new JLabel("USERNAME");
		lblUser.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 11));
		lblUser.setForeground(UITheme.TEXT_GRAY);
		lblUser.setBounds(50, 195, 350, 18);
		rightPanel.add(lblUser);

		txtUsername = new JTextField();
		txtUsername.setFont(new Font(UITheme.FONT_FAMILY, Font.PLAIN, 14));
		txtUsername.setBounds(50, 215, 350, 42);
		txtUsername.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(UITheme.BORDER, 1),
				new EmptyBorder(5, 12, 5, 12)
		));
		rightPanel.add(txtUsername);

		// Password
		JLabel lblPass = new JLabel("PASSWORD");
		lblPass.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 11));
		lblPass.setForeground(UITheme.TEXT_GRAY);
		lblPass.setBounds(50, 275, 350, 18);
		rightPanel.add(lblPass);

		txtPassword = new JPasswordField();
		txtPassword.setFont(new Font(UITheme.FONT_FAMILY, Font.PLAIN, 14));
		txtPassword.setBounds(50, 295, 350, 42);
		txtPassword.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(UITheme.BORDER, 1),
				new EmptyBorder(5, 12, 5, 12)
		));
		rightPanel.add(txtPassword);

		// Login button
		JButton btnLogin = new JButton("SIGN IN");
		btnLogin.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 14));
		btnLogin.setBackground(UITheme.PRIMARY);
		btnLogin.setForeground(Color.WHITE);
		btnLogin.setFocusPainted(false);
		btnLogin.setBorderPainted(false);
		btnLogin.setBounds(50, 365, 350, 45);
		btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
		addHoverEffect(btnLogin, UITheme.PRIMARY, UITheme.PRIMARY_DARK);
		rightPanel.add(btnLogin);

		// Divider
		JLabel lblOr = new JLabel("Don't have an account?", SwingConstants.CENTER);
		lblOr.setFont(new Font(UITheme.FONT_FAMILY, Font.PLAIN, 12));
		lblOr.setForeground(UITheme.TEXT_GRAY);
		lblOr.setBounds(50, 435, 350, 20);
		rightPanel.add(lblOr);

		// Signup button
		JButton btnSignup = new JButton("CREATE ACCOUNT");
		btnSignup.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 13));
		btnSignup.setBackground(Color.WHITE);
		btnSignup.setForeground(UITheme.PRIMARY);
		btnSignup.setFocusPainted(false);
		btnSignup.setBorder(BorderFactory.createLineBorder(UITheme.PRIMARY, 2));
		btnSignup.setBounds(50, 465, 350, 42);
		btnSignup.setCursor(new Cursor(Cursor.HAND_CURSOR));
		rightPanel.add(btnSignup);

		add(rightPanel, BorderLayout.CENTER);

		// Listeners
		btnLogin.addActionListener(e -> handleLogin());
		txtPassword.addActionListener(e -> handleLogin());
		btnSignup.addActionListener(e -> {
			new SignupFrame().setVisible(true);
			dispose();
		});
	}

	private void addHoverEffect(JButton btn, Color normal, Color hover) {
		btn.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(hover); }
			public void mouseExited(java.awt.event.MouseEvent e) { btn.setBackground(normal); }
		});
	}

	private void handleLogin() {
		String username = txtUsername.getText().trim();
		String password = new String(txtPassword.getPassword());

		if (username.isEmpty() || password.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please fill in all fields!",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		Student student = AuthService.login(username, password);
		if (student != null) {
			new DashboardFrame().setVisible(true);
			dispose();
		} else {
			JOptionPane.showMessageDialog(this, "Invalid username or password!",
					"Login Failed", JOptionPane.ERROR_MESSAGE);
		}
	}
}

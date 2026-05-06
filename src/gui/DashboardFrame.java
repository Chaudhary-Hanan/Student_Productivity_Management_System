package gui;

import models.Student;
import models.Task;
import models.Exam;
import services.AuthService;
import services.FileManager;
import utils.UITheme;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class DashboardFrame extends JFrame {

	private Student currentUser;
	private JPanel contentPanel;
	private JButton activeButton;
	private JButton[] navButtons;

	public DashboardFrame() {
		currentUser = AuthService.getCurrentUser();

		setTitle("Smart Study Planner");
		setSize(1280, 760);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		initComponents();
	}

	private void initComponents() {
		setLayout(new BorderLayout());
		getContentPane().setBackground(UITheme.BG_LIGHT);

		// ===== SIDEBAR =====
		JPanel sidebar = new JPanel();
		sidebar.setLayout(null);
		sidebar.setBackground(UITheme.SIDEBAR_BG);
		sidebar.setPreferredSize(new Dimension(260, getHeight()));

		// Logo
		JLabel lblLogoIcon = new JLabel("◆", SwingConstants.LEFT);
		lblLogoIcon.setFont(new Font("Arial", Font.BOLD, 28));
		lblLogoIcon.setForeground(UITheme.PRIMARY);
		lblLogoIcon.setBounds(25, 25, 40, 40);
		sidebar.add(lblLogoIcon);

		JLabel lblLogo = new JLabel("Study Planner");
		lblLogo.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 18));
		lblLogo.setForeground(Color.WHITE);
		lblLogo.setBounds(65, 30, 200, 30);
		sidebar.add(lblLogo);

		// User profile section
		JPanel profileCard = new JPanel();
		profileCard.setLayout(null);
		profileCard.setBackground(UITheme.SIDEBAR_HOVER);
		profileCard.setBounds(20, 85, 220, 75);

		// Avatar circle
		JLabel avatar = new JLabel(String.valueOf(currentUser.getUsername().charAt(0)).toUpperCase(),
				SwingConstants.CENTER) {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(UITheme.PRIMARY);
				g2.fillOval(0, 0, getWidth(), getHeight());
				super.paintComponent(g);
			}
		};
		avatar.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 20));
		avatar.setForeground(Color.WHITE);
		avatar.setBounds(12, 17, 42, 42);
		profileCard.add(avatar);

		JLabel lblName = new JLabel(currentUser.getUsername());
		lblName.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 13));
		lblName.setForeground(Color.WHITE);
		lblName.setBounds(65, 17, 150, 20);
		profileCard.add(lblName);

		JLabel lblRole = new JLabel("Sem " + currentUser.getSemester() + " | " + currentUser.getProgram());
		lblRole.setFont(new Font(UITheme.FONT_FAMILY, Font.PLAIN, 11));
		lblRole.setForeground(UITheme.TEXT_LIGHT);
		lblRole.setBounds(65, 38, 150, 20);
		profileCard.add(lblRole);

		sidebar.add(profileCard);

		// Menu label
		JLabel lblMenu = new JLabel("MENU");
		lblMenu.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 10));
		lblMenu.setForeground(UITheme.TEXT_LIGHT);
		lblMenu.setBounds(30, 180, 100, 20);
		sidebar.add(lblMenu);

		// Navigation - using simple text symbols instead of emojis
		String[][] menuItems = {
				{"⌂", "Dashboard"},
				{"✓", "Tasks"},
				{"▦", "Study Planner"},
				{"⏱", "Pomodoro"},
				{"◎", "Goals"},
				{"✎", "Notes"},
				{"📊", "Analytics"},
				{"★", "Achievements"}
		};

		navButtons = new JButton[menuItems.length];
		int y = 210;
		for (int i = 0; i < menuItems.length; i++) {
			JButton btn = createNavButton(menuItems[i][0], menuItems[i][1]);
			btn.setBounds(15, y, 230, 44);
			sidebar.add(btn);
			navButtons[i] = btn;

			final int index = i;
			btn.addActionListener(e -> {
				setActiveButton(btn);
				handleNavigation(index);
			});
			y += 50;
		}

		// Set Dashboard active by default
		setActiveButton(navButtons[0]);

		// Logout
		JButton btnLogout = new JButton("  ⎋   Logout");
		btnLogout.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 13));
		btnLogout.setForeground(Color.WHITE);
		btnLogout.setBackground(UITheme.DANGER);
		btnLogout.setFocusPainted(false);
		btnLogout.setBorderPainted(false);
		btnLogout.setHorizontalAlignment(SwingConstants.LEFT);
		btnLogout.setBounds(15, 670, 230, 42);
		btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnLogout.addActionListener(e -> handleLogout());
		sidebar.add(btnLogout);

		add(sidebar, BorderLayout.WEST);

		// ===== CONTENT =====
		contentPanel = new JPanel(new BorderLayout());
		contentPanel.setBackground(UITheme.BG_LIGHT);
		add(contentPanel, BorderLayout.CENTER);

		showHomeDashboard();
	}

	private JButton createNavButton(String icon, String text) {
		JButton btn = new JButton();
		btn.setLayout(null);
		btn.setText("    " + icon + "    " + text);
		btn.setFont(new Font(UITheme.FONT_FAMILY, Font.PLAIN, 13));
		btn.setForeground(new Color(203, 213, 225));
		btn.setBackground(UITheme.SIDEBAR_BG);
		btn.setFocusPainted(false);
		btn.setBorderPainted(false);
		btn.setHorizontalAlignment(SwingConstants.LEFT);
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

		btn.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent e) {
				if (btn != activeButton) btn.setBackground(UITheme.SIDEBAR_HOVER);
			}
			public void mouseExited(java.awt.event.MouseEvent e) {
				if (btn != activeButton) btn.setBackground(UITheme.SIDEBAR_BG);
			}
		});

		return btn;
	}

	private void setActiveButton(JButton btn) {
		if (activeButton != null) {
			activeButton.setBackground(UITheme.SIDEBAR_BG);
			activeButton.setForeground(new Color(203, 213, 225));
			activeButton.setFont(new Font(UITheme.FONT_FAMILY, Font.PLAIN, 13));
		}
		activeButton = btn;
		btn.setBackground(UITheme.PRIMARY);
		btn.setForeground(Color.WHITE);
		btn.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 13));
	}

	private void handleNavigation(int index) {
		switch (index) {
			case 0: showHomeDashboard(); break;
			case 1:
				contentPanel.removeAll();
				contentPanel.add(new TaskFrame().getContentPane(), BorderLayout.CENTER);
				contentPanel.revalidate();
				contentPanel.repaint();
				break;
			case 3:
				contentPanel.removeAll();
				contentPanel.add(new PomodoroFrame().getContentPane(), BorderLayout.CENTER);
				contentPanel.revalidate();
				contentPanel.repaint();
				break;
			case 4:
				contentPanel.removeAll();
				contentPanel.add(new GoalsFrame().getContentPane(), BorderLayout.CENTER);
				contentPanel.revalidate();
				contentPanel.repaint();
				break;
			case 5:
				contentPanel.removeAll();
				contentPanel.add(new NotesFrame().getContentPane(), BorderLayout.CENTER);
				contentPanel.revalidate();
				contentPanel.repaint();
				break;
			default:
				JOptionPane.showMessageDialog(this, "Coming next!");
		}
	}

	private void showHomeDashboard() {
		contentPanel.removeAll();

		JPanel home = new JPanel();
		home.setLayout(null);
		home.setBackground(UITheme.BG_LIGHT);

		// Top header bar
		JPanel topBar = new JPanel();
		topBar.setLayout(null);
		topBar.setBackground(Color.WHITE);
		topBar.setBounds(0, 0, 1020, 70);
		topBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UITheme.BORDER));

		JLabel lblPageTitle = new JLabel("Dashboard");
		lblPageTitle.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 22));
		lblPageTitle.setForeground(UITheme.TEXT_DARK);
		lblPageTitle.setBounds(30, 20, 300, 30);
		topBar.add(lblPageTitle);

		JLabel lblDate = new JLabel(LocalDate.now().toString());
		lblDate.setFont(new Font(UITheme.FONT_FAMILY, Font.PLAIN, 12));
		lblDate.setForeground(UITheme.TEXT_GRAY);
		lblDate.setBounds(850, 28, 150, 20);
		lblDate.setHorizontalAlignment(SwingConstants.RIGHT);
		topBar.add(lblDate);

		home.add(topBar);

		// Welcome
		JLabel lblWelcome = new JLabel("Welcome back, " + currentUser.getUsername() + "!");
		lblWelcome.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 24));
		lblWelcome.setForeground(UITheme.TEXT_DARK);
		lblWelcome.setBounds(30, 90, 600, 30);
		home.add(lblWelcome);

		JLabel lblWelcomeSub = new JLabel("Here's what's happening with your studies today.");
		lblWelcomeSub.setFont(new Font(UITheme.FONT_FAMILY, Font.PLAIN, 13));
		lblWelcomeSub.setForeground(UITheme.TEXT_GRAY);
		lblWelcomeSub.setBounds(30, 122, 600, 20);
		home.add(lblWelcomeSub);

		// ===== STAT CARDS =====
		ArrayList<Task> tasks = FileManager.loadObjects("tasks_" + currentUser.getUsername() + ".dat");
		if (tasks == null) tasks = new ArrayList<>();
		int pending = 0, exams = 0, completed = 0;
		for (Task t : tasks) {
			if (!t.isCompleted()) pending++;
			else completed++;
			if (t instanceof Exam && !t.isCompleted()) exams++;
		}

		JPanel card1 = createStatCard("Total Points", String.valueOf(currentUser.getTotalPoints()),
				"★", UITheme.WARNING);
		card1.setBounds(30, 165, 235, 120);
		home.add(card1);

		JPanel card2 = createStatCard("Day Streak", currentUser.getStreakDays() + " days",
				"▲", UITheme.DANGER);
		card2.setBounds(280, 165, 235, 120);
		home.add(card2);

		JPanel card3 = createStatCard("Pending Tasks", String.valueOf(pending),
				"●", UITheme.PRIMARY);
		card3.setBounds(530, 165, 235, 120);
		home.add(card3);

		JPanel card4 = createStatCard("Completed", String.valueOf(completed),
				"✓", UITheme.SUCCESS);
		card4.setBounds(780, 165, 235, 120);
		home.add(card4);

		// ===== EXAM COUNTDOWN =====
		JPanel countdownPanel = createPanelCard("Exam Countdown", 30, 305, 490, 280);

		DefaultListModel<String> examModel = new DefaultListModel<>();
		for (Task t : tasks) {
			if (t instanceof Exam && !t.isCompleted()) {
				long days = ChronoUnit.DAYS.between(LocalDate.now(), t.getDeadline());
				if (days >= 0) {
					examModel.addElement("  " + t.getTitle() + " - " + t.getSubject() +
							"  |  " + days + " days left");
				}
			}
		}
		if (examModel.isEmpty()) examModel.addElement("  No upcoming exams. You're all clear!");

		JList<String> examList = new JList<>(examModel);
		examList.setFont(new Font(UITheme.FONT_FAMILY, Font.PLAIN, 13));
		examList.setBackground(Color.WHITE);
		examList.setFixedCellHeight(36);
		examList.setBorder(new EmptyBorder(5, 5, 5, 5));
		JScrollPane examScroll = new JScrollPane(examList);
		examScroll.setBounds(20, 55, 450, 210);
		examScroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER));
		countdownPanel.add(examScroll);
		home.add(countdownPanel);

		// ===== DEADLINE ALERTS =====
		JPanel alertsPanel = createPanelCard("Smart Deadline Alerts", 535, 305, 480, 280);

		DefaultListModel<String> alertModel = new DefaultListModel<>();
		for (Task t : tasks) {
			if (!t.isCompleted()) {
				String alertLevel = t.getAlertLevel();
				String marker = getAlertMarker(alertLevel);
				alertModel.addElement("  " + marker + "  [" + alertLevel + "]  " + t.getTitle() +
						"  -  " + t.getDaysLeft() + " days");
			}
		}
		if (alertModel.isEmpty()) alertModel.addElement("  No pending deadlines!");

		JList<String> alertList = new JList<>(alertModel);
		alertList.setFont(new Font(UITheme.FONT_FAMILY, Font.PLAIN, 12));
		alertList.setFixedCellHeight(36);
		alertList.setBorder(new EmptyBorder(5, 5, 5, 5));
		JScrollPane alertScroll = new JScrollPane(alertList);
		alertScroll.setBounds(20, 55, 440, 210);
		alertScroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER));
		alertsPanel.add(alertScroll);
		home.add(alertsPanel);

		// Motivational quote
		JPanel quotePanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				GradientPaint gp = new GradientPaint(0, 0, UITheme.PRIMARY,
						getWidth(), 0, UITheme.SECONDARY);
				g2.setPaint(gp);
				g2.fillRect(0, 0, getWidth(), getHeight());
			}
		};
		quotePanel.setLayout(new BorderLayout());
		quotePanel.setBounds(30, 605, 985, 80);

		String[] quotes = {
				"\" The expert in anything was once a beginner. \"",
				"\" Success is the sum of small efforts repeated daily. \"",
				"\" Don't watch the clock; do what it does. Keep going. \"",
				"\" Your only limit is your mind. \"",
				"\" The beautiful thing about learning is that nobody can take it away. \""
		};
		JLabel lblQuote = new JLabel(quotes[(int)(Math.random() * quotes.length)], SwingConstants.CENTER);
		lblQuote.setFont(new Font(UITheme.FONT_FAMILY, Font.ITALIC, 16));
		lblQuote.setForeground(Color.WHITE);
		quotePanel.add(lblQuote);
		home.add(quotePanel);

		contentPanel.add(home, BorderLayout.CENTER);
		contentPanel.revalidate();
		contentPanel.repaint();
	}

	private JPanel createStatCard(String title, String value, String icon, Color accentColor) {
		JPanel card = new JPanel();
		card.setLayout(null);
		card.setBackground(Color.WHITE);
		card.setBorder(BorderFactory.createLineBorder(UITheme.BORDER, 1));

		// Icon in colored circle
		JLabel lblIcon = new JLabel(icon, SwingConstants.CENTER) {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 30));
				g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
				super.paintComponent(g);
			}
		};
		lblIcon.setFont(new Font("Arial", Font.BOLD, 22));
		lblIcon.setForeground(accentColor);
		lblIcon.setBounds(20, 20, 50, 50);
		card.add(lblIcon);

		JLabel lblTitle = new JLabel(title);
		lblTitle.setFont(new Font(UITheme.FONT_FAMILY, Font.PLAIN, 12));
		lblTitle.setForeground(UITheme.TEXT_GRAY);
		lblTitle.setBounds(85, 22, 150, 20);
		card.add(lblTitle);

		JLabel lblValue = new JLabel(value);
		lblValue.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 24));
		lblValue.setForeground(UITheme.TEXT_DARK);
		lblValue.setBounds(85, 42, 150, 35);
		card.add(lblValue);

		return card;
	}

	private JPanel createPanelCard(String title, int x, int y, int w, int h) {
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBackground(Color.WHITE);
		panel.setBorder(BorderFactory.createLineBorder(UITheme.BORDER, 1));
		panel.setBounds(x, y, w, h);

		JLabel lblTitle = new JLabel(title);
		lblTitle.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 15));
		lblTitle.setForeground(UITheme.TEXT_DARK);
		lblTitle.setBounds(20, 18, 300, 22);
		panel.add(lblTitle);

		return panel;
	}

	private String getAlertMarker(String level) {
		switch (level) {
			case "OVERDUE": return "■";
			case "URGENT - DUE TODAY": return "●";
			case "CRITICAL": return "▲";
			case "HIGH": return "◆";
			case "MEDIUM": return "◇";
			default: return "○";
		}
	}

	private void handleLogout() {
		int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?",
				"Logout", JOptionPane.YES_NO_OPTION);
		if (confirm == JOptionPane.YES_OPTION) {
			AuthService.logout();
			new LoginFrame().setVisible(true);
			dispose();
		}
	}
}

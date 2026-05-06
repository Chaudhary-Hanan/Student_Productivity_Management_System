package gui;

import models.StudySession;
import services.AuthService;
import services.FileManager;
import utils.UITheme;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PomodoroFrame extends JFrame {

	private Timer timer;
	private int timeRemaining;
	private int sessionDuration = 25 * 60;  // 25 minutes default
	private int breakDuration = 5 * 60;
	private boolean isWorkSession = true;
	private boolean isRunning = false;
	private int sessionsCompleted = 0;

	private JLabel lblTimer, lblMode, lblSessionCount;
	private JButton btnStart, btnPause, btnReset;
	private JTextField txtSubject;
	private JComboBox<String> cmbDuration;
	private JProgressBar progressBar;

	public PomodoroFrame() {
		timeRemaining = sessionDuration;
		initComponents();
	}

	private void initComponents() {
		getContentPane().setLayout(new BorderLayout());
		getContentPane().setBackground(UITheme.BG_LIGHT);

		// Top bar
		JPanel topBar = new JPanel(null);
		topBar.setBackground(Color.WHITE);
		topBar.setPreferredSize(new Dimension(1020, 70));
		topBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UITheme.BORDER));

		JLabel lblPageTitle = new JLabel("Focus Session Timer");
		lblPageTitle.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 22));
		lblPageTitle.setForeground(UITheme.TEXT_DARK);
		lblPageTitle.setBounds(30, 20, 400, 30);
		topBar.add(lblPageTitle);

		getContentPane().add(topBar, BorderLayout.NORTH);

		// Main panel
		JPanel mainPanel = new JPanel(null);
		mainPanel.setBackground(UITheme.BG_LIGHT);

		// Timer card (large center card)
		JPanel timerCard = new JPanel(null) {
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
		timerCard.setBounds(180, 30, 640, 380);

		lblMode = new JLabel("WORK SESSION", SwingConstants.CENTER);
		lblMode.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 14));
		lblMode.setForeground(new Color(230, 230, 255));
		lblMode.setBounds(0, 35, 640, 25);
		timerCard.add(lblMode);

		lblTimer = new JLabel(formatTime(timeRemaining), SwingConstants.CENTER);
		lblTimer.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 110));
		lblTimer.setForeground(Color.WHITE);
		lblTimer.setBounds(0, 80, 640, 130);
		timerCard.add(lblTimer);

		// Progress bar
		progressBar = new JProgressBar(0, sessionDuration);
		progressBar.setValue(sessionDuration);
		progressBar.setBounds(120, 230, 400, 12);
		progressBar.setForeground(Color.WHITE);
		progressBar.setBackground(new Color(255, 255, 255, 80));
		progressBar.setBorderPainted(false);
		timerCard.add(progressBar);

		lblSessionCount = new JLabel("Sessions completed today: 0", SwingConstants.CENTER);
		lblSessionCount.setFont(new Font(UITheme.FONT_FAMILY, Font.PLAIN, 13));
		lblSessionCount.setForeground(new Color(230, 230, 255));
		lblSessionCount.setBounds(0, 260, 640, 20);
		timerCard.add(lblSessionCount);

		// Control buttons inside the card
		btnStart = new JButton("START");
		btnStart.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 14));
		btnStart.setBackground(Color.WHITE);
		btnStart.setForeground(UITheme.PRIMARY);
		btnStart.setFocusPainted(false);
		btnStart.setBorderPainted(false);
		btnStart.setBounds(170, 305, 120, 45);
		btnStart.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnStart.addActionListener(e -> startTimer());
		timerCard.add(btnStart);

		btnPause = new JButton("PAUSE");
		btnPause.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 14));
		btnPause.setBackground(new Color(255, 255, 255, 50));
		btnPause.setForeground(Color.WHITE);
		btnPause.setFocusPainted(false);
		btnPause.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
		btnPause.setBounds(300, 305, 120, 45);
		btnPause.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnPause.addActionListener(e -> pauseTimer());
		timerCard.add(btnPause);

		btnReset = new JButton("RESET");
		btnReset.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 14));
		btnReset.setBackground(new Color(255, 255, 255, 50));
		btnReset.setForeground(Color.WHITE);
		btnReset.setFocusPainted(false);
		btnReset.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
		btnReset.setBounds(430, 305, 120, 45);
		btnReset.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnReset.addActionListener(e -> resetTimer());
		timerCard.add(btnReset);

		mainPanel.add(timerCard);

		// Settings card (below timer)
		JPanel settingsCard = new JPanel(null);
		settingsCard.setBackground(Color.WHITE);
		settingsCard.setBorder(BorderFactory.createLineBorder(UITheme.BORDER, 1));
		settingsCard.setBounds(180, 430, 640, 180);

		JLabel lblSettings = new JLabel("Session Settings");
		lblSettings.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 16));
		lblSettings.setForeground(UITheme.TEXT_DARK);
		lblSettings.setBounds(25, 20, 300, 25);
		settingsCard.add(lblSettings);

		// Subject
		JLabel lblSubj = new JLabel("STUDYING");
		lblSubj.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 11));
		lblSubj.setForeground(UITheme.TEXT_GRAY);
		lblSubj.setBounds(25, 60, 100, 18);
		settingsCard.add(lblSubj);

		txtSubject = new JTextField("General Study");
		txtSubject.setFont(new Font(UITheme.FONT_FAMILY, Font.PLAIN, 13));
		txtSubject.setBounds(25, 82, 340, 36);
		txtSubject.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(UITheme.BORDER, 1),
				BorderFactory.createEmptyBorder(5, 10, 5, 10)));
		settingsCard.add(txtSubject);

		// Duration
		JLabel lblDur = new JLabel("DURATION");
		lblDur.setFont(new Font(UITheme.FONT_FAMILY, Font.BOLD, 11));
		lblDur.setForeground(UITheme.TEXT_GRAY);
		lblDur.setBounds(390, 60, 100, 18);
		settingsCard.add(lblDur);

		cmbDuration = new JComboBox<>(new String[]{"15 min", "25 min", "30 min", "45 min", "60 min"});
		cmbDuration.setSelectedIndex(1);
		cmbDuration.setFont(new Font(UITheme.FONT_FAMILY, Font.PLAIN, 13));
		cmbDuration.setBounds(390, 82, 220, 36);
		cmbDuration.setBackground(Color.WHITE);
		cmbDuration.addActionListener(e -> updateDuration());
		settingsCard.add(cmbDuration);

		JLabel lblHint = new JLabel("Tip: Stay focused for the full session, then take a 5-min break.");
		lblHint.setFont(new Font(UITheme.FONT_FAMILY, Font.ITALIC, 12));
		lblHint.setForeground(UITheme.TEXT_GRAY);
		lblHint.setBounds(25, 135, 600, 20);
		settingsCard.add(lblHint);

		mainPanel.add(settingsCard);

		getContentPane().add(mainPanel, BorderLayout.CENTER);
	}

	private void updateDuration() {
		if (isRunning) return;
		String selected = (String) cmbDuration.getSelectedItem();
		int mins = Integer.parseInt(selected.split(" ")[0]);
		sessionDuration = mins * 60;
		timeRemaining = sessionDuration;
		progressBar.setMaximum(sessionDuration);
		progressBar.setValue(sessionDuration);
		lblTimer.setText(formatTime(timeRemaining));
	}

	private void startTimer() {
		if (isRunning) return;
		isRunning = true;
		cmbDuration.setEnabled(false);
		txtSubject.setEnabled(false);

		if (timer == null) {
			timer = new Timer(1000, e -> tick());
		}
		timer.start();
	}

	private void pauseTimer() {
		if (timer != null && isRunning) {
			timer.stop();
			isRunning = false;
		}
	}

	private void resetTimer() {
		if (timer != null) timer.stop();
		isRunning = false;
		isWorkSession = true;
		timeRemaining = sessionDuration;
		lblTimer.setText(formatTime(timeRemaining));
		lblMode.setText("WORK SESSION");
		progressBar.setValue(sessionDuration);
		cmbDuration.setEnabled(true);
		txtSubject.setEnabled(true);
	}

	private void tick() {
		timeRemaining--;
		lblTimer.setText(formatTime(timeRemaining));
		progressBar.setValue(timeRemaining);

		if (timeRemaining <= 0) {
			timer.stop();
			isRunning = false;

			if (isWorkSession) {
				// Save study session
				int minutes = sessionDuration / 60;
				StudySession session = new StudySession(txtSubject.getText().trim(), minutes);
				session.setCompleted(true);

				String file = "sessions_" + AuthService.getCurrentUser().getUsername() + ".dat";
				ArrayList<StudySession> sessions = FileManager.loadObjects(file);
				if (sessions == null) sessions = new ArrayList<>();
				sessions.add(session);
				FileManager.saveObjects(sessions, file);

				// Award points
				AuthService.getCurrentUser().addPoints(20);
				AuthService.updateUser(AuthService.getCurrentUser());

				sessionsCompleted++;
				lblSessionCount.setText("Sessions completed today: " + sessionsCompleted);

				JOptionPane.showMessageDialog(this,
						"Great work! Session complete. +20 points awarded.\nTime for a 5-min break!");

				// Switch to break
				isWorkSession = false;
				timeRemaining = breakDuration;
				lblMode.setText("BREAK TIME");
				progressBar.setMaximum(breakDuration);
				progressBar.setValue(breakDuration);
				lblTimer.setText(formatTime(timeRemaining));
			} else {
				JOptionPane.showMessageDialog(this, "Break over! Ready for another session?");
				isWorkSession = true;
				timeRemaining = sessionDuration;
				lblMode.setText("WORK SESSION");
				progressBar.setMaximum(sessionDuration);
				progressBar.setValue(sessionDuration);
				lblTimer.setText(formatTime(timeRemaining));
				cmbDuration.setEnabled(true);
				txtSubject.setEnabled(true);
			}
		}
	}

	private String formatTime(int seconds) {
		int mins = seconds / 60;
		int secs = seconds % 60;
		return String.format("%02d:%02d", mins, secs);
	}
}

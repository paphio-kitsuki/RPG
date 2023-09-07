package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import game.choose.ChosenEvent;
import game.choose.ChosenListener;
import utility.ChooseGroup;
import utility.KeyList;
import utility.MyButton;

public class TitlePanel extends JPanel implements ActionListener, ChosenListener {

	private final NextListener window;
	private JLabel titleLabel = new JLabel("<html>Vtuber<br>&emsp;&emsp;Adventure");
	private ChooseGroup group = new ChooseGroup();
	private MyButton buttons[] = new MyButton[2];
	private boolean flag = false;
	private KeyList key = null;

	public TitlePanel(MainWindow window) {
		this.window = window;
		this.key = window.getKeyList();
		setLayout(null);
		setOpaque(false);
		titleLabel.setForeground(Color.BLACK);
		titleLabel.setBounds((MainWindow.size.width - 700) / 2, 50, 700, 200);
		titleLabel.setFont(new Font("", Font.BOLD | Font.ITALIC, 90));
		add(titleLabel);
		group.setBounds((MainWindow.size.width - 200) / 2 - 10, MainWindow.size.height - 50 - 150 - 10, 220, 170);
		buttons[0] = new MyButton("はじめから");
		buttons[1] = new MyButton("つづきから");
		for (int i = 0; i < buttons.length; i++) {
			buttons[i].setBounds(10, 10 + 100 * i, 200, 50);
			buttons[i].setFont(new Font("", Font.BOLD, 30));
			buttons[i].addActionListener(this);
			buttons[i].setFocusable(false);
			group.add(buttons[i]);
		}
		key.addUpdateListener(group);
		key.addChildKeyListener(group);
		group.addChosenListener(this);
		add(group);
	}

	public boolean getFlag() {
		return flag;
	}

	private void finish() {
		key.removeUpdateListener(group);
		key.removeChildKeyListener(group);
		window.next(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == buttons[0]) {
			flag = false;
			finish();
		} else if (e.getSource() == buttons[1]) {
			flag = true;
			finish();
		}
	}

	@Override
	public void chosen(ChosenEvent e) {
		switch (e.getIndex()) {
		case 1:
			flag = true;
		case 0:
			finish();
			break;
		}
	}

	public void paintComponent(Graphics g) {
		MainWindow.drawBackground((Graphics2D) g);
		super.paintComponent(g);
	}
}

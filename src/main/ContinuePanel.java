package main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import utility.Data;
import utility.MyColor;

public class ContinuePanel extends JPanel implements MouseListener, KeyListener {

	private static final Icon nullIcon = new Icon() {
		@Override
		public void paintIcon(Component c, Graphics g, int x, int y) {
			return;
		}

		@Override
		public int getIconWidth() {
			return 0;
		}

		@Override
		public int getIconHeight() {
			return 0;
		}
	};
	private static final SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy / MM / dd (E) ");
	private static final SimpleDateFormat dateFormat2 = new SimpleDateFormat("kk");
	private static final SimpleDateFormat dateFormat3 = new SimpleDateFormat(" : mm");

	private JPanel dataPanel;
	private JScrollPane dataScroll;
	private JScrollBar scrollbar;
	private Data originDatas[];
	private Data datas[];
	private JRadioButton[] datanames;
	private ButtonGroup datagroup;
	private Data data;
	private NextListener nl;
	private MouseEvent tmpMouseEvent = null;
	private JLabel info = new JLabel("読み込むデータを選んでください。");
	private String sortLabels[] = { "並び順：名前(昇順)", "並び順：名前(降順)", "並び順：日時(昇順)", "並び順：日時(降順)" };
	private int mode = 0;
	private JLabel nowSort = new JLabel(sortLabels[mode]);

	public ContinuePanel(NextListener nl) {
		this.nl = nl;
		setLayout(null);
		setOpaque(false);
		info.setForeground(Color.BLACK);
		info.setOpaque(true);
		info.setBackground(MyColor.toAlpha(Color.WHITE, 100));
		info.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 25));
		info.setBounds(50, 20, 400, 50);
		add(info);
		nowSort.setForeground(Color.BLACK);
		nowSort.setOpaque(true);
		nowSort.setBackground(MyColor.toAlpha(Color.WHITE, 100));
		nowSort.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 25));
		nowSort.setBounds(570, 20, 240, 50);
		add(nowSort);
		dataPanel = new JPanel();
		dataScroll = new JScrollPane(dataPanel);
		scrollbar = dataScroll.getVerticalScrollBar();
		originDatas = Data.searchFiles();
		datas = originDatas;
		datanames = new JRadioButton[datas.length];
		datagroup = new ButtonGroup();

		dataScroll.setBounds(50, 80, MainWindow.size.width - 100, MainWindow.size.height - 150);
		dataScroll.getVerticalScrollBar().setUnitIncrement(9);
		dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));
		dataPanel.setBackground(MyColor.toAlpha(Color.WHITE, 0));
		dataScroll.setBackground(MyColor.toAlpha(Color.WHITE, 0));
		for (int i = 0; i < datanames.length; i++) {
			datanames[i] = new JRadioButton();
			datanames[i].setHorizontalAlignment(JLabel.LEFT);
			datanames[i].setVerticalAlignment(JLabel.TOP);
			datanames[i].setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
			datanames[i].setBackground(Color.LIGHT_GRAY);
			datanames[i].setOpaque(true);
			datanames[i].setForeground(Color.BLACK);
			datanames[i].setIcon(nullIcon);
			datanames[i].setSelectedIcon(nullIcon);
			datanames[i].addMouseListener(this);
			datanames[i].setText(datas[i].getFilename());
			datanames[i].setFocusable(false);
			dataPanel.add(datanames[i]);
			datagroup.add(datanames[i]);
		}
		this.add(dataScroll);
		sort();
		if (datanames.length != 0)
			datagroup.setSelected(datanames[0].getModel(), true);
		updateChoiseText();
	}

	public Data getData() {
		return data;
	}

	public void setFlag(ButtonModel bm) {
		if (bm != null) {
			for (int i = 0; i < datanames.length; i++) {
				if (datanames[i].getModel().equals(bm)) {
					this.data = datas[i];
					return;
				}
			}
		}
		this.data = null;
	}

	/*
			public void chooseSort(JButton o) {
				for (JButton tmp : sortChoise) {
					tmp.setEnabled(true);
					if (o.equals(tmp))
						tmp.setEnabled(false);
				}
				sort();
			}
		
			public void chooseAscending(JButton o) {
				for (JButton tmp : ascending) {
					tmp.setEnabled(true);
					if (tmp.equals(o))
						tmp.setEnabled(false);
				}
				sort();
			}
		*/
	public void sort() {
		/*
		int sort = 0;
		int ascend = 0;
		for (int i = 0; i < sortChoise.length; i++)
			if (!sortChoise[i].isEnabled())
				sort = i;
		for (int i = 0; i < ascending.length; i++)
			if (!ascending[i].isEnabled())
				ascend = i;
				*/
		int sort = mode / 2;
		int ascend = mode % 2;
		Data target = null;
		if (datagroup.getSelection() != null) {
			for (int i = 0; i < datas.length; i++) {
				if (datagroup.getSelection().equals(datanames[i].getModel()))
					target = datas[i];
			}
		}
		switch (sort) {
		case 0:
			datas = Data.sortOnFilename(originDatas, ascend == 0);
			break;
		case 1:
			datas = Data.sortOnDate(originDatas, ascend == 0);
			break;
		}
		if (target != null) {
			datagroup.clearSelection();
			for (int i = 0; i < datas.length; i++) {
				if (target.equals(datas[i]))
					datagroup.setSelected(datanames[i].getModel(), true);
			}
		}
		nowSort.setText(sortLabels[mode]);
		repaint(nowSort.getBounds());
		updateChoiseText();
	}

	public static String convertDate(Date date) {
		String tmp = dateFormat2.format(date);
		if (tmp.equals("24")) {
			tmp = "00";
		}
		return dateFormat1.format(date) + tmp + dateFormat3.format(date);
	}

	public int getChosenIndex() {
		int index = -1;
		for (int i = 0; i < datanames.length; i++) {
			if (datagroup.getSelection() != null
					&& datagroup.getSelection().equals(datanames[i].getModel()))
				index = i;
		}
		return index;
	}

	public void setChosenIndex(int index) {
		if (index < 0 || index > datanames.length)
			return;
		datagroup.clearSelection();
		datagroup.setSelected(datanames[index].getModel(), true);
		updateChoiseText();
	}

	public void updateChoiseText() {
		if (datanames.length == 0)
			return;
		int index = -1;
		for (int i = 0; i < datanames.length; i++) {
			datanames[i].setBackground(Color.LIGHT_GRAY);
			datanames[i].setText("<html>" + datas[i].getFilename() + "&emsp;＜ 最終プレイ日時 : ");
			if (datas[i].getLatestTime() != -1)
				datanames[i].setText(datanames[i].getText()
						+ convertDate(Date.from(Instant.ofEpochMilli(datas[i].getLatestTime()))) + " ＞");
			else
				datanames[i].setText(datanames[i].getText() + "データが破損しています。 ＞");
			if (datagroup.getSelection() != null
					&& datagroup.getSelection().equals(datanames[i].getModel()))
				index = i;
		}
		if (index != -1) {
			JRadioButton tmp = datanames[index];
			tmp.setText(tmp.getText() + "<br><br>最後にいた場所 … " + datas[index].getBackground().getName());
			tmp.setBackground(Color.WHITE);
		}
		for (int i = 0; i < datanames.length; i++) {
			datanames[i].setText(datanames[i].getText() + "<br><br></html>");
			datanames[i].repaint(0);
		}
		if (index >= 4)
			scrollbar.setValue((index - 3) * 74);
		else
			scrollbar.setValue(0);
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (tmpMouseEvent == null)
			tmpMouseEvent = e;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		MouseEvent tmpEvent = tmpMouseEvent;
		tmpMouseEvent = null;
		if (!tmpEvent.getSource().equals(e.getSource()))
			return;
		updateChoiseText();
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	public void paintComponent(Graphics g) {
		MainWindow.drawBackground((Graphics2D) g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
		g2.setPaint(Color.BLACK);
		g2.drawString("← → A D で並び順変更", 60, MainWindow.size.height - 15);
		super.paintComponent(g);
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int index = getChosenIndex();
		int mode = this.mode;
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
		case KeyEvent.VK_KP_UP:
		case KeyEvent.VK_W:
			index--;
			break;
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_KP_DOWN:
		case KeyEvent.VK_S:
			index++;
			break;
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_KP_LEFT:
		case KeyEvent.VK_A:
			mode--;
			if (mode < 0)
				mode = 3;
			break;
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_KP_RIGHT:
		case KeyEvent.VK_D:
			mode++;
			mode %= 4;
			break;
		case KeyEvent.VK_ENTER:
		case KeyEvent.VK_Z:
			if (datanames.length != 0) {
				setFlag(datagroup.getSelection());
				nl.next(this);
			}
			break;
		case KeyEvent.VK_X:
			setFlag(null);
			nl.next(this);
			break;
		}
		if (datanames.length != 0) {
			index = (index + datanames.length) % datanames.length;
			if (index != getChosenIndex())
				setChosenIndex(index);
			if (mode != this.mode) {
				this.mode = mode;
				sort();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

}

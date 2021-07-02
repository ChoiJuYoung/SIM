package userInterface;

import java.awt.Toolkit;
import java.awt.Dimension;

import java.awt.EventQueue;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import mapProcessor.MapInitializer;
import mapProcessor.ReadMapController;
import userInterface.SearchManager;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

@SuppressWarnings("serial")
public class DisplayMapUI extends JFrame  implements Runnable
{
	private int _width, _height; // 화면 전체 해상도 가로와 세로
	
	private SearchManager sm;
	
	private JPanel contentPane;
	private MainPanel drawPane;
	private ReadMapController rmc;
	
	public static void main(String[] args) 
	{
		EventQueue.invokeLater(new Runnable() 
		{
			public void run() 
			{
				try 
				{
					DisplayMapUI frame = new DisplayMapUI();
					frame.setVisible(true);
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		});
	}
	
	public DisplayMapUI() 
	{
		sm = new SearchManager(this);
		rmc = new ReadMapController();

		Dimension res = Toolkit.getDefaultToolkit().getScreenSize();
		_width = res.width;
		_height = res.height;
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1540 * _width / 1920 , 810 * _height / 1080);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnInit = new JButton("Init");
		btnInit.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				initMap();
				btnInit.setVisible(false);
				// END
			}
		});
		btnInit.setBounds(1400, 28, 106, 23);
		contentPane.add(btnInit);
		
		JButton btnStart = new JButton("startSearch");
		
				
		btnStart.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				startSearch();
				btnStart.setVisible(false);
			}
		});
		btnStart.setBounds(1400, 78, 106, 23);
		contentPane.add(btnStart);
		
		drawPane = new MainPanel();
		drawPane.setBounds(0, 0, 1360, 810);
		contentPane.add(drawPane);
		
		Thread thr = new Thread(this);
		thr.start();
	}

	private class MainPanel extends JPanel
	{
		private int _px1, _py1, _px2, _py2;
		  
		private int _xdiff;
		private int _ydiff;
		
		private Vector<Point> baseS;
		private Vector<Point> baseE;
		
		private Vector<Point> usedS;
		private Vector<Point> usedE;
		
		private Point position;
		
		private Point[][] point;

		public MainPanel()
		{
			baseS = new Vector<Point>();
			baseE = new Vector<Point>();
			usedS = new Vector<Point>();
			usedE = new Vector<Point>();
			  
			position = new Point(-1, -1);			
		}
		
		public void init(int x, int y)
		{
			getWorld(x, y);
		}
		
		public void initPos(int x, int y)
		{
			position = point[x][y];
		}

		
		private void getWorld(int x, int y)
		{
			  point = new Point[x][y];
			  
			  _px1 = 12;
			  _py1 = 750 * _height / 1080;
			  _px2 = 1350 * _width / 1920;
			  _py2 = 10;
			  
			  _xdiff = (_px2 - _px1) / (x - 1);
			  _ydiff = (_py1 - _py2) / (y - 1);
			  
			  for(int i = 0; i < x; i++)
				  for(int j = 0; j < y; j++)
					  point[i][j] = new Point(_px1 + i * _xdiff, _py1 - j * _ydiff);

			  
			  for(int i = 0; i < x; i++)
			  {
				  for(int j = 0; j < y - 1; j++)
				  {
					  baseS.add(point[i][j]);
					  baseE.add(point[i][j + 1]);
				  }
			  }

			  for(int j = 0; j < y; j++)
			  {
				  for(int i = 0; i < x - 1; i++)
				  {
					  baseS.add(point[i][j]);
					  baseE.add(point[i + 1][j]);
				  }
			  }
		}
		
		private void updatePosition()
		{
			int x = rmc.getCurrRobPos().getX();
			int y = rmc.getCurrRobPos().getY();
			path(x,  y);
			
			position = point[x][y];
		}
		
		private void path(int x, int y)
		{
			usedS.add(position);
			usedE.add(point[x][y]);
		}
		
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			
			// 
			for(int i = 0; i < baseS.size(); i++)
			{
				int x1 = baseS.get(i).x;
				int y1 = baseS.get(i).y;
				
				int x2 = baseE.get(i).x;
				int y2 = baseE.get(i).y;
				
				g.drawLine(x1, y1, x2, y2);
			}
			
			// 
			g.setColor(Color.red);
			
			for(int i = 0; i < usedS.size(); i++)
			{
				int x1 = usedS.get(i).x;
				int y1 = usedS.get(i).y;
				
				int x2 = usedE.get(i).x;
				int y2 = usedE.get(i).y;
				
				g.drawLine(x1, y1, x2, y2);
			}
			
			for(int i = 0; i < rmc.getSpotsLeft().size(); i++)
			{
				int x = _px1 + rmc.getSpotsLeft().get(i).getX() * _xdiff - 24;
				int y = _py1 - rmc.getSpotsLeft().get(i).getY() * _ydiff - 24;

				Image spotImg = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("nonvisitedspot.png"));
				g.drawImage(spotImg, x, y, this);
			}
			
			for(int i = 0; i < rmc.getSpotsVisited().size(); i++)
			{
				int x = _px1 + rmc.getSpotsVisited().get(i).getX() * _xdiff - 24;
				int y = _py1 - rmc.getSpotsVisited().get(i).getY() * _ydiff - 24;

				Image vSpotImg = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("visitedspot.png"));
				g.drawImage(vSpotImg, x, y, this);
			}

			for(int i = 0; i < rmc.getHazards().size(); i++)
			{
				int x = _px1 + rmc.getHazards().get(i).getX() * _xdiff - 24;
				int y = _py1 - rmc.getHazards().get(i).getY() * _ydiff - 24;

				Image hazardImg = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("hazard.png"));
				g.drawImage(hazardImg, x, y, this);
			}

			for(int i = 0; i < rmc.getColors().size(); i++)
			{
				int x = _px1 + rmc.getColors().get(i).getX() * _xdiff - 24;
				int y = _py1 - rmc.getColors().get(i).getY() * _ydiff - 24;

				Image colorImg = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("color.png"));
				g.drawImage(colorImg, x, y, this);
			}

			if(position.x != -1)
			{
				int x = _px1 + rmc.getCurrRobPos().getX() * _xdiff - 24;
				int y = _py1 - rmc.getCurrRobPos().getY() * _ydiff - 24;
				
				Image posImg = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("robot" + rmc.getCurrRobDir() + ".png"));
				g.drawImage(posImg, x, y, this);
			}
		}
	}
	
	public void updatePosition()
	{		
		drawPane.updatePosition();
		drawPane.repaint();
	}

	@Override
	public void run() 
	{
		while(true)
		{
			drawPane.repaint();
		}
	}

	public void displayMsg(String msg)
	{
		JOptionPane.showMessageDialog(null, msg);
	}
	
	private void startSearch()
	{
		new Thread(sm).start();		
	}
	
	private void initMap()
	{
		MapInitializer mi = new MapInitializer();
		
		// Map 초기화 진행
		boolean result = false;
		int cnt = 0;
		
		while(true)
		{
			if(cnt == 5) break;
			
			String msg = "";
			
			int x, y;
	
			if(cnt == 0) msg = "맵 사이즈를 입력하세요. x, y형식입니다. ex) 7,8";
			else if(cnt == 1) msg = "시작위치를 입력하세요. x, y형식입니다. ex) 0,0";
			else if(cnt == 2) msg = "탐지된 탐색지점을 입력하세요. x, y형식입니다. 모두 입력하였다면 -1을 입력해주세요. ex) 3,3 OR -1";
			else if(cnt == 3) msg = "탐지된 중요지역을 입력하세요. x, y형식입니다. 모두 입력하였다면 -1을 입력해주세요. ex) 2,3 OR -1";
			else if(cnt == 4) msg = "탐지된 위험지역을 입력하세요. x, y형식입니다. 모두 입력하였다면 -1을 입력해주세요. ex) 1,3 OR -1";
			
			String data = JOptionPane.showInputDialog(msg);
			
			if(cnt >= 2 && data.equals("-1")) 
			{
				cnt++;
				continue;
			}
			
			if(data.indexOf(",") == -1) continue;
					
			
			data = data.replace(" ", "");
			
			try
			{
				x = Integer.parseInt(data.split(",")[0]);
				y = Integer.parseInt(data.split(",")[1]);
			}
			catch(Exception err)
			{
				displayMsg("WRONG INPUT");
				continue;
			}
			
			switch(cnt)
			{
			case 0:
				result = mi.enterMapSize(x, y);
	
				if(!result) 
				{
					displayMsg("WRONG SIZE");
					break;
				}
				
				drawPane.init(x + 1, y + 1);
				cnt++;
				break;
			case 1:
				result = mi.enterCurrPos(x, y);
				if(!result)
				{
					displayMsg("WRONG POSITION");
					break;
				}
				
				drawPane.initPos(x, y);
				cnt++;
				break;
			case 2:
				result = mi.enterSpot(x, y);
				
				if(!result)
					displayMsg("WRONG SPOT");
				break;
			case 3:
				result = mi.enterColor(x, y);
				
				if(!result)
					displayMsg("WRONG COLOR BLOB");
				break;
			case 4:
				result = mi.enterHazard(x, y);
				
				if(!result)
					displayMsg("WRONG HAZARD");
				break;
			}
		}		
	}
}
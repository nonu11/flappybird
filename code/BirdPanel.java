package com.sun.flyingbird;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Blob;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class BirdPanel extends JPanel implements MouseListener,KeyListener{
	Bird bird=new Bird();
	boolean flog;
	int state=START;
	int gx=0;
	int score;
	int life;
	
	public static int START=1;
	public static int RUNNING=2;
	public static int GAMEOVER=3;
	
	ArrayList<Money> moneys=new ArrayList<Money>();
	MaxScore maxScore=new MaxScore();
	ArrayList<Column> columns=new ArrayList<Column>();
	ArrayList<Bird> birds=new ArrayList<Bird>();
	
	public BirdPanel() {
		flog=true;
		score=0;
		life=1;
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		//背景
		Image bg=new ImageIcon("bird/bg.png").getImage();
		g.drawImage(bg, 0, 0, null);
		
		//画鸟和柱子
		if (state==RUNNING ||state==GAMEOVER) {
			for (int i = 0; i < columns.size(); i++) {
				columns.get(i).draw(g);
			}
			for (int i = 0; i < moneys.size(); i++) {
				moneys.get(i).draw(g);
			}
			bird.draw(g);
		}
		if (state==GAMEOVER) {
			Image image=new ImageIcon("bird/gameover.png").getImage();
			g.drawImage(image, 0, 0,null);
			g.setColor(Color.black);
			try {
				maxScore.max(score/10);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Font f = new Font("微软雅黑", Font.BOLD, 20);
			g.setFont(f);
			g.setColor(Color.red);
			if (score/10>=maxScore.max) {
				g.drawString("历史最高："+score/10+"m", 150, 250);
			}
			else {
				g.drawString("历史最高："+maxScore.max+"m", 150, 250);
			}
			
		}
		
		//地面
		Image ground=new ImageIcon("bird/ground.png").getImage();
		g.drawImage(ground, gx, 498, null);
		
		if (state==START) {
			Image image=new ImageIcon("bird/start.png").getImage();
			g.drawImage(image, 0, 0 ,null);
		}
		if (state!=START) {
		//分数
			Font f = new Font("微软雅黑", Font.BOLD, 18);
			g.setFont(f);
			g.setColor(Color.black);
			g.drawString("前进距离：       m", 20, 20);
			g.setColor(Color.green);
			g.drawString(""+score/10+"", 110, 20);
			g.setColor(Color.black);
			g.drawString("lief：", 20, 40);
			g.setColor(Color.red);
			g.drawString(""+life+"", 60, 40);
		}
	}
	public void move() {
		for (int i = 0; i < columns.size(); i++) {
			columns.remove(i);
		}
		new Thread(){
			public void run() {
				int count=0;
				while (true) {
					if (state==RUNNING) {
						if (gx>=-368) {
							gx-=2;
						}else {
							gx=0;
						}
						if (bird.y<0) {
							bird.y=0;
						}
						bird.move(flog);
						flog=true;
						
						if (count%120==0) {
							Column column=new Column();
							columns.add(column);
						}
						if (count%600==0) {
							Money money = new Money();
							moneys.add(money);
						}
						count++;
						
						for (int i = 0; i < columns.size(); i++) {
							columns.get(i).change();
						}
						for (int i = 0; i < columns.size(); i++) {
							if (columns.get(i).x<-78) {
								columns.remove(i);
							}
						}
						for (int i = 0; i < moneys.size(); i++) {
							moneys.get(i).change();
						}
						for (int i = 0; i < moneys.size(); i++) {
							if (moneys.get(i).x<-50) {
								moneys.remove(i);
							}
						}
						for (int i = 0; i < moneys.size(); i++) {
							if (bird.x<=moneys.get(i).x && bird.x+56>=moneys.get(i).x &&
									((bird.y>=0 && bird.y<=moneys.get(i).y)&& 
									(bird.y+56>=moneys.get(i).y+10 && bird.y<=498))) {
								moneys.remove(i);
								life++;
							}
						}
						
						//检测碰撞
						for (int i = 0; i < columns.size(); i++) {
							if (bird.x+56>=columns.get(i).x && bird.x<=columns.get(i).x+78 &&
									((bird.y>=0 && bird.y<=columns.get(i).y+522)|| 
									(bird.y>=columns.get(i).y+622 && bird.y<=498))) {
								life--;
								if (life<=0) {
									while (bird.y<=462) {
											bird.death();
											repaint();
											try {
												Thread.sleep(20);
											} catch (InterruptedException e) {
												e.printStackTrace();
											}
									}
									state=GAMEOVER;
									this.stop();
								}
								else {
									columns.remove(i);
								}
							}
						}
						score+=1;
						if (bird.y>464) {
							life--;
							if (life<=0) {
								state=GAMEOVER;
								repaint();
								this.stop();
							}else {
								columns.clear();
								bird.y=298;
								bird.speed=20;
								bird.t=0.02;
								repaint();
							}
						}
					}	
					repaint();
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
				}
				
			}
		}.start();
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (state==START) {
			state=RUNNING;
		}
		
		if (state==GAMEOVER) {
			bird.y=298;
			score=0;
			life=1;
			columns.clear();
			moneys.clear();
			repaint();
			move();//重新启动线程
			state=START;
		}
		if (state==RUNNING) {
			flog=false;
			repaint();
			bird.t=0.02;
			bird.bool=false;
			
		}
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
	//	flog=false;
		//repaint();
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
	//	flog=true;
	//	repaint();
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyChar()==KeyEvent.VK_SPACE) {
			if (state==START) {
				state=RUNNING;
				
			}
			
			if (state==GAMEOVER) {
				bird.y=298;
				score=0;
				life=1;
				columns.clear();
				moneys.clear();
				repaint();
				move();//重新启动线程
				state=START;
			}
			if (state==RUNNING) {
				flog=false;
				repaint();
				bird.t=0.02;
				bird.bool=false;
				
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}

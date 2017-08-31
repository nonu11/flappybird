package com.sun.flyingbird;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;

public class Bird {
	int x,y;
	int index=0;
	int v;
	double speed;
	double alpha;
	double t;
	boolean bool;
	
	Image[] image;
	
	public Bird() {
		x=187;
		y=298;
		speed=20;
		v=2500;
		t=0.02;
		bool=true;
		
		image=new Image[8];
		for (int i = 0; i < image.length; i++) {
			image[i]=new ImageIcon("bird/"+i+".png").getImage();
		}
	}
	public void draw(Graphics g) {
		g.drawImage(image[index%8], x, y, null);
		index++;
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public void move(boolean flog) {
		if (flog) {
			y+=0.5*18*t*t;
			t+=0.02;
			bool=false;
		}
		else {
			if (v*t-0.5*18*t*t>0) {
				y-=v*t-0.5*18*t*t;
				bool=true;
			}
			else {
				flog=!flog;
				bool=true;
			}
		}
	}
	public void death() {
		y+=10;
	}

}

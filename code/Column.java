package com.sun.flyingbird;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;

public class Column {
	int x,y;
	Image image;
	int speed;
	public Column() {
		speed = 2;
		x=500;
		y=(int)(Math.random()*190)-400;
		image=new ImageIcon("bird/column.png").getImage();
	}
	
	public void draw(Graphics g) {
		g.drawImage(image, x, y, null);
	}
	public void change() {
		x-=speed;
	}
}

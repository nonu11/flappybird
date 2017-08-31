package com.sun.flyingbird;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

public class Money {
	
	int x,y;
	Image image;
	int speed;
	
	public Money() {
		speed = 2;
		x=650;
		y=(int)(Math.random()*150)+200;
		image=new ImageIcon("bird/money.png").getImage();
	}
	
	public void draw(Graphics g) {
		g.drawImage(image, x, y,20,20, null);
	}
	public void change() {
		x-=speed;
	}
}

package com.sun.flyingbird;

import java.awt.print.Pageable;

import javax.swing.JFrame;

public class BirdFrame {
	JFrame frame;
	BirdPanel panel;
	
	public BirdFrame() {
		frame=new JFrame();
		panel=new BirdPanel();
		
		frame.add(panel);
		frame.addKeyListener(panel);
		frame.addMouseListener(panel);
		
		
		
		show();
		panel.move();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
	}
	public void show() {
		
		frame.setSize(432, 644);
		frame.setTitle("FlappyBird");
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(3);
		frame.setVisible(true);
	}
	public static void main(String[] args) {
		BirdFrame birdFrame=new BirdFrame();
	}
}

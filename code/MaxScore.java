package com.sun.flyingbird;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;

public class MaxScore {
	int current;
	int max;
	
	public void max(int c) throws Exception{
		current =c;
		File file=new File("max.txt");
		if (!file.exists()) {
			file.createNewFile();
		}
		FileReader fileReader=new FileReader(file);
		BufferedReader r=new BufferedReader(fileReader);
		try {
			String read;
			int result = 0;
			while ((read=r.readLine())!=null) {
				result=result+Integer.parseInt(read);
			}
			max=result;
			
			if (current>=max) {
				BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
				writer.write(""+current);
				writer.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

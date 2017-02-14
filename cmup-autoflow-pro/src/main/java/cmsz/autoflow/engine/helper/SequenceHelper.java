package cmsz.autoflow.engine.helper;

import java.util.Random;

public class SequenceHelper {
	
	static int seq;
	static
	{
		Random r=new Random();
		seq=r.nextInt(1000000);
	}
	
	synchronized public static int getSequence(){
		return seq++;
	};

}

package cmsz.autoflow.engine.helper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamHelper {
	
	public static final int BUFFERSIZE = 4096;
	
	public static byte[] readBytes(InputStream in) throws IOException
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		transfer(in, out);
		return out.toByteArray();
	}
	
	public static long transfer(InputStream in, OutputStream out) throws IOException
	{
		long total = 0;
		byte[] buffer = new byte[BUFFERSIZE];
		for (int count; (count = in.read(buffer)) != -1;) {
			out.write(buffer, 0, count);
			total += count;
		}
		return total;
	}
	
	/**
	 * 根据文件名称resource打开输入流，并返回
	 * @param resource
	 * @return
	 */
	public static InputStream openStream(String resource) {
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		InputStream stream = classLoader.getResourceAsStream(resource);

		if (stream == null) {
			stream = StreamHelper.class.getClassLoader().getResourceAsStream(resource);
		}

		return stream;
	}

}

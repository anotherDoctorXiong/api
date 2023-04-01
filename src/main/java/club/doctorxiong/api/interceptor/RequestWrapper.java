package club.doctorxiong.api.interceptor;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * http请求参数包装类
 * @author ...
 */
public class RequestWrapper extends HttpServletRequestWrapper implements Serializable {
	private static final long serialVersionUID = -4411772756136009352L;

	private final String body;
	private final Charset charset;

	public RequestWrapper(HttpServletRequest request) {
		super(request);
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;
		InputStream inputStream = null;
		Charset charset = StandardCharsets.UTF_8;
		try {
			inputStream = request.getInputStream();
			if (inputStream != null) {
				charset = obtainCharset(request);
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream, charset));
				char[] charBuffer = new char[128];
				int bytesRead = -1;
				while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
					stringBuilder.append(charBuffer, 0, bytesRead);
				}
			} else {
				stringBuilder.append("");
			}
		} catch (IOException ignored) {

		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		this.charset = charset;
		body = stringBuilder.toString();
	}

	private Charset obtainCharset(HttpServletRequest request) {
		if (request.getContentType() != null){
			boolean isMultipart = request.getContentType().contains("multipart/form-data");
			if (isMultipart){
				return StandardCharsets.ISO_8859_1;
			}
		}
		return StandardCharsets.UTF_8;
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes(charset));
		return new ServletInputStream() {
			@Override
			public boolean isFinished() {
				return false;
			}

			@Override
			public boolean isReady() {
				return false;
			}

			@Override
			public void setReadListener(ReadListener readListener) {
			}

			@Override
			public int read() throws IOException {
				return byteArrayInputStream.read();
			}
		};

	}

	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(this.getInputStream()));
	}

	public String getBody() {
		return this.body;
	}
}

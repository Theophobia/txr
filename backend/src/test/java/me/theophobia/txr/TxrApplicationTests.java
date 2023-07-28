package me.theophobia.txr;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class TxrApplicationTests {

	public static class BufferedImageTypeAdapter extends TypeAdapter<BufferedImage> {

		@Override
		public void write(JsonWriter out, BufferedImage image) throws IOException {
			String base64Image = convertToBase64(image);
			out.value(base64Image);
		}

		@Override
		public BufferedImage read(JsonReader in) throws IOException {
			String base64Image = in.nextString();
			return convertFromBase64(base64Image);
		}

		private String convertToBase64(BufferedImage image) {
			return Base64.getEncoder().encodeToString(imageToByteArray(image));
		}

		private BufferedImage convertFromBase64(String base64Image) {
			byte[] imageBytes = Base64.getDecoder().decode(base64Image);
			return byteArrayToImage(imageBytes);
		}

		private byte[] imageToByteArray(BufferedImage image) {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			try {
				ImageIO.write(image, "png", outputStream);
				return outputStream.toByteArray();
			}
			catch (IOException e) {
				return null;
			}
		}

		private BufferedImage byteArrayToImage(byte[] bytes) {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
			try {
				return ImageIO.read(inputStream);
			}
			catch (final Exception e) {
				return null;
			}
		}
	}

	@Test
	void contextLoads() {
		Gson gson = new GsonBuilder()
			.registerTypeAdapter(BufferedImage.class, new BufferedImageTypeAdapter())
			.setPrettyPrinting()
			.create();

		Map<String, Object> obj = new HashMap<>();
		obj.put("username", "Theophobia");
		obj.put("password", "123");
		obj.put("img", new BufferedImage(512, 512, BufferedImage.TYPE_INT_ARGB));

		System.out.println(gson.toJson(obj));
	}

}

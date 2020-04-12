package converter.modules;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;

// import Apache HTTP Client v 4.3
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

import constants.Constants;
import converter.constants.API;
import extractor.Extractor;
import ltscontrol.LTSControl;

public class Download {

	public Download(int fileId, String fileName) {
		try {

			String outputPath = getOutput();

			CloseableHttpClient httpClient = API.getHttpClient();
			HttpGet request = new HttpGet(API.FILES_URL + fileId + "/content");

			CloseableHttpResponse response = httpClient.execute(request);
			HttpEntity responseContent = response.getEntity();

			BufferedInputStream bis = new BufferedInputStream(responseContent.getContent());
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputPath + "/" + Extractor.deAccent(fileName)));

			int inByte;
			while ((inByte = bis.read()) != -1) {
				bos.write(inByte);
			}

			System.out.println("[LTS CONTROL - ADVANCED PDF DATA EXTRACTOR] CSV file downloaded.");

			response.close();
			httpClient.close();
			bos.close();
			bis.close();

			LTSControl.getExtractor().execute(new File(outputPath + "/" + fileName));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getOutput() {
		Calendar calendar = Calendar.getInstance();

		String outputPath = Constants.MAIN_DIRECTORY + "files/" + calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DAY_OF_MONTH);

		if (!new File(outputPath).exists())
			new File(outputPath).mkdirs();

		return outputPath;
	}

}
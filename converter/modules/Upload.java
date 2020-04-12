package converter.modules;

import java.io.File;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import converter.constants.API;
import converter.utils.LogUtils;

public class Upload {

	public Upload(String sourceFile) {
		try {
			CloseableHttpClient httpClient = API.getHttpClient();
			HttpEntity requestContent = MultipartEntityBuilder.create().addPart("source_file", new FileBody(new File(sourceFile))).addPart("target_format", new StringBody("csv", ContentType.TEXT_PLAIN)).build();
			HttpPost request = new HttpPost(API.JOBS_URL);
			request.setEntity(requestContent);

			CloseableHttpResponse response = httpClient.execute(request);
			HttpEntity responseContent = response.getEntity();
			
			String result = EntityUtils.toString(responseContent, "UTF-8");

			JSONObject json = new JSONObject(result);
			
			LogUtils.log("Upload response: " + json.toString());

			System.out.println(json);

			response.close();
			httpClient.close();

			Thread.sleep(3000);

			new StatusCheck(json.getInt("id"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
package converter.modules;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import converter.constants.API;
import converter.utils.LogUtils;

public class StatusCheck {

	private boolean hasSucess;

	public StatusCheck(int jobId) {
		execute(jobId);
	}

	private void execute(int jobId) {
		if (hasSucess)
			return;

		try {
			CloseableHttpClient httpClient = API.getHttpClient();
			HttpGet request = new HttpGet(API.JOBS_URL + "/" + jobId);

			CloseableHttpResponse response = httpClient.execute(request);
			HttpEntity responseContent = response.getEntity();
			
			String result = EntityUtils.toString(responseContent, "UTF-8");

			JSONObject json = new JSONObject(result);

			System.out.println(json);
			
			LogUtils.log("StatusCheck response: " + json.toString());

			response.close();
			httpClient.close();

			while (!hasSucess) {
				if (!hasSucess && json.getString("status").equalsIgnoreCase("successful")) {
					hasSucess = true;

					JSONObject object = ((JSONObject) json.getJSONArray("target_files").get(0));

					String name = object.getString("name");

					SimpleDateFormat dateFormat = new SimpleDateFormat("HHmm");

					new Download(object.getInt("id"), dateFormat.format(new Date()) + "_" + name);
					break;
				} else {
					Thread.sleep(5000);
					execute(jobId);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
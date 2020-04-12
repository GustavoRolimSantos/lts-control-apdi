package converter.constants;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class API {

    public static final String API_KEY = "secret_key";
    public static final String BASE_URL = "url";
    public static final String JOBS_URL = BASE_URL + "/v1/jobs";
    public static final String FILES_URL = BASE_URL + "/v1/files/";
    
    public static CloseableHttpClient getHttpClient() {
		CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(API_KEY, ""));

		CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider)
				.build();

		return httpClient;
	}
	
}

package net;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import start.Info;

@SuppressWarnings("deprecation")
public class Connect {

	// 游戏版本
	private static String UserAgent = "";
	private static final String Auth = "eWa25vrE";
	private static final String Key = "2DbcAh3G";

	private static DefaultHttpClient client;
	private static CryptoCn dk;

	public Connect() {
		client = new DefaultHttpClient();
		HttpParams hp = client.getParams();
		hp.setParameter("http.socket.timeout", 0x7530);
		hp.setParameter("http.connection.timeout", 0x7530);
		UserAgent = "	Million/"
				+ Info.userAgent
				+ " (GT-I9100; GT-I9100; 4.0.4) samsung/GT-I9100/GT-I9100:4.0.4"
				+ "/IMM76L/eng.build.20130311.122614:eng/release-keys";
	}

	// 访问请求
	public byte[] connectToServer(String url, List<NameValuePair> content)
			throws Exception {
		dk = new CryptoCn();
		List<NameValuePair> post = dk.addnew_crypt_K_param(content, url);
		HttpPost hp = new HttpPost(url);
		hp.setHeader("User-Agent", UserAgent);
		hp.setHeader("Accept-Encoding", "gzip, deflate");
		hp.setEntity(new UrlEncodedFormEntity(post, "UTF-8"));

		AuthScope as = new AuthScope(hp.getURI().getHost(), hp.getURI()
				.getPort());
		CredentialsProvider cp = client.getCredentialsProvider();
		UsernamePasswordCredentials upc = new UsernamePasswordCredentials(Auth,
				Key);
		cp.setCredentials(as, upc);
		byte[] b = null;
		try {
			b = client.execute(hp, new HttpResponseHandler());
		} catch (Exception e) {
			throw e;
		}

		/* end */
		if (b != null) {
			if (url.contains("gp_verify_receipt?")) {
				// need to be decoded
				return null;
			}
			try {
				return CryptoCn.decode(b);
			} catch (Exception ex) {
				throw ex;
			}
		} else {
			return connectToServer(url, content);
		}
	}

}

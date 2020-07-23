package com.unindra.flq;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.futuristicbear.americanquiz.R;
import com.unindra.framework.SoundHandler;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;

public class WikiPageActivity extends X implements OnClickListener {

	private String htmlData;
	private String url;
	private String clubName;
	private HttpConnector conn;
	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wiki_page);
		Bundle data = getIntent().getExtras();
		url = data.getString("URL");
		clubName = data.getString("CLUB");
		initView();
	}
	

	private void getHttpData() {
		try {
			if (conn.get() != null) {
				htmlData = conn.getResponseMsg();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		conn = new HttpConnector();
		conn.execute(url);
		getHttpData();
		webView.loadDataWithBaseURL(url, htmlData, "text/html", "UTF-8", null);
	}

	private void initView() {
		// Init Header
		initHeader(clubName, R.drawable.icon_question, this);
		webView = (WebView) findViewById(R.id.WV_wiki);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onClick(View arg0) {
		if (arg0.getId() == R.id.BTN_back) {
			SoundHandler.getInstance().playSound(context, SoundHandler.SOUND_CLICK);
			finish();
		}
	}

	private class HttpConnector extends AsyncTask<String, String, String> {

		private String responseMsg;

		@Override
		protected String doInBackground(String... uri) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response;
			String responseString = null;
			try {
				response = httpclient.execute(new HttpGet(uri[0]));
				StatusLine statusLine = response.getStatusLine();
				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					responseString = out.toString();
					responseMsg = responseString;
				} else {
					// Closes the connection.
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (ClientProtocolException e) {
				// TODO Handle problems..
			} catch (IOException e) {
				// TODO Handle problems..
			}
			return responseString;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			responseMsg = result;
		}

		public String getResponseMsg() {
			return responseMsg;
		}
	}



}

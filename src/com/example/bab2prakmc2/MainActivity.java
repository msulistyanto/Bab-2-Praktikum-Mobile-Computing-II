package com.example.bab2prakmc2;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.bab1prakmc2.R;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	protected static final String String = null;
	private EditText kotaEdit;
	private Button cariTmbl;
	private TextView cuaca;
	private TextView suhu;
	// private TextView tekananText;
	private TextView tekanan;
	private TextView lembab;
	private TextView degAngin;
	private TextView kecAngin;
	private TextView dump;

	String data = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		kotaEdit = (EditText) findViewById(R.id.kotaEdit);
		cariTmbl = (Button) findViewById(R.id.cariTmbl);
		cuaca = (TextView) findViewById(R.id.cuacaText);
		suhu = (TextView) findViewById(R.id.suhu);
		tekanan = (TextView) findViewById(R.id.tekanan);
		lembab = (TextView) findViewById(R.id.lembab);
		degAngin = (TextView) findViewById(R.id.degAngin);
		kecAngin = (TextView) findViewById(R.id.kecAngin);
		kotaEdit.setText("Malang,Id");

		cariTmbl.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final String input = kotaEdit.getText().toString();
				JSONParser task = new JSONParser();
				task.execute(input);
			}
		});
	}

	private String getData(String inp) {
		String data1 = null;
		final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=";

		InputStream is = null;

		URL u;
		HttpURLConnection uc;

		try {
			uc = (HttpURLConnection) (new URL(BASE_URL + inp)).openConnection();
			uc.setRequestMethod("GET");
			uc.setReadTimeout(10000 /* milliseconds */);
			uc.setConnectTimeout(15000 /* milliseconds */);

			uc.setDoInput(true);
			uc.setDoOutput(true);
			uc.connect();

			StringBuffer buffer = new StringBuffer();
			is = uc.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = null;
			while ((line = br.readLine()) != null)
				buffer.append(line + "\r\n");

			is.close();
			uc.disconnect();
			data1 = buffer.toString();
			// return data;
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return data1;
	}

	private class JSONParser extends AsyncTask<String, Void, String> {
		@Override
		protected java.lang.String doInBackground(java.lang.String... params) {
			String data = getData(params[0]);
			return data;
		}
		
		@Override
		protected void onPostExecute(String data) {
			// TODO Auto-generated method stub
			super.onPostExecute(data);
			try {				
				JSONArray cuacaArray = (new JSONObject(data)).getJSONArray("weather");
				cuaca.setText(cuacaArray.getJSONObject(0).getString("main")	+ " ( "
						+ cuacaArray.getJSONObject(0).getString("description")	+ " ) : ");

				JSONObject mainObject = (new JSONObject(data)).getJSONObject("main");
				suhu.setText(""	+ Math.round(mainObject.getDouble("temp") - 273.15)	+ " C");
				tekanan.setText("" + mainObject.getDouble("pressure") + " hPa");
				lembab.setText("" + mainObject.getInt("humidity") + " %");

				JSONObject windObject = (new JSONObject(data)).getJSONObject("wind");
				kecAngin.setText("" + windObject.getDouble("speed") + " m/s ");
				degAngin.setText("" + windObject.getDouble("deg") + " deg");				
			} catch (JSONException e) {	e.printStackTrace();}
		}
	}

}

package pt.iade.quickwork.DownloadTasks;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class StringDownload  extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... urls) {

        String result = "";
        URL url;
        HttpURLConnection urlConnection = null;

        try{
            url = new URL(urls[0]);
            urlConnection = (HttpURLConnection) url.openConnection();

            InputStream in = urlConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);

            int data = reader.read();
            while(data != -1)
            {
                char current = (char)data;
                result += current;
                data = reader.read();
            }

            /*
        JSONArray arr = new JSONArray();
        Iterator x = jsonObject.keys();
        while(x.hasNext()){
            String key = (String) x.next();
            arr.put(jsonObject.get(key));
        }

        */
            return result;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    protected void onPostExecute(String string) {
        super.onPostExecute(string);
    }
}



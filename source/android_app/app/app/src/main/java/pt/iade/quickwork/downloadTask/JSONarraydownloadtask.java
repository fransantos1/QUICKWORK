package pt.iade.quickwork.downloadTask;

import android.os.AsyncTask;

import org.json.JSONArray;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JSONarraydownloadtask extends AsyncTask<String, Void, JSONArray> {
    @Override

    protected JSONArray doInBackground(String... urls) {




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

            JSONArray jsonArray = new JSONArray(result);
            /*
            ArrayList<String> myItems = new ArrayList<String>();
            for(int i=0; i< jsonArray.length(); i++){
                myItems.add(jsonArray.getString(i));
            }
            int size = myItems.size();
            String[] stringArray = myItems.toArray(new String[size]);
            StringBuffer sb = new StringBuffer();
            for(int i = 0; i < stringArray.length; i++) {
                sb.append(stringArray[i]);
            }
            String str = sb.toString();
            Log.i("JOBsS", str);*/
            return jsonArray;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    protected void onPostExecute(JSONArray jsonArray) {

        super.onPostExecute(jsonArray);
    }
}

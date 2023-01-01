package pt.iade.quickwork.JSONtasks;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PutTask extends AsyncTask<String, Void, Integer> {

    private static final String USER_AGENT = "application/json";

    //private static final String POST_PARAMS = "userName=jd";

    @Override
    protected Integer doInBackground(String... urls) {
        String result;
        URL url;
        HttpURLConnection urlConnection = null;
        int responseCode;




        try {

            url = new URL(urls[0]);
            result = urls[1];
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("PUT");
            con.setRequestProperty("Content-Type", USER_AGENT);

            con.setDoOutput(true);
            OutputStream os = con.getOutputStream();
            os.write(result.getBytes());
            Log.i("Put Test", result);
            os.flush();
            os.close();

            responseCode = con.getResponseCode();
            System.out.println("POST Response Code :: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // print result
                System.out.println(response.toString());
                return responseCode;
            } else {
                System.out.println("POST request did not work.");
                return responseCode;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }
}


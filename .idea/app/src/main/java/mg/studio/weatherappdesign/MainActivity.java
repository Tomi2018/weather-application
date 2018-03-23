package mg.studio.weatherappdesign;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import bean.Demo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;



public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void btnClick(View view) {
        new DownloadUpdate().execute();
    }





    private class DownloadUpdate extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {
            String stringUrl = "http://v.juhe.cn/weather/index?format=2&cityname=%E8%8B%8F%E5%B7%9E&key=a69ce5c05f3fc6a9ebf7cf5a22bd0574";
            HttpURLConnection urlConnection = null;
            BufferedReader reader;

            try {
                URL url = new URL(stringUrl);

                // Create the request to get the information from the server, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestMethod("GET");
                urlConnection.connect();



                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();//将Json中的数据写入inputstream
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.

                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;//显示的温度数据

              // int  temp = Demo.ResultBean.SkBean.temp();


                while ((line = reader.readLine()) != null) {
                    // Mainly needed for debugging

                    JSONObject obj=JSONObject.fromObject(line);
                    line=obj.getString("resultcode");
                  /*获取返回状态码*/
                    if(line!=null&&line.equals("200")){
                    /*获取数组数据*/
                        line=obj.getString("result");
                        JSONArray arr=JSONArray.fromObject(line);
                        line=obj.getString("sk");
                        obj=JSONObject.fromObject(line);
                        //今日温度
                        line=obj.getString("temp");

                    }

                buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                //The temperature
                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String temperature) {
            //Update the temperature displayed
            ((TextView) findViewById(R.id.temperature_of_the_day)).setText(temperature);
        }
    }
}

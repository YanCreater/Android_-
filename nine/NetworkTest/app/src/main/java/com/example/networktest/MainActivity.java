package com.example.networktest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView responseText;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button sendRequest=findViewById(R.id.send_request);
        responseText=findViewById(R.id.send_request);
        sendRequest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.send_request:
                //sendRequestWithHttpURLConnect();

                sendRequestWithOkhttp();
               // Log.d("AAA", "onClick:一点击发送请求 ");
                default:
        }
    }

    String url="http://10.24.39.97/get_data.json";
    OkHttpClient client=new OkHttpClient();

    private String Go(String url) throws IOException{
        Request request=new Request.Builder()
                .url(url)
                .build();
        try(Response response=client.newCall(request).execute()){
            return response.body().string();
        }
    }

    private void sendRequestWithHttpURLConnect(){
        //开启线程来发起网络请求
        new Thread(new Runnable(){
            @Override
            public void run() {
                HttpURLConnection connection=null;
                BufferedReader reader =null;
                try{
                    URL url=new URL("https://www.baidu.com");
                    connection=(HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in =connection.getInputStream();
                    //下面对获取到的输入流进行读取
                    reader =new BufferedReader(new InputStreamReader(in));
                    StringBuilder response=new StringBuilder();
                    String line;
                    while((line=reader.readLine())!=null){
                        response.append(line);
                    }
                    showResponse(response.toString());
                }catch (Exception e){
                    e.printStackTrace();
                }finally{
                    if(reader!=null){
                        try{
                            reader.close();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                    if(connection!=null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    private void sendRequestWithOkhttp(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{


                    //showResponse( Go(url));
//                            //.url("http://10.24.39.97/get_data.xml")
//                            .url("http://www.baidu.com")
//                            .build();
//                    Response response=client.newCall(request).execute();
//                    String responseData=response.body().string();
                       // parseXMLWithSAX(Go(url));
//                    //两种解析XMl格式的方法
//                   showResponse(responseData);
                    //parseXMLWithPull(Go(url));
                    //两种解析JSON格式的方法
                    //parseJSONWithJSONObject(Go(url));
                    //使用GSON解析json文件
                    parseJSONWithGSON(Go(url));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void showResponse(final String response){
        runOnUiThread(new Runnable(){
            @Override
            public void run() {
                responseText.setText(response);
            }
        });
    }

    private void parseXMLWithPull(String xmlData){
        try{
            XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser=factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlData));
            int eventType=xmlPullParser.getEventType();
            String id="";
            String name="";
            String version="";
            while(eventType!=XmlPullParser.END_DOCUMENT){
                String nodeName=xmlPullParser.getName();
                switch (eventType){
                    case XmlPullParser.START_TAG:{
                        if("id".equals(nodeName)){
                            id=xmlPullParser.nextText();
                        }else if("name".equals(nodeName)){
                            name=xmlPullParser.nextText();
                        }else if("version".equals(nodeName)){
                            version=xmlPullParser.nextText();
                        }
                        break;
                    }
                    case XmlPullParser.END_TAG:{
                        if("app".equals(nodeName)){
//                            Log.d("AAA", "id is :"+id);
//                            Log.d("AAA", "name is :"+name);
//                            Log.d("AAA", "version is :"+version);
                            showResponse("id is :"+id+"\nname is :"+name+"\nversion is :"+version);
                        }
                        break;
                    }
                    default:
                        break;
                }
                eventType=xmlPullParser.next();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void parseXMLWithSAX(String xmlData){

        try{
            SAXParserFactory factory=SAXParserFactory.newInstance();
            XMLReader xmlReader=factory.newSAXParser().getXMLReader();
            ContentHandler handler=new ContentHandler();
            xmlReader.setContentHandler(handler);
            xmlReader.parse(new InputSource(new StringReader(xmlData)));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void parseJSONWithJSONObject(String jsonData){
        try{
            JSONArray jsonArray=new JSONArray(jsonData);
            for(int  i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                String id =jsonObject.getString("id");
                String name =jsonObject.getString("name");
                String version =jsonObject.getString("version");
                Log.d("JSON__JSONObject", "id is : "+id);
                Log.d("JSON__JSONObject", "name is : "+name);
                Log.d("JSON__JSONObject", "version is : "+version);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void parseJSONWithGSON(String jsonData){
        Gson gson=new Gson();
        List<App> appList=gson.fromJson(jsonData,new TypeToken<List<App>>(){}.getType());
        for(App app:appList){

            Log.d("AAAA", "parseJSONWithGSON: id is  "+app.getId());
            Log.d("AAAA", "parseJSONWithGSON: name is  "+app.getName());
            Log.d("AAAA", "parseJSONWithGSON:version is  "+app.getVersion());

        }

    }
}

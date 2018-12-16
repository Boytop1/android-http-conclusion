package com.example.hasee.http;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.os.Handler;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends ActionBarActivity {
   // 192.168.43.244
    private TextView text;
    private String url = "http://192.168.43.244/http.php";
    final Handler handler=new Handler();
    final StringBuilder sb=new StringBuilder();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = (TextView) findViewById(R.id.showresult);
        Button button=(Button)findViewById(R.id.button);
        final EditText username=(EditText)findViewById(R.id.username);
        final EditText password=(EditText)findViewById(R.id.password);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                      // +用UrlConnection方法的Post方式来发送json信息，并获取php服务器返回的信息
                        try {
                            //用json传输数据
                            JSONObject js=new JSONObject();
                            js.put("username2",username.getText().toString().trim());
                            js.put("password2",password.getText().toString().trim());
                            URL httpUrl = new URL(url);
                            //建立连接；
                            HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
                            //设置超时时间
                            conn.setReadTimeout(10000);
                            //设置请求方式，默认为GET
                            conn.setRequestMethod("POST");
                            //使用这个，以后就可以使用conn.getOutputStream().write()
                            conn.setDoOutput(true);
                            //使用这个，以后就可以使用conn.getOutputStream().read()
                            conn.setDoInput(true);
                            //是否使用缓存，默认不使用，Post请求不能使用缓存
                            conn.setUseCaches(false);
                            //设置http请求头，
                            conn.setRequestProperty("Connection", "Keep-Alive");
                            conn.setRequestProperty("Charset","UTF-8");
                            //设置json类型
                            conn.setRequestProperty("Content-Type","application/json;charset=UTF-8");
                            conn.setRequestProperty("accept", "application/json");
                            //开始连接
                            conn.connect();
                            //判断json是否为空,将json中的数据发送到服务端，以便和服务端的数据进行对比
                             if (!TextUtils.isEmpty(js.toString().trim())) {
                             //定义一个发送json的流
                             OutputStream ows =conn.getOutputStream();
                             //发送json数据，必须以字节类型
                             ows.write(js.toString().getBytes());
                             //刷新
                             ows.flush();
                             ows.close();
                             }
                            if (conn.getResponseCode() == 200) {//判断连接是否成功

                                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));//使用bufferreader向输入流中读取数据
                                final String line = reader.readLine();

                                handler.post(new Runnable() {//使用Handler更新text,不然会报异常
                                  @Override
                                   public void run() {
                                            text.setText(line);
                                       }
                              });}
                            }catch(Exception e){
                                e.printStackTrace();}
// ----------------------------------------------------------------------------------------------------------------------------------------------
                        //使用HttpClient方法的Get方式获取服务器信息
//// 			？后面跟要传送的键值对，多个键值对之间用&连接。这里第一个键值对的键名为name，键值为输入的值；第二个键名为password，键值为输入值。
//                        try {
//                            String URL = url + "?username1=" + username.getText().toString() + "&password1=" + password.getText().toString();
//                            HttpGet httpGet = new HttpGet(URL);
//
//                            HttpParams params = new BasicHttpParams();
////			                采用UTF-8编码格式
//                            params.setParameter("charset", HTTP.UTF_8);
////	                        设置连接超时时间为8秒
//                            HttpConnectionParams.setConnectionTimeout(params, 8 * 1000);
////	                        设置数据请求时间为8秒
//                            HttpConnectionParams.setSoTimeout(params, 8 * 1000);
////	                        新建一个Http客户端对象
//                            HttpClient httpClient = new DefaultHttpClient(params);
////	                        用该对象发送GET请求，返回一个Http响应
//                            HttpResponse httpResponse = httpClient.execute(httpGet);
////	                        判断是否获得正确的响应，然后执行相应的操作
//                            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
////			                这里要注意一下返回的结果用UTF-8编码，否则当返回结果有中文时可能会出现乱码。最好所有的地方都统一编码
//                                String strResult = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
//                                if (strResult.equals("Login Success")) {
//                                    handler.post(new Runnable() {//使用Handler更新text,不然我会报异常
//                                    @Override
//                                   public void run() {
//                                            text.setText("SUCCESS");
//                                      }
//                            });
//                                } else {
//                                        handler.post(new Runnable() {//使用Handler更新text,不然我会报异常
//                                            @Override
//                                            public void run() {
//                                                text.setText("Fail");
//                                            }
//                                        });
//                                }
//                            }
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
// ----------------------------------------------------------------------------------------------------------------------------------------------
                        //使用HttpClient方法的Post方式获取服务器信息
//                       //该list的数据类型是NameValuePair（简单名称值对节点类型），这个代码多处用于Java像url发送Post请求。在发送post请求时用该list来存放参数。
//                        NameValuePair nameValuePair1 = new BasicNameValuePair("username2", username.getText().toString().trim());
//                        NameValuePair nameValuePair2 = new BasicNameValuePair("password2", password.getText().toString().trim());
//                         //将创建好的名值对加入到list中
//                        List<NameValuePair> list = new ArrayList<NameValuePair>();
//                        list.add(nameValuePair1);
//                        list.add(nameValuePair2);
//                        try{
//                            //用刚才的list填充一个http请求实体，并采用UTF-8编码
//                            HttpEntity requesteEntity = new UrlEncodedFormEntity(list,HTTP.UTF_8);
//                            //第一个POST请求
//                            HttpPost httpPost = new HttpPost(url);
//                            //设置POST请求的数据实体
//                            httpPost.setEntity(requesteEntity);
//
//                            HttpParams parms = new BasicHttpParams();
//                            //设置超时时间和请求时间
//                            HttpConnectionParams.setConnectionTimeout(parms, 8 * 1000);
//                            HttpConnectionParams.setSoTimeout(parms, 8 * 1000);
//                            //新建一个http客户端对象
//                            HttpClient httpClient = new DefaultHttpClient(parms);
//                            //发送POST请求，返回一个http响应
//                            HttpResponse httpResponse = httpClient.execute(httpPost);
//
//                            if(httpResponse.getStatusLine().getStatusCode() == 200) {
//                           //返回响应的结果
//                                String strResult = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
//                                if (strResult.equals("Login Success")) {
//                                    handler.post(new Runnable() {//使用Handler更新text,不然我会报异常
//                                        @Override
//                                        public void run() {
//                                            text.setText("Success");
//                                        }
//                                    });
//                                } else {
//                                    handler.post(new Runnable() {//使用Handler更新text,不然我会报异常
//                                            @Override
//                                            public void run() {
//                                                text.setText("Fail");
//                                            }
//                                        });
//                                }
//                            }
//                        }
//                        catch(Exception e){
//                            e.printStackTrace();
//                        }
                    }
                }.start();
            }
        });}

            @Override
            public boolean onCreateOptionsMenu(Menu menu) {
                // Inflate the menu; this adds items to the action bar if it is present.
                getMenuInflater().inflate(R.menu.menu_main, menu);
                return true;
            }

            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                // Handle action bar item clicks here. The action bar will
                // automatically handle clicks on the Home/Up button, so long
                // as you specify a parent activity in AndroidManifest.xml.
                int id = item.getItemId();

                //noinspection SimplifiableIfStatement
                if (id == R.id.action_settings) {
                    return true;
                }

                return super.onOptionsItemSelected(item);
            }

    }

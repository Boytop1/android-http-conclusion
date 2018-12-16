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
                      // +��UrlConnection������Post��ʽ������json��Ϣ������ȡphp���������ص���Ϣ
                        try {
                            //��json��������
                            JSONObject js=new JSONObject();
                            js.put("username2",username.getText().toString().trim());
                            js.put("password2",password.getText().toString().trim());
                            URL httpUrl = new URL(url);
                            //�������ӣ�
                            HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
                            //���ó�ʱʱ��
                            conn.setReadTimeout(10000);
                            //��������ʽ��Ĭ��ΪGET
                            conn.setRequestMethod("POST");
                            //ʹ��������Ժ�Ϳ���ʹ��conn.getOutputStream().write()
                            conn.setDoOutput(true);
                            //ʹ��������Ժ�Ϳ���ʹ��conn.getOutputStream().read()
                            conn.setDoInput(true);
                            //�Ƿ�ʹ�û��棬Ĭ�ϲ�ʹ�ã�Post������ʹ�û���
                            conn.setUseCaches(false);
                            //����http����ͷ��
                            conn.setRequestProperty("Connection", "Keep-Alive");
                            conn.setRequestProperty("Charset","UTF-8");
                            //����json����
                            conn.setRequestProperty("Content-Type","application/json;charset=UTF-8");
                            conn.setRequestProperty("accept", "application/json");
                            //��ʼ����
                            conn.connect();
                            //�ж�json�Ƿ�Ϊ��,��json�е����ݷ��͵�����ˣ��Ա�ͷ���˵����ݽ��жԱ�
                             if (!TextUtils.isEmpty(js.toString().trim())) {
                             //����һ������json����
                             OutputStream ows =conn.getOutputStream();
                             //����json���ݣ��������ֽ�����
                             ows.write(js.toString().getBytes());
                             //ˢ��
                             ows.flush();
                             ows.close();
                             }
                            if (conn.getResponseCode() == 200) {//�ж������Ƿ�ɹ�

                                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));//ʹ��bufferreader���������ж�ȡ����
                                final String line = reader.readLine();

                                handler.post(new Runnable() {//ʹ��Handler����text,��Ȼ�ᱨ�쳣
                                  @Override
                                   public void run() {
                                            text.setText(line);
                                       }
                              });}
                            }catch(Exception e){
                                e.printStackTrace();}
// ----------------------------------------------------------------------------------------------------------------------------------------------
                        //ʹ��HttpClient������Get��ʽ��ȡ��������Ϣ
//// 			�������Ҫ���͵ļ�ֵ�ԣ������ֵ��֮����&���ӡ������һ����ֵ�Եļ���Ϊname����ֵΪ�����ֵ���ڶ�������Ϊpassword����ֵΪ����ֵ��
//                        try {
//                            String URL = url + "?username1=" + username.getText().toString() + "&password1=" + password.getText().toString();
//                            HttpGet httpGet = new HttpGet(URL);
//
//                            HttpParams params = new BasicHttpParams();
////			                ����UTF-8�����ʽ
//                            params.setParameter("charset", HTTP.UTF_8);
////	                        �������ӳ�ʱʱ��Ϊ8��
//                            HttpConnectionParams.setConnectionTimeout(params, 8 * 1000);
////	                        ������������ʱ��Ϊ8��
//                            HttpConnectionParams.setSoTimeout(params, 8 * 1000);
////	                        �½�һ��Http�ͻ��˶���
//                            HttpClient httpClient = new DefaultHttpClient(params);
////	                        �øö�����GET���󣬷���һ��Http��Ӧ
//                            HttpResponse httpResponse = httpClient.execute(httpGet);
////	                        �ж��Ƿ�����ȷ����Ӧ��Ȼ��ִ����Ӧ�Ĳ���
//                            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
////			                ����Ҫע��һ�·��صĽ����UTF-8���룬���򵱷��ؽ��������ʱ���ܻ�������롣������еĵط���ͳһ����
//                                String strResult = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
//                                if (strResult.equals("Login Success")) {
//                                    handler.post(new Runnable() {//ʹ��Handler����text,��Ȼ�һᱨ�쳣
//                                    @Override
//                                   public void run() {
//                                            text.setText("SUCCESS");
//                                      }
//                            });
//                                } else {
//                                        handler.post(new Runnable() {//ʹ��Handler����text,��Ȼ�һᱨ�쳣
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
                        //ʹ��HttpClient������Post��ʽ��ȡ��������Ϣ
//                       //��list������������NameValuePair��������ֵ�Խڵ����ͣ����������ദ����Java��url����Post�����ڷ���post����ʱ�ø�list����Ų�����
//                        NameValuePair nameValuePair1 = new BasicNameValuePair("username2", username.getText().toString().trim());
//                        NameValuePair nameValuePair2 = new BasicNameValuePair("password2", password.getText().toString().trim());
//                         //�������õ���ֵ�Լ��뵽list��
//                        List<NameValuePair> list = new ArrayList<NameValuePair>();
//                        list.add(nameValuePair1);
//                        list.add(nameValuePair2);
//                        try{
//                            //�øղŵ�list���һ��http����ʵ�壬������UTF-8����
//                            HttpEntity requesteEntity = new UrlEncodedFormEntity(list,HTTP.UTF_8);
//                            //��һ��POST����
//                            HttpPost httpPost = new HttpPost(url);
//                            //����POST���������ʵ��
//                            httpPost.setEntity(requesteEntity);
//
//                            HttpParams parms = new BasicHttpParams();
//                            //���ó�ʱʱ�������ʱ��
//                            HttpConnectionParams.setConnectionTimeout(parms, 8 * 1000);
//                            HttpConnectionParams.setSoTimeout(parms, 8 * 1000);
//                            //�½�һ��http�ͻ��˶���
//                            HttpClient httpClient = new DefaultHttpClient(parms);
//                            //����POST���󣬷���һ��http��Ӧ
//                            HttpResponse httpResponse = httpClient.execute(httpPost);
//
//                            if(httpResponse.getStatusLine().getStatusCode() == 200) {
//                           //������Ӧ�Ľ��
//                                String strResult = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
//                                if (strResult.equals("Login Success")) {
//                                    handler.post(new Runnable() {//ʹ��Handler����text,��Ȼ�һᱨ�쳣
//                                        @Override
//                                        public void run() {
//                                            text.setText("Success");
//                                        }
//                                    });
//                                } else {
//                                    handler.post(new Runnable() {//ʹ��Handler����text,��Ȼ�һᱨ�쳣
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

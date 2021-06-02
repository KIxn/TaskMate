package com.example.taskmate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;

public class MainActivity extends AppCompatActivity {

    //authorization variable
    public static boolean bAuthorized = false;

    //regex email validator
    public static boolean isValidEmail(String enteredEmail){
        String EMAIL_REGEX ="^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}";
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(enteredEmail);
        return ((!enteredEmail.isEmpty()) && (enteredEmail!=null) && (matcher.matches()));
    }
    //for errors on sign-up/log-in
    public void shaker(View v){
        Animation animShake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake);
        v.startAnimation(animShake);
    }

    //conv hex to string
    static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
    //hashing function SHA256 hashing function
    public static String hasher(byte[] input) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA256");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
        byte[] result = md.digest(input);
        return bytesToHex(result);
    }

    //handle radio button
    public String checkbtn(View v){
        int radioid = rgp.getCheckedRadioButtonId();

        rgb_chk = findViewById(radioid);

        if(rgb_chk.getText().toString().equals("Lecturer")){
            return "LECT";
        }else{
            return "STUD";
        }
    }

    //handle return


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE){
            finishAndRemoveTask();
        }
    }

    private static final Charset UTF_8 = StandardCharsets.UTF_8;

    //needed test if user already exists
    public static final boolean[] flag = {false};
    String tmpstr = "";
    ProgressDialog progressDialog;

    //components
    LinearLayout logfrm;
    Button logbtn;
    Button signbtn;
    EditText txtemail;
    EditText txtpswd;
    CheckBox chbrem;
    RadioGroup rgp; RadioButton rgb_chk;
    SharedPreferences sharedPreferences;
    //vars
    String email;String pswd_hash;final String[] perm = {""};

    //key for activity
    private static final int REQ_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Collect Views
        logfrm = (LinearLayout) findViewById(R.id.loginlt);
        logbtn = (Button) findViewById(R.id.logbtn);
        signbtn = (Button) findViewById(R.id.signbtn);
        txtemail = (EditText) findViewById(R.id.txtemail);
        txtpswd = (EditText) findViewById(R.id.txtpswd);
        rgp = (RadioGroup) findViewById(R.id.rgp);
        rgb_chk = (RadioButton) findViewById(R.id.rgbLect);
        chbrem = (CheckBox) findViewById(R.id.rememberchk);
        sharedPreferences = getSharedPreferences("userLog",MODE_PRIVATE);

        //check if user has selected to remember
        if(!sharedPreferences.getString("userDetails","null").equals("null")){
            try {
                JSONObject jsonObject = new JSONObject(sharedPreferences.getString("userDetails",""));
                //automatically log in
                Intent intent = new Intent(MainActivity.this,NavDrawerActivity.class);
                intent.putExtra("USER_ID",jsonObject.getString("USER_ID"));
                intent.putExtra("PERM",jsonObject.getString("PERM"));
                startActivityForResult(intent,REQ_CODE);
                findViewById(R.id.Root).setVisibility(View.GONE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //else continue signup/log in

        //on permisssion change
        rgp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rgb_chk = findViewById(checkedId);

                if(rgb_chk.getText().toString().equals("Lecturer")){
                    perm[0] = "LECT";
                }else{
                    perm[0] = "STUD";
                }
            }
        });

        //Click Listener for Onclick Event logbtn
        logbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //validate email on log in
                email = txtemail.getText().toString();
                //check if empty
                if(email.length() == 0){
                    shaker(logfrm);
                    Toast.makeText(MainActivity.this, "Please Enter E-mail address", Toast.LENGTH_LONG).show();
                    txtemail.setText("");
                    txtemail.setFocusable(true);
                    return;
                }else if(!isValidEmail(email)){
                    shaker(logfrm);
                    Toast.makeText(MainActivity.this, "Please Ensure YOU have entered a VALID E-mail address", Toast.LENGTH_LONG).show();
                    txtemail.setText("");
                    txtemail.setFocusable(true);
                    return;
                }
                //validate passwd
                String pswd = txtpswd.getText().toString();
                if(pswd.length() == 0){
                    shaker(logfrm);
                    Toast.makeText(MainActivity.this, "Please Enter a Password", Toast.LENGTH_SHORT).show();
                    txtpswd.setFocusable(true);
                    return;
                }

                //store pswd hash
                pswd_hash = hasher(pswd.getBytes(UTF_8));

                //create components needed for internet requests
                PhpReq ReqAgent = new PhpReq();
                HttpUrl httpurl = new HttpUrl.Builder()
                        .scheme("https")
                        .host("lamp.ms.wits.ac.za")
                        .addPathSegment("home").addPathSegment("s2307935").addPathSegment("getUser.php")
                        .addQueryParameter("email",email)
                        .build();
                Request req = new Request.Builder()
                        .url(httpurl)
                        .get()
                        .build();
                //check if email exists in db
                ReqAgent.sendRequest(MainActivity.this, req, new RequestHandler() {
                    @Override
                    public void processResponse(String resp) {
                        try {
                            JSONArray arr = new JSONArray(resp);
                            //check if email was found
                            if(arr.length() != 0){
                                //email not found, so create user
                                JSONObject jsonObject = arr.getJSONObject(0);
                                if((email.equals(jsonObject.getString("EMAIL"))) && (pswd_hash.equals(jsonObject.getString("PSWD")))){
                                    bAuthorized = true;
                                    ////////////////////////////////save log in//////////////////////////
                                    if (chbrem.isChecked()){
                                        sharedPreferences = getSharedPreferences("userLog",MODE_PRIVATE);
                                        //asynchronous write
                                        String strDets = "{USER_ID:"+String.valueOf(jsonObject.getInt("USER_ID"))+",PERM:"+jsonObject.getString("PERM")+"}";
                                        sharedPreferences.edit().putString("userDetails",strDets).apply();
                                    }
                                    //launch activity
                                    Intent intent = new Intent(MainActivity.this,NavDrawerActivity.class);
                                    intent.putExtra("USER_ID",jsonObject.getString("USER_ID"));
                                    intent.putExtra("PERM",jsonObject.getString("PERM"));
                                    startActivityForResult(intent,REQ_CODE);
                                    findViewById(R.id.Root).setVisibility(View.GONE);
                                }else{
                                    Toast.makeText(MainActivity.this, "Invalid Password!", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                shaker(logfrm);
                                txtemail.setText("");
                                txtpswd.setText("");
                                Toast.makeText(MainActivity.this, "User Not Found: Please Re-enter Email and Password", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        ////////////////////////////////////complete
        //Sign-Up on-click
        signbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag[0] = false;
                //validate email on log in
                email = txtemail.getText().toString();
                //check if empty
                if(email.length() == 0){
                    shaker(logfrm);
                    Toast.makeText(MainActivity.this, "Please Enter E-mail address", Toast.LENGTH_LONG).show();
                    txtemail.setText("");
                    txtemail.setFocusable(true);
                    return;
                }else if(!isValidEmail(email)){
                    shaker(logfrm);
                    Toast.makeText(MainActivity.this, "Please Ensure YOU have entered a VALID E-mail address", Toast.LENGTH_LONG).show();
                    txtemail.setText("");
                    txtemail.setFocusable(true);
                    return;
                }
                //validate passwd
                String pswd = txtpswd.getText().toString();
                if(pswd.length() == 0){
                    shaker(logfrm);
                    Toast.makeText(MainActivity.this, "Please Enter a Password", Toast.LENGTH_SHORT).show();
                    txtpswd.setFocusable(true);
                    return;
                }

                //store pswd hash
                pswd_hash = hasher(pswd.getBytes(UTF_8));
                System.out.println(pswd_hash);
                //collect Permission
                rgp.setVisibility(View.VISIBLE);
                if(!perm[0].equals("")) {
                    //use AsyncTask to accomplih this
                    tmpstr = "https://lamp.ms.wits.ac.za/home/s2307935/checkUser.php?email=" + email;
                    new MyAsyncTask().execute();
                    return;
                }else{
                    shaker(logfrm);
                    Toast.makeText(MainActivity.this, "Please Select Your Role", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public class MyAsyncTask extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // display a progress dialog for good user experiance
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Please Wait, while we Verify if User: " +email+ " Already exists");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        //fetch data asynchronously
        @Override
        protected String doInBackground(String... strings) {
            // implement API in background and store the response in current variable
            // implement API in background and store the response in current variable
            String current = "";
            try {
                URL url;
                HttpURLConnection urlConnection = null;
                try {
                    url = new URL(tmpstr);

                    urlConnection = (HttpURLConnection) url
                            .openConnection();

                    InputStream in = urlConnection.getInputStream();

                    InputStreamReader isw = new InputStreamReader(in);

                    int data = isw.read();
                    while (data != -1) {
                        current += (char) data;
                        data = isw.read();
                        System.out.print(current);

                    }
                    // return the data to onPostExecute method
                    return current;

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
            return current;
        }

        //when data is retrieved so that we can check if user already exists or nah
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //use results
            final JSONObject[] jsonObject = new JSONObject[1];
            if(s.equals("false")){flag[0] = true;}//if user not found, then we can add user
            progressDialog.dismiss();

            if (flag[0]) {
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Creating User: " + email + "...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                //post results to server
                PhpReq ReqAgent = new PhpReq();
                //create components needed for internet requests
                HttpUrl httpurl = new HttpUrl.Builder()
                        .scheme("https")
                        .host("lamp.ms.wits.ac.za")
                        .addPathSegment("home").addPathSegment("s2307935").addPathSegment("createUser.php")
                        .build();
                RequestBody requestBody = new FormBody.Builder()
                        .add("email", email).add("pswd", pswd_hash).add("perm", perm[0])
                        .build();
                Request req = new Request.Builder()
                        .url(httpurl)
                        .post(requestBody)
                        .build();
                ReqAgent.sendRequest(MainActivity.this, req, new RequestHandler() {
                    @Override
                    public void processResponse(String resp) {
                        Toast.makeText(MainActivity.this, resp, Toast.LENGTH_SHORT).show();
                        if (chbrem.isChecked()){
                            //////////////////////////////////save user//////////////////////////////////////////////
                            PhpReq ReqAgent1 = new PhpReq();
                            HttpUrl httpUrl = new HttpUrl.Builder()
                                    .scheme("https")
                                    .host("lamp.ms.wits.ac.za").addPathSegment("home").addPathSegment("s2307935").addPathSegment("getUser.php")
                                    .addQueryParameter("email",email)
                                    .build();
                            Request req1 = new Request.Builder()
                                    .url(httpUrl)
                                    .get().build();
                            ReqAgent1.sendRequest(MainActivity.this, req1, new RequestHandler() {
                                @Override
                                public void processResponse(String resp) {
                                    try {
                                        JSONObject jsonObject1 =  new JSONArray(resp).getJSONObject(0);
                                        //save to sharedpref
                                        sharedPreferences = getSharedPreferences("userLog",MODE_PRIVATE);
                                        //asynchronous write
                                        String strDets = "{USER_ID:"+String.valueOf(jsonObject1.getInt("USER_ID"))+",PERM:"+jsonObject1.getString("PERM")+"}";
                                        sharedPreferences.edit().putString("userDetails",strDets).apply();
                                        Intent intent = new Intent(MainActivity.this,NavDrawerActivity.class);
                                        intent.putExtra("USER_ID",jsonObject1.getString("USER_ID"));
                                        intent.putExtra("PERM",jsonObject1.getString("PERM"));
                                        startActivityForResult(intent,REQ_CODE);
                                        findViewById(R.id.Root).setVisibility(View.GONE);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }else{
                            //if dont wanna remember
                            PhpReq ReqAgent1 = new PhpReq();
                            HttpUrl httpUrl = new HttpUrl.Builder()
                                    .scheme("https")
                                    .host("lamp.ms.wits.ac.za").addPathSegment("home").addPathSegment("s2307935").addPathSegment("getUser.php")
                                    .addQueryParameter("email",email)
                                    .build();
                            Request req1 = new Request.Builder()
                                    .url(httpUrl)
                                    .get().build();
                            ReqAgent1.sendRequest(MainActivity.this, req1, new RequestHandler() {
                                @Override
                                public void processResponse(String resp) {
                                    try {
                                        JSONObject jsonObject1 =  new JSONArray(resp).getJSONObject(0);
                                        Intent intent = new Intent(MainActivity.this,NavDrawerActivity.class);
                                        intent.putExtra("USER_ID",jsonObject1.getString("USER_ID"));
                                        intent.putExtra("PERM",jsonObject1.getString("PERM"));
                                        startActivityForResult(intent,REQ_CODE);
                                        findViewById(R.id.Root).setVisibility(View.GONE);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }
                });
                progressDialog.dismiss();
            } else {
                shaker(logfrm);
                txtemail.setText("");
                txtpswd.setText("");
                txtemail.requestFocus();
                Toast.makeText(MainActivity.this, "User Already Exists...", Toast.LENGTH_SHORT).show();
            }

                flag[0] = false;
        }
    }

}
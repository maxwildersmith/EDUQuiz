package com.example.quiz.eduquiz;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;

public class Quiz extends AppCompatActivity {
    private TextView title, timer,question;
    private RadioButton[] options;//opt1,opt2,opt3,opt4;
    private CountDownTimer time;
    private JSONObject data;
    private String API = "/api/v1/Articles/List?expand=1";
    public static final String DATA_NAME = "data stuffs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        title = (TextView)findViewById(R.id.title);
        timer = (TextView)findViewById(R.id.timer);
        question = (TextView)findViewById(R.id.question);

        options = new RadioButton[4];
        options[0] = (RadioButton)findViewById(R.id.opt1);
        options[1] = (RadioButton)findViewById(R.id.opt2);
        options[2] = (RadioButton)findViewById(R.id.opt3);
        options[3] = (RadioButton)findViewById(R.id.opt4);




        getData();
    }

    private void getData() {
        try {
            Log.e("asdf",getIntent().getStringExtra(DATA_NAME));
            Log.e("asdf",PersonSearch.execute(getIntent().getStringExtra(DATA_NAME)+API,null,"").get());

            data = new JSONObject(search.execute(getIntent().getStringExtra(DATA_NAME)+API,null,"").get());
            Toast.makeText(Quiz.this, data.getJSONArray("items").getJSONObject(0).getString("title"), Toast.LENGTH_SHORT).show();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class PersonSearch extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                URLConnection connection = url.openConnection();

                String out="";

                InputStream in = connection.getInputStream();
                BufferedReader read =  new BufferedReader(new InputStreamReader(in));
                String line;
                while((line=read.readLine())!= null)
                    out+=line;
                Log.e("asdf",out);
                return out;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    }
}

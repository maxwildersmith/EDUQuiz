package com.example.quiz.eduquiz;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import org.json.JSONArray;
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
    private Button submit;
    private CountDownTimer time;
    private JSONObject data;
    public static final String DATA_NAME = "data stuffs";
    private static final String GET_ARTICLES = "/api/v1/Articles/List?";
    private static final String GET_INFO = "/api/v1/Articles/AsSimpleJson?id=";
    private int correctopt;
    private boolean started = false;

    private String[] questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        title = (TextView)findViewById(R.id.title);
        timer = (TextView)findViewById(R.id.timer);
        question = (TextView)findViewById(R.id.question);
        submit = (Button)findViewById(R.id.next);

        questions = new String[5];

        options = new RadioButton[4];
        options[0] = (RadioButton)findViewById(R.id.opt1);
        options[1] = (RadioButton)findViewById(R.id.opt2);
        options[2] = (RadioButton)findViewById(R.id.opt3);
        options[3] = (RadioButton)findViewById(R.id.opt4);



        submit.setText("Start!");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });


    }

    private void getData() {
        if(started)
            checkAnswer();
        try {
            String s = new PersonSearch().execute("http://"+getIntent().getStringExtra(DATA_NAME)+GET_ARTICLES,null,"").get();
            try {
                JSONArray articleList = new JSONObject(s).getJSONArray("items");
                Log.e("asdf",articleList.length()+"");
                JSONObject[] articles = new JSONObject[articleList.length()];
                for(int i=0;i<articles.length;i++)
                    articles[i] = articleList.getJSONObject(i);
                int art = (int)(Math.random()*articles.length);
                int correctOpt = (int)(Math.random()*4);
                int x=-1;
                for(int i=0;i<options.length;i++)
                    if((x=(int)(Math.random()*articles.length))!=art)
                        for(RadioButton r: options)
                            if(r.getText().equals(articles[x].getString("title")))
                                break;
                            else
                                options[i].setText(articles[x].getString("title"));
                    else
                        i--;
                options[correctOpt].setText(articles[art].getString("title"));
                JSONArray body = new JSONObject(new PersonSearch().execute("http://"+getIntent().getStringExtra(DATA_NAME)+GET_INFO+articles[art].getString("id"),null,"").get()).getJSONArray("sections");
                JSONArray content = null;
                question.setText("");
                for(int i=0;i<body.length();i++)
                    if((content = body.getJSONObject(i).getJSONArray("content")).length()!=0){
                        String restofarticle = content.getJSONObject(0).getString("text").substring(content.getJSONObject(0).getString("text").indexOf("."));
                        question.setText(restofarticle.substring(0,restofarticle.indexOf(".")+1));
                    }
                if(question.getText().equals(""))
                    question.setText("No valid articles");

                //question.setText(new PersonSearch().execute("http://"+getIntent().getStringExtra(DATA_NAME)+GET_INFO+articles[art].getString("id"),null,"").get());

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("asdf",""+s);
            }


            started=true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        submit.setText("Next");

    }

    private void checkAnswer() {

    }

    public class PersonSearch extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            Log.e("asdf","async: "+urls[0]);
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
            Log.e("asdf","stuff");
            return null;
        }

    }
}

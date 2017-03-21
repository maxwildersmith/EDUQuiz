package com.example.quiz.eduquiz;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

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
    private int correctOpt,score,qsLeft;
    private boolean started = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        title = (TextView)findViewById(R.id.title);
        timer = (TextView)findViewById(R.id.timer);
        question = (TextView)findViewById(R.id.question);
        submit = (Button)findViewById(R.id.next);

        score=0;
        qsLeft=10;

        options = new RadioButton[4];
        options[0] = (RadioButton)findViewById(R.id.opt1);
        options[1] = (RadioButton)findViewById(R.id.opt2);
        options[2] = (RadioButton)findViewById(R.id.opt3);
        options[3] = (RadioButton)findViewById(R.id.opt4);



        submit.setText("Start!");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer();
            }
        });


    }



    private void getData() {

        title.setText("Score: "+score+", "+qsLeft+" questions left.");
        if(qsLeft<=0)
            finish();

        if(time!=null)
            time.cancel();
        try {
            String s = new PersonSearch().execute("http://"+getIntent().getStringExtra(DATA_NAME)+GET_ARTICLES,null,"").get();
            try {
                JSONArray articleList = new JSONObject(s).getJSONArray("items");
                Log.e("asdf",articleList.length()+"");
                JSONObject[] articles = new JSONObject[articleList.length()];
                for(int i=0;i<articles.length;i++)
                    articles[i] = articleList.getJSONObject(i);
                int art = (int)(Math.random()*articles.length);
                 correctOpt = (int)(Math.random()*4);
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
                Log.e("asdf",""+body.getJSONObject(0));
                Log.e("asdf","2"+body.getJSONObject(0).getJSONArray("content"));
                question.setText("");
                String restofarticle;
                for(int i=0;i<body.length();i++) {
                    Log.e("asdf", "2" + i + " " + body.getJSONObject(i).getJSONArray("content"));
                    if ((content = body.getJSONObject(i).getJSONArray("content")).length() != 0) {
                        if ((content.getJSONObject(0).has("text"))) {
                            try {
                                restofarticle = content.getJSONObject(0).getString("text").substring(content.getJSONObject(0).getString("text").indexOf("."));
                                restofarticle = restofarticle.substring(0,restofarticle.indexOf("."));
                            } catch (java.lang.StringIndexOutOfBoundsException e) {
                                restofarticle = content.getJSONObject(0).getString("text");
                            }
                                question.setText(restofarticle);

                        }
                    }
                }



                if(question.getText().equals("")) {
                    question.setText("No valid articles");
                    getData();
                }

//                time = new CountDownTimer(10000,1000) {
//                    @Override
//                    public void onTick(long l) {
//                        timer.setText(l/1000.+" seconds left");
//                    }
//
//                    @Override
//                    public void onFinish() {
//                        Toast.makeText(Quiz.this, "Out of Time", Toast.LENGTH_SHORT).show();
//                        //checkAnswer();
//
//                    }
//                };
//                time.start();
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
        qsLeft--;
        if(started)
            if(options[correctOpt].isChecked()){
                score++;
                Toast.makeText(Quiz.this, "Correct!", Toast.LENGTH_SHORT).show();
                getSharedPreferences("stuff",0).edit().putInt("score",1+getSharedPreferences("stuff", Context.MODE_PRIVATE).getInt("score",0)).commit();
            }
        //else
            //time.cancel();


        getData();
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

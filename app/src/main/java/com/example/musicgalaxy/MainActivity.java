package com.example.musicgalaxy;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    int index = 0;
    Button next,prev,play;
    MediaPlayer mediaPlayer;
    TextView textView;
    SeekBar seekBar;
    String []names = {"Pain theme"  , "Akatsuki theme" , "Peaceful theme" , "Sadness and sorrow theme" , "Samidare theme"
                      , "Obito death theme", "Itachi theme" };
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Array list for collecting songs in a list
        final ArrayList<Integer> songs = new ArrayList<>();
        songs.add(0, R.raw.a);
        songs.add(1, R.raw.b);
        songs.add(1, R.raw.c);
        songs.add(1, R.raw.d);
        songs.add(1, R.raw.e);
        songs.add(1, R.raw.f);
        songs.add(1, R.raw.g);
        mediaPlayer = MediaPlayer.create(getApplicationContext(),songs.get(index));

        play = findViewById(R.id.play);
        textView = findViewById(R.id.textView);
        seekBar = findViewById(R.id.seekBar);
        next = findViewById(R.id.next);
        prev = findViewById(R.id.prev);
        textView.setText(names[index]);

        // play and pause button working
        play.setOnClickListener(new View.OnClickListener()
        {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view)
            {
                seekBar.setMax(mediaPlayer.getDuration());
                if (mediaPlayer!= null && mediaPlayer.isPlaying())
                {
                    mediaPlayer.pause();
                    play.setText("Play");
                }
                else
                {
                    mediaPlayer.start();
                    play.setText("Pause");
                }
                textView.setText(names[index]);

            }
        });

        // next button working
        next.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (mediaPlayer!= null){
                    play.setText("Pause");
                }

                if (index < songs.size()-1)
                {
                    index = index + 1;
                }
                else
                {
                    index = 0;
                }

                if (mediaPlayer.isPlaying())
                {
                    mediaPlayer.stop();
                }
                mediaPlayer = MediaPlayer.create(getApplicationContext(),songs.get(index));
                mediaPlayer.start();
                textView.setText(names[index]);
            }
        });

        // previous button working
        prev.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (mediaPlayer != null)
                {
                    play.setText("Pause");
                }

                if (index > 0){
                    index = index - 1;
                }
                else
                {
                    index = songs.size() - 1;
                }

                if (mediaPlayer.isPlaying())
                {
                    mediaPlayer.stop();
                }

                mediaPlayer = MediaPlayer.create(getApplicationContext(),songs.get(index));
                mediaPlayer.start();
                textView.setText(names[index]);
            }
        });

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                seekBar.setMax(mediaPlayer.getDuration());
            }
        });
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser)
                {
                    mediaPlayer.seekTo(progress);
                    seekBar.setProgress(progress);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while (mediaPlayer != null)
                {
                    try
                    {
                        if (mediaPlayer.isPlaying())
                        {
                            Message message = new Message();
                            message.what = mediaPlayer.getCurrentPosition();
                            handler.sendMessage(message);
                            Thread.sleep(1000);
                        }
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    @SuppressLint("Handler") Handler handler = new Handler () {
        @Override
        public void handleMessage (Message msg) {
            seekBar.setProgress(msg.what);
        }
    };
}
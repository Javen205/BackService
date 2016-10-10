package com.javen.demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.javen.service.AmService;
import com.javen.service.R;

/**
 * 推荐参考文章
 * http://blog.csdn.net/mad1989/article/details/22492519
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, AmService.class);
        startService(intent);
    }
}

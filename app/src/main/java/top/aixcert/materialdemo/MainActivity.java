package top.aixcert.materialdemo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import top.aixcert.materialdemo.activity.HomeActivity;
import top.aixcert.materialdemo.data.HitokotoModel;
import top.aixcert.materialdemo.utils.RateLimitInterceptor;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // 启动 HomeActivity
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);

        // 结束当前 Activity
        finish();

    }



}
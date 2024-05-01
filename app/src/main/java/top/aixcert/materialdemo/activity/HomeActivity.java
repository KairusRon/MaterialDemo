package top.aixcert.materialdemo.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import top.aixcert.materialdemo.R;
import top.aixcert.materialdemo.data.HitokotoModel;
import top.aixcert.materialdemo.data.ImageModel;
import top.aixcert.materialdemo.utils.RateLimitInterceptor;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    String hitokoto = null;

    // 文字部分控件
    private TextView tv_hitokoto;
    private Button hitokoto_refresh;
    private Button hitokoto_copy;

    //图片部分控件
    private ImageView iv_image;
    private Button img_refresh;
    private Button img_save;
    int maxRequestsPerSecond = 1;   // 每秒最多请求次数
    int intervalSeconds = 3;    // 请求间隔时间

    // 切换线程
    private static final int MSG_UPDATE_HITOKOTO = 1;
    private final Handler handler = new Handler(msg -> {
        if (msg.what == MSG_UPDATE_HITOKOTO) {
            hitokoto = (String) msg.obj;
            updateHitokoto(hitokoto);
        }
        return true;
    });


    private final OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new RateLimitInterceptor(maxRequestsPerSecond, intervalSeconds)).build();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);


        tv_hitokoto = findViewById(R.id.tv_hitokoto);
        hitokoto_refresh = findViewById(R.id.hitokoto_refresh);
        img_refresh = findViewById(R.id.img_refresh);

        iv_image = findViewById(R.id.iv_image);

        // 读取上一次显示的句子
        SharedPreferences sharedPreferences = getSharedPreferences("hitokoto", MODE_PRIVATE);
        hitokoto = sharedPreferences.getString("hitokoto", "");
        updateHitokoto(hitokoto);
        hitokoto_refresh.setOnClickListener(view -> {
            fetchHitokoto();
        });

        img_refresh.setOnClickListener(view -> {
            fetchImage();
        });

    }

    // 获取一言
    private void fetchHitokoto() {
        String hitokotoUrl = "https://v1.hitokoto.cn/";
        Request request = new Request.Builder().url(hitokotoUrl).get().build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                tv_hitokoto.setText("访问失败，请检查网络");
                Log.e(TAG, "onFailure: 访问失败：" + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    Log.d(TAG, "onResponse: 访问成功：" + responseData);
                    //解析Json
                    HitokotoModel hitokotoModel = new Gson().fromJson(responseData, HitokotoModel.class);
                    String hitokoto = hitokotoModel.getHitokoto();
                    Log.d(TAG, "onResponse-str: " + hitokoto);

                    // 发送消息更新UI
                    handler.obtainMessage(MSG_UPDATE_HITOKOTO, hitokoto).sendToTarget();
                } else {
                    Log.e(TAG, "onResponse: 请求失败：" + response.code());
                }
            }
        });
    }

    // 更新UI
    private void updateHitokoto(String hitokoto) {
        tv_hitokoto.setText(hitokoto);

    }

    // 获取图片
    private void fetchImage() {
        Log.d(TAG, "fetchImage: 开始获取图片");
        // JSON文件的URL
        String imageUrl = "https://t.mwm.moe/ycy/?json";
        Request.Builder url = new Request.Builder().url(imageUrl);
        new OkHttpClient.Builder().build().newCall(url.build()).enqueue(new Callback() {

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                if (response.isSuccessful()){
                    String json = response.body().string();
                    ImageModel imageModel = new Gson().fromJson(json, ImageModel.class);

                    // 获取图片链接
                    String imageUrl = imageModel.getUrl();
                    Log.d(TAG, "onResponse: " + imageUrl);


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            // 显示图片
                            Glide.with(HomeActivity.this).load(imageUrl).into(iv_image);
                            Log.d(TAG, "run: 图片加载成功：" +imageUrl);
                        }
                    });

                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(HomeActivity.this, "获取图片失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}




package top.aixcert.materialdemo.activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import top.aixcert.materialdemo.R;
import top.aixcert.materialdemo.data.HitokotoModel;
import top.aixcert.materialdemo.utils.RateLimitInterceptor;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private TextView tv_hitokoto;
    private Button hitokoto_refresh;
    int maxRequestsPerSecond = 1;   // 每秒最多请求次数
    int intervalSeconds = 3;    // 请求间隔时间

    // 切换线程
    private static final int MSG_UPDATE_HITOKOTO = 1;
    private final Handler handler = new Handler(msg -> {
        if (msg.what == MSG_UPDATE_HITOKOTO) {
            String hitokoto = (String) msg.obj;
            updateHitokoto(hitokoto);
        }
        return true;
    });


    private final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addInterceptor(new RateLimitInterceptor(maxRequestsPerSecond, intervalSeconds))
            .build();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);


        tv_hitokoto = findViewById(R.id.tv_hitokoto);
        hitokoto_refresh = findViewById(R.id.hitokoto_refresh);


        hitokoto_refresh.setOnClickListener(view -> {
            fetchHitokoto();
        });
    }

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


}

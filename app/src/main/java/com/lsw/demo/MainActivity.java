package com.lsw.demo;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.lsw.demo.api.DownloadApi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private Button mButton;
    private static final String TAG = "MainActivity";
    private static final String BASE_URL = "http://pic8.qiyipic.com/image/";
    private static final String DOWNLOAD_URL = "http://pic8.qiyipic.com/image/20170413/47/c7/bk_100084970_r_601_m2.jpg";
    private static final String BOOK_ID = "208766239";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton = (Button)findViewById(R.id.download);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDownload(BOOK_ID,DOWNLOAD_URL);
            }
        });
    }

    private void startDownload(String bookId,String url) {

        final File file = new File(getCoverPicturePath(bookId,url));
        if(file.exists()){
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .build();
        DownloadApi downloadService = retrofit.create(DownloadApi.class);

        Call<ResponseBody> call = downloadService.downloadCoverPicture(url);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "server contacted and has file");
                    String imgCacheDirectory = file.getAbsolutePath();
                    inputstream2File(response.body().byteStream(), imgCacheDirectory);
                } else {
                    Log.d(TAG, "server contact failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "error");
            }
        });
    }

    public void inputstream2File(InputStream inputStream, String filePath) {
        File file = new File(filePath);
        if(!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        inputstream2file(inputStream, file);
    }

    public void inputstream2file(InputStream input, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = input.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getCoverPicturePath(String bookId,String url){

        if(TextUtils.isEmpty(bookId)||TextUtils.isEmpty(url)){
            return "";
        }
        String picName = url.substring(url.lastIndexOf("/")+1);
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        File sdDir;
        if (sdCardExist)
        {
            sdDir = Environment.getExternalStorageDirectory();
            StringBuilder path = new StringBuilder();
            path.append(sdDir.getAbsolutePath());
            path.append("/QYReader/cover_picture");
            path.append("/" + bookId);
            path.append("/" + picName);
            return path.toString();
        }
        return "";
    }

}

package com.example.wushufeng.myupdate.testretrofitdownload;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final FileDownloadService downloadService = ServiceGenerator.createService(FileDownloadService.class);

        downloadService.downloadFileWithDynamicUrlSync("https://github.com/shufengwu/update_server/raw/master/app-debug.apk")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ResponseBody>() {
                               @Override
                               public void call(ResponseBody responseBody) {
                                   File futureStudioIconFile = new File(Environment.getExternalStorageDirectory(), "updata.apk");
                                   InputStream inputStream = null;
                                   FileOutputStream fos = null;
                                   BufferedInputStream bis = null;
                                   try {
                                       inputStream = responseBody.byteStream();
                                       fos = new FileOutputStream(futureStudioIconFile);
                                       bis = new BufferedInputStream(inputStream);
                                       byte[] fileReader = new byte[1024];
                                       long fileSize = responseBody.contentLength();
                                       System.out.println("file size: " + fileSize);

                                       int len;
                                       int total = 0;
                                       while ((len = bis.read(fileReader)) != -1) {
                                           fos.write(fileReader, 0, len);
                                           total += len;
                                           //获取当前下载量,更新progressdialog
                                           //更新notification
                                           System.out.println("file download: " + total + " of " + fileSize);
                                       }
                                       fos.close();
                                       bis.close();
                                       inputStream.close();
                                   } catch (IOException e) {
                                       e.printStackTrace();
                                   }
                               }
                           }, new Action1<Throwable>() {
                               @Override
                               public void call(Throwable throwable) {

                               }
                           }

                );


    }
}

package com.example.wushufeng.myupdate.testretrofitdownload;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by wushufeng on 2016/12/26.
 */

public interface FileDownloadService {
    @Streaming
    @GET
    Observable<ResponseBody> downloadFileWithDynamicUrlSync(@Url String fileUrl);
}

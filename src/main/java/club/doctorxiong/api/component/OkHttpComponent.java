package club.doctorxiong.api.component;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author XiongXin
 * @date 2021/12/9
 * @menu
 */
@Slf4j
@Component
public class OkHttpComponent {

    @Autowired
    private OkHttpClient okHttpClient;

    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    /**
     * get
     * @param url 请求的url
     * @return
     */
    public String getWithHeaders(String url, Headers headers) {
        Request request = new Request.Builder()
                .url(url)
                .headers(headers)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            log.error("okHttp3 getWithHeaders "+url+" error:"+e.getMessage());
            return null;
        }
    }

    /**
     * get
     * @param url 请求的url
     * @return
     */
    public String get(String url) {

        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            log.error("okHttp3 get "+url+" error:"+e.getMessage());
            return null;
        }
    }

    /**
     * post
     * @param url 请求的url
     * @return
     */
    public  String post(String url, String jsonStr) {
        RequestBody body = RequestBody.create(JSON, jsonStr);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            log.error("okHttp3 post "+url+" error:"+e.getMessage());
            return null;
        }
    }

    /**
     * post
     * @param url 请求的url
     * @return
     */
    public  String postWithHeaders(String url, String jsonStr, Headers headers) {
        RequestBody body = RequestBody.create(JSON, jsonStr);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .headers(headers)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            log.error("okHttp3 postWithHeaders "+url+" error:"+e.getMessage());
            return null;
        }
    }
}

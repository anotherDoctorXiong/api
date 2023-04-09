package club.doctorxiong.api.common;



public class RequestContext {

    private static final ThreadLocal<String> THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 获取请求信息
     *
     */
    public static String getRequest() {
        return THREAD_LOCAL.get();
    }

    /**
     * 设置请求信息
     */
    public static void setRequest(String request){
        THREAD_LOCAL.set(request);
    }
}

package club.doctorxiong.api.uitls;


import com.alibaba.fastjson.JSONArray;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static club.doctorxiong.api.common.LocalDateTimeFormatter.getLocalDateByTimestamp;


/**
 * @author : 熊鑫
 * @ClassName : ToolSupport
 * @description : 数据解析器
 * @date : 2019/6/4 16:05
 */
public class StringUtil {


    /**
     * @param str
     * @auther: 熊鑫
     * @return: java.lang.String
     * @date: 2019/6/10 20:34
     * @Description: 从字符串解析key
     * 实例：var hq_str_s_sz399415="I100,4524.75,38.968,0.87,9877903,1407559"
     * 此处key为hq_str_s_sz399415
     */
    public static String getKey(String str) {

        if(str==null){
            return null;
        }
        int key_end_index = str.indexOf("=") - 1;

        String key = str.substring(str.indexOf("var") +3, key_end_index+1);

        return key;
    }

    /**
     * @param str
     * @auther: 熊鑫
     * @return: java.lang.String
     * @date: 2019/6/10 20:36
     * @Description: 从字符串解析value
     * 实例：var hq_str_s_sz399415="I100,4524.75,38.968,0.87,9877903,1407559"
     * 此处value为"I100,4524.75,38.968,0.87,9877903,1407559"
     */
    public static String getValue(String str) {
        int n = 1;
        while (str.charAt(str.indexOf("=") + n) == ' ') {
            n++;
        }
        char sign = str.charAt(str.indexOf("=") + n);
        String result = str.substring(str.indexOf(sign));
        return result;
    }


    public static String getSubValue(String value){
        value = getValue(value);
        return value.substring(1, value.length() - 1);
    }

    /**
     * @param fundName
     * @name: getTypeFromName
     * @auther: 熊鑫
     * @return: java.lang.String
     * @date: 2019/6/20 17:35
     * @description:
     */
    public static String getTypeFromName(String fundName) {
        if (fundName.contains("债")) {
            return "债券型";
        }
        if (fundName.contains("股票")) {
            return "股票型";
        }
        if (fundName.contains("债")) {
            return "债券型";
        }
        if (fundName.contains("指数") ) {
            return "指数型";
        }

        for (int i = fundName.length(); --i >= 0;) {
            if (Character.isDigit(fundName.charAt(i))) {
                return "指数型";
            }
        }
        return "混合型";
    }

    public static boolean isDigit(String str){
        if(str == null){
            return false;
        }
        for (int i = str.length(); --i >= 0;) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;

    }
    /**
     * @name: jsonToTwoArr
     * @auther: 熊鑫
     * @param jsonArr
     * @return: int[][]
     * @date: 2019/6/24 12:30
     * @description: 数组嵌套数组的json转换为二维数组
     */
    public static String[][] jsonToTwoArr1(JSONArray jsonArr) {
        return jsonArr.stream().map(obj -> {
            JSONArray jsonArray = (JSONArray) obj;
            String str = getLocalDateByTimestamp(jsonArray.getLong(0)).toString();
            return new String[]{
                    str,
                    jsonArray.getString(1)
            };
        }).collect(Collectors.toList()).toArray(new String[jsonArr.size()][2]);
    }
    /**
     * @name: jsonToTwoArr
     * @auther: 熊鑫
     * @param jsonArray
     * @return: java.lang.Object[][]
     * @date: 2019/6/24 12:34
     * @description: 数组嵌套key-value的json丢弃key
     */
    /*public static String[][] jsonToTwoArr2(JSONArray jsonArray) {
        List<NetWorthDataDTO> list = jsonArray.toJavaList(NetWorthDataDTO.class);
        return list.parallelStream().map(netWorthDataDTO -> netWorthDataDTO.getDataArr()).collect(Collectors.toList()).toArray(new String[list.size()][4]);
    }*/

    /**
     * @name: getTotalStockCode
     * @auther: 熊鑫
     * @param originalStockCode
     * @return: java.lang.String
     * @date: 2019/9/26 13:38
     * @description: 将原始股票代码转换为带交易所前缀的股票代码
     * 主要作用是判断是上交所还是深交所然后前缀加上"sh","sz"
     */
    public static String getTotalStockCode(String originalStockCode){
        if(originalStockCode.startsWith("sh")||originalStockCode.startsWith("sz")){
            return originalStockCode;
        }
        if(originalStockCode.startsWith("6")||originalStockCode.startsWith("9")||originalStockCode.startsWith("730")||
                originalStockCode.startsWith("000300")||originalStockCode.startsWith("5")||originalStockCode.startsWith("110")){
            return "sh"+originalStockCode;
        }
        return "sz"+originalStockCode;
    }

    /**
     * @name: getRandomString
     * @auther: 熊鑫
     * @param length
     * @return: java.lang.String
     * @date: 2020/1/11 11:06
     * @description: 生成给定长度的随机字符串
     */
    public static String getRandomString(int length){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public static String getCaptcha(){
        String str="0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<4;i++){
            int number=random.nextInt(10);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    /**
     * @name: isPhone
     * @auther: 熊鑫
     * @param phone
     * @return: boolean
     * @date: 2019/11/8 16:15
     * @description: 正则判断手机号是否有效
     */
    public static boolean isPhone(String phone) {
        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
        if (phone.length() != 11) {
            return false;
        } else {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phone);
            return m.matches();
        }
    }

    public static boolean isEmail(String email) {
        String regex = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        return m.matches();

    }

    /**
     * @name: ObjectToBytes
     * @auther: 熊鑫
     * @param obj
     * @return: byte[]
     * @date: 2020/9/2 16:55
     * @description: 将数据转换为字节数组
     */
    public static byte[] ArrToBytes(String[][] obj){
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            ObjectOutputStream outputStream = new ObjectOutputStream(byteArrayOutputStream);
            outputStream.writeObject(obj);
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @name: getIndex
     * @auther: 熊鑫
     * @param arrData
     * @param date
     * @return: java.lang.Integer
     * @date: 2020/9/5 14:34
     * @description: 通过二分法获取数据下标,日期格式统一为yyyy-MM-dd,使用compare比较,不存在则取左边的
     *
     */
    public static Integer getIndexOrLeft(String[][] arrData,String date){
        if(date.compareTo(arrData[arrData.length-1][0]) > 0){
            return arrData.length-1;
        }
        int n = arrData.length;
        int left = 0, right = n - 1, ans = n;
        while (left <= right) {
            int mid = ((right - left) >> 1) + left;
            if (date.compareTo(arrData[mid][0]) == 0) {
                return mid;
            } else if(date.compareTo(arrData[mid][0]) < 0){
                ans = mid;
                right = mid - 1;
            }else {
                left = mid + 1;
            }
        }
        return ans;
    }

    /**·······
     * @name: getIndexOrLeft
     * @auther: 熊鑫
     * @param arrData
     * @param date
     * @return: java.lang.Integer
     * @date: 2020/9/6 23:36
     * @description: 通过二分法获取数据下标,日期格式统一为yyyy-MM-dd,使用compare比较,不存在则取右边的
     */
    public static Integer getIndexOrRight(String[][] arrData,String date){
        int n = arrData.length;
        int left = 0, right = n - 1, ans = n;
        while (left <= right) {
            int mid = ((right - left) >> 1) + left;
            if (date.compareTo(arrData[mid][0]) == 0) {
                return mid;
            } else if(date.compareTo(arrData[mid][0]) < 0){
                right = mid - 1;
            }else {
                ans = mid;
                left = mid + 1;
            }
        }
        return ans;
    }

    /**
     * 算法名
     */
    public static final String KEY_NAME = "AES";
    /**
     * 解析密钥
     */
    private static final String CIPHER_ALGORITHM_CBC = "AES/CBC/NoPadding";

    /**
     * @name: phoneDecrypt
     * @return: java.lang.String
     * @date: 2020/6/26 22:52
     * @description: 针对订单手机号的解析
     */
    public static String phoneDecrypt(String data, String key, String iv) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC);
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), KEY_NAME);
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
            byte[] original = cipher.doFinal(new Base64().decode(data));
            String originalString = new String(original);
            return originalString;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 莱茵斯坦距离——相似度计算
     *
     * @param str1
     * @param str2
     */
    public static Float levenshtein(String str1, String str2) {
        //--- 计算两个字符串的长度。
        int len1 = str1.length();
        int len2 = str2.length();
        // 建立数组，比字符长度的大一个空间
        int[][] matrix = new int[len1 + 1][len2 + 1];
        // 分别为两个字符串赋值到矩阵中
        for (int a = 0; a <= len1; a++) {
            matrix[a][0] = a;
        }
        for (int a = 0; a <= len2; a++) {
            matrix[0][a] = a;
        }
        // 计算两个字符是否一样，计算左上的值
        int temp;
        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                //判断两个字符是否一致
                if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                    temp = 0;
                } else {
                    temp = 1;
                }
                // 取三个值中最小的
                // matrix[i - 1][j - 1] + temp 替换
                // matrix[i][j - 1] + 1 追加
                // matrix[i - 1][j] + 1 删除
                matrix[i][j] = min(matrix[i - 1][j - 1] + temp, matrix[i][j - 1] + 1,
                        matrix[i - 1][j] + 1);
            }
        }
        // 计算相似度
        float similarity = 1 - (float) matrix[len1][len2]
                / Math.max(str1.length(), str2.length());

        return similarity;
    }

    // 得到最小值
    private static int min(int... is) {//int... 获取未知长度的int
        int min = Integer.MAX_VALUE;
        for (int i : is) {
            if (min > i) {
                min = i;
            }
        }
        return min;
    }

    /**
     * md5加密
     * @author xiongxin
     * @param text
     * @return java.lang.String
     */
    public static String getMd5(String text) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] bytes = md5.digest(text.getBytes(StandardCharsets.UTF_8));

        StringBuilder builder = new StringBuilder();

        for (byte aByte : bytes) {
            builder.append(Integer.toHexString((0x000000FF & aByte) | 0xFFFFFF00).substring(6));
        }

        return builder.toString();
    }

    public static List<BigDecimal> getNumStrList(String str){
        List<BigDecimal> res = new LinkedList<>();
        if(str != null){
            for (int i = 0; i < str.length(); i++) {
                Character character = str.charAt(i);
                StringBuffer stringBuffer = new StringBuffer();
                while (isNum(character) && i < str.length()){
                    stringBuffer.append(character);
                    character = str.charAt(++i);
                }
                if(stringBuffer.length() > 0){
                    res.add(new BigDecimal(stringBuffer.toString()));
                }
            }
        }
        return res;
    }


    // 是数字或者.
    public static boolean isNum(Character character){
        if(character >= 48 && character <= 57){
            return true;
        }else if(character == 46){
            return true;
        }
        return false;
    }



    /**
     * 判断
     *
     * @param str
     * @return boolean
     */
    public static boolean isBlank(String str){
        return str == null || str.length() == 0;
    }

    public static boolean isNotBlank(String str){
        return str != null && str.trim().length() > 0;
    }



}

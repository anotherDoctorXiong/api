package club.doctorxiong.api.service;

import club.doctorxiong.api.common.dto.TokenDTO;
import club.doctorxiong.api.common.request.TokenOrderRequest;
import club.doctorxiong.api.entity.Token;
import club.doctorxiong.api.uitls.StringUtil;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenService {

    private static final String LOCAL_FILE = "C:\\Users\\xiong\\IdeaProjects\\api\\token.txt";
    private static final String PROD_FILE = "/monitor/health";

    // public ConcurrentHashMap<>
    // @Value("$spring.profiles.active")
    private String profiles = "local";

    public static List<TokenDTO> TOKEN_LIST = new ArrayList<>();

    public void refreshToken() {
        String filePath = profiles.equals("local")?LOCAL_FILE:PROD_FILE;
        try {
            StringBuilder jsonLine = new StringBuilder();
            List<String> list = Files.readAllLines(Paths.get(filePath));
            list.forEach(v -> jsonLine.append(v));
            TOKEN_LIST = JSONArray.parseArray(jsonLine.toString(),TokenDTO.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   /* public void updateTokenFile(TokenDTO tokenDTO) {
        String filePath = profiles.equals("local")?LOCAL_FILE:PROD_FILE;
        try {
            StringBuilder jsonLine = new StringBuilder();
            List<String> list = Files.readAllLines(Paths.get(filePath));
            list.forEach(v -> jsonLine.append(v));
            JSONArray.parseArray(jsonLine.toString(),TokenDTO.class).forEach(tokenDTO -> {
                tokenMap.put(tokenDTO.getToken(),tokenDTO);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void finishTokenOrder(TokenOrderRequest tokenOrderRequest) {

        LocalDate now = LocalDate.now();
        Optional<TokenDTO> tokenOptional = TOKEN_LIST.stream().filter(token -> token.getPhone().equals(tokenOrderRequest.getPhone()) && token.getType().equals(tokenOrderRequest.getOrderType())).findAny();

        // 长时间续费优惠6个月以上赠1个月,1年赠三个月
        if(tokenOrderRequest.getMonthCount() >= 12){
            tokenOrderRequest.setMonthCount(tokenOrderRequest.getMonthCount() + 3);
        }else if(tokenOrderRequest.getMonthCount() >= 6){
            tokenOrderRequest.setMonthCount(tokenOrderRequest.getMonthCount() + 1);
        }
        LocalDate expireTime = now.plusMonths(tokenOrderRequest.getMonthCount());
        if (tokenOptional.isPresent()) {
            TokenDTO exitToken = tokenOptional.get();
            //续费
            if (exitToken.getEndDate().compareTo(now) < 0) {
                // 已过期
                exitToken.setEndDate(expireTime);
            } else {
                //未过期
                exitToken.setEndDate(exitToken.getEndDate().plusMonths(tokenOrderRequest.getMonthCount()));
            }
            //清除该token当天的限制缓存,防止续费后仍提示过期。
            redisTemplate.delete(exitToken.getToken());
            tokenService.saveOrUpdate(exitToken);
        } else {
            //订购token服务
            String token = StringUtil.getRandomString(10);
            while (tokenService.getToken(token) != null) {
                token = StringUtil.getRandomString(10);
            }
            Token newToken = new Token(tokenOrderRequest.getPhone(),expireTime, token, tokenOrderRequest.getOrderType());
            tokenService.save(newToken);
        }
        log.info(tokenOrderRequest.getPhone()+"_充值成功服务为期_"+ tokenOrderRequest.getMonthCount()+"个月");
    }

    public static void main(String[] args) {
        TokenService service = new TokenService();
        service.refreshTokenMap();
    }*/
}

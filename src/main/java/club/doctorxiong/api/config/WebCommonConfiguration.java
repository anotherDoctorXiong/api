package club.doctorxiong.api.config;

import club.doctorxiong.api.common.LocalDateTimeFormatter;
import club.doctorxiong.api.interceptor.AuthInterceptor;
import club.doctorxiong.api.interceptor.CommonHttpInterceptor;
import club.doctorxiong.api.interceptor.RepeatedlyReadFilter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;



/**
 * 通用web配置初始化
 *
 * @author zhongshenghua
 * @date 2021/5/17
 */
@Configuration
public class WebCommonConfiguration implements WebMvcConfigurer {

    /**
     * 初始化通用拦截器
     *
     * @return bean
     */
    @Bean
    public HandlerInterceptor getInterceptor() {
        return new CommonHttpInterceptor();
    }

    /**
     * 登录校验拦截器
     *
     * @return AuthInterceptor
     */
    @Bean
    public AuthInterceptor authInterceptor() {
        return new AuthInterceptor();
    }


    @Bean
    public FilterRegistrationBean repeatedlyReadFilter() {
        FilterRegistrationBean<RepeatedlyReadFilter> registration = new FilterRegistrationBean<>();
        RepeatedlyReadFilter repeatedlyReadFilter = new RepeatedlyReadFilter();
        registration.setFilter(repeatedlyReadFilter);
        registration.addUrlPatterns("/*");
        return registration;
    }

    /**
     * 可定义多个拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 定义过滤拦截的url名称，拦截所有请求
        registry.addInterceptor(this.getInterceptor()).addPathPatterns("/**");
        // 定义登录权限校验拦截器
        registry.addInterceptor(this.authInterceptor()).addPathPatterns("/**")
                .excludePathPatterns("/order/**");
    }

    /**
     * 跨域处理
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.setAllowCredentials(true);
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.setMaxAge(600L);
        UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();
        corsConfigurationSource.registerCorsConfiguration("/**", config);
        return new CorsFilter(corsConfigurationSource);
    }



    @Bean
    public Converter<String, LocalDate> localDateConverter() {
        return new Converter<String, LocalDate>() {
            @Override
            public LocalDate convert(String source) {

                try {
                    return LocalDate.parse(source, LocalDateTimeFormatter.DATE_FORMAT_COMMON);
                } catch (Exception e) {
                    return null;
                }
            }
        };
    }


    @Bean
    public Converter<String, LocalDateTime> localDateTimeConverter() {
        return new Converter<String, LocalDateTime>() {
            @Override
            public LocalDateTime convert(String source) {
                try {
                    return LocalDateTime.parse(source, LocalDateTimeFormatter.TIME_FORMAT_COMMON);
                } catch (Exception e) {
                    return null;
                }
            }
        };
    }

    /**
     * json序列化及反序列化配置
     */
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 时间对象自定义格式化
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
            @Override
            public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
                long timestamp = Long.parseLong(jsonParser.getText());
                Instant instant = Instant.ofEpochMilli(timestamp);
                return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            }
        });
        javaTimeModule.addSerializer(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
            @Override
            public void serialize(LocalDateTime localDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeNumber(localDateTime.toInstant(ZoneOffset.ofHours(8)).toEpochMilli());
            }
        });
        // BigDecimal保留四位小数
        javaTimeModule.addSerializer(BigDecimal.class, new JsonSerializer<BigDecimal>() {
            @Override
            public void serialize(BigDecimal value, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
                if (value != null) {
                    // 保留四位小数，四舍五入
                    BigDecimal number = value.setScale(4, RoundingMode.HALF_UP);
                    jsonGenerator.writeNumber(number);
                } else {
                    jsonGenerator.writeNumber(value);
                }
            }
        });
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(LocalDateTimeFormatter.DATE_FORMAT_COMMON));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(LocalDateTimeFormatter.DATE_FORMAT_COMMON));
        // javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
        // javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(LocalDateTimeFormatter.TIME_FORMAT_COMMON));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(LocalDateTimeFormatter.TIME_FORMAT_COMMON));
        // Long转换为String传输
        javaTimeModule.addSerializer(Long.class, ToStringSerializer.instance);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.registerModule(javaTimeModule);
        converter.setObjectMapper(mapper);
        return converter;
    }


}

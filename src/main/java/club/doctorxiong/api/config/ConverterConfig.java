package club.doctorxiong.api.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Configuration
public class ConverterConfig {



    /**
     * json序列化及反序列化配置
     */

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

        javaTimeModule.addDeserializer(BigDecimal.class, new JsonDeserializer<BigDecimal>() {
            @Override
            public BigDecimal deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) {
                try {
                    return new BigDecimal(jsonParser.getText());
                } catch (IOException e) {
                    return null;
                }
            }
        });
        // Long转换为String传输
        javaTimeModule.addSerializer(Long.class, ToStringSerializer.instance);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        mapper.registerModule(javaTimeModule);
        converter.setObjectMapper(mapper);
        return converter;
    }
}

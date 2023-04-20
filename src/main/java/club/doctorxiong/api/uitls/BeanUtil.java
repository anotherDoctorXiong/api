package club.doctorxiong.api.uitls;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

import java.io.*;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * bean复制转换工具类
 * @author xiongxin
 * @date 2021/5/17
 */
public class BeanUtil {

    private static MapperFacade mapper = null;

    static {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        mapper = mapperFactory.getMapperFacade();
    }

    /**
     * 基于Dozer转换对象的类型.
     */
    public static <S, D> D map(S source, Class<D> destinationClass) {
        return mapper.map(source, destinationClass);
    }

    /**
     * 基于Dozer转换Collection中对象的类型.
     */
    public static <S, D> List<D> mapList(Iterable<S> sourceList, Class<D> destinationClass) {
        return sourceList != null ? mapper.mapAsList(sourceList, destinationClass) : null;
    }

    /**
     * 将source复制到dest
     *
     * @param source 源对象
     * @param dest 拷贝到目标对象
     * @param <S> 源对象类型
     * @param <D> 目标对象类型
     */
    public static <S, D> void map(S source, D dest) {
        if (source != null && dest != null) {
            mapper.map(source, dest);
        }
    }


    /**
     * 使用Gzip算法压缩Java对象并存储到字节数组中
     * @param t 待压缩的Java对象
     * @return 压缩后的字节数组
     * @throws IOException
     */
    public static <T extends Serializable> byte[] compressObject(T t) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(new GZIPOutputStream(baos));
        oos.writeObject(t);
        oos.close();
        byte[] compressed = baos.toByteArray();
        baos.close();
        return compressed;
    }

    /**
     * 从字节数组中解压缩为Java对象
     * @param compressed 压缩后的字节数组
     * @return 解压缩后的Java对象
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static <T extends Serializable> T decompressObject(byte[] compressed) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(compressed);
        ObjectInputStream ois = new ObjectInputStream(new GZIPInputStream(bais));
        T obj = (T) ois.readObject();
        ois.close();
        return obj;
    }
}

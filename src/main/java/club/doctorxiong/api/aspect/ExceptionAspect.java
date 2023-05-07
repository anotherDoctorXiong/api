package club.doctorxiong.api.aspect;


import club.doctorxiong.api.common.CommonResponse;
import club.doctorxiong.api.common.InnerException;
import club.doctorxiong.api.common.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.UnexpectedTypeException;
import javax.validation.executable.ValidateOnExecution;


/**
 * @author : 熊鑫
 * @description :同一异常处理
 * @date : 2019/5/30 14:12
 */
@RestControllerAdvice
@Slf4j
public class ExceptionAspect {


    @ResponseBody
    @ValidateOnExecution
    @ExceptionHandler({BindException.class, MissingServletRequestParameterException.class, UnexpectedTypeException.class, TypeMismatchException.class})
    public CommonResponse validBindException(HttpServletRequest req, HttpServletResponse res, Exception ex) {
        if (ex instanceof BindException) {
            FieldError error = ((BindException) ex).getFieldError();
            CommonResponse response = new CommonResponse();
            response.setCode(CommonResponse.Code.FAIL);
            response.setMessage(String.format("(%s)%s,value:%s", error.getField(), error.getDefaultMessage(), error.getRejectedValue()));
            return response;
        }

        if (ex instanceof MissingServletRequestParameterException) {
            MissingServletRequestParameterException e = (MissingServletRequestParameterException) ex;
            log.info("MissingServletRequestParameterException: {%s}", e);
            CommonResponse response = new CommonResponse();
            response.setCode(CommonResponse.Code.FAIL);
            response.setMessage(String.format("缺少请求参数:%s", e.getParameterName()));
            return response;
        }

        if (ex instanceof TypeMismatchException) {
            TypeMismatchException e = (TypeMismatchException) ex;
            CommonResponse response = new CommonResponse();
            response.setCode(CommonResponse.Code.FAIL);
            response.setMessage(String.format("参数类型错误:%s,需要类型:%s", e.getPropertyName(), e.getRequiredType()));
            return response;
        }

        if (ex instanceof UnexpectedTypeException) {
            CommonResponse response = new CommonResponse();
            response.setCode(CommonResponse.Code.FAIL);
            response.setMessage("参数类型错误");
            return response;
        }

        CommonResponse response = new CommonResponse();
        response.setCode(CommonResponse.Code.FAIL);
        response.setMessage("内部异常捕捉机制未配置");
        return response;
    }


    /**
     * Method错误处理
     *
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public CommonResponse handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        CommonResponse response = new CommonResponse();
        response.setCode(CommonResponse.Code.FAIL);
        response.setMessage(String.format("不支持 METHOD:%s,只支持:%s", ex.getMethod(), String.join("; ", ex.getSupportedMethods())));
        return response;
    }

    /**
     * 请求ContentType类型错误处理
     *
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
    public CommonResponse handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex) {
        CommonResponse response = new CommonResponse();
        response.setCode(CommonResponse.Code.FAIL);
        response.setMessage(String.format("不支持请求格式 Content-Type:%s", ex.getContentType()));
        return response;
    }


    /**
     * 请求参数json解释失败错误处理
     *
     * @param ex 异常
     * @return 返回响应数据
     */
    @ResponseBody
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public CommonResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        CommonResponse response = new CommonResponse();
        response.setCode(CommonResponse.Code.PRECONDITION_FAILD);
        response.setMessage("解释请求参数为JSON时失败,请检查JSON数据格式");
        return response;
    }

    /**
     * 处理通用异常
     *
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(InnerException.CommonException.class)
    public CommonResponse handleCommonException(InnerException.CommonException ex) {
        CommonResponse response = new CommonResponse();
        response.setCode(ex.getCode());
        response.setMessage(ex.getMessage());
        return response;
    }

    /**
     * 未知错误处理 发送钉钉
     * @param ex 异常
     * @return
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public CommonResponse handleException(HttpServletRequest req, Exception ex) {
        // 错误直接抛出
        log.error("系统内部异常 请求信息{}异常信息{}",RequestContext.getRequest(),ex);
        CommonResponse response = new CommonResponse();
        response.setCode(CommonResponse.Code.ERROR);
        response.setMessage("系统繁忙,请稍后再试");
        return response;
    }
}

package cc.caiweiwei.framework.shiguangcms.webmvc.handler;

import cc.caiweiwei.framework.shiguangcms.common.constant.CommonBusinessCodeEnum;
import cc.caiweiwei.framework.shiguangcms.common.exception.BaseException;
import cc.caiweiwei.framework.shiguangcms.common.utils.BusinessCodeMessageUtil;
import cc.caiweiwei.framework.shiguangcms.core.entity.result.BaseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * 描述：全局异常处理
 *
 * @author CaiZhengwei
 * @since 2024/1/2 17:14
 */
@RestControllerAdvice(basePackages = {"cc.caiweiwei"})
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Throwable.class)
    public BaseResult handleUnknownException(Throwable e) {
        logger.error("发生未知异常，异常栈信息：", e);
        return BaseResult.errorOf(CommonBusinessCodeEnum.UNKNOWN_EXCEPTION_CODE, BusinessCodeMessageUtil.getBusinessCodeMessage(CommonBusinessCodeEnum.UNKNOWN_EXCEPTION_CODE, "未知错误！"));
    }

    @ExceptionHandler(BaseException.class)
    public BaseResult handleBaseException(BaseException e) {
        logger.error("发生自定义异常，异常栈信息是：", e);
        return BaseResult.errorOf(e.getBusinessCode(), e.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    public BaseResult handleNullPointerException(NullPointerException e) {
        logger.error("发生空指针异常，异常栈信息是：", e);
        return BaseResult.errorOf(CommonBusinessCodeEnum.NULL_POINTER_EXCEPTION_CODE, e.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public BaseResult handleConstraintViolationException(ConstraintViolationException e) {
        logger.error("发生参数校验异常，异常栈信息是：", e);
        String message = e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining());
        return BaseResult.errorOf(CommonBusinessCodeEnum.DATA_NOT_MATCH_EXCEPTION_CODE, message);
    }

    @ExceptionHandler(BindException.class)
    public BaseResult handleBindException(BindException e) {
        logger.error("发生绑定异常，异常栈信息是：", e);
        String message = e.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining());
        return BaseResult.errorOf(CommonBusinessCodeEnum.DATA_NOT_MATCH_EXCEPTION_CODE, message);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public BaseResult handleIllegalArgumentException(Exception e) {
        logger.error("发生非法参数异常，异常栈信息是：", e);
        return BaseResult.errorOf(CommonBusinessCodeEnum.ILLEGAL_PARAM_EXCEPTION_CODE, e.getMessage());
    }

}

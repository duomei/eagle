package cn.jf.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.text.SimpleDateFormat;

/**
 * 用户操作日志，切面监控类
 *
 * @author jf
 * @create 2017-12-06 17:27
 **/
@Aspect
@Component
public class LogAspect {

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    /**
     * 配置切入点，该方法无实现，主要为方便同类中其他方法使用该配置的切入点<p>
     * 监控添加了cn.jf.annotation.OperLog注解的类
     */
    @Pointcut("@annotation(cn.jf.annotation.OperLog)")
    public void logAspect() {
    }

    /**
     * 配置前置通知,使用在方法logAspect()上注册的切入点 同时接受JoinPoint切入点对象,可以没有该参数
     * @param point
     */
    @Before("logAspect()")
    public void before(JoinPoint point) {
    }

    /**
     * 配置后置通知,使用在方法logAspect()上注册的切入点
     * @param joinPoint
     */
    @After("logAspect()")
    public void after(JoinPoint joinPoint) {
    }

    /**
     * 配置环绕通知,使用在方法logAspect上注册的切入点
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("logAspect()")
    public Object around(JoinPoint joinPoint) throws Throwable {
        String successFlg = "1";
        // 获取方法开始时间
        long start = System.currentTimeMillis();
        //方法执行前初始化异常信息
        String exceptionInfo = "";
        try {
            return ((ProceedingJoinPoint) joinPoint).proceed();// 执行目标方法
        } catch (Exception e) {
            successFlg = "0"; //记录成功-失败标识

            //格式化异常信息用于记录（会影响程序执行时间的统计）
            exceptionInfo = e.toString();
            StackTraceElement[] trace = e.getStackTrace();
            for (StackTraceElement s : trace) {
                exceptionInfo += "\tat " + s + "\r\n";
            }
            throw e;
        }finally {
            long end = System.currentTimeMillis();
            // 获取机器IP、名称信息
            InetAddress ia = InetAddress.getLocalHost();
            String hostName = ia.getHostName();
            String hostIP = ia.getHostAddress();

            // 记录毫秒级开始与结束时间
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:SSS");
            String startTime = sdf.format(start);
            String endTime = sdf.format(end);

            // 访问目标方法的参数,默认第一个参数为服务主键
            //Object[] args = joinPoint.getArgs();
            //Object obj = args.length>0&&null!=args[0]?args[0]:null;

            //设置监控信息
//            MonitInfo monitInfo = new MonitInfo();
//            if(obj != null && obj instanceof MonitInfo) {
//                monitInfo = (MonitInfo) obj;
//                monitInfo.setServiceName(joinPoint.getSignature().getDeclaringTypeName());
//                monitInfo.setMethodName(joinPoint.getSignature().getName());
//                monitInfo.setCallStack(joinPoint.toShortString());
//                monitInfo.setStartTime(startTime);
//                monitInfo.setEndTime(endTime);
//                monitInfo.setUseTimeMs(end-start);
//                monitInfo.setHostIP(hostIP);
//                monitInfo.setHostName(hostName);
//                monitInfo.setSuccessFlg(successFlg);
//                if(exceptionInfo != null) {
//                    monitInfo.setE(exceptionInfo);
//                }
//
//                Gson gson = new Gson();
//                String monitorMsg = gson.toJson(monitInfo);
//                this.sendMsg2Mongo(monitorMsg);
            }
            //logger.info("spring aop around " + joinPoint + "\tUse time : " + (end - start)	+ " ms!");
    }

    // 配置后置返回通知,使用在方法logAspect()上注册的切入点
    @AfterReturning("logAspect()")
    public void afterReturn(JoinPoint joinPoint) {
    }

    // 配置抛出异常后通知,使用在方法logAspect()上注册的切入点
    @AfterThrowing(pointcut = "logAspect()", throwing = "e")
    public void afterThrow(JoinPoint joinPoint, Exception e) {
        //logger.error("spring aop afterThrow " + joinPoint + "\t" + e.getMessage(),e);
    }
}

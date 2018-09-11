package hello.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 日期工具类
 * @author xiang.wei
 * @create 2018/1/4 9:05
 */
public class DateUtil {
    private final static Map<String, ThreadLocal<SimpleDateFormat>> sdfMap = new HashMap<String, ThreadLocal<SimpleDateFormat>>();

    public final static String MDHMSS = "MMddHHmmssSSS";
    public final static String YMD = "yyyyMMdd";
    public final static String YMD_ = "yyyy-MM-dd";
    public final static String HMS = "HHmmss";
    public final static String YMDHMS = "yyyyMMddHHmmss";
    public final static String YM = "yyMM";
    public final static String YMDHMS_ = "yyyy-MM-dd HH:mm:ss";
    public final static String Y2M2D2HMS_ = "yyyy/MM/dd HH:mm:ss";
    public final static String Y2M2D2_ = "yyyy/MM/dd";
    /**
     * @param pattern
     * @return java.text.SimpleDateFormat 该实例
     * @description 根据map中的key找出对应线程中的sdf实例
     * @date: 2017/5/30 10:37
     * @author:xiang.wei
     */
    private static SimpleDateFormat getsdf(final String pattern) {
        ThreadLocal<SimpleDateFormat> sdfThread = sdfMap.get(pattern);
        if (sdfThread == null) {
            //双重检验,防止sdfMap被多次put进去值,和双重锁单例原因是一样的
            synchronized (DateUtil.class) {
                // 只有Map中还没有这个pattern的sdf才会生成新的sdf并放入map
                // 这里是关键,使用ThreadLocal<SimpleDateFormat>替代原来直接new SimpleDateFormat
                sdfThread = sdfMap.get(pattern);
                if (sdfThread == null) {
                    sdfThread = new ThreadLocal<SimpleDateFormat>() {
                        @Override
                        protected SimpleDateFormat initialValue() {
                            return new SimpleDateFormat(pattern);
                        }
                    };
                    sdfMap.put(pattern, sdfThread);
                }

            }
        }
        return sdfThread.get();
    }

    /**
     * @param dateStr 时间串
     * @param pattern 给定转换格式
     * @return java.util.Date
     * @description 按照指定pattern方式解析日期
     * @date: 2017/5/30 11:22
     * @author:xiang.wei
     */
    public static Date parseDate(String dateStr, String pattern) {
        Date date = null;
        try {
            date = parseDateCanThrow(dateStr, pattern);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * @param date    需要格式化的date
     * @param pattern 给定转换格式
     * @return java.lang.String 时间串
     * @description 按照指定pattern的方式格式化时间
     * @date 2017/5/30 11:51
     * @author xiang.wei
     */
    public static String formatDate(Date date, String pattern) {
        return DateUtil.getsdf(pattern).format(date);
    }

    /**
     * @param date
     * @return
     */
    public static String formatDate(Date date){
        return DateUtil.getsdf(DateUtil.YMDHMS_).format(date);
    }

    public static Date parseDateCanThrow(String date, String pattern) throws ParseException {
        return DateUtil.getsdf(pattern).parse(date);
    }

    /**
     * @author xiang.wei
     * @date 2017/5/29 23:41
     * @Description:
     */
    public static Calendar getCurrentDay() {
        return Calendar.getInstance();
    }

    /**
     * @param
     * @return java.util.Date
     * @description
     * @method getToday
     * @date: 2017/5/30 10:18
     * @author:xiang.wei
     */
    public static Date getToday() {
        Calendar calendar = getCurrentDay();
        return calendar.getTime();
    }

    /**
     * @param date
     * @return java.util.Date
     * @description
     * @method getTomorrow
     * @date: 2017/5/30 10:17
     * @author:xiang.wei
     */
    public static Date getTomorrow(Date date) {
        Calendar calendar = getCurrentDay();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, +1);
        return calendar.getTime();
    }

    /**
     * 得到本年
     * @return
     */
    public static String getCurrentYear() {
        Calendar calendar = getCurrentDay();
        return String.valueOf(calendar.get(Calendar.YEAR));
    }

    /**
     * 获取当前月份之前的月份
     * @param num 数量
     * @return
     * 例如需要获取当前月份之前的第三个月，则传入的num=-3
     */
    public static Date getNumMonth(int num) {
        Calendar calendar = getCurrentDay();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, num);
        return calendar.getTime();
    }
}

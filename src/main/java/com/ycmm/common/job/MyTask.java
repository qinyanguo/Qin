package com.ycmm.common.job;



import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MyTask {

//    @Scheduled(cron = "0 0 * * * ?") //每小时执行一次
    public void firstTime() throws Exception {

        Calendar calendar = Calendar.getInstance();
        Date currentStartTime = calendar.getTime();
        //如果是工作日且距离当前时间超过两天
        if (isWorkday(currentStartTime)) {
            if (overtime(new Date(), 48)) {
                //执行逻辑
            }

            List<Map<String, Object>> data = testData();  //测试数据

            for (Map<String, Object> map : data) {
                map.put("state", "1");
                update(map); //更新操作

            }
        }

    }

    /**
     * 判断是否是工作日
     * <p>
     * true:工作日   false:休息日
     * <p>
     * 与"休息日列表"有关,该列表所示日期区间必须涵盖本参数date
     *
     * @param date
     */

    private boolean isWorkday(Date date) {

        String yyyyMMdd = new SimpleDateFormat("yyyyMMdd").format(date);
        //判断是否是周末  且  排除要上班的周末
        if (weekendWork().contains(yyyyMMdd)) {
            return true;
        }else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int result = cal.get(Calendar.DAY_OF_WEEK);
//            int saturday = Calendar.SATURDAY;
//            int sunday = Calendar.SUNDAY;
            if (result == Calendar.SATURDAY || result == Calendar.SUNDAY) {
                return false;
            }
        }

        return !playdayCalendar().contains(yyyyMMdd);

    }

    /**
     * 因放假要上班的周末
     * @return
     */
    private List<String> weekendWork() {
        String []  weekendWorks = {
                "20180929",   "20180930"
        };

        return Arrays.asList(weekendWorks);
    }

    /**
     * 休息日列表,按月份列出
     * <p>
     * 格式:yyyyMMdd
     */

    private List<String> playdayCalendar() {

        String[] playdays = {

                /**九月*/
                "20180924",

                /**十月*/
                "20181001", "20181002", "20181003", "20181004", "20181005"
        };

        return Arrays.asList(playdays);

    }


    /**
     * 计算是否超过h小时
     */

    private boolean overtime(Date oldDate, int h) {

        long maxSeconds = h * 60 * 60; //秒数

        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();

        long now = calendar.getTimeInMillis() / 1000;

        calendar.clear();
        calendar.setTime(oldDate);

        long old = calendar.getTimeInMillis() / 1000;

        int num = countPlayday(oldDate, today); //计算期间休息的天数

        int playdaySeconds = num * 24 * 60 * 60; //休息天总秒数

        //公式: 现在日期转换总分钟数 - 过去日期转换总分钟数 - 此期间休息天的分钟数 >= 参数h小时的分钟数

        if (now / 60 - old / 60 - playdaySeconds / 60 >= maxSeconds / 60) { //精确到分,如果精确到秒去掉除60

            return true;

        }

        return false;

    }


    /**
     * 计算时间段内有几天休息天
     * <p>
     * 与"休息日列表"有关,该列表必须涵盖此时间段 begin ~ end
     *
     * @param begin 一般指某条数据的创建时间
     * @param end   当前时间
     */

    private int countPlayday(Date begin, Date end) {

        int num = 0;

        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTime(begin);
        long beginMillis = calendar.getTimeInMillis();
        calendar.setTime(end);
        long endMillis = calendar.getTimeInMillis();
        if (beginMillis <= endMillis) {
            long millis = beginMillis;
            long onedayMillis = 24 * 60 * 60 * 1000; //1天的毫秒数

            while (true) {

                calendar.setTimeInMillis(millis);
                if (!isWorkday(calendar.getTime())) { //非工作日
                    num++;
                }

                millis = millis + onedayMillis;  //下一天
                if (millis / (60 * 1000) > endMillis / (60 * 1000)) { //精确到分
                    break;
                }
            }
        }

        return num;

    }




    /**
     * 对象转Integer
     */

    private Integer parseInt(Object obj) {

        try {

            return Integer.parseInt(obj.toString());

        } catch (Exception e) {

            return -100;

        }

    }


    //=========下面测试方法=====================================


    private String format(Date date) {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return df.format(date);


    }

    private Date parse(String str) throws ParseException {

        Date date = null;

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        date = df.parse(str);

        return date;

    }


    private int update(Map<String, Object> map) { //模拟更新

        return 1;

    }


    private List<Map<String, Object>> testData() { //模拟数据

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Map<String, Object> map = new HashMap<String, Object>();

        for (int i = 0; i < 10; i++) {

            map = new HashMap<String, Object>();

            list.add(map);

        }

        return list;

    }

    public static String isWeekend(String bDate) throws ParseException {
        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date bdate = format1.parse(bDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(bdate);
        if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
            return "OK";
        } else{
            return "NO";
        }

    }

    public static void main(String[] args) throws Exception {


        MyTask t = new MyTask();

        //      System.out.println(t.format(new Date()));

        //      System.out.println(t.parse("2015-3-10 12:12:59"));

        Calendar calendar = Calendar.getInstance();
        //      calendar.set(calendar.HOUR_OF_DAY, 17);
//        System.out.println(t.format(calendar.getTime()));
//        calendar.set(calendar.MONTH, 3); //4月份
//        calendar.set(calendar.DAY_OF_MONTH, 25);

//              System.out.println(t.overtime(calendar.getTime(), 24));


        String d = "2018-07-20 00:01:00";
//        String d = "2018-07-27 00:31:58";
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf1.parse(d);
        System.out.println(t.overtime(date, 135));
//              System.out.println(t.isWorkday(date));;
//              System.out.println(t.isWorkday(calendar.getTime()));;


//        System.out.println(t.countPlayday(new Date(), calendar.getTime()));


    }
}


package com.huang.job.producer;

import cn.hutool.core.io.FileUtil;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author : Huang GuoJun
 * @since : 2023-07-11
 */
public class DealApollo {

    static final Pattern pattern = Pattern.compile("//(\\d+\\.\\d+\\.\\d+\\.\\d+)");

    public static void main(String[] args) {
        String s = "dzg-bills-kafka-es,\n" +
                "dzg-finance,\n" +
                "dzg-finance-job,\n" +
                "galaxy-advance-payment,\n" +
                "galaxy-basic,\n" +
                "galaxy-basic-cache,\n" +
                "galaxy-basic-executor,\n" +
                "galaxy-event-job,\n" +
                "personnel-platform,\n" +
                "galaxy-basic,\n" +
                "galaxy-quote-config,\n" +
                "galaxy-quote-job,\n" +
                "galaxy-basic,\n" +
                "galaxy-basic";
        String old = "galaxy-clear-task,\n" +
                "galaxy-clear-task-master,\n" +
                "galaxy-disp-bills-job,\n" +
                "galaxy-bills-job,\n" +
                "galaxy-send-canal-broker,\n" +
                "galaxy-quote-config,\n" +
                "chq-basic,\n" +
                "galaxy-send-bills,\n" +
                "dzg-app-job,\n" +
                "galaxy-disp-goods-payment-broker,\n" +
                "dzg-send-bills-broker,\n" +
                "galaxy-bills-send-kafka2es,\n" +
                "galaxy-send-bills-broker,\n" +
                "galaxy-bills-send-task,\n" +
                "galaxy-bills-mq2es,\n" +
                "galaxy-signdispfee-broker,\n" +
                "galaxy-change-record,\n" +
                "galaxy-data-process-core,\n" +
                "galaxy-quote-avg-task,\n" +
                "galaxy-disp-broker,\n" +
                "galaxy-disp-bills-kafka2es,\n" +
                "galaxy-quote-calculate,\n" +
                "galaxy-disp-bills";
        Set<String> strings = filterAppId(s);
        Set<String> strings1 = filterAppId(old);
        strings.removeAll(strings1);
        for (String string : strings) {
            System.out.println(string);
        }

    }



    public static Set<String> filterAppId(String s){
        Set<String> set  = new HashSet<>();
        for (String s1 : s.split(",")) {
            String trim = s1.trim();
            set.add(trim);
        }
        return set;
    }





    /**
     * 修改apollo配置
     */
    public static void writeApolloConfig(){
        Map<String, String> dbMap = initMap();
        // 读取配置文件
        File file = FileUtil.file("C:\\Users\\86199\\Desktop\\工作\\wdgj-appid");
        for (File listFile : file.listFiles()) {
            String name = listFile.getName();
            // 跳过这两个非配置文件
            if ("shell.sh".equals(name) || "total.txt".equals(name)) {
                continue;
            }
            // 读取文件
            List<String> stringList = readFile(listFile);
            List<String> replacedTxt = new ArrayList<>();
            boolean changed = false;
            for (String s : stringList) {
                // 需要处理的
                String needDeal = isNeedDeal(s, dbMap);
                if (!s.equals(needDeal)){
                    changed = true;
                }
                replacedTxt.add(needDeal);
            }
            if (!changed){
                System.out.println(name);
            }
            FileUtil.writeLines(replacedTxt,"C:\\Users\\86199\\Desktop\\工作\\newApolloConfig\\"+name,StandardCharsets.UTF_8);
        }
    }


    public static List<String> readFile(File txt) {
        return FileUtil.readLines(txt, StandardCharsets.UTF_8);
    }



    public static String isNeedDeal(String line,Map<String, String> dbMap) {
        Set<Map.Entry<String, String>> entries = dbMap.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            String key = entry.getKey();
            if (line.contains(key) && !line.startsWith("#")){
                // 获取原来的ip地址
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    String ipAddress = matcher.group(1);
                    if (ipAddress.contains("10.131")){
                        return line.replace(ipAddress, entry.getValue());
                    }
                }
            }
        }
        return line;
    }

    /**
     * 数据库对应的二号机房的数据库地址
     * @return
     */
    private static Map<String, String> initMap() {
        Map<String, String> dbs = new HashMap<>();
        dbs.put("galaxy_basic", "10.181.54.21:3401");
        dbs.put("galaxy_disp_bills1", "10.181.54.21:3401");
        dbs.put("galaxy_quote", "10.181.54.21:3401");
        dbs.put("ydserver", "10.181.54.21:3401");
        dbs.put("galaxy_disp_bills2", "10.181.54.21:3402");
        dbs.put("galaxy_disp_bills3", "10.181.54.21:3404");
        dbs.put("galaxy_disp_bills4", "10.181.54.21:3406");
        dbs.put("galaxy_disp_bills5", "10.181.53.19:3401");
        dbs.put("galaxy_disp_bills6", "10.181.53.19:3402");
        dbs.put("galaxy_disp_bills7", "10.181.54.19:3401");
        dbs.put("galaxy_disp_bills8", "10.181.60.19:3402");
        dbs.put("galaxy_send_bills_0", "10.181.63.19:3406");
        dbs.put("galaxy_send_bills_1", "10.181.53.12:3403");
        dbs.put("galaxy_send_bills_3", "10.181.53.12:3404");
        dbs.put("galaxy_send_bills_5", "10.181.53.12:3405");
        dbs.put("galaxy_send_bills_7", "10.181.53.12:3406");
        dbs.put("galaxy_send_bills_2", "10.181.54.19:3403");
        dbs.put("galaxy_send_bills_4", "10.181.54.19:3404");
        dbs.put("galaxy_send_bills_6", "10.181.54.19:3405");
        dbs.put("galaxy_send_bills_8", "10.181.54.19:3406");
        return dbs;
    }


}

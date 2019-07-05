package org.code4everything.wetool.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.system.SystemUtil;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.code4everything.boot.base.constant.IntegerConsts;
import org.code4everything.wetool.Config.WeConfig;
import org.code4everything.wetool.factory.BeanFactory;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * @author pantao
 * @since 2018/3/31
 */
@Slf4j
@UtilityClass
public class WeUtils {

    private static final String TIME_VARIABLE = "%(TIME|time)%";

    private static final String DATE_VARIABLE = "%(DATE|date)%";

    public static boolean isWindows() {
        return SystemUtil.getOsInfo().getName().startsWith("Window");
    }

    public static void addFiles(List<File> src, List<File> adds) {
        if (CollUtil.isEmpty(adds)) {
            return;
        }
        WeConfig config = BeanFactory.get(WeConfig.class);
        for (File file : adds) {
            if (!config.getFileFilter().getFilterPattern().matcher(file.getName()).matches()) {
                // 文件不匹配
                continue;
            }
            if (file.isFile() && !src.contains(file)) {
                src.add(file);
            } else if (file.isDirectory()) {
                addFiles(src, CollUtil.newArrayList(file.listFiles()));
            }
        }
    }

    public static String parseFolder(File file) {
        return file.isDirectory() ? file.getAbsolutePath() : file.getParent();
    }

    public static String replaceVariable(String str) {
        str = StrUtil.nullToEmpty(str);
        if (StrUtil.isNotEmpty(str)) {
            Date date = new Date();
            str = str.replaceAll(DATE_VARIABLE, DateUtil.formatDate(date));
            str = str.replaceAll(TIME_VARIABLE, DateUtil.formatTime(date));
        }
        return str;
    }

    public static int parseInt(String num, int minVal) {
        int n = 0;
        if (NumberUtil.isNumber(num)) {
            n = NumberUtil.parseInt(num);
        }
        return Math.max(n, minVal);
    }

    public static void exitSystem() {
        log.info("quit application......");
        System.exit(IntegerConsts.ZERO);
    }
}
package xin.awell.filesync.util;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;

public class Format {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    private static final NumberFormat numberFormat = NumberFormat.getNumberInstance();

    static {
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(0);
    }

    public static String formatFileSize(long size){
        long t = 1<<10;

        if(size < 1024)
            return size + "B";

        if((size / t) < 1024)
            return numberFormat.format((Double.valueOf(size) / t )) + "KB";

        t <<= 10;

        if ((size / t) < 1024)
            return numberFormat.format((Double.valueOf(size) / t )) + "MB";

        size/= 1024;
        if((size / t) < 1024)
            return numberFormat.format((Double.valueOf(size) / t )) + "GB";
        else
            return size + "";
    }

    public static String formatFileModifiedTime(long time){
        return dateFormat.format(time);
    }
}

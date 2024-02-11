package net.pmkjun.planetskilltimer.util;

public class Timeformat {
    public static String getString(long ms){
        long minute, second;
        if(ms >= 60000){
            minute = ms / 60000;
            second = (ms - (minute * 60000)) / 1000;
            return String.format("%d:%02d",minute,second);
        }
        else{
            return String.format("%.1f",(ms/(double)1000));
        }
    }
}

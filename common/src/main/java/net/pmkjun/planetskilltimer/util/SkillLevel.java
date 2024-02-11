package net.pmkjun.planetskilltimer.util;

public class SkillLevel {
    private static final int[][] skillTimetable = {
            {15000, 6430, 150000, 21430}, //농사 : 재보충
            {15000, 1500, 200000, 2000}, //벌목 : 벌목기
            {5000, 5000, 200000, 5000}, //채광 : 빠른채광
            {5000, 1000, 300000, 5000} //발굴 : 지형수정
            //ms
    };
    public static int getActivateTime(int skilltype, int skilllevel){
        if(skilllevel / 7 == 0) return 0;
        if(skilllevel > 97) skilllevel = 97;
        if(skilltype == 0 && skilllevel > 56) skilllevel = 56;
        return skillTimetable[skilltype][0] + (skilllevel / 7 - 1) * skillTimetable[skilltype][1];
    }
    public static int getCooldownTime(int skilltype, int skilllevel){
        if(skilllevel / 7 == 0) return 0;
        if(skilllevel > 97) skilllevel = 97;
        if(skilltype == 0 && skilllevel > 56) skilllevel = 56;
        return skillTimetable[skilltype][2] - (skilllevel / 7 - 1) * skillTimetable[skilltype][3];
    }
}
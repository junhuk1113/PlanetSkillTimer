package net.pmkjun.planetskilltimer.file;

import java.io.Serializable;

public class Data implements Serializable {
    public long[] lastSkillTime = new long[4];
    public boolean toggleSkilltimer = true;
    public boolean[] toggleSkills = { true, true, true, true };;
    public int SkillTimerXpos = 0;
    public int SkillTimerYpos = 1000;

    public boolean toggleAlertSound = true;
}

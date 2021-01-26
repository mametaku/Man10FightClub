package red.man10;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import red.man10.fightclub.FightClub;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static red.man10.fightclub.FightClub.autoRunnerAvailable;

public class MFCAutoRunner {
    public JavaPlugin plugin;

    public MFCAutoRunner(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public static boolean isEnabled = false;

    public static List<String> runnerSchedules = new ArrayList<>();

    FightClub.MFCModes mode = FightClub.MFCModes.Off;

    private void startFree() {
        Bukkit.getServer().broadcastMessage("§e============== §d●§f●§a●§e　Man10 Fight Club　§d●§f●§a● §e===============");
        Bukkit.getServer().broadcastMessage("§b§l§nMFCの練習試合を開催します！ ぜひご参加ください！");
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mfc free");
    }

    private void startNormal() {
        Bukkit.getServer().broadcastMessage("§e============== §d●§f●§a●§e　Man10 Fight Club　§d●§f●§a● §e===============");
        Bukkit.getServer().broadcastMessage("§c§l§kXXXXX §r§c§l§n今宵はMFCの決戦だ！ 集え！戦士たちよ！ §c§l§kXXXXX");
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mfc on");
    }

    private void stopMFC() {
        Bukkit.getServer().broadcastMessage("§e============== §d●§f●§a●§e　Man10 Fight Club　§d●§f●§a● §e===============");
        Bukkit.getServer().broadcastMessage("§l今日はここまで！ また次回参加してくれ！");
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mfc off");
    }

    private int isMfcTime() {
        String[] arrayedSchedule;
        for (String runnerSchedule : runnerSchedules) {
            arrayedSchedule = runnerSchedule.split(":");
            int nowDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
            int nowHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            int nowMinute = Calendar.getInstance().get(Calendar.MINUTE);
            try {
                if (Integer.parseInt(arrayedSchedule[1]) == nowHour) {
                    if (Integer.parseInt(arrayedSchedule[0]) == nowDay) {
                        int endTime = Integer.parseInt(arrayedSchedule[2]);
                        int delay = endTime * 60 - nowHour * 60 + nowMinute;
                        if (delay < 0) {
                            return -1;
                        }
                        return delay;
                    }
                }
            } catch (NumberFormatException e) {
                return -1;
            }
        }
        return -1;
    }

    public void enableAutoRunner() {
        if (!isEnabled) return;

        Calendar cal = Calendar.getInstance();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                while (autoRunnerAvailable) {
                    Thread.sleep(1000 * 60 * 10);
                    int time = isMfcTime();
                    if (time == -1) return; // 例外 or 今じゃない
                    if (FightClub.mode == FightClub.MFCModes.Off) { // MFCがOffか確認
                        startFree();
                        Thread.sleep(time * 60);
                        while (true) {
                            if (FightClub.currentStatus == FightClub.Status.Entry) { // 試合をやっているか確認
                                stopMFC();
                                return;
                            } else {
                                Thread.sleep(1000 * 10);
                            }
                        }
                    } else {
                        Thread.sleep(1000 * 60 * 60); // もし18時なってMFCが開催されていたらその日はもう開催しない
                    }
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        });
    }
}
package red.man10;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import red.man10.fightclub.FightClub;

import java.util.Calendar;

import static red.man10.fightclub.FightClub.autoRunnerAvailable;

public class MFCAutoRunner {
    public JavaPlugin plugin;

    public MFCAutoRunner(JavaPlugin plugin){
        this.plugin = plugin;
    }

    private boolean isEnabled = false;

    private void startFree() {
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mfc free");
    }

    private void startNormal() {
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mfc on");
    }

    private void stopMFC() {
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mfc off");
    }

    public void enableAutoRunner() {
        if (!isEnabled) return;

        Calendar cal = Calendar.getInstance();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            while (autoRunnerAvailable) {
                try {
                    Thread.sleep(1000);
                    if (cal.get(Calendar.HOUR_OF_DAY) == 18) { //18時のとき
                        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) { // 土曜日のとき
                            if (FightClub.mode == FightClub.MFCModes.Off) { //MFCがOffのとき
                                startFree();
                                Thread.sleep(1000 * 60 * 60); //1時間開催
                                while (true) {
                                    if (FightClub.currentStatus == FightClub.Status.Entry) { // 試合をやっているかどうか確認
                                        stopMFC();
                                        return;
                                    }else{
                                        Thread.sleep(1000);
                                    }
                                }
                            } else {
                                Thread.sleep(1000 * 60 * 60); // もし18時なってMFCが開催されていたらその日はもう開催しない
                            }
                        } else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) { // 日曜日のとき
                            if (FightClub.mode == FightClub.MFCModes.Off) { //MFCがOffのとき
                                startNormal();
                                Thread.sleep(1000 * 60 * 60 * 2); //2時間開催
                                while (true) {
                                    if (FightClub.currentStatus == FightClub.Status.Entry) { // 試合をやっているかどうか確認
                                        stopMFC();
                                        return;
                                    }else{
                                        Thread.sleep(1000);
                                    }
                                }
                            } else {
                                Thread.sleep(1000 * 60 * 60); // もし18時なってMFCが開催されていたらその日はもう開催しない
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

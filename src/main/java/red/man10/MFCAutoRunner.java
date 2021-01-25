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

    public static boolean isEnabled = false;

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

    public void enableAutoRunner() {
        if (!isEnabled) return;

        Calendar cal = Calendar.getInstance();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            while (autoRunnerAvailable) {
                try {
                    Thread.sleep(1000 * 60 * 10); // 10分ごとに確認
                    if (cal.get(Calendar.HOUR_OF_DAY) == 18) { //18時か確認
                        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) { // 土曜日か確認
                            if (FightClub.mode == FightClub.MFCModes.Off) { // MFCがOffか確認
                                startFree();
                                while (true) {
                                    if (cal.get(Calendar.HOUR_OF_DAY) == 19) { // 19時か確認 （Free終了時間）
                                        while (true) {
                                            if (FightClub.currentStatus == FightClub.Status.Entry) { // 試合をやっているか確認
                                                stopMFC();
                                                return;
                                            } else {
                                                Thread.sleep(1000 * 10);
                                            }
                                        }
                                    }else{
                                        Thread.sleep(1000 * 60 * 10);
                                    }
                                }
                            } else {
                                Thread.sleep(1000 * 60 * 60); // もし18時なってMFCが開催されていたらその日はもう開催しない
                            }
                        } else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) { // 日曜日か確認
                            if (FightClub.mode == FightClub.MFCModes.Off) { // MFCがOffか確認
                                startNormal();
                                while (true) {
                                    if (cal.get(Calendar.HOUR_OF_DAY) == 20) { // 20時か確認 （Normal終了時間）
                                        while (true) {
                                            if (FightClub.currentStatus == FightClub.Status.Entry) { // 試合をやっているか確認
                                                stopMFC();
                                                return;
                                            } else {
                                                Thread.sleep(1000 * 10);
                                            }
                                        }
                                    }else{
                                        Thread.sleep(1000 * 60 * 10);
                                    }
                                }
                            } else {
                                Thread.sleep(1000 * 60 * 60 * 2); // もし18時なってMFCが開催されていたらその日はもう開催しない
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

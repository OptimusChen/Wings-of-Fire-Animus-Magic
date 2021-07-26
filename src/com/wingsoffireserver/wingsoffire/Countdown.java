package com.wingsoffireserver.wingsoffire;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;

public class Countdown implements Runnable {

    private JavaPlugin plugin;

    private Integer assignedTaskId;

    private int ticks;
    private int ticksLeft;

    private Consumer<Countdown> everyTick;
    private Runnable beforeTimer;
    private Runnable afterTimer;

    public Countdown(JavaPlugin plugin, int ticks,
                          Runnable beforeTimer, Runnable afterTimer,
                          Consumer<Countdown> everyTick) {
        this.plugin = plugin;
        this.ticks = ticks;
        this.ticksLeft = ticks;
        this.beforeTimer = beforeTimer;
        this.afterTimer = afterTimer;
        this.everyTick = everyTick;
    }

    @Override
    public void run() {
        // After the timer expires
        if (ticksLeft < 1) {
            if (afterTimer != null){
                afterTimer.run();
            }

            if (assignedTaskId != null) {
                Bukkit.getScheduler().cancelTask(assignedTaskId);
            }

            return;
        }

        // when the timer starts
        if (ticksLeft == ticks) {
            if (beforeTimer != null) {
                beforeTimer.run();
            }
        }

        // call every Tick
        everyTick.accept(this);

        // decrement the ticksLeft
        ticksLeft --;
    }

    public int getTotalTicks() {
        return ticks;
    }

    public int getIicksLeft() {
        return ticksLeft;
    }

    public int getTicksPassed() {
        return ticks - ticksLeft;
    }

    public void scheduleTimer() {
        this.assignedTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 0, 1L);
    }

}

package io.github.namiuni.osakana.minecraft.paper.utility;

import com.google.inject.Inject;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class OsakanaScheduler {

    private final Plugin plugin;

    @Inject
    private OsakanaScheduler(final Plugin plugin) {
        this.plugin = plugin;
    }

    public BukkitTask runTask(final Runnable task) {
        return Bukkit.getScheduler().runTask(this.plugin, task);
    }
}

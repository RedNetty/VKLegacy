package me.kayaba.guilds.enums;

import me.kayaba.guilds.manager.*;

import java.util.*;

public enum Dependency {
    VAULT("Vault", true),
    VANISHNOPACKET("VanishNoPacket", false),
    ESSENTIALS("Essentials", false),
    BOSSBARAPI("BossBarAPI", false),
    BARAPI("BarAPI", false),
    SCOREBOARDSTATS("ScoreboardStats", false),
    WORLDGUARD("WorldGuard", false, new DependencyManager.WorldGuardFlagInjector()),
    HOLOGRAPHICDISPLAYS("HolographicDisplays", false, new DependencyManager.HolographicDisplaysAPIChecker());

    private final String name;
    private final boolean hardDependency;
    private final Set<DependencyManager.AdditionalTask> additionalTasks = new HashSet<>();


    Dependency(String name, boolean hardDependency) {
        this.name = name;
        this.hardDependency = hardDependency;
    }


    Dependency(String name, boolean hardDependency, DependencyManager.AdditionalTask... additionalTasks) {
        this(name, hardDependency);
        Collections.addAll(this.additionalTasks, additionalTasks);
    }


    public String getName() {
        return name;
    }


    public boolean isHardDependency() {
        return hardDependency;
    }


    public boolean hasAdditionalTasks() {
        return !additionalTasks.isEmpty();
    }


    public Set<DependencyManager.AdditionalTask> getAdditionalTasks() {
        return additionalTasks;
    }
}

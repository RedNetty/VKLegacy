package me.kayaba.guilds.manager;

import com.sk89q.worldguard.protection.flags.*;
import me.bpweber.practiceserver.*;
import me.kayaba.guilds.api.util.reflect.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.exception.*;
import me.kayaba.guilds.util.*;
import me.kayaba.guilds.util.reflect.*;
import org.bukkit.plugin.*;

import java.util.*;

public class DependencyManager {
    private static final PracticeServer plugin = PracticeServer.getInstance();
    private final Map<Dependency, Plugin> pluginMap = new HashMap<>();

    public void setUp() throws FatalKayabaGuildsException {
        try {
            checkDependencies();
        } catch (MissingDependencyException e) {
            throw new FatalKayabaGuildsException("Could not satisfy dependencies", e);
        }
    }


    public void checkDependencies() throws MissingDependencyException {
        pluginMap.clear();

        for (Dependency dependency : Dependency.values()) {
            Plugin plugin = getPlugin(dependency.getName());

            if (plugin != null) {
                pluginMap.put(dependency, plugin);
                LoggerUtils.info("Found plugin " + dependency.getName());

                if (dependency.hasAdditionalTasks()) {
                    for (AdditionalTask additionalTask : dependency.getAdditionalTasks()) {
                        try {
                            LoggerUtils.info("Running additional task '" + additionalTask.getClass().getSimpleName() + "' for " + dependency.getName());
                            additionalTask.run();
                            additionalTask.onSuccess();
                        } catch (Exception e) {
                            additionalTask.onFail();
                            AdditionalTaskException taskException = new AdditionalTaskException("Could not pass additional task '" + additionalTask.getClass().getSimpleName() + "' for " + dependency.getName(), e);

                            if (!additionalTask.isFatal()) {
                                LoggerUtils.exception(taskException);
                                continue;
                            }

                            throw new MissingDependencyException("Invalid dependency " + dependency.getName(), taskException);
                        }
                    }
                }
            } else {
                if (dependency.isHardDependency()) {
                    throw new MissingDependencyException("Missing dependency " + dependency.getName());
                } else {
                    LoggerUtils.info("Could not find plugin: " + dependency.getName() + ", disabling certain features");
                }
            }
        }


        Config.BOSSBAR_ENABLED.set(Config.BOSSBAR_ENABLED.getBoolean() && (ConfigManager.getServerVersion().isNewerThan(ConfigManager.ServerVersion.MINECRAFT_1_8_R2) || plugin.getDependencyManager().isEnabled(Dependency.BARAPI) || plugin.getDependencyManager().isEnabled(Dependency.BOSSBARAPI)));
        Config.BOSSBAR_RAIDBAR_ENABLED.set(Config.BOSSBAR_RAIDBAR_ENABLED.getBoolean() && Config.BOSSBAR_ENABLED.getBoolean());
        Config.HOLOGRAPHICDISPLAYS_ENABLED.set(Config.HOLOGRAPHICDISPLAYS_ENABLED.getBoolean() && plugin.getDependencyManager().isEnabled(Dependency.HOLOGRAPHICDISPLAYS));
    }


    public boolean isEnabled(Dependency dependency) {
        return pluginMap.containsKey(dependency);
    }


    private Plugin getPlugin(String name) {
        return ListenerManager.getLoggedPluginManager().getPlugin(name);
    }


    @SuppressWarnings("unchecked")
    public <T extends Plugin> T get(Dependency dependency, Class<T> cast) {
        return (T) pluginMap.get(dependency);
    }

    public static class HolographicDisplaysAPIChecker extends AdditionalTask {

        public HolographicDisplaysAPIChecker() {
            super(true);
        }

        @Override
        public void run() throws ClassNotFoundException {
            Reflections.getClass("com.gmail.filoghost.holographicdisplays.api.HologramsAPI");
        }
    }

    public static class WorldGuardFlagInjector extends AdditionalTask {

        public WorldGuardFlagInjector() {
            super(false);
        }

        @Override
        public void run() throws Exception {
            if (!Config.REGION_WORLDGUARD.getBoolean()) {
                LoggerUtils.info("Skipping WorldGuardFlag Injector. Disabled in config");
                return;
            }

            plugin.getRegionManager().createWorldGuardFlag();
            FieldAccessor<Flag[]> defaultFlagFlagListField = Reflections.getField(DefaultFlag.class, "flagsList", Flag[].class);
            defaultFlagFlagListField.setNotFinal();
            Flag[] array = defaultFlagFlagListField.get(null);
            List<Flag> list = new ArrayList<>();
            Collections.addAll(list, array);
            list.add((StateFlag) RegionManager.WORLDGUARD_FLAG);
            defaultFlagFlagListField.set(list.toArray(new Flag[list.size()]));
            LoggerUtils.info("Successfully injected WorldGuard Flag");
        }

        @Override
        public void onFail() {
            Config.REGION_WORLDGUARD.set(false);
            LoggerUtils.info("WorldGuard region checking disabled due to additional task failure.");
        }
    }

    public interface RunnableWithException {

        void run() throws Exception;
    }

    public static abstract class AdditionalTask implements RunnableWithException {
        private final boolean fatal;


        public AdditionalTask(boolean fatal) {
            this.fatal = fatal;
        }


        public boolean isFatal() {
            return fatal;
        }


        public void onFail() {

        }


        public void onSuccess() {

        }
    }
}

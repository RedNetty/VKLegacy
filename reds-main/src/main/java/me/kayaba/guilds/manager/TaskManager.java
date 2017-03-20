package me.kayaba.guilds.manager;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.runnable.*;
import me.kayaba.guilds.util.*;

import java.util.*;
import java.util.concurrent.*;

public class TaskManager {
    private static final ScheduledExecutorService worker = Executors.newSingleThreadScheduledExecutor();
    private final Map<Task, ScheduledFuture<?>> taskRunnableMap = new HashMap<>();

    public enum Task {
        AUTOSAVE(null, RunnableAutoSave.class, Config.SAVEINTERVAL),
        LIVEREGENERATION(null, RunnableLiveRegeneration.class, Config.LIVEREGENERATION_TASKINTERVAL),
        CLEANUP(Config.CLEANUP_ENABLED, RunnableInactiveCleaner.class, Config.CLEANUP_STARTUPDELAY, Config.CLEANUP_INTERVAL),
        TABLIST_REFRESH(Config.TABLIST_ENABLED, RunnableRefreshTabList.class, Config.TABLIST_REFRESH);

        private final ConfigWrapper start;
        private final ConfigWrapper interval;
        private final ConfigWrapper condition;
        private final Class clazz;


        Task(ConfigWrapper condition, Class<? extends Runnable> clazz, ConfigWrapper both) {
            this.clazz = clazz;
            this.start = both;
            this.interval = both;
            this.condition = condition;
        }


        Task(ConfigWrapper condition, Class<? extends Runnable> clazz, ConfigWrapper start, ConfigWrapper interval) {
            this.clazz = clazz;
            this.start = start;
            this.interval = interval;
            this.condition = condition;
        }


        public Class getClazz() {
            return clazz;
        }


        public long getStart() {
            return start.getSeconds();
        }


        public long getInterval() {
            return interval.getSeconds();
        }


        public boolean checkCondition() {
            return condition == null || condition.getBoolean();
        }
    }


    public void startTask(Task task) {
        if (isStarted(task)) {
            LoggerUtils.info("Task " + task.name() + " has been already started");
            return;
        }

        try {
            Runnable taskInstance = (Runnable) task.getClazz().newInstance();
            ScheduledFuture<?> future = worker.scheduleAtFixedRate(taskInstance, task.getStart(), task.getInterval(), TimeUnit.SECONDS);
            taskRunnableMap.put(task, future);
        } catch (InstantiationException | IllegalAccessException e) {
            LoggerUtils.exception(e);
        }
    }


    public void stopTask(Task task) {
        if (isStarted(task)) {
            taskRunnableMap.get(task).cancel(true);
            LoggerUtils.info("Task " + task.name() + " has been stopped");
        }
    }


    public void stopTasks() {
        for (Task task : taskRunnableMap.keySet()) {
            stopTask(task);
        }
    }


    public boolean isStarted(Task task) {
        return taskRunnableMap.containsKey(task) && !taskRunnableMap.get(task).isCancelled();
    }


    public void runTasks() {
        for (Task task : Task.values()) {
            boolean condition = task.checkCondition();

            if (condition && !isStarted(task)) {
                startTask(task);
                LoggerUtils.info("Task " + task.name() + " has been started");
            } else if (isStarted(task) && !condition) {
                stopTask(task);
                LoggerUtils.info("Task " + task.name() + " has been stopped");
            }
        }
    }
}

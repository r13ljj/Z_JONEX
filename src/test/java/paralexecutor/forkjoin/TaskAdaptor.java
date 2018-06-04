package paralexecutor.forkjoin;

import paralexecutor.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.RecursiveTask;

/**
 * <pre>
 *
 *  File: TaskAdaptor.java
 *
 *  Copyright (c) 2018, globalegrow.com All Rights Reserved.
 *
 *  Description:
 *  TODO
 *
 *  Revision History
 *  Date,					Who,					What;
 *  2018/6/1				lijunjun				Initial.
 *
 * </pre>
 */
public class TaskAdaptor extends RecursiveTask<Object> {

    private List<Task> tasks;

    public TaskAdaptor(List<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    protected Object compute() {
        if (tasks == null){
            return null;
        }
        if (tasks.size() == 1){
            return tasks.get(0).execute();
        }
        List<TaskAdaptor> adaptors = new ArrayList<>();
        for (Task task : tasks){
            TaskAdaptor adaptor = new TaskAdaptor(Arrays.asList(task));
            adaptors.add(adaptor);
        }
        List<Object> list = new ArrayList<>();
        for (TaskAdaptor a : adaptors){
            list.add(a.join());
        }
        return list;
    }
}

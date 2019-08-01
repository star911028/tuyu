package com.fengyuxing.tuyu.task2;

import java.util.Collection;
import java.util.LinkedList;

public class SyncSchduler<T> {
    private final LinkedList<T> queue = new LinkedList<>();
    public Callback<T> callback;

    public SyncSchduler(Callback<T> callback) {
        if (callback == null) {
            throw new RuntimeException("没事别JB实例化");
        }
        this.callback = callback;
    }

    public void next() {
        if (!queue.isEmpty()) {
            T data = queue.pop();
            callback.apply(data);
        }
    }

    public void add(T t) {
        queue.add(t);
    }

    public void addAll(Collection<T> t) {
        queue.addAll(t);
    }

    public interface Callback<T> {
        void apply(T data);
    }
}
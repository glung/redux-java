package com.redux.storage;

import com.redux.State;
import com.redux.draw.model.MyPath;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class StateTree implements State {

    public static StateTree create() {
        final LinkedList<MyPath> paths = new LinkedList<>();
        paths.add(MyPath.create());
        return new StateTree(paths);
    }

    private final LinkedList<MyPath> paths;

    private StateTree(LinkedList<MyPath> paths) {
        this.paths = paths;
    }

    public StateTree addLast(MyPath path) {
        final LinkedList<MyPath> newPathList = new LinkedList<>(paths);
        newPathList.addLast(path);
        return new StateTree(newPathList);
    }

    public StateTree removeLast() {
        final LinkedList<MyPath> newPathList = new LinkedList<>(paths);
        newPathList.removeLast();
        return new StateTree(newPathList);
    }

    public MyPath getLast() {
        return paths.getLast();
    }

    public List<MyPath> get() {
        return Collections.unmodifiableList(paths);
    }
}

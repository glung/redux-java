package com.redux.devtools;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

class DevToolStateStorage<A, S> {

    private static final String TIMESTAMP = "timestamp";
    private static final String COMMITTED_STATE = "committedState";
    private static final String STAGED_ACTIONS = "stagedActions";
    private static final String SKIPPED_ACTION_INDEXES = "skippedActionIndexes";
    private static final String COMPUTED_STATES = "computedStates";
    private static final String STARTED_AT = "startedAt";
    private static final String FINISHED_AT = "finishedAt";
    private static final String CURRENT_STATE_INDEX = "currentStateIndex";
    private static final Type TYPE_LIST_LONG = new TypeToken<ArrayList<Long>>() {
    }.getType();
    private static final Type TYPE_LIST_INTEGER = new TypeToken<ArrayList<Integer>>() {
    }.getType();

    private final File fileStorage;
    private final Converter<A> actionConverter;
    private final Converter<S> stateConverter;

    public DevToolStateStorage(File fileStorage, Converter<A> actionConverter, Converter<S> stateConverter) {
        this.fileStorage = fileStorage;
        this.actionConverter = actionConverter;
        this.stateConverter = stateConverter;
    }

    void store(DevToolState<A, S> state) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(fileStorage);
            store(state, writer);
        } catch (IOException | JsonIOException e) {
            e.printStackTrace();
        } finally {
            closeQuietly(writer);
        }
    }

   DevToolState<A, S> load() {
        DevToolState<A, S> devToolState = null;
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(fileStorage);
            devToolState = load(fileReader);
        } catch (FileNotFoundException | JsonIOException | JsonSyntaxException | MandatoryJsonElementMissingException e) {
            fileStorage.delete();
            e.printStackTrace();
        } finally {
            closeQuietly(fileReader);
        }
        return devToolState;
    }

    private void store(DevToolState<A, S> state, FileWriter writer) {
        final Gson gson = new Gson();
        final JsonObject root = new JsonObject();
        root.add(TIMESTAMP, gson.toJsonTree(state.timestamp));
        root.add(COMMITTED_STATE, serializeState(state.committedState));
        root.add(STAGED_ACTIONS, parseActions(state.stagedActions));
        root.add(SKIPPED_ACTION_INDEXES, gson.toJsonTree(state.skippedActionIndexes));
        root.add(COMPUTED_STATES, serializeStates(state.computedStates));
        root.add(STARTED_AT, gson.toJsonTree(state.startedAt));
        root.add(FINISHED_AT, gson.toJsonTree(state.finishedAt));
        root.add(CURRENT_STATE_INDEX, gson.toJsonTree(state.currentStateIndex, Integer.class));
        gson.toJson(root, writer);
    }

    private DevToolState<A, S> load(FileReader fileReader) {
        final JsonObject root = (JsonObject) new JsonParser().parse(fileReader);
        final Gson gson = new Gson();
        return new DevToolState<>(
                getAsElement(root, TIMESTAMP).getAsLong(),
                parseState(getAsElement(root, COMMITTED_STATE)),
                parseActions(getAsJsonArray(root, STAGED_ACTIONS)),
                gson.<List<Integer>>fromJson(getAsJsonArray(root, SKIPPED_ACTION_INDEXES), TYPE_LIST_INTEGER),
                parseStates(getAsJsonArray(root, COMPUTED_STATES)),
                gson.<List<Long>>fromJson(getAsJsonArray(root, STARTED_AT), TYPE_LIST_LONG),
                gson.<List<Long>>fromJson(getAsJsonArray(root, FINISHED_AT), TYPE_LIST_LONG),
                getAsElement(root, CURRENT_STATE_INDEX).getAsInt()
        );
    }

    private JsonArray getAsJsonArray(JsonObject root, String key) {
        final JsonArray jsonArray = root.getAsJsonArray(key);
        if (jsonArray == null) {
            throw new MandatoryJsonElementMissingException(key);
        }
        return jsonArray;
    }

    private JsonElement getAsElement(JsonObject root, String key) {
        final JsonElement jsonElement = root.get(key);
        if (jsonElement == null) {
            throw new MandatoryJsonElementMissingException(key);
        }
        return jsonElement;
    }

    private JsonElement serializeStates(List<S> states) {
        final JsonArray array = new JsonArray();
        for (S state : states) {
            array.add(serializeState(state));
        }
        return array;
    }

    private JsonElement serializeState(S state) {
        return new JsonParser().parse(stateConverter.toJson(state));
    }

    private JsonElement parseActions(List<A> actions) {
        final JsonArray array = new JsonArray();
        for (A action : actions) {
            array.add(serializeAction(action));
        }
        return array;
    }

    private JsonElement serializeAction(A action) {
        return new JsonParser().parse(actionConverter.toJson(action));
    }

    private List<S> parseStates(JsonArray asJsonArray) {
        final ArrayList<S> states = new ArrayList<>();
        for (JsonElement jsonElement : asJsonArray) {
            states.add(parseState(jsonElement));
        }
        return states;
    }

    private List<A> parseActions(JsonArray asJsonArray) {
        final ArrayList<A> actions = new ArrayList<>();
        for (JsonElement jsonElement : asJsonArray) {
            actions.add(actionConverter.fromJson(jsonElement.toString()));
        }
        return actions;
    }

    private S parseState(JsonElement asJsonObject) {
        return stateConverter.fromJson(asJsonObject.toString());
    }

    private void closeQuietly(Closeable closeable) {
        if (closeable == null) return;
        try {
            closeable.close();
        } catch (IOException ignored) {
        }
    }

    private static class MandatoryJsonElementMissingException extends RuntimeException {
        public MandatoryJsonElementMissingException(String jsonElement) {
            super(jsonElement + " is missing");
        }
    }
}

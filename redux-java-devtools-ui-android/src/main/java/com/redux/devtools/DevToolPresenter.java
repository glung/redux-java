package com.redux.devtools;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.redux_java_devtool_ui_android.R;
import com.redux.Store;
import com.redux.Subscriber;

public class DevToolPresenter<A, S> {
    private final Store<DevToolAction<A>, DevToolState<A, S>> devStore;

    private final OnClickListener onClickReset = new OnClickListener() {
        @Override public void onClick(View view) {
            devStore.dispatch(DevToolAction.<A>forReset());
        }
    };

    private final OnClickListener onClickCommit = new OnClickListener() {
        @Override public void onClick(View view) {
            devStore.dispatch(DevToolAction.<A>forCommit());
        }
    };

    private final OnClickListener onClickRollback = new OnClickListener() {
        @Override public void onClick(View view) {
            devStore.dispatch(DevToolAction.<A>forRollback());
        }
    };

    private final OnClickListener onClickJumToState = new OnClickListener() {
        @Override public void onClick(View view) {
            final int position = recyclerView.getChildAdapterPosition(view);
            devStore.dispatch(DevToolAction.<A>forJumToState(position));
        }
    };

    private final OnClickListener onClickEnable = new OnClickListener() {
        @Override public void onClick(View view) {
            final CheckBox checkBox = (CheckBox) view.findViewById(R.id.dev_action_toggle);
            final int posittion = (int) checkBox.getTag();
            devStore.dispatch(DevToolAction.<A>forEnable(posittion, checkBox.isChecked()));
        }
    };

    private RecyclerView recyclerView;


    public DevToolPresenter(Store<DevToolAction<A>, DevToolState<A, S>> devStore) {
        this.devStore = devStore;
    }

    public void bind(View view) {
        view.findViewById(R.id.dev_reset).setOnClickListener(onClickReset);
        view.findViewById(R.id.dev_commit).setOnClickListener(onClickCommit);
        view.findViewById(R.id.dev_roll_back).setOnClickListener(onClickRollback);
        final Adapter<A, S> adapter = new Adapter<>(devStore.getState(), onClickEnable, onClickJumToState);

        recyclerView = (RecyclerView) view.findViewById(R.id.dev_actions);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        devStore.subscribe(new Subscriber() {
            @Override public void onStateChanged() {
                adapter.setState(devStore.getState());
                adapter.notifyDataSetChanged();
            }
        });
    }

    private static class Adapter<A, S> extends RecyclerView.Adapter<ViewHolder> {
        private final OnClickListener onClickCheckBox;
        private final OnClickListener onClickJumToState;
        private final ColorBuilder colorBuilder;
        private DevToolState<A, S> state;

        private Adapter(DevToolState<A, S> state,
                        OnClickListener onClickCheckBox,
                        OnClickListener onClickJumToState) {
            this.colorBuilder = new ColorBuilder();
            this.state = state;
            this.onClickCheckBox = onClickCheckBox;
            this.onClickJumToState = onClickJumToState;
        }

        @Override public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_action, viewGroup, false));
        }

        @Override public void onBindViewHolder(ViewHolder viewHolder, int position) {
            final long latency = state.finishedAt.get(position) - state.startedAt.get(position);

            final View parent = viewHolder.itemView;
            parent.setOnClickListener(onClickJumToState);

            final A action = state.stagedActions.get(position);
            final TextView actionView = (TextView) parent.findViewById(R.id.dev_action);
            actionView.setText(action.toString());

            final TextView latencyView = (TextView) parent.findViewById(R.id.dev_latency);
            latencyView.setBackgroundColor(colorBuilder.forValue(latency, 50));
            latencyView.setText(latency + " ms");

            final CheckBox enabledView = ((CheckBox) parent.findViewById(R.id.dev_action_toggle));
            final boolean isSkipped = state.skippedActionIndexes.contains(position);
            enabledView.setChecked(!isSkipped);
            enabledView.setOnClickListener(onClickCheckBox);
            enabledView.setTag(position);

            parent.setActivated(!isSkipped && position <= state.currentStateIndex);
            parent.setSelected(position == state.currentStateIndex);
        }

        @Override public int getItemCount() {
            return state.stagedActions.size();
        }

        public void setState(DevToolState<A, S> state) {
            this.state = state;
        }
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

    }

}

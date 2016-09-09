package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.copyyeeaootf.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yo on 2016/7/29.
 */
public class Fragment_In_6 extends Fragment {
    private Button button;
    private View view;
    private List<String> list = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab6, container, false);
        initView();
        return view;
    }

    private void initView() {
        button = (Button) view.findViewById(R.id.id_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select(v);
            }
        });
        for (int i = 0; i < 30; i++) {
            list.add("item" + (i + 1));
        }
    }

    private void select(View v) {
        RecyclerView recyclerView = (RecyclerView) LayoutInflater.from(getActivity()).inflate(R.layout.list_1, null);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        Adapter adapter = new Adapter();
        recyclerView.setAdapter(adapter);

        final BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        dialog.setContentView(recyclerView);
        dialog.show();

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position, String text) {
                Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    class Adapter extends RecyclerView.Adapter<Holder>{
        private OnItemClickListener mItemClickListener;
        public void setOnItemClickListener(OnItemClickListener li){
            mItemClickListener = li;
        }
        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.list, parent, false);
            return new Holder(item);
        }

        @Override
        public void onBindViewHolder(final Holder holder, int position) {
            holder.tv.setText(list.get(position));
            if (mItemClickListener != null){
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemClickListener.onItemClick(holder.getLayoutPosition(),
                                holder.tv.getText().toString());
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView tv;
        public Holder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.id_textView);
        }
    }

    interface OnItemClickListener{
        void onItemClick(int position, String text);
    }

}

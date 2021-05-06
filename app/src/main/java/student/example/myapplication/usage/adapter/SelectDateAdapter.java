package student.example.myapplication.usage.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import student.example.myapplication.R;

import java.util.ArrayList;

public class SelectDateAdapter extends RecyclerView.Adapter<SelectDateAdapter.SelectDateViewHolder>{

    private ArrayList<String> mDateList;

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    //define interface
    public  interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public SelectDateAdapter(ArrayList<String> dateList) {
        this.mDateList = dateList;
    }

    @Override
    public SelectDateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.selece_date_item, parent, false);
        SelectDateViewHolder holder = new SelectDateViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(SelectDateViewHolder holder, int position) {
        holder.tv_item_select_date.setText( mDateList.get(position));
        final int i = position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v,i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDateList.size();
    }

    public class SelectDateViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_item_select_date;

        public SelectDateViewHolder(View itemView) {
            super(itemView);
            tv_item_select_date = (TextView) itemView.findViewById(R.id.tv_item_select_date);
        }
    }
}

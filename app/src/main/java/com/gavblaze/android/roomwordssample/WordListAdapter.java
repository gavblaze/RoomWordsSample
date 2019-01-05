package com.gavblaze.android.roomwordssample;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.WordViewholder> {
    List<Word> mWordList;
    private OnItemClickListener mOnItemClickListener;


    public WordListAdapter(OnItemClickListener onItemClickListener) {
        this.mWordList = new ArrayList<>();
        this.mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onClicked(Word word);
    }

    @NonNull
    @Override
    public WordViewholder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        return new WordViewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewholder holder, int position) {
        holder.mText.setText(mWordList.get(position).getWord());
    }

    @Override
    public int getItemCount() {
        if (mWordList != null) {
            return mWordList.size();
        } else {
            return 0;
        }
    }

    public void swapData(List<Word> wordList) {
        mWordList = wordList;
        notifyDataSetChanged();
    }

    class WordViewholder extends RecyclerView.ViewHolder {
        TextView mText;
        WordViewholder(@NonNull final View itemView) {
            super(itemView);
            mText = itemView.findViewById(R.id.text_View);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Word wordAtPosition = mWordList.get(position);
                    mOnItemClickListener.onClicked(wordAtPosition);
                }
            });
        }
    }
}

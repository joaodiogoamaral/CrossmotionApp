package com.example.joao.crossmotion;

import android.content.Context;
import android.content.ContextWrapper;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FileItemAdapter extends RecyclerView.Adapter<FileItemAdapter.ViewHolder> {
    private static final String TAG = "FileItemAdapter";




    private static List<FileItem> mDataSet;

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;


        private boolean isChecked;

        public ViewHolder(View v, final Context context) {
            super(v);
            isChecked=false;

            // Define click listener for the ViewHolder's View.
            /*v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    int adapterPosition = getAdapterPosition();
                    if (isChecked==false) {
                        textView.setBackgroundColor(ContextCompat.getColor(context,R.color.blue_highlight));
                        textView.setTextColor(ContextCompat.getColor(context,R.color.white));
                        isChecked = true;

                    }
                    else  {

                        textView.setBackgroundColor(ContextCompat.getColor(context,R.color.white));
                        textView.setTextColor(ContextCompat.getColor(context,R.color.black_overlay));
                        isChecked = false;
                    }



                }
            });*/



            textView = v.findViewById(R.id.textView);
        }

        public TextView getTextView() {
            return this.textView;
        }

        public boolean isChecked()
        {
            return this.isChecked;
        }

        public void setChecked(boolean val)
        {
            this.isChecked=val;
        }






        /*public SparseBooleanArray getSelectedFiles()
        {
            return itemStateArray;
        }*/
    }
    // END_INCLUDE(recyclerViewSampleViewHolder)

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
     */
    public FileItemAdapter(List<FileItem> dataSet) {

        mDataSet = dataSet;


        /*
        for(int i=0;i<dataSet.size();i++)
        {
            //FileItem fI = new FileItem(dataSet[i]);
            //mDataSet.add(fI);
            Log.e(TAG,"dataset_i:"+dataSet.get(i).getFileName());
            Log.e(TAG,"ADDED:"+mDataSet.get(i).getFileName());
        }*/
    }






    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.file_text_view, viewGroup, false);
        final ViewHolder vh = new ViewHolder(v,viewGroup.getContext());

        vh.textView.setBackgroundColor(ContextCompat.getColor(viewGroup.getContext(),R.color.white));
        vh.textView.setTextColor(ContextCompat.getColor(viewGroup.getContext(),R.color.black_overlay));

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int adapterPosition = vh.getAdapterPosition();
                if (vh.isChecked==false) {
                    vh.textView.setBackgroundColor(ContextCompat.getColor(viewGroup.getChildAt(adapterPosition).getContext(),R.color.blue_highlight));
                    vh.textView.setTextColor(ContextCompat.getColor(viewGroup.getChildAt(adapterPosition).getContext(),R.color.white));
                    vh.setChecked(true);
                    mDataSet.get(adapterPosition).setSelected(true);

                }
                else  {

                    vh.textView.setBackgroundColor(ContextCompat.getColor(viewGroup.getChildAt(adapterPosition).getContext(),R.color.white));
                    vh.textView.setTextColor(ContextCompat.getColor(viewGroup.getChildAt(adapterPosition).getContext(),R.color.black_overlay));
                    vh.setChecked(false);
                    mDataSet.get(adapterPosition).setSelected(false);
                }



            }
        });

        return vh;
    }




    // END_INCLUDE(recyclerViewOnCreateViewHolder)

    // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        //Log.d(TAG, "Element " + position + " set.");

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        viewHolder.getTextView().setText(mDataSet.get(position).getFileName());
        Log.e("SETTING FILE:",mDataSet.get(position).getFileName()+ "position" + position);




    }
    // END_INCLUDE(recyclerViewOnBindViewHolder)

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }


    public List<FileItem> getmDataSet()
    {

        return mDataSet;
    }


}

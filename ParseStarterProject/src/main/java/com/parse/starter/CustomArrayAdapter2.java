package com.parse.starter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

public class CustomArrayAdapter2 extends ArrayAdapter<Goal> implements View.OnClickListener {
    private final Context context;
    //holds a reference to mainAct goalStore. and when updated here, it updates there. i think.
    //this is because it has been made static - so accessible from whole app (previously i implemented a callback interface to frag2, then accessed its getActivity to edit in mainAct. bad.
    private  GoalStore1 goalStore;

    public CustomArrayAdapter2(Context context, GoalStore1 g) {
        super(context, R.layout.goal_list_item, (List<Goal>) g.list);

        this.context = context;
        this.goalStore = g;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.goal_list_item, parent, false);
            holder = new ViewHolder();
            holder.linearLayout = (LinearLayout) convertView.findViewById(R.id.buttonsLayout);
            holder.goalTitleView = (TextView) convertView.findViewById(R.id.goalTitle);
            holder.numberOutOfView = (TextView) convertView.findViewById(R.id.goalTargets);
            holder.percentView = (TextView) convertView.findViewById(R.id.goalPercent);
            holder.percentageBar = (ProgressBar) convertView.findViewById(R.id.progressBar1);

            holder.b0 = (ImageView) convertView.findViewById(R.id.button1);
            holder.b1 = (ImageView) convertView.findViewById(R.id.button2);
            holder.b2 = (ImageView) convertView.findViewById(R.id.button3);
            holder.b3 = (ImageView) convertView.findViewById(R.id.button4);
            holder.b4 = (ImageView) convertView.findViewById(R.id.button5);
            holder.b5 = (ImageView) convertView.findViewById(R.id.button6);
            holder.b6 = (ImageView) convertView.findViewById(R.id.button7);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.linearLayout.setTag(position);
        Goal g = goalStore.getAt(position);
        holder.goalTitleView.setText(g.name);
        holder.numberOutOfView.setText(g.getTargets());
        holder.percentView.setText(g.getPercentage());
        holder.percentageBar.setProgress((int) g.percent);
        holder.b0.setOnClickListener(this);
        holder.b0.setTag(0);
        if(g.buttons[0]) {
            holder.b0.setImageResource(R.drawable.m1);}
        else{holder.b0.setImageResource(R.drawable.m2);}
        holder.b1.setOnClickListener(this);
        holder.b1.setTag(1);
        if(g.buttons[1]) {
            holder.b1.setImageResource(R.drawable.t1);}
        else{holder.b1.setImageResource(R.drawable.t2);}
        holder.b2.setOnClickListener(this);
        holder.b2.setTag(2);
        if(g.buttons[2]) {
            holder.b2.setImageResource(R.drawable.w1);}
        else{holder.b2.setImageResource(R.drawable.w2);}
        holder.b3.setOnClickListener(this);
        holder.b3.setTag(3);
        if(g.buttons[3]) {
            holder.b3.setImageResource(R.drawable.t1);}
        else{holder.b3.setImageResource(R.drawable.t2);}
        holder.b4.setOnClickListener(this);
        holder.b4.setTag(4);
        if(g.buttons[4]) {
            holder.b4.setImageResource(R.drawable.f1);}
        else{holder.b4.setImageResource(R.drawable.f2);}
        holder.b5.setOnClickListener(this);
        holder.b5.setTag(5);
        if(g.buttons[5]) {
            holder.b5.setImageResource(R.drawable.s1);}
        else{holder.b5.setImageResource(R.drawable.s2);}
        holder.b6.setOnClickListener(this);
        holder.b6.setTag(6);
        if(g.buttons[6]) {
            holder.b6.setImageResource(R.drawable.s1);}
        else{holder.b6.setImageResource(R.drawable.s2);}

        holder.percentageBar.getProgressDrawable().setColorFilter(Color.parseColor("#47aff3"), PorterDuff.Mode.MULTIPLY);
        return convertView;
    }

    @Override
    public void onClick(View v) {

        LinearLayout l = (LinearLayout)v.getParent();
        Goal g = goalStore.getAt(Integer.parseInt(l.getTag().toString()));
        for(int i=0; i < goalStore.getSize(); i++){
            if(goalStore.getAt(i).equals(g)){
                g.buttonClick(Integer.parseInt(v.getTag().toString()));

                ProfileMainActivity.goalStore.saveToDatabase();

                notifyDataSetChanged();

            }
        }

    }


    static class ViewHolder {
        // this enables reuse. recyler view it is called? means that it only has to do a findviewbyid call once then reuse it, saving valuable processing time.
        //this means that things like scrolling etc are smoother
        private LinearLayout linearLayout;
        private TextView goalTitleView;
        private TextView numberOutOfView;
        private TextView percentView;
        private ImageView b0,b1,b2,b3,b4,b5,b6;
        private ProgressBar percentageBar;
    }




}


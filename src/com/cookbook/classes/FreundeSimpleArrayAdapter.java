package com.cookbook.classes;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cookbook.R;

public class FreundeSimpleArrayAdapter  extends ArrayAdapter<String> {
		  private final Context context;
		  private final ArrayList<String> friends;

		  public FreundeSimpleArrayAdapter(Context context, ArrayList<String> friends) {
		    super(context, R.layout.layout_freunde_liste, friends);
		    this.context = context;
		    this.friends = friends;
		  }

		  @Override
		  public View getView(int position, View convertView, ViewGroup parent) {
		    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    View rowView = inflater.inflate(R.layout.layout_freunde_liste, parent, false);
		    TextView textView = (TextView) rowView.findViewById(R.id.tv_freunde_liste);
		    ImageView imageView = (ImageView) rowView.findViewById(R.id.iv_freunde_liste);
		    textView.setText(friends.get(position));


		    return rowView;
		  }
}

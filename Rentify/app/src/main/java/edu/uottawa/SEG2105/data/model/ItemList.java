package edu.uottawa.SEG2105.data.model;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import edu.uottawa.SEG2105.*;

public class ItemList extends ArrayAdapter<Item> {
        private Activity context;
        List<Item> items;

        public ItemList(Activity context, List<Item> cg) {
            super(context, R.layout.layout_category_list, cg);
            this.context = context;
            this.items = cg;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View listViewItem = inflater.inflate(R.layout.layout_item_list, null, true);

            TextView textViewCategory = (TextView) listViewItem.findViewById(R.id.textItemCategory);
            TextView textViewName = (TextView) listViewItem.findViewById(R.id.textItemName);
            TextView textViewDesc = (TextView) listViewItem.findViewById(R.id.textItemDescription);
            TextView textViewFee = (TextView) listViewItem.findViewById(R.id.textItemFee);
            TextView textViewBegin = (TextView) listViewItem.findViewById(R.id.textItemBegin);
            TextView textViewEnd = (TextView) listViewItem.findViewById(R.id.textItemEnd);
            TextView textViewRequest = (TextView) listViewItem.findViewById(R.id.textItemRequest);
            TextView textViewResponse = (TextView) listViewItem.findViewById(R.id.textItemResponse);

            Item cg = items.get(position);
			textViewCategory.setText(cg._category);
            textViewName.setText(cg._name);
            textViewDesc.setText(cg._desc);
            textViewFee.setText(cg._fee);
            textViewBegin.setText(cg._begin);
            textViewEnd.setText(cg._end);
            if (cg._Renter.length() > 0 )
                textViewRequest.setText("requested by: " + cg._Renter);
            else
                textViewRequest.setText("");
            textViewResponse.setText(cg._Response);
			
            return listViewItem;
        }
    }

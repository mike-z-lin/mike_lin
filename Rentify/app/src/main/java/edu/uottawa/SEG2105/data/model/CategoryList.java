package edu.uottawa.SEG2105.data.model;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import edu.uottawa.SEG2105.*;

public class CategoryList extends ArrayAdapter<Category> {
        private Activity context;
        List<Category> categories;

        public CategoryList(Activity context, List<Category> cg) {
            super(context, R.layout.layout_category_list, cg);
            this.context = context;
            this.categories = cg;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View listViewItem = inflater.inflate(R.layout.layout_category_list, null, true);

            TextView textViewName = (TextView) listViewItem.findViewById(R.id.textCategoryName);
            TextView textViewDesc = (TextView) listViewItem.findViewById(R.id.textCategoryDescripton);

            Category cg = categories.get(position);
            textViewName.setText(cg._name);
            textViewDesc.setText(cg._desc);
            return listViewItem;
        }
    }

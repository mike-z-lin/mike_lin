package edu.uottawa.SEG2105.data.model;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import edu.uottawa.SEG2105.*;

public class UserList extends ArrayAdapter<User> {
        private Activity context;
        List<User> users;

        public UserList(Activity context, List<User> us) {
            super(context, R.layout.layout_category_list, us);
            this.context = context;
            this.users = us;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View listViewItem = inflater.inflate(R.layout.layout_user_list, null, true);

            TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
            TextView textViewEmail = (TextView) listViewItem.findViewById(R.id.textViewEmail);
            TextView textViewRole = (TextView) listViewItem.findViewById(R.id.textViewRole);
            TextView textViewDisabled = (TextView) listViewItem.findViewById(R.id.textViewDisabled);

            User cg = users.get(position);
            textViewName.setText(cg.name);
            textViewEmail.setText(cg.email);
            textViewRole.setText(cg.role);
            textViewDisabled.setText(cg.disabled? "disabled":"enabled");

            return listViewItem;
        }
    }

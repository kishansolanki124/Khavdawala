package com.app.khavdawala.ui.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.app.khavdawala.R;

import java.util.HashMap;
import java.util.List;

public class CustomizedExpandableListAdapter extends BaseExpandableListAdapter {

    private final Context context;
    private final List<String> expandableTitleList;
    private final HashMap<String, String> expandableDetailList;

    public CustomizedExpandableListAdapter(Context context, List<String> expandableListTitle,
                                           HashMap<String, String> expandableListDetail) {
        this.context = context;
        this.expandableTitleList = expandableListTitle;
        this.expandableDetailList = expandableListDetail;
    }

    @Override
    public Object getChild(int childPosition, int expanded_ListPosition) {
        return this.expandableDetailList.get(this.expandableTitleList.get(childPosition));
    }

    @Override
    public long getChildId(int listPosition, int expanded_ListPosition) {
        return expanded_ListPosition;
    }

    @Override
    public View getChildView(int childPosition, final int expanded_ListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.expandable_list_child_item, null);
        }
        TextView expandedListTextView = (TextView) convertView.findViewById(R.id.expandedListItem);
        String listDescription = (String) getChild(childPosition, expanded_ListPosition);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            expandedListTextView.setText(Html.fromHtml(listDescription, Html.FROM_HTML_MODE_COMPACT));
        } else {
            expandedListTextView.setText(Html.fromHtml(listDescription));
        }
        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableTitleList.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableTitleList.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.expandable_list_group_item, null);
        }
        TextView listTitleTextView = (TextView) convertView.findViewById(R.id.listTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}
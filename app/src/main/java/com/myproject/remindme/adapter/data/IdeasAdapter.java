package com.myproject.remindme.adapter.data;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.myproject.remindme.R;
import com.myproject.remindme.database.data.AbstractData;
import com.myproject.remindme.database.data.Idea;
import com.myproject.remindme.dialog.IdeaDialog;
import com.myproject.remindme.eventbus.BusProvider;
import com.myproject.remindme.eventbus.IdeasChangeEvent;

import java.util.ArrayList;
import java.util.List;

import static com.myproject.remindme.dialog.IdeaDialog.IDEA_REVISE;

/**
 * Created by Alexey on 1/21/2016.
 */
public class IdeasAdapter extends AbstractAdapter<Idea> {

    public IdeasAdapter(FragmentActivity activity, View view, List<Idea> ideaList) {
        this.dataList = ideaList;
        this.drawableList = ideaList;
        this.activity = activity;
        this.layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.textNotFound = (TextView) view.findViewById(R.id.textNotFound);
        this.textNotFound.setText(R.string.exception_not_found_idea);
        this.setLayout(R.layout.listview_item_ideas);
    }

    @Override
    public void filter(String search) {
        if (search.isEmpty()) {
            drawableList = dataList;
            this.textNotFound.setText(R.string.exception_not_found_birthday);
        } else {
            drawableList = new ArrayList<>();
            for (Idea idea : dataList) {
                if (idea.getHeader().contains(search)) {
                    drawableList.add(idea);
                } else if (idea.getIdea().contains(search)) {
                    drawableList.add(idea);
                } else if (idea.getKeyword().contains(search)) {
                    drawableList.add(idea);
                }
            }
        }
    }

    @Override
    protected void showInformation(int position, View view) {
        Idea idea = (Idea) getItem(position);
        TextView textHandler = (TextView) view.findViewById(R.id.textHeader);
        TextView textIdea = (TextView) view.findViewById(R.id.textIdea);
        TextView textKeyword = (TextView) view.findViewById(R.id.textKeyword);
        textHandler.setText(idea.getHeader());
        textIdea.setText(idea.getIdea());
        textKeyword.setText(idea.getKeyword());
    }

    @Override
    protected void showPopupMenu(final View view) {
        PopupMenu popupMenu = new PopupMenu(this.getActivity(), view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_listview, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Idea idea = (Idea) getItem(view.getId());
                if (item.getItemId() == R.id.menu_item_listview_delete) {
                    BusProvider.getInstance().post(new IdeasChangeEvent(AbstractData.ELEMENT_DELETE, idea));
                } else if (item.getItemId() == R.id.menu_item_listview_revise) {
                    IdeaDialog dialog = new IdeaDialog(IDEA_REVISE, idea);
                    dialog.show(getActivity().getFragmentManager(), IdeaDialog.TAG);
                }
                return true;
            }
        });
        popupMenu.show();
    }
}

package com.myproject.remindme.dialog;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.myproject.remindme.R;
import com.myproject.remindme.database.data.AbstractData;
import com.myproject.remindme.database.data.Idea;
import com.myproject.remindme.eventbus.BusProvider;
import com.myproject.remindme.eventbus.IdeasChangeEvent;

/**
 * Created by Alexey on 1/21/2016.
 */
@SuppressLint("ValidFragment")
public class IdeaDialog extends DialogFragment {

    public final static String TAG = "IdeaDialog";
    public final static int IDEA_ADD = 1;
    public final static int IDEA_REVISE = 2;

    private View view;
    private EditText editHeader;
    private EditText editIdea;
    private EditText editKeyword;
    private Idea idea;
    private int operation;

    @SuppressLint("ValidFragment")
    public IdeaDialog(int operation) {
        this(operation, null);
    }

    @SuppressLint("ValidFragment")
    public IdeaDialog(int operation, Idea idea) {
        this.operation = operation;
        this.idea = idea;
        if (operation != IDEA_ADD && operation != IDEA_REVISE) {
            throw new IllegalArgumentException("Operation not found");
        }
        if (operation == IDEA_REVISE && idea == null) {
            throw new IllegalArgumentException("Element idea is equal to zero");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        view = inflater.inflate(R.layout.dialog_idea, null);

        initToolBar();
        initEditHeader();
        initEditIdea();
        initEditKeyword();

        builder.setView(view)
                .setNegativeButton(R.string.button_cancel, null);

        if (operation == IDEA_ADD) {
            builder.setPositiveButton(R.string.button_add, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onClickIdeaAdd();
                }
            });
        } else if (operation == IDEA_REVISE) {
            builder.setPositiveButton(R.string.button_revise, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onClickIdeaRevise();
                }
            });
        }

        return builder.create();
    }

    private void onClickIdeaRevise() {
        String handler = editHeader.getText().toString();
        if (isEmpty(handler)) {
            return;
        }
        String idea = editIdea.getText().toString();
        if (isEmpty(idea)) {
            return;
        }
        String keyword = editKeyword.getText().toString();
        if (isEmpty(keyword)) {
            return;
        }
        this.idea.setHeader(handler);
        this.idea.setIdea(idea);
        this.idea.setKeyword(keyword);
        BusProvider.getInstance().post(new IdeasChangeEvent(AbstractData.ELEMENT_REVISE, this.idea));
    }

    private void onClickIdeaAdd() {
        String handler = editHeader.getText().toString();
        if (isEmpty(handler)) {
            return;
        }
        String idea = editIdea.getText().toString();
        if (isEmpty(idea)) {
            return;
        }
        String keyword = editKeyword.getText().toString();
        if (isEmpty(keyword)) {
            return;
        }
        this.idea = new Idea(handler, idea, keyword);
        BusProvider.getInstance().post(new IdeasChangeEvent(AbstractData.ELEMENT_ADD, this.idea));
    }

    private boolean isEmpty(String s) {
        if (s.isEmpty()) {
            Toast.makeText(getActivity(), getString(R.string.exception_not_fill_fields), Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    private void initEditHeader() {
        editHeader = (EditText) view.findViewById(R.id.editHeader);
        if (operation == IDEA_REVISE) {
            editHeader.setText(idea.getHeader());
        }
    }

    private void initEditIdea() {
        editIdea = (EditText) view.findViewById(R.id.editIdea);
        if (operation == IDEA_REVISE) {
            editIdea.setText(idea.getIdea());
        }
    }

    private void initEditKeyword() {
        editKeyword = (EditText) view.findViewById(R.id.editKeyword);
        if (operation == IDEA_REVISE) {
            editKeyword.setText(idea.getKeyword());
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initToolBar() {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar_idea);
        toolbar.setNavigationIcon(R.drawable.ic_ideas);
        if (operation == IDEA_ADD) {
            toolbar.setTitle(getString(R.string.ideas_add));
        } else if (operation == IDEA_REVISE) {
            toolbar.setTitle(getString(R.string.ideas_revise));
        }
    }
}

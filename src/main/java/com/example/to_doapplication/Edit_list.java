package com.example.to_doapplication;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.to_doapplication.Database.listDatabaseHandler;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class Edit_list extends BottomSheetDialogFragment {
    public static final String TAG = "ActionBottomDialoglists";
    private EditText newListname,items;
    private Button newlistSaveButton;

    private listDatabaseHandler db;

    public static Edit_list newInstance(){
        return new Edit_list();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_add_list, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newListname = requireView().findViewById(R.id.newListname);
        newlistSaveButton = getView().findViewById(R.id.save_listButton);
        items=getView().findViewById(R.id.items);
        boolean isUpdate = false;

        final Bundle bundle = getArguments();
        if(bundle != null){
            isUpdate = true;
            String list = bundle.getString("listname");
            newListname.setText(list);
            items.setText(bundle.getString("items"));
            assert list != null;
            if(list.length()>0)
                newlistSaveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
        }

        db = new listDatabaseHandler(getActivity());
        db.openDatabase();

        newListname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    newlistSaveButton.setEnabled(false);
                    newlistSaveButton.setTextColor(Color.GRAY);
                }
                else{
                    newlistSaveButton.setEnabled(true);
                    newlistSaveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        final boolean finalIsUpdate = isUpdate;
        newlistSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = newListname.getText().toString();
                String Items= items.getText().toString();
                if(finalIsUpdate){
                    db.updateList(bundle.getInt("id"),text,Items);
                }

                dismiss();
            }
        });
    }


    @Override
    public void onDismiss(@NonNull DialogInterface dialog){
        Activity activity = getActivity();
        if(activity instanceof DialogCloseListner)
            ((DialogCloseListner)activity).handleDialogClose(dialog);
    }
}

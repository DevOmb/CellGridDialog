package com.devomb.cellgriddialog.sample;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.devomb.cellgriddialog.CellGridDialog;
import com.devomb.cellgriddialog.CellGridDialogBuilder;

import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 */
public class SampleFragment extends Fragment implements CellGridDialog.OnAcceptListener {

    private Button singlePageButton;
    private Button multiPageButton;
    private Button multiPageListButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sample, container, false);

        singlePageButton = (Button) view.findViewById(R.id.single_page_button);
        singlePageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CellGridDialogBuilder.create(getChildFragmentManager(), 80)
                        .select(41)
                        .onAcceptListener(SampleFragment.this)
                        .show();
            }
        });
        multiPageButton = (Button) view.findViewById(R.id.multi_page_button);
        multiPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CellGridDialogBuilder.create(getChildFragmentManager(), 6, 22)
                        .select(2, 17)
                        .onAcceptListener(SampleFragment.this)
                        .show();
            }
        });
        multiPageListButton = (Button) view.findViewById(R.id.multi_page_list_button);
        multiPageListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CellGridDialogBuilder.create(getChildFragmentManager(), 4, Arrays.asList(13, 12, 12, 11))
                        .select(1, 8)
                        .onAcceptListener(SampleFragment.this)
                        .show();
            }
        });

        return view;
    }


    @Override
    public void onAccept(int pageIndex, int cellIndex) {
        System.out.println("Page " + (pageIndex + 1) + " Cell " + (cellIndex + 1));
    }
}

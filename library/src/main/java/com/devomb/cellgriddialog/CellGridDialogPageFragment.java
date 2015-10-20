package com.devomb.cellgriddialog;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class CellGridDialogPageFragment extends Fragment implements View.OnClickListener {

    //region declaration
    //region constant
    private static final String ENTRY_TAG_PREFIX = "Ent";
    private static final int VISIBLE_GRID_COUNT = 5;
    private static final int MARGIN = 10;
    //endregion

    //region inner field
    private LinearLayout.LayoutParams cellParams;
    private LinearLayout.LayoutParams rowParams;

    private boolean initialized;
    private int cellCount;
    private int selectedCellIndex = -1;
    private OnSelectedCellChangedListener onSelectedCellChangedListener;
    //endregion

    //region view
    private LinearLayout mainLayout;
    //endregion
    //endregion

    //region instantiation
    public void init(int cellCount) {
        this.cellCount = cellCount;
        initialized = true;
    }
    //endregion

    //region create
    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (!initialized) {
            throw new NullPointerException("Not initialized");
        }
        super.onCreate(savedInstanceState);
        loadResources();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_page, container, false);
        init(view);
        return view;
    }
    //endregion

    //region init
    private void init(View view) {
        Context context = getContext();

        mainLayout = (LinearLayout) view.findViewById(R.id.dialog_page_main_layout);

        int rowCellCount = cellCount / VISIBLE_GRID_COUNT;

        for (int i = 0; i < rowCellCount; i++) {
            mainLayout.addView(row(context, i, VISIBLE_GRID_COUNT));
        }

        int lastRowCellCount = cellCount % VISIBLE_GRID_COUNT;
        if (lastRowCellCount > 0) {
            mainLayout.addView(row(context, rowCellCount, lastRowCellCount));
        }

        if (selectedCellIndex > -1) {
            View v = mainLayout.findViewWithTag(ENTRY_TAG_PREFIX + selectedCellIndex);
            if (v != null) {
                v.performClick();
            }
        }
    }
    //endregion

    //region setter
    public void setOnSelectedCellChangedListener(OnSelectedCellChangedListener onSelectedCellChangedListener) {
        this.onSelectedCellChangedListener = onSelectedCellChangedListener;
    }

    public void setSelectedCell(int cellIndex) {
        this.selectedCellIndex = cellIndex;
    }
    //endregion

    //region helper
    private void loadResources() {
        rowParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rowParams.setMargins(MARGIN, 0, 0, 0);

        int buttonSize = (getResources().getDimensionPixelSize(R.dimen.dialog_width) - ((VISIBLE_GRID_COUNT + 1) * MARGIN)) / VISIBLE_GRID_COUNT;
        cellParams = new LinearLayout.LayoutParams(buttonSize, buttonSize);
        cellParams.setMargins(0, MARGIN, MARGIN, 0);
    }

    private LinearLayout row(Context context, int rowIndex, int columnCount) {
        LinearLayout row = new LinearLayout(context);
        row.setLayoutParams(rowParams);
        for (int j = 0; j < columnCount; j++) {
            int cellIndex = (rowIndex * VISIBLE_GRID_COUNT) + j;
            if (cellIndex > cellCount) {
                break;
            }
            Button cell = new Button(context);
            cell.setLayoutParams(cellParams);
            cell.setGravity(Gravity.CENTER);
            cell.setBackgroundResource(R.drawable.round_button_background);
            cell.setOnClickListener(this);
            cell.setText(String.valueOf(cellIndex + 1));
            cell.setTag(ENTRY_TAG_PREFIX + cellIndex);
            row.addView(cell);
        }
        return row;
    }

    private int getCellIndex(View v) {
        return Integer.parseInt(v.getTag().toString().replace(ENTRY_TAG_PREFIX, ""));
    }
    //endregion

    //region interface implementation
    @Override
    public void onClick(View v) {
        selectedCellIndex = getCellIndex(v);
        if (onSelectedCellChangedListener != null) {
            onSelectedCellChangedListener.onSelectedCellChanged(v, selectedCellIndex);
        }
        v.setSelected(true);
    }
    //endregion

    //region interface
    public interface OnSelectedCellChangedListener {
        void onSelectedCellChanged(View cell, int cellNumber);
    }
    //endregion
}

package com.devomb.cellgriddialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.devomb.disableables.widget.DButton;
import com.devomb.disableables.widget.DImageButton;

import java.util.List;

/**
 * Created by Ombrax on 18/10/2015.
 */
public class CellGridDialog extends DialogFragment implements CellGridDialogPageFragment.OnSelectedCellChangedListener {

    //region declaration
    //region inner field
    private int pages;
    private int cellsPerPage;
    private List<Integer> cellsPerPageList;

    private boolean singlePage;
    private CellGridDialogAdapter adapter;
    private View selectedCell;
    //endregion

    //region variable
    private int currentPageIndex;
    private int selectedCellIndex;
    private int selectedPageIndex;
    private OnAcceptListener onAcceptListener;
    private String pageName;
    private String singlePageName;
    //endregion

    //region view
    private DImageButton previousButton;
    private DImageButton nextButton;
    private TextView topLabel;
    private ViewPager viewPager;
    private DButton acceptButton;
    //endregion
    //endregion

    //region create
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog, container, false);
        init(view);
        return view;
    }
    //endregion

    //region init
    private void init(View view) {
        //Get views
        previousButton = (DImageButton) view.findViewById(R.id.tv_show_dialog_previous_button);
        nextButton = (DImageButton) view.findViewById(R.id.tv_show_dialog_next_button);
        topLabel = (TextView) view.findViewById(R.id.tv_show_dialog_top_label);
        viewPager = (ViewPager) view.findViewById(R.id.tv_show_dialog_grid_view_pager);
        acceptButton = (DButton) view.findViewById(R.id.tv_show_dialog_accept_button);

        //Set Listeners
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPageIndex > 0) {
                    viewPager.setCurrentItem(--currentPageIndex);
                }
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPageIndex < pages) {
                    viewPager.setCurrentItem(++currentPageIndex);
                }
            }
        });
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onAcceptListener != null) {
                    onAcceptListener.onAccept(selectedPageIndex, selectedCellIndex);
                    dismiss();
                }
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPageIndex = position;
                updateTopNavigation();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //Set Viewpager
        viewPager.setAdapter(getProperAdapter());
        viewPager.setCurrentItem(currentPageIndex);
        viewPager.setOffscreenPageLimit(0);

        //Update Attributes
        acceptButton.setEnabled(false);
        updateTopNavigation();
        if (singlePage) {
            topLabel.setText(singlePageName != null ? singlePageName : "Page");
        }
    }
    //endregion

    //region helper
    private CellGridDialogAdapter getProperAdapter() {
        if (singlePage) {
            adapter = new CellGridDialogAdapter(getChildFragmentManager(), cellsPerPage);
        } else if (cellsPerPageList != null) {
            adapter = new CellGridDialogAdapter(getChildFragmentManager(), pages, cellsPerPageList);
        } else {
            adapter = new CellGridDialogAdapter(getChildFragmentManager(), pages, cellsPerPage);
        }
        adapter.setOnSelectedCellChangedListener(this);
        adapter.setSelectedCell(currentPageIndex, selectedCellIndex);
        return adapter;
    }

    private void updateTopNavigation() {
        previousButton.setEnabled(currentPageIndex > 0);
        nextButton.setEnabled(currentPageIndex < (pages - 1));
        topLabel.setText((pageName != null ? pageName : "Page ") + (currentPageIndex + 1));
    }
    //endregion

    //region setter
    public void setPages(int pages) {
        this.pages = pages;
    }

    public void setCellsPerPage(int cellsPerPage) {
        this.cellsPerPage = cellsPerPage;
    }

    public void setCellsPerPage(List<Integer> cellsPerPageList) {
        this.cellsPerPageList = cellsPerPageList;
    }

    public void setSinglePage(boolean singlePage) {
        this.singlePage = singlePage;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public void setSinglePageName(String singlePageName) {
        this.singlePageName = singlePageName;
    }

    public void setCurrentCell(int currentPageIndex, int currentCellIndex) {
        if (singlePage && currentPageIndex != 0) {
            throw new IllegalArgumentException("Invalid select call");
        }
        if (currentPageIndex > pages || currentPageIndex < 0) {
            throw new IllegalArgumentException("Page (" + currentPageIndex + ") is not in range [0, " + (pages - 1) + "]");
        }
        if (cellsPerPageList != null) {
            int cells = cellsPerPageList.get(currentPageIndex);
            if (currentCellIndex > cells || currentCellIndex < 0) {
                throw new IllegalArgumentException("Cell (" + currentCellIndex + ") is not in range [0, " + (cells - 1) + "]");
            }
        } else if (currentCellIndex > cellsPerPage || currentCellIndex < 0) {
            throw new IllegalArgumentException("Cell (" + currentCellIndex + ") is not in range [0, " + (cellsPerPage - 1) + "]");
        }
        this.currentPageIndex = currentPageIndex;
        this.selectedCellIndex = currentCellIndex;
    }

    public void setOnAcceptListener(OnAcceptListener onAcceptListener) {
        this.onAcceptListener = onAcceptListener;
    }
    //endregion

    //region interface implementation
    @Override
    public void onSelectedCellChanged(View cell, int cellIndex) {
        //Minimum ViewPager offScreenPageLimit is 1, so at minimum 2 pages are always kept in memory
        //If a cell were to be selected on one of the adjoining pages (in memory),
        //the cell would still be selected when returning from the page with the newly selected cell
        //This way we ensure that only one cell (in memory) is selected at all times
        if (selectedCell != null) {
            selectedCell.setSelected(false);
        }
        selectedCell = cell;

        selectedCellIndex = cellIndex;
        selectedPageIndex = currentPageIndex;
        adapter.setSelectedCell(selectedPageIndex, selectedCellIndex);
        acceptButton.setEnabled(true);
    }
    //endregion

    //region interface
    public interface OnAcceptListener {
        void onAccept(int season, int episode);
    }
    //endregion
}

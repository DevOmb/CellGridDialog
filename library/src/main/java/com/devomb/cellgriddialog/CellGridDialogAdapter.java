package com.devomb.cellgriddialog;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Ombrax on 18/10/2015.
 */
public class CellGridDialogAdapter extends FragmentPagerAdapter {

    //region variable
    private int pages;
    private int cellsPerPage;
    private List<Integer> cellsPerPageList;

    private int pageIndex;
    private int cell = -1;
    private CellGridDialogPageFragment.OnSelectedCellChangedListener onSelectedCellChangedListener;
    //endregion

    //region constructor
    public CellGridDialogAdapter(FragmentManager fm, int pages, int cellsPerPage) {
        super(fm);
        this.pages = pages;
        this.cellsPerPage = cellsPerPage;
    }

    public CellGridDialogAdapter(FragmentManager fm, int cells) {
        this(fm, 1, cells);
    }

    public CellGridDialogAdapter(FragmentManager fm, int pages, List<Integer> cellsPerPageList) {
        super(fm);
        this.pages = pages;
        this.cellsPerPageList = cellsPerPageList;
    }
    //endregion

    //region setter
    public void setSelectedCell(int pageIndex, int cell) {
        this.pageIndex = pageIndex;
        this.cell = cell;
    }

    public void setOnSelectedCellChangedListener(CellGridDialogPageFragment.OnSelectedCellChangedListener onSelectedCellChangedListener) {
        this.onSelectedCellChangedListener = onSelectedCellChangedListener;
    }
    //endregion

    //region override
    @Override
    public Fragment getItem(int pIndex) {
        if (cellsPerPageList != null && cellsPerPageList.size() > pIndex) {
            cellsPerPage = cellsPerPageList.get(pIndex);
        }
        CellGridDialogPageFragment fragment = new CellGridDialogPageFragment();
        fragment.init(cellsPerPage);
        fragment.setOnSelectedCellChangedListener(onSelectedCellChangedListener);
        return fragment;
    }

    @Override
    public int getCount() {
        return pages;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        CellGridDialogPageFragment fragment = (CellGridDialogPageFragment) super.instantiateItem(container, position);
        fragment.setSelectedCell(pageIndex == position ? cell : -1);
        return fragment;
    }
    //endregion

}

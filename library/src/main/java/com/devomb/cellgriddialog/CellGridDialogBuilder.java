package com.devomb.cellgriddialog;

import android.support.v4.app.FragmentManager;

import java.util.List;

/**
 * Created by Ombrax on 19/10/2015.
 */
public class CellGridDialogBuilder {

    //region inner field
    private FragmentManager fragmentManager;
    private CellGridDialog dialog;
    //endregion

    //region private constructor
    private CellGridDialogBuilder(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
        this.dialog = new CellGridDialog();
    }
    //endregion

    //region creation
    public static CellGridDialogBuilder create(FragmentManager fragmentManager, int pages, int cellsPerPage) {
        CellGridDialogBuilder builder = new CellGridDialogBuilder(fragmentManager);
        builder.dialog.setPages(pages);
        builder.dialog.setCellsPerPage(cellsPerPage);
        return builder;
    }

    public static CellGridDialogBuilder create(FragmentManager fragmentManager, int pages, List<Integer> cellsPerPageList) {
        if (pages != cellsPerPageList.size()) {
            throw new IllegalArgumentException("Size difference");
        }
        CellGridDialogBuilder builder = new CellGridDialogBuilder(fragmentManager);
        builder.dialog.setPages(pages);
        builder.dialog.setCellsPerPage(cellsPerPageList);
        return builder;
    }

    public static CellGridDialogBuilder create(FragmentManager fragmentManager, int cells) {
        CellGridDialogBuilder builder = new CellGridDialogBuilder(fragmentManager);
        builder.dialog.setCellsPerPage(cells);
        builder.dialog.setSinglePage(true);
        return builder;
    }
    //endregion

    //region builder
    public CellGridDialogBuilder header(String headerPrefix, String header){
        dialog.setPageName(headerPrefix);
        dialog.setSinglePageName(header);
        return this;
    }

    public CellGridDialogBuilder select(int pageIndex, int cellIndex) {
        dialog.setCurrentCell(pageIndex, cellIndex);
        return this;
    }

    public CellGridDialogBuilder select(int cellIndex) {
        return select(0, cellIndex);
    }

    public CellGridDialogBuilder onAcceptListener(CellGridDialog.OnAcceptListener onAcceptListener) {
        dialog.setOnAcceptListener(onAcceptListener);
        return this;
    }

    public void show() {
        dialog.show(fragmentManager, null);
    }
    //endregion
}

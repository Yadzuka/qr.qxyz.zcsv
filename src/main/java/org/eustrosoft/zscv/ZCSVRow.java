// EustroSoft.org PSPN/CSV project
//
// (c) Alex V Eustrop & EustroSoft.org 2020
// 
// LICENSE: BALES, ISC, MIT, BSD on your choice
//
//

package org.eustrosoft.zscv;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

/**
 * single row from CSV file
 */
public class ZCSVRow {

    private ZCSVRow previousRow = null;
    private boolean is_ro = false; //read only
    private boolean is_dirty = false; //read only
    private Vector dataInRow = null;
    private String[] name_map = null;

    public String setStringSpecificIndex(int i, String str) {
        if (is_ro) return (null); is_dirty = true;
        if (dataInRow == null) dataInRow = new Vector(Math.abs(i)+1);
        if (i < dataInRow.size()) return (null);

        dataInRow.set(i, str);
        String ov = (String) dataInRow.get(i);
        return (ov);
    }

    public String setNewName(String name, String dataInRow) {
        return setStringSpecificIndex(name2column(name), dataInRow);
    }

    public String get(int i) {
        if (dataInRow == null)
            return (null);
        if (i > dataInRow.size())
            return (null);
        return (String)dataInRow.get(i);
    }

    public String get(String name) {
        return get(name2column(name));
    }

    public int name2column(String name) {
        if (name_map != null && name != null)
            for (int i = 0; i < name_map.length; i++)
                if (name.equals(name_map[i]))
                    return (i);
        return (-1);
    }

    public void setRO() {
        is_ro = true;
    }

    public boolean isRO() {
        return (is_ro);
    }

    public void setPrevious(ZCSVRow previous) {
        previousRow = previous;
    }

    public ZCSVRow getPrevious() {
        return (previousRow);
    }

    public void setNames(String[] names) {
        name_map = names;
    }

    public ZCSVRow getNames() {
        return this;
    }

    // constructors
    public ZCSVRow() {
    }

    public ZCSVRow(String row) {
    }

    public ZCSVRow(String row, String delimiter) {
    }

    public ZCSVRow(String[] values) {
    }

    public ZCSVRow(String[] values, String[] names) {
    }
} //ZCSVRow

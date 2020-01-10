// EustroSoft.org PSPN/CSV project
//
// (c) Alex V Eustrop & EustroSoft.org 2020
// 
// LICENSE: BALES, ISC, MIT, BSD on your choice
//
//

package org.eustrosoft.zscv;

import java.util.Vector;

/**
 * single row from CSV file
 */
public class ZCSVRow {

    private ZCSVRow previousRow = null;
    private boolean is_ro = false; //read only
    private boolean is_dirty = false; //read only
    private Vector v = null;
    private String[] name_map = null;

    public String get(int i) {
        if (v == null)
            return (null);
        if (i > v.size())
            return (null);
        return (String) v.get(i);
    }

    public String set(int i, String str) {
        if (is_ro)
            return (null);
        is_dirty = true;
        if (v == null)
            v = new Vector(i);
        if (i < v.size())
            return (null);

        String ov = (String) v.get(i);
        v.set(i, v);
        return (ov);
    }

    public String get(String name) {
        return (get(name2column(name)));
    }

    public String set(String name, String v) {
        return "1";//(name2column(name));
    }

    public int name2column(String name) {
        if (!(name_map == null || name == null)) {
            for (int i = 0; i < name_map.length; i++) {
                if (name.equals(name_map[i]))
                    return (i);
            }
        }
        return (-1);
    }

    public int n2c(String name) {
        return (name2column(name));
    }

    public void setRO() {
        is_ro = true;
    }

    public boolean isRO() {
        return (is_ro);
    }

    public void setPrevious(ZCSVRow pr) {
        previousRow = pr;
    }

    public ZCSVRow getPrevious() {
        return (previousRow);
    }

    public void setNames(String[] names) {
        name_map = names;
    }

    public ZCSVRow getNames() {
        return this;//(name_map);
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

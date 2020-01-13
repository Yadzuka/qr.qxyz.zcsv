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
    public String DELIMETER = ";";

    private boolean is_row = false; //read only
    private boolean is_dirty = false; //read only

    private ZCSVRow previousRow = null;
    private String[] nameMap = null;
    private Vector dataInRow = null;

    public String setStringSpecificIndex(int i, String str) {
        try {
            if (i < 0)
                throw new ZCSVException("Индекс указан неправильно!");
            if (is_row) return null;
            is_dirty = true;
            if (dataInRow == null) dataInRow = new Vector(i + 1);
            if (i < dataInRow.size()) return (null);

            dataInRow.set(i, str);
            String ov = (String) dataInRow.get(i);
            return (ov);
        }catch (ZCSVException ex){
            ex.printError();
        }
        return null;
    }

    public String setNewName(String name, String dataInRow) {
        try {
            int index = name2column(name);
            if (index == -1)
                throw new ZCSVException("Название параметра не найдено!");

            return setStringSpecificIndex(name2column(name), dataInRow);
        }catch (ZCSVException ex) {
            ex.printError();
        }
        return null;
    }

    public String get(int i) {
        try {
            if (dataInRow == null)
                throw new ZCSVException("Данные не загружены!");
            if (i >= dataInRow.size() || i < 0)
                throw new ZCSVException("Индекс задан неправильно!");
            return (String) dataInRow.get(i);
        }catch (ZCSVException ex){
            ex.printError();
        }
        return null;
    }

    public String get(String name) {
        return get(name2column(name));
    }

    public int name2column(String name) {
        try {
            if (nameMap != null && name != null) {
                for (int i = 0; i < nameMap.length; i++)
                    if (name.equals(nameMap[i]))
                        return (i);
                    else
                        throw new ZCSVException("Искомый параметр не найден!");
            }else
                throw new ZCSVException("Параметры или строка не заданы!");
        }catch (ZCSVException ex){
            ex.printError();
        }
        return -1;
    }

    public void setRow() {
        is_row = true;
    }

    public boolean isRow() {
        return (is_row);
    }

    public void setPrevious(ZCSVRow previous) {
        previousRow = previous;
    }

    public ZCSVRow getPrevious() {
        return (previousRow);
    }

    public void setNames(String[] names) {
        nameMap = names;
    }

    public ZCSVRow getNames() {
        return this;
    }

    @Override
    public String toString() {
        StringBuilder returnString = new StringBuilder();

        for (int i = 0; i < dataInRow.size(); i++) {
            if (i < dataInRow.size() - 1) {
                returnString.append(dataInRow.get(i).toString());
                returnString.append(DELIMETER);
            } else {
                returnString.append(dataInRow.get(i).toString());
            }
        }

        return returnString.toString();
    }

    private void splitString(String str){
        dataInRow = new Vector();

        for(String s : str.trim().split(DELIMETER)){
            dataInRow.add(s.trim());
        }
    }

    // constructors
    public ZCSVRow() {
    }

    public ZCSVRow(String row) {
        splitString(row);
    }

    public ZCSVRow(String row, String delimiter) {
        DELIMETER = delimiter;
        splitString(row);
    }

    public ZCSVRow(String[] values) {
        setNames(values);
    }

    public ZCSVRow(String[] values, String[] names) {
        setNames(values);
        for(String s : names){
            dataInRow.add(s);
        }
    }
} //ZCSVRow

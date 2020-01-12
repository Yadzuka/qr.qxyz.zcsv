// EustroSoft.org PSPN/CSV project
//
// (c) Alex V Eustrop & EustroSoft.org 2020
// 
// LICENSE: BALES, ISC, MIT, BSD on your choice
//
//

package org.eustrosoft.zscv;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.PrintWriter;

/**
 * exception which this package able to throw.
 */
public class ZCSVException extends Exception {

    private static PrintWriter outWriter;
    private final Logger logger = LogManager.getLogger(ZCSVException.class);

    // constructors
    public ZCSVException() {

    }

    ZCSVException(Exception e) {
        super(e.getMessage());
    }

    public ZCSVException(String s) {
        super(s);
    }

    void WriteError(String str){
        System.out.println(str);
    }

} //ZCSVException

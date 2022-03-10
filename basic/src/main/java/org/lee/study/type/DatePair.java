package org.lee.study.type;

import java.util.Date;

public class DatePair extends Pair<Date> {
    private Date val;

    @Override
    public Date getVal() {
        return val;
    }

    @Override
    public void setVal(Date val) {
        this.val = val;
    }
}

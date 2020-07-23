package com.haojie.others;

import java.util.ArrayList;
import java.util.Stack;

public class BrowseRecord {
    public ArrayList<SingleBrowseRecord> records = new ArrayList<>();
    public static final int maxNumOfRecords = 10;

    public BrowseRecord() {

    }

    public void addRecord(SingleBrowseRecord singleBrowseRecord) {
        int size = this.records.size();

        int newImageID = singleBrowseRecord.getImageID();
        //得到新加入记录的imageID；


        for (int i = 0; i <= records.size() - 1; i++) {
            if (records.get(i).getImageID() == newImageID) {
                return;
            }
        }//遍历现有的记录 如果有记录的imageID和新的imageID重合，则return 什么都不做


        if (size < maxNumOfRecords) {//如果浏览记录小于最大条数的话
            records.add(singleBrowseRecord);
        } else {
            for (int i = 0; i < maxNumOfRecords - 2; i++) {
                records.set(i, records.get(i + 1));
            }//将所有的单一记录，全部向前移一位，原先index=0处的，不要了
            records.set(maxNumOfRecords - 1, singleBrowseRecord);
        }

    }

}

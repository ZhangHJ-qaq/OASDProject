package com.haojie.others;

import java.util.ArrayList;
import java.util.Stack;

/**
 * The holistic browse record of a user. Including an arraylist of at most 10 single browse records.
 */
public class BrowseRecord {
    //Single BrowseRecord Arraylist
    public ArrayList<SingleBrowseRecord> records = new ArrayList<>();

    public static final int maxNumOfRecords = 10;

    public BrowseRecord() {

    }

    /**
     * To add a new browseRecord to the arraylist
     * @param singleBrowseRecord The new SingleBrowseRecord
     */
    public void addRecord(SingleBrowseRecord singleBrowseRecord) {
        int size = this.records.size();

        int newImageID = singleBrowseRecord.getImageID();
        //Get the new imageID of the new singleBrowseRecord


        for (int i = 0; i <= records.size() - 1; i++) {
            if (records.get(i).getImageID() == newImageID) {
                return;
            }
        }//To iterate the previous records. If one imageID in them matches the newImageID, then do nothing.


        if (size < maxNumOfRecords) {//If the previous records we have now is smaller than the max num of records allowed
            records.add(singleBrowseRecord);
        } else {

            //Otherwise, we will move all the singlebrowseRecords by one index, discarding the first record.
            //And then set the last record as the new singleBrowseRecord.
            for (int i = 0; i <= maxNumOfRecords - 2; i++) {
                records.set(i, records.get(i + 1));
            }
            records.set(maxNumOfRecords - 1, singleBrowseRecord);
        }

    }

}

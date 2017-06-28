/*
 * Created by Itzik Braun on 12/3/2015.
 * Copyright (c) 2015 deluge. All rights reserved.
 *
 * Last Modification at: 3/12/15 4:27 PM
 */

package co.chatsdk.ui.threads;

import java.util.Comparator;
import java.util.Date;

public class ThreadsItemSorter implements Comparator<ThreadsListAdapter.ThreadListItem> {
    public static final int ORDER_TYPE_ASC = 0;
    public static final int ORDER_TYPE_DESC = 1;

    private int order = ORDER_TYPE_DESC;

    public ThreadsItemSorter(){}

    public ThreadsItemSorter(int order) {
        this.order = order;
    }

    @Override
    public int compare(ThreadsListAdapter.ThreadListItem t1, ThreadsListAdapter.ThreadListItem t2) {

        Date x, y;
        if (t1.getLastMessageDate() == null)
        {
            
            x = new Date();
        }
        else x = t1.getLastMessageDate();

        if(t2.getLastMessageDate() == null)
        {
            y = new Date();
        } else y = t2.getLastMessageDate();

        if (order == ORDER_TYPE_ASC)
            return x.compareTo(y);
        else 
            return y.compareTo(x);
    }
}

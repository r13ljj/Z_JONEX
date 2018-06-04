package paralexecutor.futures;

import com.google.common.util.concurrent.ListenableFuture;

/**
 * <pre>
 *
 *  File: SearchService.java
 *
 *  Copyright (c) 2018, globalegrow.com All Rights Reserved.
 *
 *  Description:
 *  TODO
 *
 *  Revision History
 *  Date,					Who,					What;
 *  2018/6/4				lijunjun				Initial.
 *
 * </pre>
 */
public interface SearchService {

    ListenableFuture<QueryResult> query(String params);

}

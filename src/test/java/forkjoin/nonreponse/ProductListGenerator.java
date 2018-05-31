package forkjoin.nonreponse;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *
 *  File: ProductListGenerator.java
 *
 *  Copyright (c) 2018, globalegrow.com All Rights Reserved.
 *
 *  Description:
 *  TODO
 *
 *  Revision History
 *  Date,					Who,					What;
 *  2018/5/30				lijunjun				Initial.
 *
 * </pre>
 */
public class ProductListGenerator {

    public List<Product> generate (int size) {
        List<Product> ret=new ArrayList<>();
        for (int i=0; i<size; i++){
            Product product=new Product();
            product.setName("Product"+i);
            product.setPrice(0.1);
            ret.add(product);
        }
        return ret;
    }

}

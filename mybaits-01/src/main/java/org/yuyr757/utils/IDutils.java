package org.yuyr757.utils;

import java.util.UUID;
import org.junit.Test;

public class IDutils {
    public static String getID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    @Test
    public void test(){
        System.out.println(IDutils.getID());
    }
}

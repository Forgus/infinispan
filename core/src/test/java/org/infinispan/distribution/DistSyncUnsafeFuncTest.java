package org.infinispan.distribution;

import org.testng.annotations.Test;

@Test(groups = "functional", testName = "distribution.DistSyncUnsafeFuncTest", enabled = false)
public class DistSyncUnsafeFuncTest extends DistSyncFuncTest {
   public DistSyncUnsafeFuncTest() {
      sync = true;
      tx = false;
      testRetVals = false;
   }
}

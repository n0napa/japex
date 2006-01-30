/*
 * Japex ver. 1.0 software ("Software")
 * 
 * Copyright, 2004-2005 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * This Software is distributed under the following terms:
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, is permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * 
 * Redistribution in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * 
 * Neither the name of Sun Microsystems, Inc., 'Java', 'Java'-based names,
 * nor the names of contributors may be used to endorse or promote products
 * derived from this Software without specific prior written permission.
 * 
 * The Software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS
 * SHALL NOT BE LIABLE FOR ANY DAMAGES OR LIABILITIES SUFFERED BY LICENSEE
 * AS A RESULT OF OR RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THE
 * SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE
 * LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED
 * AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR
 * INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 * 
 * You acknowledge that the Software is not designed, licensed or intended
 * for use in the design, construction, operation or maintenance of any
 * nuclear facility.
 */

package com.sun.japex.jdsl.nativecode;

import com.sun.japex.*;
import java.util.List;
import com.sun.japex.Constants;
import com.sun.japex.Japex;
import com.sun.japex.JapexDriverBase;
import com.sun.japex.TestCase;
import com.sun.japex.TestCaseImpl;
import com.sun.japex.Util;

/**
 *
 * @author Paul.Sandoz@sun.com
 * @author Santiago.PericasGeertsen@sun.com
 */
public class JapexNativeDriver extends JapexDriverBase {
    
    public void setDriver(Driver driver) {
        super.setDriver(driver);
        
        String library = driver.getParam("libraryName");
        if (library != null) {
            System.loadLibrary(library);
        }
        else {
            throw new RuntimeException("JavaNativeDriver requires setting parameter 'libraryName'");
        }
    }
    
    /**
     * Execute the run phase. This method can be executed concurrently
     * by multiple threads. Care should be taken to ensure proper
     * synchronization. Note that parameter getters and setters are
     * already synchronized.
     *
     * This method defers to simple native methods that perform the
     * work of the loop (for time or for iterations).
     */
    public void run() {
        if (Japex.verbose) {
            System.out.println("             " + 
                Thread.currentThread().getName() + " run()"); 
        }

        long millis;
        TestCaseImpl tc = _testCase;
        
        // Force GC
        System.gc();
        // Get elapsed time for all GCs
        List<Long> gCStartTimes = getGCAbsoluteTimes();
        
        // Get number of threads to adjust iterations
        int nOfThreads = tc.getIntParam(Constants.NUMBER_OF_THREADS);
        
        int runIterations = 0;
        String runTime = tc.getParam(Constants.RUN_TIME);
        if (runTime != null) {
            runIterations = runLoopDuration(Util.parseDuration(runTime));
        }
        else {
            // Adjust runIterations based on number of threads
            runIterations = tc.getIntParam(Constants.RUN_ITERATIONS) / nOfThreads;
	    runLoopIterations(runIterations);
        }

        // Get the total time take for GC over the measurement period
        _gCTime = getGCRelativeTotalTime(gCStartTimes);
        
        // Accumulate actual number of iterations
        synchronized (tc) {
            int actualRunIterations =  
                tc.hasParam(Constants.ACTUAL_RUN_ITERATIONS) ? 
                    tc.getIntParam(Constants.ACTUAL_RUN_ITERATIONS) : 0;
            tc.setIntParam(Constants.ACTUAL_RUN_ITERATIONS, 
                           actualRunIterations + runIterations);
        }
    }

    // JapexDriver Interface ---------------------------------------------
    
    /**
     * Called once when the class is loaded.
     */
    native public void initializeDriver();
    
    /**
     * Execute prepare phase. 
     */
    native public void prepare(TestCase testCase);
    
    /**
     * Called once or more for every test, before calling run. Default 
     * implementation is to call run().
     */
    native public void warmup(TestCase testCase);
    
    /**
     * Called once or more for every test to obtain perf data.
     *
     */
    native public void run(TestCase testCase);
    
    /**
     * Called exactly once after calling run. 
     */
    native public void finish(TestCase testCase);
    
    /**
     * Called after all tests are completed.
     */
    native public void terminateDriver();        
    
    // Internal JNI Interface --------------------------------------------
    
    /**
     * Called for looping over a specified duration
     */
    native public int runLoopDuration(long duration);

    /**
     * Called for looping over a specified number iterations
     */
    native public void runLoopIterations(int iterations);
    
}

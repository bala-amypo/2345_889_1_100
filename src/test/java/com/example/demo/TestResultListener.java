package com.example.demo;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestResultListener implements ITestListener {
    
    @Override
    public void onTestStart(ITestResult result) {
        // Optional: log when test starts
    }
    
    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println(result.getMethod().getMethodName() + " - PASS");
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println(result.getMethod().getMethodName() + " - FAIL");
    }
    
    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println(result.getMethod().getMethodName() + " - SKIP");
    }
    
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // Not used
    }
    
    @Override
    public void onStart(ITestContext context) {
        // Optional: log when suite starts
    }
    
    @Override
    public void onFinish(ITestContext context) {
        // Optional: log when suite finishes
    }
}
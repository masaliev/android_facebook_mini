package com.github.masaliev.facebookmini.utils.rx;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.TestScheduler;

/**
 * Created by mbt on 10/17/17.
 */
public class TestSchedulerProvider implements SchedulerProvider{

    private final TestScheduler mTestScheduler;

    public TestSchedulerProvider(TestScheduler mTestScheduler) {
        this.mTestScheduler = mTestScheduler;
    }


    @Override
    public Scheduler ui() {
        return mTestScheduler;
    }

    @Override
    public Scheduler computation() {
        return mTestScheduler;
    }

    @Override
    public Scheduler io() {
        return mTestScheduler;
    }
}
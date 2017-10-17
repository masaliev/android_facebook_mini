package com.github.masaliev.facebookmini.utils.rx;

import io.reactivex.Scheduler;

/**
 * Created by mbt on 10/16/17.
 */

public interface SchedulerProvider {
    Scheduler ui();

    Scheduler computation();

    Scheduler io();
}

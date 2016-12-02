package com.cegeka.tetherj.api;

import com.cegeka.tetherj.EthEvent;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Handle for filter watches.
 */
public class TetherjFilterWatch {

    private TetherjHandle<List<EthEvent>> watchHandle;
    private AtomicBoolean isCancelled = new AtomicBoolean(false);

    public TetherjFilterWatch(TetherjHandle<List<EthEvent>> watchHandle) {
        this.watchHandle = watchHandle;
    }

    /**
     * Cancels watch of filter. Watch callback will stop being called.
     */
    public void cancel() {
        isCancelled.set(true);
    }

    public TetherjHandle<List<EthEvent>> getWatchHandle() {
        return watchHandle;
    }

    public void setWatchHandle(TetherjHandle<List<EthEvent>> watchHandle) {
        this.watchHandle = watchHandle;
    }

    public AtomicBoolean getIsCancelled() {
        return isCancelled;
    }

    public void setIsCancelled(AtomicBoolean isCancelled) {
        this.isCancelled = isCancelled;
    }
}

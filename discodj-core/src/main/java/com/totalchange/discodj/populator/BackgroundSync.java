/**
 * Copyright 2015 Ralph Jones
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.totalchange.discodj.populator;

import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class BackgroundSync {
    private static final long DELAY_BETWEEN_EXECUTIONS_IN_MS = 5000;
    private static final long TIME_TO_WAIT_FOR_SHUTDOWN_IN_MS = 10000;

    private static final Logger logger = LoggerFactory
            .getLogger(BackgroundSync.class);

    private static class Executor implements Runnable {
        private static final Logger logger = LoggerFactory
                .getLogger(Executor.class);

        private SyncSearchFromCatalogue syncSearchFromCatalogue;

        private Executor(SyncSearchFromCatalogue syncSearchFromCatalogue) {
            this.syncSearchFromCatalogue = syncSearchFromCatalogue;
        }

        @Override
        public void run() {
            logger.trace("Beginning scheduled sync task");
            try {
                syncSearchFromCatalogue.sync();
            } catch (Exception ex) {
                logger.error("Unexpected failure sync-ing catalogue", ex);
            }
            logger.trace("Ending scheduled sync task, next will run in {}ms",
                    DELAY_BETWEEN_EXECUTIONS_IN_MS);
        }
    }

    private SyncSearchFromCatalogue syncSearchFromCatalogue;
    private ScheduledExecutorService scheduledExecutorService;

    @Inject
    public BackgroundSync(SyncSearchFromCatalogue syncSearchFromCatalogue) {
        this.syncSearchFromCatalogue = syncSearchFromCatalogue;
        this.scheduledExecutorService = null;
    }

    public synchronized void start() throws RejectedExecutionException {
        if (this.scheduledExecutorService != null) {
            throw new IllegalStateException("Already started");
        }

        logger.trace("Starting up scheduled executor");
        this.scheduledExecutorService = Executors
                .newSingleThreadScheduledExecutor();
        this.scheduledExecutorService.scheduleWithFixedDelay(new Executor(
                syncSearchFromCatalogue), 0, DELAY_BETWEEN_EXECUTIONS_IN_MS,
                TimeUnit.MILLISECONDS);
    }

    public synchronized void stop() {
        if (this.scheduledExecutorService != null) {
            logger.trace("Shutting down scheduled executor");
            this.scheduledExecutorService.shutdown();

            try {
                logger.trace("Waiting {}ms for shutdown to complete");
                if (!this.scheduledExecutorService.awaitTermination(
                        TIME_TO_WAIT_FOR_SHUTDOWN_IN_MS, TimeUnit.MILLISECONDS)) {
                    logger.warn("Timed out waiting for clean shut down, "
                            + "killing executor");
                    this.scheduledExecutorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                logger.warn("Interrupted while awaiting clean shut down", e);
            }

            this.scheduledExecutorService = null;
        } else {
            logger.debug("Attempting to stop already stopped executor");
        }
    }
}

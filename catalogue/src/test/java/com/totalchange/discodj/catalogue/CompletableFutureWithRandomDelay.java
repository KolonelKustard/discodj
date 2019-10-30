package com.totalchange.discodj.catalogue;

import com.totalchange.discodj.server.media.MediaEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

public final class CompletableFutureWithRandomDelay {
    private static final Logger logger = LoggerFactory.getLogger(CompletableFutureWithRandomDelay.class);

    public static <T> CompletableFuture<T> completeInABitWithThing(final int minMs, final int maxMs, final T thing) {
        final int delay = randomNumberBetween(minMs, maxMs);

        final CompletableFuture<T> completableFuture = new CompletableFuture<>();
        Executors.newSingleThreadExecutor(new NamedThreadFactory("randomly-delayed")).submit(() -> {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                logger.error("Errrr interrupted", e);
                completableFuture.completeExceptionally(e);
            }

            logger.debug("Completing after {} with thing '{}'", delay, thing);
            completableFuture.complete(thing);
        });
        return completableFuture;
    }

    private static int randomNumberBetween(int min, int max) {
        final double randomNum = Math.random();
        final double range = max - min;
        final double randomRange = range * randomNum;
        return min + (int) Math.round(randomRange);
    }
}

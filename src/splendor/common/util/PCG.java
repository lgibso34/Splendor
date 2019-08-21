/*
 * minimalist PCG Random Number Generation for Java.
 *
 * Copyright 2014 Melissa O'Neill <oneill@pcg-random.org>, 2019 Shawn Shadrix <shshadri@ncsu.edu>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * For additional information about the PCG random number generation scheme,
 * including its license and other licensing options, visit
 *
 *       http://www.pcg-random.org
 */
package splendor.common.util;

import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.concurrent.atomic.AtomicLong;

public class PCG {
    private AtomicLong state;
    private long inc;

    private final static long MULTIPLIER = 6364136223846793005L;

    public PCG() {
        seed();
    }

    public PCG(long initState, long initSeq) {
        seed(initState, initSeq);
    }

    public int next(int bits) {
        long oldState, nextState;
        AtomicLong seed = this.state;
        do {
            oldState = seed.get();
            nextState = oldState * MULTIPLIER + inc;
        } while (!seed.compareAndSet(oldState, nextState));

        int xorShifted = (int) (((nextState >>> 18) ^ nextState) >>> 27);
        int rot = (int) (nextState >>> 59);
        return Integer.rotateRight(xorShifted, rot) >>> (32 - bits);
    }

    public int nextInt() {
        return next(32);
    }

    public long nextInt(int upperBound) { //stolen from Java.util.Random
        if (upperBound <= 0)
            throw new IllegalArgumentException("upperBound must be a non-zero positive integer");

        int r = next(31);
        int m = upperBound - 1;
        if ((upperBound & m) == 0)  // i.e., bound is a power of 2
            r = (int)((upperBound * (long)r) >> 31);
        else {
            for (int u = r;
                 u - (r = u % upperBound) + m < 0;
                 u = next(31));
        }
        return r;
    }

    public boolean nextBoolean() {
        return next(1) != 1;
    }

    private void seed(long initState, long initSeq) {
        this.state = new AtomicLong(0);
        inc = 2 * initSeq + 1;
        nextInt();
        this.state.set(state.get() + initState);
        nextInt();
    }

    private void seed() {
        seed(System.nanoTime(), getHQLongSeed());
    }

    private static long getHQLongSeed() {
        SecureRandom sec = new SecureRandom();
        byte[] sBuf = sec.generateSeed(8);
        ByteBuffer bb = ByteBuffer.wrap(sBuf);
        return bb.getLong();
    }
}

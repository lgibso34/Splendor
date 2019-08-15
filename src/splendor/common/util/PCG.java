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

public class PCG {
    private long state;
    private long inc;

    private final static long PERMUTATION = 6364136223846793005L;

    public PCG() {
        seed();
    }
    public PCG(long initState, long initSeq) {
        seed(initState, initSeq);
    }

    public long nextInt() {
        long oldState = state;
        state = oldState * PERMUTATION + inc;
        int xorShifted = (int) (((oldState >>> 18) ^ oldState) >>> 27);
        int rot = (int) (oldState >>> 59);
        //return (xorShifted >>> rot) | (xorShifted << ((-rot) & 31));
        return Integer.toUnsignedLong(Integer.rotateRight(xorShifted, rot));
    }

    public long nextInt(int upperBound) {
        if (upperBound <= 0)
            throw new IllegalArgumentException("upperBound must be a non-zero positive integer");
        int threshold = (0x10000000 - upperBound) % upperBound;
        long output = nextInt();
        while (output < threshold)
            output = nextInt();
        return output % upperBound;
    }

    public boolean nextBoolean() {
        return nextInt(2) == 1;
    }

    private void seed(long initState, long initSeq) {
        state = 0;
        inc = 2 * initSeq + 1;
        nextInt();
        state += initState;
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

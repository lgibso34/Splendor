package lib.test;

import splendor.common.util.PCG;

class testPRNG {
    private static final PCG rand = new PCG();
    private static int numNegative = 0;
    private static int numHeads = 0;
    private static int fourPlus = 0;

    public static void main(String[] args) {
        int numTrials = 10000;
        for(int i = 0; i < numTrials; i++){
            if(rand.nextInt() < 0)
                numNegative++;
        }

        for(int i = 0; i < numTrials; i++){
            if(rand.nextInt(6) > 2) //0..5 represents 1..6, >2 => 3,4,5 == 4,5,6
                fourPlus++;
        }

        for(int i = 0; i < numTrials; i++){
            if(rand.nextBoolean())
                numHeads++;
        }

        System.out.println("numTrials: " + numTrials);
        System.out.println("numNeg: " + numNegative + " " + (float) numNegative / numTrials * 100 + "%");
        System.out.println("fourPlus: " + fourPlus + " " + (float) fourPlus / numTrials * 100 + "%");
        System.out.println("numHeads: " + numHeads + " " + (float) numHeads / numTrials * 100 + "%");
    }
}
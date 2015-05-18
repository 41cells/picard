package picard.analysis;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

/**
 * Created by davidben on 5/18/15.
 */
public class TheoreticalSensitivityTest {

    @Test
    public void testRouletteWheel() throws Exception {

        //test that a deterministic roulette wheel only gives one value
        List<Double> deterministicWeights = Arrays.asList(0.0, 1.0, 0.0);
        final TheoreticalSensitivity.RouletteWheel deterministicWheel = new TheoreticalSensitivity.RouletteWheel(deterministicWeights);
        for (int n = 0; n < 10; n++) Assert.assertEquals(deterministicWheel.draw(), 1);

        //test the sums of this deterministic wheel: a sum of n 1's equals n
        List<ArrayList<Double>> deterministicSums = deterministicWheel.sampleCumulativeSums(10, 1);
        for (int n = 0; n < 10; n++) Assert.assertEquals(deterministicSums.get(n).get(0), n);

        //test that an unfair coin w/ probability 1/4 of heads gives roughly 1/4 heads
        double p = 0.25;
        double sum = 0.0;
        int N = 10000;
        double stdDev = Math.sqrt(p*(1-p)/N);
        List<Double> unfairCoinWeights = Arrays.asList(1-p, p);
        final TheoreticalSensitivity.RouletteWheel coinFlipWheel = new TheoreticalSensitivity.RouletteWheel(unfairCoinWeights);
        for (int n = 0; n < N; n++) sum += coinFlipWheel.draw();
        double average = sum / N;
        Assert.assertEquals(average, p, 10*stdDev);
    }

}

package csp15cap.fitstart;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class FatSecretAddFragmentTest {

    @Test
    public void extractNumbersFromDesc_NonFloatNumbers() {
        //ARRANGE
        FatSecretAddFragment testfragment = new FatSecretAddFragment();
        ArrayList<Long> expected = new ArrayList<>();
        int serving = 100;
        expected.add(4504L);//cals
        expected.add(815L);//carbs
        expected.add(52L);//protein
        expected.add(144L);//fat
        String testString = "Per 100g - Calories: 4504kcal | Fat: 144g | Carbs: 815g | Protein: 52g";

        //ACT
        ArrayList<Long> result = testfragment.extractNumbersFromDesc(testString, serving);

        //ASSERT
        assertEquals(expected.get(0), result.get(0));
        assertEquals(expected.get(1), result.get(1));
        assertEquals(expected.get(2), result.get(2));
        assertEquals(expected.get(3), result.get(3));
    }

    @Test
    public void extractNumbersFromDesc_NonFloatNumbers_ServingSizeLessThan100() {
        //ARRANGE
        FatSecretAddFragment testfragment = new FatSecretAddFragment();
        ArrayList<Long> expected = new ArrayList<>();
        int serving = 25;//serving size
        expected.add(50L);//cals
        expected.add(56L);//carbs
        expected.add(2L);//protein
        expected.add(1L);//fat
        String testString = "Per 100g - Calories: 200kcal | Fat: 4g | Carbs: 20g | Protein: 8g";

        //ACT
        ArrayList<Long> result = testfragment.extractNumbersFromDesc(testString, serving);

        //ASSERT
        assertEquals(expected.get(0), result.get(0));
        assertEquals(expected.get(1), result.get(1));
        assertEquals(expected.get(2), result.get(2));
        assertEquals(expected.get(3), result.get(3));
    }

    @Test
    public void extractNumbersFromDesc_NonFloatNumbers_ServingSizeGreaterThan100() {
        //ARRANGE
        FatSecretAddFragment testfragment = new FatSecretAddFragment();
        ArrayList<Long> expected = new ArrayList<>();
        int serving = 150;//serving size
        expected.add(300L);//cals
        expected.add(30L);//carbs
        expected.add(12L);//protein
        expected.add(6L);//fat
        String testString = "Per 100g - Calories: 200kcal | Fat: 4g | Carbs: 20g | Protein: 8g";

        //ACT
        ArrayList<Long> result = testfragment.extractNumbersFromDesc(testString, serving);

        //ASSERT
        assertEquals(expected.get(0), result.get(0));
        assertEquals(expected.get(1), result.get(1));
        assertEquals(expected.get(2), result.get(2));
        assertEquals(expected.get(3), result.get(3));
    }

    @Test
    public void extractNumbersFromDesc_FloatNumbers() {
        //ARRANGE
        FatSecretAddFragment testfragment = new FatSecretAddFragment();
        ArrayList<Long> expected = new ArrayList<>();
        int serving = 100;
        expected.add(501L);//cals
        expected.add(159L);//carbs
        expected.add(28L);//protein
        expected.add(26L);//fat
        String testString = "Per 100g - Calories: 500.5kcal | Fat: 25.8g | Carbs: 158.6g | Protein: 28.2g";

        //ACT
        ArrayList<Long> result = testfragment.extractNumbersFromDesc(testString, serving);

        //ASSERT
        assertEquals(expected.get(0), result.get(0));
        assertEquals(expected.get(1), result.get(1));
        assertEquals(expected.get(2), result.get(2));
        assertEquals(expected.get(3), result.get(3));
    }
    @Test
    public void extractNumbersFromDesc_FloatNumbers_ServingSizeLessThan100() {
        //ARRANGE
        FatSecretAddFragment testfragment = new FatSecretAddFragment();
        ArrayList<Long> expected = new ArrayList<>();
        int serving = 25;
        expected.add(125L);//cals
        expected.add(40L);//carbs
        expected.add(7L);//protein
        expected.add(6L);//fat
        String testString = "Per 100g - Calories: 500.5kcal | Fat: 25.8g | Carbs: 158.6g | Protein: 28.2g";

        //ACT
        ArrayList<Long> result = testfragment.extractNumbersFromDesc(testString, serving);

        //ASSERT
        assertEquals(expected.get(0), result.get(0));
        assertEquals(expected.get(1), result.get(1));
        assertEquals(expected.get(2), result.get(2));
        assertEquals(expected.get(3), result.get(3));
    }
    @Test
    public void extractNumbersFromDesc_FloatNumbers_ServingSizeGreaterThan100() {
        //ARRANGE
        FatSecretAddFragment testfragment = new FatSecretAddFragment();
        ArrayList<Long> expected = new ArrayList<>();
        int serving = 200;
        expected.add(1001L);//cals
        expected.add(317L);//carbs
        expected.add(56L);//protein
        expected.add(52L);//fat
        String testString = "Per 100g - Calories: 500.5kcal | Fat: 25.8g | Carbs: 158.6g | Protein: 28.2g";

        //ACT
        ArrayList<Long> result = testfragment.extractNumbersFromDesc(testString, serving);

        //ASSERT
        assertEquals(expected.get(0), result.get(0));
        assertEquals(expected.get(1), result.get(1));
        assertEquals(expected.get(2), result.get(2));
        assertEquals(expected.get(3), result.get(3));
    }
}
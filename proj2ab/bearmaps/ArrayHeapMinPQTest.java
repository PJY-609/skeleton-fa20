package bearmaps;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class ArrayHeapMinPQTest {
    @Test
    public void sanityCheck() {
        Random rand = new Random();

        ArrayHeapMinPQ<Integer> pq = new ArrayHeapMinPQ<>();

        for (int i = 0; i < 10000; i++) {
            int item = rand.nextInt();
            if (!pq.contains(item)) {
                double p = 100 * rand.nextDouble();
                pq.add(item, p);
            }
        }
    }

    @Test
    public void getSmallestTest() {
        Random rand = new Random();

        ArrayHeapMinPQ<Integer> pq = new ArrayHeapMinPQ<>();
        Integer minItem = null;
        double minPriority = Double.MAX_VALUE;
        
        for (int i = 0; i < 100000; i++) {
            int item = rand.nextInt();
            if (!pq.contains(item)) {
                double p = 100 * rand.nextDouble();
                pq.add(item, p);
                
                if (p < minPriority) {
                    minPriority = p;
                    minItem = item;
                }
            }
        }

        assertEquals(pq.getSmallest(), minItem);
    }

    @Test
    public void containsTest() {
        Random rand = new Random();

        ArrayHeapMinPQ<Integer> pq = new ArrayHeapMinPQ<>();
        ArrayList<Integer> items = new ArrayList<>();

        for (int i = 0; i < 100000; i++) {
            int item = rand.nextInt();
            if (!pq.contains(item)) {
                double p = 100 * rand.nextDouble();
                pq.add(item, p);

                items.add(item);
            }
        }

        HashSet<Integer> contained = new HashSet<>(items);

        for (int i = 1; i < 20; i++) {
            int index = rand.nextInt(100000);
            int item = items.get(index);
            assertEquals(contained.contains(item), pq.contains(item));
        }

        for (int i = 1; i < 20; i++) {
            int item = rand.nextInt();
            assertEquals(contained.contains(item), pq.contains(item));
        }
    }

    @Test
    public void removeSmallestTest() {
        Random rand = new Random();

        ArrayHeapMinPQ<Double> pq = new ArrayHeapMinPQ<>();
        ArrayList<Double> items = new ArrayList<>();

        for (int i = 0; i < 100000; i++) {
            Double item = 100 * rand.nextDouble();
            if (!pq.contains(item)) {

                pq.add(item, item);

                items.add(item);
            }
        }

        Collections.sort(items);

        for (Double item: items) {
            Double rm = pq.removeSmallest();
            assertEquals(item, rm);
        }
    }

    @Test
    public void changePriorityTest() {
        Random rand = new Random();

        ArrayHeapMinPQ<Double> pq = new ArrayHeapMinPQ<>();
        ArrayList<Double> items = new ArrayList<>();

        for (int i = 0; i < 100000; i++) {
            Double item = 100 * rand.nextDouble();
            if (!pq.contains(item)) {

                pq.add(item, item);

                items.add(-item);
            }
        }

        for (Double item : items) {
            pq.changePriority(-item, item);
        }

        Collections.sort(items);

        for (Double item: items) {
            Double rm = pq.removeSmallest();
            rm = -rm;
            assertEquals(item, rm);
        }
    }
}

package gemini.util.sort;

/**
 * A Sorter implementation using the QuickSort algorithm.
 */
public class QuickSorter implements Sorter
{

/**
 * Sort the given array of objects using the given Comparator.
 */
public void
sort(Object[] objA, Comparator comp)
{
   sort(objA, 0, objA.length, comp);
}

/**
 * Sort the given portion of the array of objects using the given Comparator.
 */
public void
sort(Object[] objA, int offset, int length, Comparator comp)
{
   _sort(objA, offset, offset + length - 1, comp);
}

/**
 * Implementation of the QuickSort algorithm.
 */
private void
_sort(Object[] objA, int low, int high, Comparator comp)
{
   int tmpL = low;
   int tmpH = high;
   Object mid;

   if (high > low) {
      mid = objA[ (low + high)/2 ];

      // Partition the array around the midpoint
      while (tmpL <= tmpH) {
         while ((tmpL < high) && (comp.compare(objA[tmpL], mid) < 0)) {
            ++tmpL;
         }
         while ((tmpH > low) && (comp.compare(objA[tmpH], mid) > 0)) {
            --tmpH;
         }
         if (tmpL <= tmpH) {
            Object tmp = objA[tmpL];
            objA[tmpL] = objA[tmpH];
            objA[tmpH] = tmp;

            ++tmpL;
            --tmpH;
         }
      }

      // Sort the lower partition
      if (low < tmpH) {
         _sort(objA, low, tmpH, comp);
      }

      // Sort the upper partition
      if (tmpL < high) {
         _sort(objA, tmpL, high, comp);
      }
   }
}

public static void main(String[] args)
{
   Sorter    s = new QuickSorter();
   boolean res = SortTester.testAll(s);
   if (res) {
      System.exit(0);
   }
   System.exit(-1);
}

}

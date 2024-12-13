package Estruturas.SortSearch;

/**
 * The SortAndSearch class contains methods for sorting and searching arrays of objects.
 */
public class SortAndSearch {
    /**
     * Sorts the specified array of objects using an insertion
     * sort algorithm.
     *
     * @param data the array to be sorted
     */
    public static <T extends Comparable<? super T>> void insertionSort(T[] data) {
        for (int index = 1; index < data.length; index++) {
            T key = data[index];
            int position = index;

            /** Shift larger values to the right */
            while (position > 0 && data[position - 1].compareTo(key) > 0) {
                data[position] = data[position - 1];
                position--;
            }
            data[position] = key;
        }
    }

    /**
     * Sorts the specified array of objects using a bubble sort
     * algorithm.
     *
     * @param data the array to be sorted
     */
    public static <T extends Comparable<? super T>> void bubbleSort(T[] data) {
        int position, scan;
        T temp;
        for (position = data.length - 1; position >= 0; position--) {
            for (scan = 0; scan <= position - 1; scan++) {
                if (data[scan].compareTo(data[scan + 1]) > 0) {
                    /** Swap the values */
                    temp = data[scan];
                    data[scan] = data[scan + 1];
                    data[scan + 1] = temp;
                }
            }
        }
    }

    /**
     * Sorts the specified array of objects using the quick sort
     * algorithm.
     *
     * @param data the array to be sorted
     * @param min  the integer representation of the minimum value
     * @param max  the integer representation of the maximum value
     */
    public static <T extends Comparable<? super T>> void quickSort(T[] data, int min, int max) {
        int indexofpartition;
        if (max - min > 0) {
            /** Create partitions */
            indexofpartition = findPartition(data, min, max);
            /** Sort the left side */
            quickSort(data, min, indexofpartition - 1);
            /** Sort the right side */
            quickSort(data, indexofpartition + 1, max);
        }
    }

    /**
     * Used by the quick sort algorithm to find the partition.
     *
     * @param data the array to be sorted
     * @param min  the integer representation of the minimum value
     * @param max  the integer representation of the maximum value
     */
    private static <T extends Comparable<? super T>> int findPartition(T[] data, int min, int max) {
        int left, right;
        T temp, partitionelement;
        int middle = (min + max) / 2;

        // use middle element as partition
        partitionelement = data[middle];
        left = min;
        right = max;

        while (left < right) {
            /** search for an element that is > the partitionelement */
            while (data[left].compareTo(partitionelement) < 0) {
                left++;
            }

            /** search for an element that is < the partitionelement */
            while (data[right].compareTo(partitionelement) > 0) {
                right--;
            }

            /** swap the elements */
            if (left < right) {
                temp = data[left];
                data[left] = data[right];
                data[right] = temp;
            }
        }

        // Move partition element to partition index
        temp = data[min];
        data[min] = data[right];
        data[right] = temp;

        return right;
    }

    /**
     * Sorts the specified array of objects using the merge sort
     * algorithm.
     *
     * @param data the array to be sorted
     * @param min  the integer representation of the minimum value
     * @param max  the integer representation of the maximum value
     */
    public static <T extends Comparable<? super T>> void mergeSort(T[] data, int min, int max) {
        if (min >= max) {
            return;
        }

        // Create a temporary array for merging
        @SuppressWarnings("unchecked")
        T[] temp = (T[]) new Comparable[max - min + 1];

        int pivot = (min + max) / 2;

        // Recursively sort the two halves
        mergeSort(data, min, pivot);
        mergeSort(data, pivot + 1, max);

        // Merge the sorted halves
        int left = min;
        int right = pivot + 1;
        int index = 0;

        while (left <= pivot && right <= max) {
            if (data[left].compareTo(data[right]) <= 0) {
                temp[index++] = data[left++];
            } else {
                temp[index++] = data[right++];
            }
        }

        // Copy remaining elements from the left half, if any
        while (left <= pivot) {
            temp[index++] = data[left++];
        }

        // Copy remaining elements from the right half, if any
        while (right <= max) {
            temp[index++] = data[right++];
        }

        // Copy the merged data back into the original array
        System.arraycopy(temp, 0, data, min, temp.length);
    }

    /**
     * Searches the specified array of objects using a
     * linear search algorithm.
     *
     * @param data   the array to be sorted
     * @param min    the integer representation of the min value
     * @param max    the integer representation of the max value
     * @param target the element being searched for
     * @return true if the desired element is found
     */
    public static <T extends Comparable<? super T>> boolean linearSearch(T[] data, int min, int max, T target) {
        int index = min;
        boolean found = false;
        while (!found && index <= max) {
            if (data[index].compareTo(target) == 0)
                found = true;
            index++;
        }
        return found;
    }

    /**
     * Searches the specified array of objects using a
     * binary search algorithm.
     * 
     * @param data   the array to be sorted
     * @param min    the integer representation of the minimum value
     * @param max    the integer representation of the maximum value
     * @param target the element being searched for
     * @return true if the desired element is found
     */
    public static <T extends Comparable<? super T>> boolean binarySearch(T[] data, int min, int max, T target) {
        boolean found = false;
        int midpoint = (min + max) / 2; // determine the midpoint
        if (data[midpoint].compareTo(target) == 0)
            found = true;
        else if (data[midpoint].compareTo(target) > 0) {
            if (min <= midpoint - 1)
                found = binarySearch(data, min, midpoint - 1, target);
        } else if (midpoint + 1 <= max)
            found = binarySearch(data, midpoint + 1, max, target);
        return found;
    }
}

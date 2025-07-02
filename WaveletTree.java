import java.util.*;

// Main class representing a Wavelet Tree
public class WaveletTree
{
    private int low, high; // Range [low, high] that this node is responsible for
    private WaveletTree left, right; // Left and right children
    private int[] bitVectorPrefixSum; // Prefix sum of zeros in bitVector
    private boolean[] bitVector; // Bit vector: false for left, true for right

    // Constructor to build the Wavelet Tree
    public WaveletTree(int[] data, int minVal, int maxVal) 
    {
        low = minVal; // Assign lower bound
        high = maxVal; // Assign upper bound

        if (data.length == 0) return; // Base case: empty array, do nothing

        bitVectorPrefixSum = new int[data.length + 1]; // Prefix sum has length one more than bit vector
        bitVector = new boolean[data.length]; // Initialize bit vector for current node

        // Base case: all elements are equal
        if (low == high) 
        { 
            for (int i = 0; i < data.length; i++) 
            {
                bitVectorPrefixSum[i + 1] = bitVectorPrefixSum[i] + 1; // All go to left (or stay), count as zeros
                bitVector[i] = false; // Arbitrary: all go left since only one value exists
            }
            return; // Leaf node, no need to split further
        }

        int mid = (low + high) / 2; // Midpoint to divide values between left and right
        List<Integer> leftList = new ArrayList<>(); // List to store values going to left child
        List<Integer> rightList = new ArrayList<>(); // List to store values going to right child

        // Partition the input data based on mid value
        for (int i = 0; i < data.length; i++) 
        {
            if (data[i] <= mid) 
            {
                bitVector[i] = false; // Goes to left
                leftList.add(data[i]); // Add to left list
                bitVectorPrefixSum[i + 1] = bitVectorPrefixSum[i] + 1; // Increase prefix sum of zeros
            } 
            else 
            {
                bitVector[i] = true; // Goes to right
                rightList.add(data[i]); // Add to right list
                bitVectorPrefixSum[i + 1] = bitVectorPrefixSum[i]; // No change to zero count
            }
        }

        // Convert leftList to int array
        int[] leftArr = new int[leftList.size()];
        for (int i = 0; i < leftList.size(); i++) 
        {
            leftArr[i] = leftList.get(i);
        }

        // Convert rightList to int array
        int[] rightArr = new int[rightList.size()];
        for (int i = 0; i < rightList.size(); i++) 
        {
            rightArr[i] = rightList.get(i);
        }

        left = new WaveletTree(leftArr, low, mid); // Recursively build left subtree
        right = new WaveletTree(rightArr, mid + 1, high); // Recursively build right subtree
    }

    // Access the original value at a specific index
    public int access(int index) 
    {
        if (low == high) 
        {
            return low; // Leaf node reached, return value
        }

        boolean bit = bitVector[index]; // Get whether it went left or right
        int newIndex = rankBit(bit, index + 1) - 1; // Map to child index

        if (!bit) 
        {
            return left.access(newIndex); // Go to left subtree
        } 
        else 
        {
            return right.access(newIndex); // Go to right subtree
        }
    }

    // Count how many times x occurs from index 0 to i
    public int rankOccurrences(int i, int x) 
    {
        if (i < 0 || x < low || x > high) return 0; // Out of bounds or invalid x

        if (low == high) 
        {
            return Math.min(i + 1, bitVector.length); // All values match, return count up to i
        }

        int mid = (low + high) / 2; // Get mid value

        if (x <= mid) 
        {
            int newI = rankBit(false, i + 1) - 1; // Map to left subtree
            return newI < 0 ? 0 : left.rankOccurrences(newI, x); // Recurse left
        } 
        else 
        {
            int onesBefore = (i + 1) - bitVectorPrefixSum[i + 1]; // Count ones (right values)
            return right.rankOccurrences(onesBefore - 1, x); // Recurse right
        }
    }

    // Count how many bits (either 0 or 1) are in range [0, pos]
    public int rankBit(boolean bit, int pos) 
    {
        if (pos < 0 || pos >= bitVectorPrefixSum.length) 
        {
            throw new IllegalArgumentException("Position out of bounds"); // Invalid position
        }
        return bit ? pos - bitVectorPrefixSum[pos] : bitVectorPrefixSum[pos]; // Return count of bit
    }

    // Find the k-th smallest value in range [l, r]
    public int quantile(int l, int r, int k) {
        if (l > r || k < 1 || k > (r - l + 1)) 
        {
            throw new IllegalArgumentException("Invalid range or k"); // Input validation
        }

        if (low == high) return low; // Leaf node, return value

        int leftL = bitVectorPrefixSum[l]; // Map l to left subtree
        int leftR = bitVectorPrefixSum[r + 1]; // Map r+1 to left subtree
        int inLeft = leftR - leftL; // Number of elements in left subtree

        if (k <= inLeft) 
        {
            return left.quantile(leftL, leftR - 1, k); // Recurse left
        } 
        else 
        {
            int rightL = l - leftL; // Map l to right subtree
            int rightR = r - leftR + 1; // Map r to right subtree
            return right.quantile(rightL, rightR - 1, k - inLeft); // Recurse right
        }
    }

    // Print the structure of the Wavelet Tree
    public void printTree(String prefix) 
    {
        System.out.println(prefix + "[" + low + "-" + high + "] " + Arrays.toString(bitVectorPrefixSum)); // Print current node range and prefix sum
        if (left != null) left.printTree(prefix + "  L-"); // Recurse left with indentation
        if (right != null) right.printTree(prefix + "  R-"); // Recurse right with indentation
    }

    // Helper method to find minimum value in array
    public static int getMin(int[] arr) 
    {
        int min = arr[0]; // Start with first element
        for (int i = 1; i < arr.length; i++) 
        {
            if (arr[i] < min) 
            {
                min = arr[i]; // Update min
            }
        }
        return min; // Return minimum
    }

    // Helper method to find maximum value in array
    public static int getMax(int[] arr) 
    {
        int max = arr[0]; // Start with first element
        for (int i = 1; i < arr.length; i++) 
        {
            if (arr[i] > max) 
            {
                max = arr[i]; // Update max
            }
        }
        return max; // Return maximum
    }


}

import java.util.*;

// real life example / situation implementation of the algorithm.
public class SalaryAnalysis 
{
    public static void main(String[] args) 
    {
        // Creating a Scanner object to read input from the user
        Scanner input = new Scanner(System.in);

        // Welcome message to the user
        System.out.println("Welcome to TechCorp HR Salary Analyzer!");

        // Prompting user to enter employee salaries
        System.out.println("Enter employee salaries (space-separated): ");
        String line = input.nextLine().trim(); // Reading the line of input and removing leading/trailing spaces

        // Checking if the input line is empty
        if (line.isEmpty()) 
        {
            System.out.println("Salary list cannot be empty."); // Show error if input is empty
            return; // Exit the program
        }

        // Splitting the input string by spaces into individual elements
        String[] elements = line.split(" ");
        int[] salaries = new int[elements.length]; // Array to hold parsed integer salaries

        try 
        {
            // Parsing each string element into an integer salary
            for (int i = 0; i < elements.length; i++) 
            {
                salaries[i] = Integer.parseInt(elements[i]);
            }
        } 
        catch (NumberFormatException e) 
        {
            // Handling case when input is not a valid integer
            System.out.println("Invalid salary input. Only integers allowed.");
            return; // Exit the program
        }

        // Finding minimum and maximum salary values for Wavelet Tree bounds
        int min = WaveletTree.getMin(salaries);
        int max = WaveletTree.getMax(salaries);

        // Constructing the Wavelet Tree with salary data
        WaveletTree wt = new WaveletTree(salaries, min, max);

        wt.printTree(""); // Print tree structure

        // Confirming successful construction
        System.out.println("\nWavelet Tree constructed for employee salaries!");

        // Start of interactive menu loop
        while (true) 
        {
            // Displaying the main menu
            System.out.println("\nMenu:");
            System.out.println("1. Get salary of an employee");
            System.out.println("2. Count employees with a specific salary up to a position");
            System.out.println("3. Find the k-th smallest salary in a team range");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            int choice;
            try 
            {
                choice = input.nextInt(); // Read user menu selection
            } 
            catch (InputMismatchException e) 
            {
                System.out.println("Invalid input. Enter a number from 1–4."); // Handle non-integer input
                input.next(); // Clear invalid input token
                continue; // Go back to menu
            }

            // Handling user's menu choice
            switch (choice) 
            {
                case 1:
                    // Option 1: Get salary of an employee at a given index
                    System.out.print("Enter employee index (0-based): ");
                    int idx = input.nextInt(); // Read index
                    try 
                    {
                        // Display salary using Wavelet Tree's access method
                        System.out.println("Salary of employee[" + idx + "] = $" + wt.access(idx));
                    } 
                    catch (Exception e) 
                    {
                        // Handle out-of-bounds or other errors
                        System.out.println("Invalid index.");
                    }
                    break;

                case 2:
                    // Option 2: Count employees with specific salary up to a position
                    System.out.print("Enter position (i) and salary to count (x): ");
                    int i = input.nextInt(); // Read position
                    int x = input.nextInt(); // Read salary to count
                    try 
                    {
                        // Get count using rankOccurrences
                        int count = wt.rankOccurrences(i, x);
                        System.out.println("Employees with salary $" + x + " from index 0 to " + i + " = " + count);
                    } 
                    catch (Exception e) 
                    {
                        // Handle invalid input
                        System.out.println("Invalid input.");
                    }
                    break;

                case 3:
                    // Option 3: Find k-th smallest salary in a range
                    System.out.print("Enter start index (l), end index (r), and k: ");
                    int l = input.nextInt(); // Start index
                    int r = input.nextInt(); // End index
                    int k = input.nextInt(); // k-th position
                    try 
                    {
                        // Use quantile method to find k-th smallest salary
                        int salary = wt.quantile(l, r, k);
                        System.out.println("The " + k + "-th smallest salary in employees[" + l + " to " + r + "] = $" + salary);
                    } 
                    catch (Exception e) 
                    {
                        // Handle invalid range or out-of-bounds values
                        System.out.println("Invalid input or out of range.");
                    }
                    break;

                case 4:
                    // Option 4: Exit the application
                    System.out.println("Exiting Salary Analyzer. Goodbye!");
                    return; // Exit the program

                default:
                    // If user enters a number outside the 1-4 range
                    System.out.println("Please choose a valid option (1-4).");
            }
        }
    }
}
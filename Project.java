import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.util.function.*;
import java.util.stream.*;

interface Trackable {
    void trackPackage();
}

class Delivery {
    protected String destination;

    Delivery(String destination) {
        this.destination = destination;
    }

    void displayDelivery() {
        System.out.println("Destination: " + destination);
    }
}

// CO4: Inheritance, interface implementation
class CourierPackage extends Delivery implements Trackable, Comparable<CourierPackage> {

    private String packageId;
    private String sender;
    private String receiver;
    private double weight;
    private String status;

    static int packageCount = 0;
    static final String COMPANY = "FastTrack Couriers";

    // CO4: Default constructor
    CourierPackage() {
        this("UNKNOWN", "Unknown", "Unknown", 0.0, "Pending", "Unknown");
    }

    // CO4: Parameterized constructor
    CourierPackage(String packageId, String sender, String receiver,
                   double weight, String status, String destination) {
        super(destination);
        this.packageId = packageId;
        this.sender = sender;
        this.receiver = receiver;
        this.weight = weight;
        this.status = status;
        packageCount++;
    }

    // CO4: Constructor overloading
    CourierPackage(String packageId, String receiver, String destination) {
        this(packageId, "Unknown", receiver, 1.0, "Pending", destination);
    }

    // CO4: Getters and setters
    public String getPackageId() {
        return packageId;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public double getWeight() {
        return weight;
    }

    public String getStatus() {
        return status;
    }

    public String getDestination() {
        return destination;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // CO4: Method overriding
    @Override
    public void displayDelivery() {
        System.out.println("Package " + packageId + " is going to " + destination);
    }

    @Override
    public void trackPackage() {
        System.out.println("Package ID: " + packageId);
        System.out.println("Receiver: " + receiver);
        System.out.println("Destination: " + destination);
        System.out.println("Status: " + status);
    }

    // CO3: Method overloading
    void displayPackage() {
        System.out.println(this);
    }

    void displayPackage(boolean detailed) {
        if (detailed) {
            System.out.printf(
                "ID: %s | Sender: %s | Receiver: %s | Weight: %.2f kg | Destination: %s | Status: %s%n",
                packageId, sender, receiver, weight, destination, status
            );
        } else {
            System.out.println(packageId + " - " + status);
        }
    }

    // CO5: Comparable
    @Override
    public int compareTo(CourierPackage other) {
        return this.packageId.compareTo(other.packageId);
    }

    // CO5: String representation
    @Override
    public String toString() {
        return packageId + " | " + sender + " | " + receiver + " | "
                + weight + " kg | " + destination + " | " + status;
    }

    // CO5: Convert object to a file record
    String toFileString() {
        return String.join("|", packageId, sender, receiver,
                String.valueOf(weight), destination, status);
    }
}

// CO5: Comparator
class WeightComparator implements Comparator<CourierPackage> {
    @Override
    public int compare(CourierPackage a, CourierPackage b) {
        return Double.compare(a.getWeight(), b.getWeight());
    }
}

public class Project {

    // CO1: Constant and route array
    static final String FILE_NAME = "courier_data.txt";

    static String[] route = {
        "Warehouse", "City Hub", "Local Centre", "Delivery Area", "Customer"
    };

    // CO3: 2D array for delivery distance matrix
    static int[][] distances = {
        {0, 10, 20},
        {10, 0, 15},
        {20, 15, 0}
    };

    // CO6: Collections and generics
    static ArrayList<CourierPackage> packages = new ArrayList<>();
    static HashMap<String, CourierPackage> packageMap = new HashMap<>();
    static HashSet<String> destinations = new HashSet<>();

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        loadPackages();

        int choice;

        // CO2: do-while loop
        do {
            System.out.println("\n==================================");
            System.out.println(" COURIER DELIVERY ROUTE SYSTEM");
            System.out.println("==================================");
            System.out.println("1. Add Package");
            System.out.println("2. View All Packages");
            System.out.println("3. Track Package");
            System.out.println("4. Update Delivery Status");
            System.out.println("5. Display Route");
            System.out.println("6. Search Package");
            System.out.println("7. Sort Packages by ID");
            System.out.println("8. Sort Packages by Weight");
            System.out.println("9. Delivery Status Report");
            System.out.println("10. Destination Report");
            System.out.println("11. Display Distance Matrix");
            System.out.println("12. Save Packages");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            try {
                choice = Integer.parseInt(sc.nextLine());

                // CO2: switch statement
                switch (choice) {
                    case 1:
                        addPackage();
                        break;
                    case 2:
                        viewPackages();
                        break;
                    case 3:
                        trackPackage();
                        break;
                    case 4:
                        updateStatus();
                        break;
                    case 5:
                        displayRoute();
                        break;
                    case 6:
                        searchPackage();
                        break;
                    case 7:
                        sortById();
                        break;
                    case 8:
                        sortByWeight();
                        break;
                    case 9:
                        statusReport();
                        break;
                    case 10:
                        destinationReport();
                        break;
                    case 11:
                        displayMatrix();
                        break;
                    case 12:
                        savePackages();
                        break;
                    case 0:
                        savePackages();
                        System.out.println("Thank you for using the system!");
                        break;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
                choice = -1;
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                choice = -1;
            }

        } while (choice != 0);

        sc.close();
    }

    // CO3: Method and parameter passing
    static void addPackage() {

        System.out.print("Enter package ID: ");
        String id = sc.nextLine().trim();

        if (id.isEmpty() || packageMap.containsKey(id)) {
            System.out.println("ID is empty or already exists.");
            return;
        }

        System.out.print("Enter sender name: ");
        String sender = sc.nextLine().trim();

        System.out.print("Enter receiver name: ");
        String receiver = sc.nextLine().trim();

        System.out.print("Enter destination: ");
        String destination = sc.nextLine().trim();

        System.out.print("Enter weight in kg: ");

        try {
            double weight = Double.parseDouble(sc.nextLine());

            // CO1: Relational and logical operators
            if (weight <= 0 || sender.isEmpty()
                    || receiver.isEmpty() || destination.isEmpty()) {
                System.out.println("Enter valid package details.");
                return;
            }

            // CO1: Type casting and arithmetic expression
            double shippingCost = weight * 50.0;

            // CO4: Object creation
            CourierPackage p = new CourierPackage(
                    id, sender, receiver, weight, "Pending", destination
            );

            packages.add(p);
            packageMap.put(id, p);
            destinations.add(destination);

            System.out.printf("Package added successfully! Shipping estimate: Rs. %.2f%n",
                    shippingCost);

        } catch (NumberFormatException e) {
            System.out.println("Invalid weight.");
        }
    }

    // CO3: Enhanced for loop
    static void viewPackages() {

        if (packages.isEmpty()) {
            System.out.println("No packages available.");
            return;
        }

        for (CourierPackage p : packages) {
            p.displayPackage(true);
        }
    }

    static void trackPackage() {

        System.out.print("Enter package ID: ");
        String id = sc.nextLine().trim();

        CourierPackage p = packageMap.get(id);

        // CO2: if-else
        if (p != null) {
            p.trackPackage();
        } else {
            System.out.println("Package not found.");
        }
    }

    static void updateStatus() {

        System.out.print("Enter package ID: ");
        String id = sc.nextLine().trim();

        CourierPackage p = packageMap.get(id);

        if (p == null) {
            System.out.println("Package not found.");
            return;
        }

        System.out.println("1. Pending");
        System.out.println("2. In Transit");
        System.out.println("3. Out for Delivery");
        System.out.println("4. Delivered");
        System.out.print("Select new status: ");

        try {
            int option = Integer.parseInt(sc.nextLine());

            switch (option) {
                case 1:
                    p.setStatus("Pending");
                    break;
                case 2:
                    p.setStatus("In Transit");
                    break;
                case 3:
                    p.setStatus("Out for Delivery");
                    break;
                case 4:
                    p.setStatus("Delivered");
                    break;
                default:
                    System.out.println("Invalid status.");
                    return;
            }

            System.out.println("Delivery status updated successfully.");

        } catch (NumberFormatException e) {
            System.out.println("Enter a valid number.");
        }
    }

    // CO2: Nested loops
    static void displayRoute() {

        System.out.println("\nDelivery Route:");

        for (int i = 0; i < route.length; i++) {
            System.out.print(route[i]);

            if (i < route.length - 1) {
                System.out.print(" -> ");
            }
        }

        System.out.println("\n\nRoute Stops:");

        for (int i = 0; i < route.length; i++) {
            for (int j = 0; j <= i; j++) {
                System.out.print("*");
            }
            System.out.println(" " + route[i]);
        }
    }

    // CO3: Searching and recursion
    static void searchPackage() {

        System.out.print("Enter package ID to search: ");
        String key = sc.nextLine().trim();

        int index = recursiveSearch(packages, key, 0);

        if (index >= 0) {
            System.out.println("Package found:");
            packages.get(index).displayPackage(true);
        } else {
            System.out.println("Package not found.");
        }
    }

    // CO3: Recursive method with base case
    static int recursiveSearch(ArrayList<CourierPackage> list,
                               String key, int index) {

        if (index >= list.size()) {
            return -1;
        }

        if (list.get(index).getPackageId().equalsIgnoreCase(key)) {
            return index;
        }

        return recursiveSearch(list, key, index + 1);
    }

    // CO5: Comparable and Collections.sort
    static void sortById() {

        Collections.sort(packages);

        System.out.println("Packages sorted by ID:");
        viewPackages();
    }

    // CO5: Comparator and library sorting algorithm
    static void sortByWeight() {

        packages.sort(new WeightComparator());

        System.out.println("Packages sorted by weight:");
        viewPackages();
    }

    // CO6: Streams, filter, map, sorted, forEach
    static void statusReport() {

        System.out.println("\nDelivery Status Report:");

        packages.stream()
                .collect(Collectors.groupingBy(
                        CourierPackage::getStatus,
                        Collectors.counting()
                ))
                .forEach((status, count) ->
                        System.out.println(status + ": " + count));

        System.out.println("\nDelivered Packages:");

        packages.stream()
                .filter(p -> p.getStatus().equals("Delivered"))
                .map(CourierPackage::getPackageId)
                .sorted()
                .forEach(System.out::println);

        long totalDelivered = packages.stream()
                .filter(p -> p.getStatus().equals("Delivered"))
                .count();

        System.out.println("Total delivered: " + totalDelivered);
    }

    // CO6: Set, distinct destinations and joining collector
    static void destinationReport() {

        System.out.println("\nUnique Destinations:");

        destinations.stream()
                .sorted()
                .forEach(System.out::println);

        String allDestinations = destinations.stream()
                .sorted()
                .collect(Collectors.joining(", "));

        System.out.println("All destinations: " + allDestinations);
    }

    // CO3: 2D arrays and matrix arithmetic
    static void displayMatrix() {

        System.out.println("Distance Matrix:");

        for (int i = 0; i < distances.length; i++) {
            for (int j = 0; j < distances[i].length; j++) {
                System.out.printf("%5d", distances[i][j]);
            }
            System.out.println();
        }

        // CO1: Operator precedence and arithmetic
        int example = 10 + 5 * 2;
        System.out.println("Precedence example (10 + 5 * 2): " + example);
    }

    // CO5: File writing with Path, Files and try-with-resources
    static void savePackages() {

        Path path = Paths.get(FILE_NAME);

        try (BufferedWriter writer = Files.newBufferedWriter(path)) {

            for (CourierPackage p : packages) {
                writer.write(p.toFileString());
                writer.newLine();
            }

            System.out.println("Package data saved to " + path.toAbsolutePath());

        } catch (IOException e) {
            System.out.println("Error saving packages: " + e.getMessage());
        }
    }

    // CO5: File reading, split, String methods and malformed-data handling
    static void loadPackages() {

        Path path = Paths.get(FILE_NAME);

        if (!Files.exists(path)) {
            System.out.println("No previous data file found. Starting fresh.");
            return;
        }

        try (Stream<String> lines = Files.lines(path)) {

            lines.forEach(line -> {
                try {
                    String[] data = line.split("\\|", -1);

                    if (data.length != 6) {
                        System.out.println("Skipping malformed record.");
                        return;
                    }

                    double weight = Double.parseDouble(data[3]);

                    CourierPackage p = new CourierPackage(
                            data[0], data[1], data[2],
                            weight, data[5], data[4]
                    );

                    packages.add(p);
                    packageMap.put(p.getPackageId(), p);
                    destinations.add(p.getDestination());

                } catch (NumberFormatException e) {
                    System.out.println("Skipping record with invalid weight.");
                }
            });

        } catch (IOException e) {
            System.out.println("Unable to load data: " + e.getMessage());
        }
    }
}
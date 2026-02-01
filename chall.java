import java.util.*;
import java.io.*;

class Inventory implements Serializable {
    private static final long serialVersionUID = 1L;

    public String bname;
    public String bauthor;
    public String bGenre;
    public boolean isBorrowed;
    public boolean isArchived;

    public Inventory(String bname, String bauthor, String bGenre) {
        this.bname = bname;
        this.bauthor = bauthor;
        this.bGenre = bGenre;
        this.isBorrowed = false;
        this.isArchived = false;
    }

    @Override
    public String toString() {
        String status = isBorrowed ? "[BORROWED]    " : "[AVAILABLE]    ";
        String avail = isArchived ?"[ARCHIVED]  ":"";
        return avail + status + "Book: " + bname + " | Author: " + bauthor + " | Genre: " + bGenre;
    }
}

public class chall {

    public void mainMenu(){
        
        System.out.print("\033[H\033[2J");
        System.out.flush();

        System.out.println("\n-----|LIBRARY INVENTORY SYSTEM|------");
        System.out.println("1) Add Book");
        System.out.println("2) Update Book Info");
        System.out.println("3) View Book list");
        System.out.println("4) Borrowed Books");
        System.out.println("5) Archived Book");
        System.out.println("0) Exit");
    }

    void writeString(Inventory inv) {
        ArrayList<Inventory> currentLibrary = readAll();
        currentLibrary.add(inv);

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("inventory.txt"))) {
            out.writeObject(currentLibrary);
            System.out.println("Book saved. Total books: " + currentLibrary.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public ArrayList<Inventory> readAll() {
        File file = new File("inventory.txt");
        if (!file.exists() || file.length() == 0) return new ArrayList<>();

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (ArrayList<Inventory>) in.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    public void saveAll(ArrayList<Inventory> list) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("inventory.txt"))) {
            out.writeObject(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getValidChoice(Scanner input) {
    while (true) {
            try {
                String str = input.nextLine();
                return Integer.parseInt(str);
            } catch (NumberFormatException e) {
                System.out.print("Invalid input! Please enter a Displayed number: ");
            }
        }
    }
    public boolean getValidYesNo(Scanner input) {
    while (true) {
        String str = input.nextLine().trim();

        if (str.equalsIgnoreCase("yes")) {
            return true;
        } else if (str.equalsIgnoreCase("no")) {
            return false;
        } else {
            System.out.print("Invalid input! Please enter yes or no: ");
        }
    }
}

    public static void main(String[] args) {
        chall main = new chall();
        Scanner input = new Scanner(System.in);
        int choice;

        do {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            main.mainMenu();
            System.out.print("Enter choice: ");
            choice = main.getValidChoice(input);
            System.out.print("\033[H\033[2J");
            System.out.flush();

            switch (choice) {
                case 1:

                    System.out.println("----Add Book----");
                    System.out.print("Enter Book Name: "); 
                    String bname = input.nextLine();
                    System.out.print("Enter Book Author: "); 
                    String bauthor = input.nextLine();
                    System.out.print("Enter Genre: "); 
                    String bGenre = input.nextLine();

                    Inventory inv = new Inventory(bname, bauthor, bGenre);
                    main.writeString(inv);

                    System.out.println("\n[Book Added Successfully]");
                    break;
                case 2:
                    System.out.println("----Update Book Info----");
                    ArrayList<Inventory> Update = main.readAll();

                    for (Inventory b : Update) {
                        System.out.println(b);
                    }

                    System.out.print("\nEnter the EXACT name of the book to Update: ");
                    String targetName = input.nextLine();
                
                    boolean found = false;

                    
                    for (Inventory book : Update) {
                        if (book.bname.equalsIgnoreCase(targetName)) {
                            System.out.println("----Enter new details----");

                            System.out.print("New Name: ");
                            book.bname = input.nextLine();

                            System.out.print("New Author:");
                            book.bauthor = input.nextLine();

                            System.out.print("New Genre:");
                            book.bGenre = input.nextLine();
                        
                            found = true;
                            break;
                        }
                    }
                
                    if (found) {
                        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("inventory.txt"))) {
                            out.writeObject(Update);
                            System.out.println("[Book updated successfully]");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("Book not found, No change.");
                    }
                    break;
                case 3:
                    System.out.println("----Available Books----");
                    ArrayList<Inventory> list = main.readAll();
                    for (Inventory b : list) {
                        if (!b.isBorrowed) System.out.println(b);
                    }
                    System.out.print("\nBorrow a book? (yes/no): ");
                    boolean yesO = main.getValidYesNo(input);

                    if (yesO) {
                        System.out.print("Name of book: ");
                        String target = input.nextLine();

                        boolean f = false;
                        for (Inventory b : list) {
                            if (b.bname.equalsIgnoreCase(target) && !b.isBorrowed) {
                                b.isBorrowed = true;
                                f = true;
                                break;
                            }
                        }

                        if (f) {
                            main.saveAll(list);
                            System.out.println("[Status updated to Borrowed]");
                        } else {
                            System.out.println("Book not found or already borrowed.");
                        }
                    }
                    break;
                case 4:
                    System.out.println("----Borrowed Books List----");
                    ArrayList<Inventory> allBooks = main.readAll();
                    boolean anyBorrowed = false;

                    for (Inventory b : allBooks) {
                        if (b.isBorrowed) {
                            System.out.println(b);
                            anyBorrowed = true;
                        }
                    }
                    System.out.print("\nReturn Book?(yes/no):");
                    Boolean rChoice = main.getValidYesNo(input);
                    if(rChoice){
                        System.out.print("Name of book: ");
                        String target = input.nextLine();
                        boolean f = false;
                        for (Inventory b : allBooks) {
                            if (b.bname.equalsIgnoreCase(target) && b.isBorrowed) {
                                b.isBorrowed = false;
                                f = true;
                                break;
                            }
                        }
                        if (f) {
                            main.saveAll(allBooks);
                            System.out.println("Status updated to Returned.");
                        } else {
                            System.out.println("Book not found or already Returned.");
                        }
                    }   
                    //////////////////////////////////////////////////
                    if (!anyBorrowed) System.out.println("No books borrowed.");
                    break;
                //////////////////////////////////////////////////////////

                case 5:
                    System.out.println("----Archived Books----");
                    ArrayList<Inventory> arBooks = main.readAll();
                    boolean anyArchived = false;

                    for (Inventory b : arBooks) {
                        if (b.isArchived) {
                            System.out.println(b);
                            anyArchived = true;
                        }
                    }
                    System.out.print("\n1) Restore Book\n2) Archive Book\nEnter:");
                    int arChoice = main.getValidChoice(input);

                    switch (arChoice) {
                        case 1:
                            System.out.print("Name of book: ");
                            String targetRestore = input.nextLine();
                            boolean rf = false;
                            for (Inventory b : arBooks) {
                                if (b.bname.equalsIgnoreCase(targetRestore) && b.isArchived) {
                                    b.isArchived = false;
                                    rf = true;
                                    break;
                                }
                            }
                            if (rf) {
                                main.saveAll(arBooks);
                                System.out.println("Successfully Restored.");
                            } else {
                                System.out.println("Book not found or already Restored.");
                            }
                            break;
                        case 2:
                            for (Inventory b : arBooks) {
                                if (!b.isArchived) {
                                    System.out.println(b);
                                }
                            }
                            System.out.print("Name of book: ");
                            String targetArchive = input.nextLine();
                            boolean af = false;
                            for (Inventory b : arBooks) {
                                if (b.bname.equalsIgnoreCase(targetArchive)) {
                                    b.isArchived = true;
                                    b.isBorrowed = false;
                                    anyArchived = true;
                                    af = true;
                                    break;
                                }
                            }
                            if (af) {
                                main.saveAll(arBooks);
                                System.out.println("Successfully Archived.");
                            } else {
                                System.out.println("Book not found or already Archived.");
                            }
                            break;
                        default:
                            break;
                    }
                    //////////////////////////////////////////////////
                    if (!anyArchived) System.out.println("No books Archived.");
                    break;
                default:
                    System.out.println("[Thanks for using LIBRARY INVENTORY SYSTEM!]");
                    System.exit(0);
                    break;
            }
        } while (true);  
    }
}
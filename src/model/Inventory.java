package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
  @author  Shiana Wilson
  This class provides Inventory methods.
 **/
public class Inventory {
    // Provides the list of Part objects
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    // Provides the list of Product objects
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();


    /**
     * This method adds a Part object to the allParts ObservableList array.
     * This method can be accessed from all packages.
     *
     * @param newPart The new Part object to be added to the ObservableList.
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /**
     * This method will adds a product object to the allProducts ObservableList array.
     * This method can be accessed from all packages.
     *
     * @param newProduct The new Product object to be added to the ObservableList.
     */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    /**
     * This method returns the Part with a matching ID.
     * The method iterates through Inventory.getAllParts and returns
     * the first Part object with a matching ID.
     *
     * @param partId The ID for the Part object being searched for.
     * @return Returns either the matching Part, or null if not found.
     */
    public static Part PartLookUp(int partId) {
        for (int i = 0; i < Inventory.getAllParts().size(); i++) {
            Part p = Inventory.getAllParts().get(i);

            // Returns first match
            if (p.getId() == partId)
                return p;
        }
        return null;
    }

    /**
     This method will return the Product with a matching ID.
     The method iterates through Inventory.getAllProducts and will return
     the first Product object with a matching ID.

     @param productId The ID for the Product object being searched for.
     @return Returns either the matching Product, or null if not found.
     **/
    public static Product ProdLookUp(int productId) {
        for (int i = 0; i < Inventory.getAllProducts().size(); i++) {
            Product p = Inventory.getAllProducts().get(i);

            if (p.getId() == productId)
                return p;
        }
        return null;
    }

    /**
     * This method filters searched items on a TableView with Part objects.
     * This method is used to search a Part object by name and return matching items.
     *
     * @param partName A String representing the name of a Part or ID.
     * @return Returns all filtered Parts in an ObservableList if the
     * filtered parts list is not empty. Returns original ObservableList
     * if there are no filtered Parts.
     */
    public static ObservableList<Part> PartLookUp(String partName) {
        ObservableList<Part> allFilteredParts = FXCollections.observableArrayList();

        // Searches for matching characters, then adds to list
        for (Part p : Inventory.getAllParts()) {
            if (p.getName().contains(partName))
                allFilteredParts.add(p);
        }

        // Searches for integers, then adds to list
        try {
            int idPart = Integer.parseInt(partName);
            Part p = Inventory.PartLookUp(idPart);
            if (p != null)
                allFilteredParts.add(p);
        } catch (NumberFormatException e) {
            // ignore exception
        }

        return allFilteredParts;
    }

    /**
     * This method filters searched items on a TableView with Product objects.
     * This method is used to search a Product object by name and return matching items.
     *
     * @param productName A String representing the name of a Product or ID.
     * @return Returns all filtered Products in an ObservableList if the
     * filtered products list is not empty. Returns original ObservableList
     * if there are no filtered Products.
     */
    public static ObservableList<Product> ProdLookUp(String productName) {
        ObservableList<Product> allFilteredProducts = FXCollections.observableArrayList();

        for (Product p : Inventory.getAllProducts()) {
            if (p.getName().contains(productName))
                allFilteredProducts.add(p);
        }

        try {
            int productId = Integer.parseInt(productName);
            Product p = Inventory.ProdLookUp(productId);
            if (p != null)
                allFilteredProducts.add(p);
        } catch (NumberFormatException e) {
            // ignore exception
        }

        return allFilteredProducts;
    }

    /**
     * This method updates a selected Part at a provided index.
     * The method is similar to the updatePart method offered in the MainController
     * class.
     *
     * @param index        The index for the Part that will be updated in the ObservableList.
     * @param selectedPart The selected Part replacing the part at the index requested.
     */
    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    /**
     * This method implements a new Product at a provided index.
     * The method is similar to the updateProduct method offered in
     * the MainController class.
     *
     * @param index      The index for the Product that will be updated in the ObservableList.
     * @param newProduct The new Product replacing the Product at the index requested.
     */
    public static void updateProduct(int index, Product newProduct) {
        allProducts.set(index, newProduct);
    }

    /**
     * This method searches through Inventory and deletes a Part with a matching ID.
     * The method takes a Part as a parameter and deletes the matching
     * Part in Inventory.
     *
     * @param selectedPart The Part object being deleted.
     * @return Returns true if the Part object is removed from the
     * ObservableList, and returns false if item is not found.
     */
    public static boolean deletePart(Part selectedPart) {
        for (Part p : Inventory.getAllParts()) {
            if (p.getId() == selectedPart.getId()) {
                return Inventory.getAllParts().remove(p);
            }
        }
        return false;
    }

    /**
     * This method searches through Inventory and deletes a Product with a matching ID.
     * The method takes a Product as a parameter and deletes the matching
     * Product in Inventory.
     *
     * @param selectedProduct The Product object being deleted.
     * @return Returns true if the Product object is removed from the
     * ObservableList, and returns false if item is not found.
     */
    public static boolean deleteProduct(Product selectedProduct) {
        for (Product product : Inventory.getAllProducts()) {
            if (product.getId() == selectedProduct.getId()) {
                return Inventory.getAllProducts().remove(product);
            }
        }
        return false;
    }


    /**
     * This static method returns an ObservableList of all Part objects.
     * This method can be accessed from anywhere in the program to
     * make CRUD updates to the Inventory.
     *
     * @return Returns an ObservableList of all Part objects.
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }


    /**
     * This static method returns an ObservableList of all Product objects.
     * This method can be accessed from anywhere in the program to
     * make CRUD updates to the Inventory.
     *
     * @return Returns an ObservableList of all Product objects.
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }


}
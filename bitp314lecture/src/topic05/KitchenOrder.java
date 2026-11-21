package topic05;


//KitchenOrder: o
/**
 * This record represents one order ticket sent from a tablet to the kitchen,
 * e.g. #101 Kopi O. A record is a short way to declare a class that
 * only holds data: Java writes the constructor and the number() and
 * item() methods for you.
 */
public record KitchenOrder(int number, String item) { }
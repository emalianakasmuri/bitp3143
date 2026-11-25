package topic3.kopicorner;

/*
 * Program 3.5: OrderLine.java
 *
 * Program description:
 *   An immutable record holding one line of a Kopi Corner order. It implements
 *   Serializable so that whole objects can travel through an object stream.
 *
 * What it shows:
 *   The class shared by both sides of an object-based exchange. The tablet and
 *   the kitchen server must have the same version of this class.
 */
import java.io.Serializable;

/**
 * One line of a Kopi Corner order.
 *
 * @param orderNo  order number given by the kitchen, such as 104
 * @param itemCode menu item code, such as N01
 * @param itemName menu item name, such as Nasi lemak ayam
 * @param qty      quantity ordered
 */
public record OrderLine(int orderNo, String itemCode, String itemName, int qty)
        implements Serializable {
}
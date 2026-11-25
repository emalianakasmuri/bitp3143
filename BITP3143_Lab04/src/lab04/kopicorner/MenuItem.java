/*
 * Lab 04, Exercise 4: MenuItem.java
 *
 * Program description:
 *   An immutable record holding one item of the Kopi Corner menu. It
 *   implements Serializable so that it can travel through an object stream.
 */
package lab04.kopicorner;

import java.io.Serializable;

/**
 * One item of the Kopi Corner menu.
 *
 * @param code     menu item code, such as D02
 * @param name     menu item name, such as Teh Tarik
 * @param priceSen price in sen, such as 280
 */
public record MenuItem(String code, String name, int priceSen) implements Serializable {
}

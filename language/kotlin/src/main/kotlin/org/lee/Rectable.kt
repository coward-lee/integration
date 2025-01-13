package org.lee

// Inheritance between classes is declared by a colon (:). Classes are final by default; to make a class inheritable, mark it as open. ?
// interface ？
open class Shape


class Rectangle(var height: Double, var length: Double): Shape() {
    var perimeter = (height + length) * 2
}
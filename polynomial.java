package poly;

import java.io.IOException;
import java.util.Scanner;

/**
 * This class implements evaluate, add and multiply for polynomials.
 * 
 * @author runb-cs112
 *
 */
public class Polynomial {
	
	/**
	 * Reads a polynomial from an input stream (file or keyboard). The storage format
	 * of the polynomial is:
	 * <pre>
	 *     <coeff> <degree>
	 *     <coeff> <degree>
	 *     ...
	 *     <coeff> <degree>
	 * </pre>
	 * with the guarantee that degrees will be in descending order. For example:
	 * <pre>
	 *      4 5
	 *     -2 3
	 *      2 1
	 *      3 0
	 * </pre>
	 * which represents the polynomial:
	 * <pre>
	 *      4*x^5 - 2*x^3 + 2*x + 3 
	 * </pre>
	 * 
	 * @param sc Scanner from which a polynomial is to be read
	 * @throws IOException If there is any input error in reading the polynomial
	 * @return The polynomial linked list (front node) constructed from coefficients and
	 *         degrees read from scanner
	 */
	public static Node read(Scanner sc) 
	throws IOException {
		Node poly = null;
		while (sc.hasNextLine()) {
			Scanner scLine = new Scanner(sc.nextLine());
			poly = new Node(scLine.nextFloat(), scLine.nextInt(), poly);
			scLine.close();
		}
		return poly;
	}
	
	/**
	 * Returns the sum of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list
	 * @return A new polynomial which is the sum of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node add(Node poly1, Node poly2) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		
		Node pointer1 = poly1;
		Node pointer2 = poly2;
		
		Node resultingNode = null;
		Node pointer3 = resultingNode;
		
		if (poly1 == null && poly2 != null) { 
			Node p2 = null;
			Node p3 = p2;
			
			for (Node ptr = poly2; ptr != null; ptr = ptr.next) {
				if (p2 == null) {
					p2 = new Node(ptr.term.coeff, ptr.term.degree, null);
					p3 = p2;
					
				} else {
					p3.next = new Node(ptr.term.coeff, ptr.term.degree, null);
					p3 = p3.next;
				}
			}
			
			return p2;
			
		}  else if (poly2 == null && poly1 != null) {
			Node p2 = null;
			Node p3 = p2;
			
			for (Node ptr = poly1; ptr != null; ptr = ptr.next) {
				if (p2 == null) {
					p2 = new Node(ptr.term.coeff, ptr.term.degree, null);
					p3 = p2;
					
				} else {
					p3.next = new Node(ptr.term.coeff, ptr.term.degree, null);
					p3 = p3.next;
				}
			}
			
			return p2;
		} else if (poly1 == null && poly2 == null) {
			return null;
		}
		

		while (pointer1 != null) {
			while (pointer2 != null) {
				if (pointer1.term.degree == pointer2.term.degree) {
					if (resultingNode == null) {
						if ((pointer1.term.coeff + pointer2.term.coeff) == 0) {
							pointer2 = pointer2.next;
							pointer1 = pointer1.next;

							break;
						}
						resultingNode = new Node(pointer1.term.coeff + pointer2.term.coeff, pointer1.term.degree, null);
						pointer3 = resultingNode;
						pointer2 = pointer2.next;
						pointer1 = pointer1.next;

						break;
					} else {
						if ((pointer1.term.coeff + pointer2.term.coeff) == 0) {
							pointer2 = pointer2.next;
							pointer1 = pointer1.next;

							break;
						}
						pointer3.next = new Node(pointer1.term.coeff + pointer2.term.coeff, pointer1.term.degree, null);
						pointer3 = pointer3.next;
						pointer2 = pointer2.next;
						pointer1 = pointer1.next;

						break;
					}
				} else {
					if (pointer1.term.coeff == 0 && pointer2.term.coeff != 0) {
						if (resultingNode == null) {
							
							pointer1 = pointer1.next;
							
							resultingNode = new Node(pointer2.term.coeff, pointer2.term.degree, null);
							pointer3 = resultingNode;
							pointer2 = pointer2.next;
							
							break;
						}
						pointer1 = pointer1.next;
						
						pointer3.next = new Node(pointer2.term.coeff, pointer2.term.degree, null);
						pointer3 = pointer3.next;
						pointer2 = pointer2.next;
						
						break;
					} 
					
					if (pointer2.term.coeff == 0 && pointer1.term.coeff != 0) {
						if (resultingNode == null) {
							
							pointer2 = pointer2.next;
							
							resultingNode = new Node(pointer1.term.coeff, pointer1.term.degree, null);
							pointer3 = resultingNode;
							pointer1 = pointer1.next;
							
							break;
						}

						pointer2 = pointer2.next;
						
						pointer3.next = new Node(pointer1.term.coeff, pointer1.term.degree, null);
						pointer3 = pointer3.next;
						pointer1 = pointer1.next;
						
						break;
					}
					if (resultingNode == null) {
						resultingNode = new Node(pointer1.term.coeff, pointer1.term.degree, null);
						pointer3 = resultingNode;
						
						pointer3.next = new Node(pointer2.term.coeff, pointer2.term.degree, null);
						pointer3 = pointer3.next;
						
						pointer2 = pointer2.next;
						pointer1 = pointer1.next;
						break;
						
					}
					pointer3.next = new Node(pointer1.term.coeff, pointer1.term.degree, null);
					pointer3 = pointer3.next;
					
					pointer3.next = new Node(pointer2.term.coeff, pointer2.term.degree, null);
					pointer3 = pointer3.next;
					
					pointer2 = pointer2.next;
					pointer1 = pointer1.next;
					break;
				}
			}
			if (pointer2 == null && pointer1 != null) {
				pointer3.next = new Node(pointer1.term.coeff, pointer1.term.degree, null);
				pointer3 = pointer3.next;
				pointer1 = pointer1.next;

			}
		}
		
					
		return sortCombine(resultingNode);
	}
	
	private static Node sortCombine(Node poly) {
		
		Node p1 = poly;
		

		int max = 0;
		Node f = poly;
		
		while (f != null) {
			if (f.term.degree > max) {
				max = f.term.degree;
			}
			
			f = f.next;
		}
		
		f = poly;
		Node list = null;
		
		while (max >= 0) {
			while (f != null) {
				if (f.term.degree == max) {
					list = new Node(f.term.coeff, f.term.degree, list);
				}
				
				f = f.next;
			}
			
			f = poly;
			max--;
		}
		
		
		for (Node ptr = list; ptr != null; ptr = ptr.next) {
			for (Node ptr2 = ptr.next; ptr2 != null; ptr2 = ptr2.next) {
				if (ptr.term.degree == ptr2.term.degree) {
					ptr.term.coeff += ptr2.term.coeff;
					ptr.next = ptr2.next;
					
				}
		}
	}
		
		return list;
	}
	
	/** 
	 * Returns the product of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list)
	 * @return A new polynomial which is the product of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node multiply(Node poly1, Node poly2) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		
		Node pointer1 = poly1;
		Node pointer2 = poly2;
		
		Node resultingNode = null;
		Node pointer3 = resultingNode;
		
		if (poly1 == null || poly2 == null) {
			return null;
		}
		
		for (Node ptr1 = poly1; ptr1 != null; ptr1 = ptr1.next) {
			for (Node ptr2 = poly2; ptr2 != null; ptr2 = ptr2.next) {
				if (resultingNode == null) {
					if ((ptr1.term.coeff * ptr2.term.coeff) == 0) {
						break;
					}
					resultingNode = new Node(ptr1.term.coeff * ptr2.term.coeff, ptr1.term.degree + ptr2.term.degree, null);
					pointer3 = resultingNode;
				} else {
					if ((ptr1.term.coeff * ptr2.term.coeff) == 0) {
						break;
					}
					pointer3.next = new Node(ptr1.term.coeff * ptr2.term.coeff, ptr1.term.degree + ptr2.term.degree, null);
					pointer3 = pointer3.next;
				}
			}
		}
		
		
		
		return sortCombine(resultingNode);
	}

	/**
	 * Evaluates a polynomial at a given value.
	 * 
	 * @param poly Polynomial (front of linked list) to be evaluated
	 * @param x Value at which evaluation is to be done
	 * @return Value of polynomial p at x
	 */
	public static float evaluate(Node poly, float x) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		
		float sum = 0;
		
		for (Node pointer = poly; pointer != null; pointer = pointer.next) {
			if (pointer.term.degree == 0) {
				System.out.println(pointer.term.coeff);
				sum += pointer.term.coeff;
			} else {
				System.out.println(pointer.term.coeff);

				sum += (pointer.term.coeff * (float) Math.pow(x, pointer.term.degree));
			}
		}
		
		return sum;
	}
	
	/**
	 * Returns string representation of a polynomial
	 * 
	 * @param poly Polynomial (front of linked list)
	 * @return String representation, in descending order of degrees
	 */
	public static String toString(Node poly) {
		if (poly == null) {
			return "0";
		} 
		
		String retval = poly.term.toString();
		for (Node current = poly.next ; current != null ;
		current = current.next) {
			retval = current.term.toString() + " + " + retval;
		}
		return retval;
	}	
}

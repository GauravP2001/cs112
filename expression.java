package app;

import java.io.*;

import java.util.*;
import java.util.regex.*;

import structures.Stack;

public class Expression {

	public static String delims = " \t*+-/()[]";

	/**
	 * Populates the vars list with simple variables, and arrays lists with arrays
	 * in the expression. For every variable (simple or array), a SINGLE instance is
	 * created and stored, even if it appears more than once in the expression. At
	 * this time, values for all variables and all array items are set to zero -
	 * they will be loaded from a file in the loadVariableValues method.
	 * 
	 * @param expr   The expression
	 * @param vars   The variables array list - already created by the caller
	 * @param arrays The arrays array list - already created by the caller
	 */
	public static void makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
		/** COMPLETE THIS METHOD **/
		/**
		 * DO NOT create new vars and arrays - they are already created before being
		 * sent in to this method - you just need to fill them in.
		 **/

		StringTokenizer stringT = new StringTokenizer(expr, delims);

		while (stringT.hasMoreTokens()) {

			String t = stringT.nextToken();
			
			if (!arrays.contains(new Array(t)) && expr.indexOf(t) + t.length() < expr.length() && expr.indexOf(t + '[') != -1) {
				arrays.add(new Array(t));
			} else if (!vars.contains(new Variable(t)) && !Character.isDigit(t.charAt(0))) {
				vars.add(new Variable(t));
			}
			

		}

	}

	/**
	 * Loads values for variables and arrays in the expression
	 * 
	 * @param sc Scanner for values input
	 * @throws IOException If there is a problem with the input
	 * @param vars   The variables array list, previously populated by
	 *               makeVariableLists
	 * @param arrays The arrays array list - previously populated by
	 *               makeVariableLists
	 */
	public static void loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays)
			throws IOException {
		while (sc.hasNextLine()) {
			StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
			int numTokens = st.countTokens();
			String tok = st.nextToken();
			Variable var = new Variable(tok);
			Array arr = new Array(tok);
			int vari = vars.indexOf(var);
			int arri = arrays.indexOf(arr);
			if (vari == -1 && arri == -1) {
				continue;
			}
			int num = Integer.parseInt(st.nextToken());
			if (numTokens == 2) { // scalar symbol
				vars.get(vari).value = num;
			} else { // array symbol
				arr = arrays.get(arri);
				arr.values = new int[num];
				// following are (index,val) pairs
				while (st.hasMoreTokens()) {
					tok = st.nextToken();
					StringTokenizer stt = new StringTokenizer(tok, " (,)");
					int index = Integer.parseInt(stt.nextToken());
					int val = Integer.parseInt(stt.nextToken());
					arr.values[index] = val;
				}
			}
		}
	}
	// -----------------------------------------------------------------------------------------------------------------------------

	/**
	 * Evaluates the expression.
	 * 
	 * @param vars   The variables array list, with values for all variables in the
	 *               expression
	 * @param arrays The arrays array list, with values for all array items
	 * @return Result of evaluation
	 */
	public static float evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
		/** COMPLETE THIS METHOD **/
		// following line just a placeholder for compilation

		Stack<Float> stkVar = new Stack<>();
		Stack<Character> stkOp = new Stack<>();
		expr = expr.replaceAll("\\s", "");
		
		int start = 0;
		int end = 0;
		
		while (end != expr.length()) {
			
			if (expr.charAt(end) == ')') {
				if (expr.substring(start, end+1).matches("^[0-9]*$")) {
					Float tempVar = Float.parseFloat(expr.substring(start, end));
					stkVar.push(tempVar);
					
				} else {
//					System.out.println(expr.substring(start,end));
					int temp = extractValue(vars, expr.substring(start, end));
					
					String tVar = Integer.toString(temp);
					Float add = Float.parseFloat(tVar);
					stkVar.push(add);
				}
				break;
				
			} else if (expr.charAt(end) == ']') {
 			
				
				if (stkVar.isEmpty()) {

					if (expr.substring(start, end).matches("^[0-9]*$")) {
						Float tempVar = Float.parseFloat(expr.substring(start, end));
						stkVar.push(tempVar);
						
					} else {
						int temp = extractValue(vars, expr.substring(start, end));
						
						String tVar = Integer.toString(temp);
						Float add = Float.parseFloat(tVar);
						stkVar.push(add);
					}
				}
				
				break;
				
				
				
				
				
				
			} else if ((end == expr.length()-1)) {
				
				if (expr.substring(start, end+1).matches("^[0-9]*$")) {
					Float tempVar = Float.parseFloat(expr.substring(start, end+1));
					stkVar.push(tempVar);
					
				} else {
					int temp = extractValue(vars, expr.substring(start, end+1));
					String tVar = Integer.toString(temp);
					Float add = Float.parseFloat(tVar);
					stkVar.push(add);
				}
				
				end += 1;
				
			} else if (Character.isLetter(expr.charAt(end))) {
				end += 1;
//				System.out.println("end is " + end + expr.charAt(end));
				
			} else if (expr.charAt(end) == '+' || expr.charAt(end) == '-' || expr.charAt(end) == '/' || expr.charAt(end) == '*') {
				
				if (start == end) {
					stkOp.push(expr.charAt(end));
					
					start = end + 1;
					end = end + 1;
//					System.out.println("start " + expr.charAt(start));
//					System.out.println("end " + expr.charAt(end));

					continue;
				}
				
				if (expr.substring(start, end).matches("^[0-9]*$")) {
		
					Float tempVar = Float.parseFloat(expr.substring(start, end));
					stkVar.push(tempVar);
//					System.out.println("Pushed " + stkVar.peek());


					
					if (!(stkOp.isEmpty()) && (stkVar.size() >= 2)) {
						
						String before = String.valueOf(stkOp.peek());
						String next = String.valueOf(expr.charAt(end));
						
//						System.out.println(before);
//						System.out.println(next);

					
						if ((before.equals("+") || before.equals("-")) && (next.equals("+") || next.equals("-"))) {
							if (before.equals("+")) {
								float t1 = stkVar.pop();
								float t2 = stkVar.pop();
		
								String o1 = String.valueOf(stkOp.pop());
		
								stkVar.push(t2 + t1);
								stkOp.push(expr.charAt(end));

							} else {
	//							System.out.println();
								float t1 = stkVar.pop();
								float t2 = stkVar.pop();
		
								String o1 = String.valueOf(stkOp.pop());
		
								stkVar.push(t2 - t1);
								stkOp.push(expr.charAt(end));

							}
						} else if ((before.equals("*") || before.equals("/")) && (next.equals("*") || next.equals("/"))) {
							
							if (before.equals("*")) {
								float t1 = stkVar.pop();
								float t2 = stkVar.pop();
		
								String o1 = String.valueOf(stkOp.pop());
		
								stkVar.push(t2 * t1);
								stkOp.push(expr.charAt(end));

//								System.out.println("Re added " + stkVar.peek());
							} else {
					//			System.out.println();
								float t1 = stkVar.pop();
								float t2 = stkVar.pop();
		
								String o1 = String.valueOf(stkOp.pop());
		
								stkVar.push(t2 / t1);
								stkOp.push(expr.charAt(end));

							}
						} else {
							stkOp.push(expr.charAt(end));
//							System.out.println(stkOp.peek());
						}
					} else {
						stkOp.push(expr.charAt(end));
//						System.out.println("Pushed " + stkOp.peek());
					}
					
							
					start = end + 1;
					end = start;
				} else {
					
					
					int temp = extractValue(vars, expr.substring(start, end));
					String tVar = Integer.toString(temp);
					Float add = Float.parseFloat(tVar);
					stkVar.push(add);
	//				System.out.println("added " + stkVar.peek());

										
					if (!(stkOp.isEmpty()) && (stkVar.size() >= 2)) {
						
						String before = String.valueOf(stkOp.peek());
						String next = String.valueOf(expr.charAt(end));

					
						if ((before.equals("+") || before.equals("-")) && (next.equals("+") || next.equals("-"))) {
							if (before.equals("+")) {
								float t1 = stkVar.pop();
								float t2 = stkVar.pop();
		
								String o1 = String.valueOf(stkOp.pop());
		
								stkVar.push(t2 + t1);
								stkOp.push(expr.charAt(end));
//								System.out.println("re pushed " + stkVar.peek());
							} else {
//								System.out.println();
								float t1 = stkVar.pop();
								float t2 = stkVar.pop();
		
								String o1 = String.valueOf(stkOp.pop());
		
								stkVar.push(t2 - t1);
								stkOp.push(expr.charAt(end));

							}
						} else if ((before.equals("*") || before.equals("/")) && (next.equals("*") || next.equals("/"))) {
							
							if (before.equals("*")) {
								float t1 = stkVar.pop();
								float t2 = stkVar.pop();
		
								String o1 = String.valueOf(stkOp.pop());
		
								stkVar.push(t2 * t1);
								stkOp.push(expr.charAt(end));

							} else {
//								System.out.println();
								float t1 = stkVar.pop();
								float t2 = stkVar.pop();
		
								String o1 = String.valueOf(stkOp.pop());
		
								stkVar.push(t2 / t1);
								stkOp.push(expr.charAt(end));

							}
						} else {
							stkOp.push(expr.charAt(end));
//							System.out.println(stkOp.peek());

						}
					} else {
						stkOp.push(expr.charAt(end));
//						System.out.println("Pushed: " + stkOp.peek());
					}
				
					start = end + 1;
					end = start;
	//				System.out.println("the new start is at " + expr.charAt(start));
				}
				
				
			} else if  (Character.isDigit(expr.charAt(end))){
				end += 1;
				
			} else if (expr.charAt(end) == '(') {
//				System.out.println("calling parenthesis ");
				float result = evaluate(expr.substring(end+1), vars, arrays);
				stkVar.push(result);
//				System.out.println("added after evaluating parenthesis" + stkVar.peek());
				
//				System.out.println("passed in " + expr.substring(end));
				int a = getIndexOfArray(expr.substring(end));
				
				start += a;
				end += a;
				
	//			System.out.println("the end index is " + end);
				
				if (!(stkOp.isEmpty()) && (stkVar.size() >= 2) && end != expr.length()) {
					
					String before = String.valueOf(stkOp.peek());
					String next = String.valueOf(expr.charAt(end));

				
					if ((before.equals("+") || before.equals("-")) && (next.equals("+") || next.equals("-"))) {
						if (before.equals("+")) {
							float t1 = stkVar.pop();
							float t2 = stkVar.pop();
	
							String o1 = String.valueOf(stkOp.pop());
	
							stkVar.push(t2 + t1);
						} else {
	//						System.out.println();
							float t1 = stkVar.pop();
							float t2 = stkVar.pop();
	
							String o1 = String.valueOf(stkOp.pop());
	
							stkVar.push(t2 - t1);
						}
					} else if ((before.equals("*") || before.equals("/")) && (next.equals("*") || next.equals("/"))) {
						
						if (before.equals("*")) {
							float t1 = stkVar.pop();
							float t2 = stkVar.pop();
	
							String o1 = String.valueOf(stkOp.pop());
	
							stkVar.push(t2 * t1);
						} else {
//							System.out.println();
							float t1 = stkVar.pop();
							float t2 = stkVar.pop();
	
							String o1 = String.valueOf(stkOp.pop());
	
							stkVar.push(t2 / t1);
						}
					} else {
						
						stkOp.push(expr.charAt(end));
//						System.out.println(stkOp.peek());

					}
				} else {
					
					if (end == expr.length()) {
						break;
					}
//					stkOp.push(expr.charAt(end));
//					System.out.println("Pushed AA " + stkOp.peek());
				}
			
//				start = end + 1;
//				end = start;
//				
				
			} else if (expr.charAt(end) == '[') {
				
				float result = evaluate(expr.substring(end+1), vars, arrays);
				int o = (int) result;

				int z = extractArrayValue(arrays, o, expr.substring(start, end));
				
				float last = z;
				stkVar.push(last);
				int ind = getIndexBracket(expr.substring(end));
				
				

				end += ind;
				start = end;
				

			
				
				
				if (!(stkOp.isEmpty()) && (stkVar.size() >= 2) && end != expr.length()) {
					
					String before = String.valueOf(stkOp.peek());
					String next = String.valueOf(expr.charAt(end));

				
					if ((before.equals("+") || before.equals("-")) && (next.equals("+") || next.equals("-"))) {
						if (before.equals("+")) {
							float t1 = stkVar.pop();
							float t2 = stkVar.pop();
	
							String o1 = String.valueOf(stkOp.pop());
	
							stkVar.push(t2 + t1);
						} else {
//							System.out.println();
							float t1 = stkVar.pop();
							float t2 = stkVar.pop();
	
							String o1 = String.valueOf(stkOp.pop());
	
							stkVar.push(t2 - t1);
						}
					} else if ((before.equals("*") || before.equals("/")) && (next.equals("*") || next.equals("/"))) {
						
						if (before.equals("*")) {
							float t1 = stkVar.pop();
							float t2 = stkVar.pop();
	
							String o1 = String.valueOf(stkOp.pop());
	
							stkVar.push(t2 * t1);
						} else {
					//		System.out.println();
							float t1 = stkVar.pop();
							float t2 = stkVar.pop();
	
							String o1 = String.valueOf(stkOp.pop());
	
							stkVar.push(t2 / t1);
						}
					} else {
						
						stkOp.push(expr.charAt(end));
//						System.out.println(stkOp.peek());

					}
				} else {
					
					if (end == expr.length()) {
						break;
					}

				}
			} 
			
		}
		
		
		
		while (!(stkOp.isEmpty())) {
			float t1 = stkVar.pop();
			float t2 = stkVar.pop();

			String o1 = String.valueOf(stkOp.pop());
			// System.out.println("Reducing");

			if (o1.equals("+")) {
				stkVar.push(t2 + t1);
//				 System.out.println("Re Added: " + stkVar.peek());
			} else if (o1.equals("-")) {
				stkVar.push(t2 - t1);
//				 System.out.println("Re Added: " + stkVar.peek());
			} else if (o1.equals("*")) {
				stkVar.push(t2 * t1);
//				 System.out.println("Re Added: " + stkVar.peek());
			} else if (o1.equals("/")) {
				stkVar.push(t2 / t1);
//				 System.out.println("Re Added: " + stkVar.peek());
			}
		}
//		System.out.println("Returning: " + stkVar.peek() + "\n");
		
		

//		System.out.println("returning" + stkVar.peek());
		return stkVar.pop();
	}
	
	
	
	
	
	// -----------------------------------------------------------------------------------------------------------------------------

	private static int extractValue(ArrayList<Variable> var, String a) {
		for (int i = 0; i < var.size(); i++) {
			if (var.get(i).name.equals(a)) {
				return var.get(i).value;
			}
		}

		return 0;
	}

	// ------------------------------------------------------------------------------------------------------------------------------

	private static int extractValue(ArrayList<Variable> var, char obj) {
		for (int i = 0; i < var.size(); i++) {
			if (var.get(i).name.equals(String.valueOf(obj))) {
				return var.get(i).value;
			}
		}

		return 0;
	}
	
	private static int extractArrayValue(ArrayList<Array> arrays, int ind, String n) {
		int[] array = null;
		int temp = 0;
		
		for (int i = 0; i < arrays.size(); i++) {
			if (arrays.get(i).name.equals(n)) {
				temp = i;
				break;
			}
		}
		
		array = arrays.get(temp).values;
		return array[ind];
	}
	
	// ------------------------------------------------------------------------------------------------------------------------------
	
	private static int getIndexOfArray(String s) {
		
		int oC = 0;
		int counter = 0;
		
		for (int i = 0; i <= s.length() - 1; i++) {
			if (s.charAt(i) == '(') {
				oC++;
				counter++;
			} else if (s.charAt(i) == ')') {
				oC--;
				counter++;
			} else {
				counter++;
			}
			
			if (oC == 0) {
				return counter;
			}
		}
		
		return 0;
		
	}
	
	private static int getIndexBracket(String s) {
		
		int oC = 0;
		int counter = 0;
		
		for (int i = 0; i <= s.length() - 1; i++) {
			if (s.charAt(i) == '[') {
				oC++;
				counter++;
			} else if (s.charAt(i) == ']') {
				oC--;
				counter++;
			} else {
				counter++;
			}
			
			if (oC == 0) {
				return counter;
			}
		}
		
		return 0;
		
	}
	
	
	
}

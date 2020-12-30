package com.apc.tree;

import java.util.Stack;

public class BST {
	
	private static boolean isValid(String preOrder[], int n) {
		Stack<String> stack = new Stack<>();

		// init root with lowest string possible
		String root = "a";

		for (int i = 0; i < n; i++) {
			
			// node on the right and < root
			if (preOrder[i].compareTo(root) < 0) {
				return false;
			}

			// if preOrder[i] lives in the right sub tree of the stack, remove items smaller 
			// than it and make the last one removed as new root
			while (!stack.empty() && preOrder[i].compareTo(stack.peek()) > 0) {
				root = stack.peek();
				stack.pop();
			}

			//stack either empty or < root
			stack.push(preOrder[i]);
		}
		return true;
	}

	public static void main(String args[]) {
		String[] pairs = new String[] { "ab", "bf", "ac", "be", "cn" };
		if (isValid(pairs, pairs.length)) {
			System.out.println("YES");
		} else {
			System.out.println("NO");
		}
	}
}
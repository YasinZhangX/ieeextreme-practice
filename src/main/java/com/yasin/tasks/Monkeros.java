package com.yasin.tasks;

import java.util.Scanner;

/**
 * @author Yasin Zhang
 */
public class Monkeros {
    public static void main (String[] args) throws java.lang.Exception {
        Scanner in = new Scanner(System.in);
        int a = in.nextInt();
        int x[] = new int[a];
        TreeNode tree = new TreeNode();
        for (int i = 0; i < a; i++) {
            x[i] = in.nextInt();
            tree.insert(x[i]);
        }
    }
}

class TreeNode {
    int val;
    TreeNode parent, left, right;
    boolean isLeft = true;
    TreeNode root;
    int depth;

    boolean hasleft(){
        return left!=null;
    }

    boolean hasRight(){
        return right!=null;
    }

    TreeNode(boolean isLeft) {
        this.isLeft = isLeft;
    }

    TreeNode() {

    }

    void insert(int val){
        if(root == null){
            root = new TreeNode();
            root.val = val;
            root.depth = 1;
            System.out.print(root.depth + " ");
            return;
        }
        insert(val, root);
    }

    void insert(Integer val, TreeNode parent) {
        if(parent == null){
            parent = root;
        }

        int compare = val.compareTo(parent.val);

        if (compare > 0) {
            if (!parent.hasRight()) {
                parent.right = new TreeNode(false);
                parent.right.parent = parent;
                parent.right.val = val;
                parent.right.depth = parent.depth + 1;

                System.out.print(parent.right.depth + " ");
            } else {
                insert(val, parent.right);
            }
        } else {
            if (!parent.hasleft()) {
                parent.left = new TreeNode(true);
                parent.left.parent = parent;
                parent.left.val = val;
                parent.left.depth = parent.depth + 1;
                System.out.print(parent.left.depth+" ");
            } else {
                insert(val, parent.left);
            }
        }
    }
}

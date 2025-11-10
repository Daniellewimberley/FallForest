// FallForest.java
// Author: Danielle Wimberley
// Simulates leaves falling and regrowing using recursion.
// Demonstrates how small changes can ripple through a tree structure.

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

class Branch {
    private int leaves;
    private Branch[] subBranches;
    private static Random rand = new Random();
    private static FileWriter logWriter;
    private static int windStrength = 2; // number of leaves that may fall at once

    // Constructor: builds a branch with given leaves and number of sub-branches
    public Branch(int leaves, int subCount) {
        this.leaves = leaves;
        subBranches = new Branch[subCount];
        for (int i = 0; i < subCount; i++) {
            subBranches[i] = new Branch(rand.nextInt(3) + 1, rand.nextInt(2));
        }
    }

    // Assign shared FileWriter so every branch can log
    public static void setLogWriter(FileWriter writer) {
        logWriter = writer;
    }

    // Recursive function to simulate falling leaves
    public void fallLeaves(int level) throws IOException, InterruptedException {
        if (leaves <= 0 && subBranches.length == 0) return; // base case

        if (leaves > 0) {
            int leavesToFall = Math.min(leaves, rand.nextInt(windStrength) + 1);
            for (int i = 0; i < leavesToFall; i++) {
                String msg = " ".repeat(level * 2)
                        + "🍂  A leaf falls from branch level " + level;
                System.out.println(msg);
                logWriter.write(msg + "\n");
                leaves--;
                Thread.sleep(500); // gentle animation
            }
            fallLeaves(level); // recurse on same branch until no leaves left
        }

        // recurse into sub-branches
        for (Branch b : subBranches) {
            b.fallLeaves(level + 1);
        }
    }

    // Recursive method to regrow leaves in spring
    public void growLeaves(int level) throws IOException, InterruptedException {
        if (subBranches.length == 0 && leaves >= 3) return; // base case (full)

        int newLeaves = rand.nextInt(3) + 1;
        leaves += newLeaves;
        String msg = " ".repeat(level * 2)
                + "🌱  " + newLeaves + " new leaves grow on branch level " + level;
        System.out.println(msg);
        logWriter.write(msg + "\n");
        Thread.sleep(500);

        for (Branch b : subBranches) {
            b.growLeaves(level + 1);
        }
    }
}

public class FallForest {
    public static void main(String[] args) {
        System.out.println("The forest prepares for autumn...");

        try (FileWriter writer = new FileWriter("leaf_fall_log.txt")) {
            Branch.setLogWriter(writer);
            Branch tree = new Branch(3, 2);

            tree.fallLeaves(0);

            System.out.println("The forest sleeps for winter.\n");
            writer.write("The forest sleeps for winter.\n\n");

            System.out.println("Spring returns to the forest...");
            writer.write("Spring returns to the forest...\n");
            tree.growLeaves(0);

            System.out.println("The forest is alive again!");
            writer.write("The forest is alive again!\n");

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

/*
------------------------------------------
Reflection
------------------------------------------
Recursion helps the whole tree react when just one part changes. 
Each branch follows the same steps, so a tiny action like one leaf falling 
can spread through the whole structure without me writing a loop for each one. 
That’s what I think is cool about recursion, it repeats the same rule 
but still makes something complex.
I see recursion in things like trees, rivers, and even how snowflakes form. 
They all grow or split in repeating patterns. My code kind of copies that 
by letting each branch act like a smaller version of the tree.
I learned that recursion isn’t just for math problems, it can make 
programs feel more alive when you imagine how nature really works.
*/
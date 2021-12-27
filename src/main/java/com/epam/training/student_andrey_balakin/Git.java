package com.epam.training.student_andrey_balakin;

import lombok.val;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class Git {
    public static void main (String[] args) {
        List<Integer> left;
        List<Integer> right;

        val rndLimit  = 200;
        val leftSize  = new Random().nextInt(50);
        val rightSize = new Random().nextInt(50);
        left  = Stream.generate(() -> new Random().nextInt(rndLimit)).limit(leftSize).toList();
        right = Stream.generate(() -> new Random().nextInt(rndLimit)).limit(rightSize).toList();

        System.out.println(left);
        System.out.println(right);
        System.out.println(getCommonParent(left, right));
    }

    public static @Nullable Integer getCommonParent (List<Integer> left, List<Integer> right) {
        if (left == null || left.isEmpty() || right == null || right.isEmpty()) return null;
        val itLeft   = left.iterator();
        val itRight  = right.iterator();
        val setLeft  = new HashSet<Integer>();
        val setRight = new HashSet<Integer>();
        while (itLeft.hasNext() || itRight.hasNext()) {
            if (itLeft.hasNext()) {
                val leftValue = itLeft.next();
                if (setRight.contains(leftValue)) return leftValue;
                setLeft.add(leftValue);
            }
            if (itRight.hasNext()) {
                val rightValue = itRight.next();
                if (setLeft.contains(rightValue)) return rightValue;
                setRight.add(rightValue);
            }
        }
        return null;
    }
}

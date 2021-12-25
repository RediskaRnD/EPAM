package com.epam.training.student_andrey_balakin;

import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PublicToPrivateQuiz {
    private static boolean isValidAccessLevel (String accessLevel) {
        val validList = new String[] {"public", "protected", "", "private"};
        for (val level : validList) {
            if (level.equals(accessLevel)) return true;
        }
        return false;
    }

    /**
     * @param input      String (*.java)
     * @param changeFrom ("public", "protected", "private")
     * @param changeTo   ("public", "protected", "", "private")
     * @return String
     * @throws PublicToPrivateQuizException 1. input is null or incorrect Java file.
     *                                2. changeTo or changeFrom is wrong.
     */
    public static @NotNull String changeClassFieldsAndMethodsAccessLevel (String input, String changeFrom, String changeTo)
            throws PublicToPrivateQuizException {
        if (input == null) throw new PublicToPrivateQuizException("Parameter input is wrong: null");
        if (!isValidAccessLevel(changeFrom) || changeFrom.isEmpty()) {
            throw new PublicToPrivateQuizException("Parameter changeFrom is wrong: " + changeFrom);
        }
        if (!isValidAccessLevel(changeTo)) throw new PublicToPrivateQuizException("Parameter changeTo is wrong: " + changeTo);

        val classIdx = input.indexOf("class");
        if (classIdx < 0) return input;

        var curlyBracesLevel = 0;
        var wasEscapeSymbol  = false;
        var inChar           = false;
        var inString         = false;
        var inComment        = false;
        var inBigComment     = false;

        val words    = new ArrayList<Integer>();
        val inputLen = input.length();
        int i        = 0;
        val FROM_LEN = changeFrom.length();
        while (i < inputLen) {
            val publicIndex = input.indexOf(changeFrom, i);
            if (publicIndex < 0) break;
            words.add(publicIndex);
            i = publicIndex + FROM_LEN;
        }
        if (words.isEmpty()) return input;

        i = 0;
        var line         = 1;
        val it           = words.iterator();
        var wordIdx      = it.next();
        var symbolBefore = '\0';
        for (val c : input.chars().toArray()) {
            if (i == wordIdx) {
                if (curlyBracesLevel != 1 || inComment || inBigComment || inString || inChar) {
                    it.remove();
                }
                if (it.hasNext()) wordIdx = it.next();
            }
            val escaped = wasEscapeSymbol;
            switch (c) {
                case '{' -> {
                    if (!(inComment || inBigComment || inString || inChar)) {
                        ++curlyBracesLevel;
                        System.out.printf("{ %d -> Line: %d\n", curlyBracesLevel, line);
                    }
                }
                case '}' -> {
                    if (!(inComment || inBigComment || inString || inChar)) {
                        --curlyBracesLevel;
                        System.out.printf("} %d -> Line: %d\n", curlyBracesLevel, line);
                    }
                }
                case '/' -> {
                    if (!(inString || inChar)) {
                        if (inBigComment) {
                            if (symbolBefore == '*') {
                                inBigComment = false;
                                System.out.println("-BigComment");
                            }
                        } else {
                            if (symbolBefore == '/') {
                                inComment = true;
                                System.out.println("+Comment");
                            }
                        }
                    }
                }
                case '*' -> {
                    if (!(inString || inChar)) {
                        if (!inComment) {
                            if (symbolBefore == '/') {
                                inBigComment = true;
                                System.out.println("+BigComment");
                            }
                        }
                    }
                }
                case '"' -> {
                    if (!(inBigComment || inComment || inChar)) {
                        if (!wasEscapeSymbol) {
                            inString = !inString;
                            System.out.println(inString ? "+String" : "-String");
                        }
                    }
                }
                case '\\' -> {
                    wasEscapeSymbol = true;
                }
                case '\'' -> {
                    if (!(inBigComment || inComment || inString)) {
                        if (!wasEscapeSymbol) {
                            inChar = !inChar;
                            System.out.println(inChar ? "+Char" : "-Char");
                        }
                    }
                }
                case '\n' -> {
                    if (!(inString || inChar)) {
                        ++line;
                        inComment = false;
                    }
                }
            }
            symbolBefore = (char) c;
            if (escaped) {
                wasEscapeSymbol = false;
            }
            ++i;
        }
        if (words.isEmpty()) return input;

        if (curlyBracesLevel != 0) throw new PublicToPrivateQuizException("Invalid java code: curly braces.");
        if (inChar) throw new PublicToPrivateQuizException("Invalid java code: char is not finished.");
        if (inString) throw new PublicToPrivateQuizException("Invalid java code: String is not finished.");
        if (inBigComment) throw new PublicToPrivateQuizException("Invalid java code: comment is not closed \\*...");
        var idx    = 0;
        val result = new StringBuilder();
        for (val wIdx : words) {
            result.append(input, idx, wIdx).append(changeTo);
            idx = wIdx + FROM_LEN;
        }
        result.append(input, idx, inputLen);
        return result.toString();
    }
}
package advent.code.days;

import advent.code.utils.MutablePair;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class EighteenthDay extends DaySolver{

    boolean keepExploding;
    boolean keepSplitting;

    int splitCount = 0;
    Pointer<Object, Object> head;
    Pointer<Object, Object> ptr;


    List<String > allLines;
    Pointer<Object, Object> linkedList;

    int largestMagnitude = Integer.MIN_VALUE;

    static class Pointer<T, U> extends MutablePair<T, U>{
        Pointer prev;
        Pointer next;

        Integer integer = null;


        public boolean isInteger(){
            return integer != null;
        }

        public int getInteger(){
            return integer;
        }

        public void setInteger(Integer integer) {
            this.integer = integer;
        }

        public Pointer(int val){
            super(null, null);
            this.integer = val;
        }
        public Pointer(T left, U right) {
            super(left, right);
        }

        public static  <T,U> Pointer<T, U> of(T t, U u) {
            return new Pointer<>(t, u);
        }
    }

    protected EighteenthDay(String fileName) throws FileNotFoundException {
        super(fileName);
        Scanner scanner = readFile.toScanner();
        allLines = new ArrayList<>();
        while (scanner.hasNext()) {
            final String currentLine = scanner.nextLine();
            allLines.add(currentLine);
        }


    }

    private int calculateSum(Pointer<Object, Object> pointer) {
        if (pointer == null) {
            return 0;
        }
        if (pointer.isInteger()) {
            return pointer.getInteger();
        }
        int sum =0;
        if (pointer instanceof Pointer) {
            if ( pointer.getLeft() != null ) {
                sum += (calculateSum((Pointer<Object, Object>) pointer.getLeft())) * 3;
            }
            if ( pointer.getRight() != null ) {
                sum += (calculateSum((Pointer<Object, Object>) pointer.getRight())) * 2;
            }
            return sum;
        }
        return sum;
    }


    private int roundDown(int value) {
        return BigDecimal.valueOf(value).divide(BigDecimal.valueOf(2), RoundingMode.HALF_DOWN).intValue();
    }

    private int roundUp(int value) {
        return BigDecimal.valueOf(value).divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP).intValue();
    }

    private boolean split(Pointer<Object, Object> pointer) {
        if (pointer == null) {
            return false;
        }
        if (pointer.isInteger()) {
            if (pointer.integer > 9) {
                keepSplitting = true;

                var left = new Pointer(roundDown(pointer.integer));
                var right = new Pointer(roundUp(pointer.integer));
                pointer.integer = null;
                left.prev = pointer.prev;
                left.next = right;
                right.prev = left;
                right.next = pointer.next;
                if (pointer.prev != null) {
                    pointer.prev.next = left;
                }
                if (pointer.next != null) {
                    pointer.next.prev = right;
                }
                pointer.next = null;
                pointer.prev = null;
                pointer.setLeft(left);
                pointer.setRight(right);
                splitCount += 1;
                return true;
            }
            return false;
        }
        if (pointer instanceof Pointer) {
            if ( pointer.getLeft() != null ) {
                if (split((Pointer<Object, Object>) pointer.getLeft())) {
                    return true;
                }
            }
            if ( pointer.getRight() != null ) {
                return split((Pointer<Object, Object>) pointer.getRight());
            }
        }
        return false;

    }

    private boolean explode(Pointer<Object, Object> pointer, int depth, boolean fromLeft) {
        if (pointer == null) {
            return false;
        }
        if (pointer.isInteger()) {
            if (depth  > 4) {
                keepExploding = true;
                if (pointer.prev.isInteger() && fromLeft) {
                    pointer.prev.integer += pointer.integer;
                }
                if (pointer.next != null && pointer.next.isInteger() && !fromLeft) {
                    pointer.next.integer += pointer.integer;
                }
                return true;
            }
            return false;
        }
        if (pointer instanceof Pointer) {
            boolean pairWasDepthFour = true;
            if ( pointer.getLeft() != null ) {
                pairWasDepthFour &= explode((Pointer<Object, Object>) pointer.getLeft(), depth + 1, true);
            }
            if ( pointer.getRight() != null ) {
                pairWasDepthFour &= explode((Pointer<Object, Object>) pointer.getRight(), depth + 1, false);
            }
            if (pairWasDepthFour) {
                replacePoint(pointer);
            }
        }
        return false;

    }

    private void replacePoint(Pointer<Object, Object> replace) {
        replace.prev = ((Pointer<Object, Object>)replace.getLeft()).prev;
        replace.next = ((Pointer<Object, Object>)replace.getRight()).next;
        if (replace.prev != null) {
            replace.prev.next = replace;
        }
        if (replace.next != null) {
            replace.next.prev = replace;
        }
        replace.setRight(null);
        replace.setLeft(null);
        replace.integer = 0;
    }



    private void createLinkedList(Pointer<Object, Object> pointer, int depth) {
        if (pointer == null) {
            return;
        }
        if (pointer.isInteger()) {
            pointer.prev = ptr;
            ptr.next = pointer;
            ptr = pointer;
            return;
        }
        if (pointer instanceof Pointer) {
            if ( pointer.getLeft() != null ) {
                createLinkedList((Pointer<Object, Object>) pointer.getLeft(), depth + 1);
            }
            if ( pointer.getRight() != null ) {
                createLinkedList((Pointer<Object, Object>) pointer.getRight(), depth + 1);
            }
            return;
        }
        return;
    }

    @Override
    public void solveFirstProblem() {
        Pointer<Object, Object> ptr = null;
        Stack<Pointer> mutablePairs = new Stack<>();
        boolean wasCalledOnce = false;
        for (String currentLine : this.allLines){
            for (int i = 0; i < currentLine.length(); i ++) {
                char currentChar = currentLine.charAt(i);
                if (currentChar == '[') {
                    if (head == null) {
                        head = Pointer.of(null, null);
                        ptr = head;
                        mutablePairs.push(ptr);
                    }
                    else {
                        if (ptr.getLeft() == null) {
                            ptr.setLeft(Pointer.of(null, null));
                            ptr = (Pointer) ptr.getLeft();
                            mutablePairs.push(ptr);
                        }
                        else if (ptr.getRight() == null) {
                            ptr.setRight(Pointer.of(null, null));
                            ptr = (Pointer) ptr.getRight();
                            mutablePairs.push(ptr);
                        }
                    }
                }
                else if (Character.isDigit(currentChar)) {
                    if (ptr.getLeft() == null) {
                        ptr.setLeft(new Pointer(currentChar - '0'));
                    }
                    else {
                        ptr.setRight(new Pointer(currentChar - '0'));
                        mutablePairs.pop();
                        if (mutablePairs.size() > 0) {
                            ptr = mutablePairs.peek();
                            while (ptr.getRight() != null) {
                                mutablePairs.pop();
                                if (mutablePairs.size() > 0) {
                                    ptr = mutablePairs.peek();
                                }
                                else {
                                    ptr = null;
                                    break;
                                }
                            }
                        }
                        else  {
                            ptr = null;
                        }

                    }
                }
            }

            mutablePairs.clear();
            Pointer<Object, Object> nextPair = Pointer.of(this.head, null);
            this.head = nextPair;
            ptr = nextPair;
            mutablePairs.push(ptr);
            if (wasCalledOnce) {
                // dummy head
                linkedList = Pointer.of(null, null);
                this.ptr = linkedList;
                createLinkedList((Pointer<Object, Object>)this.head.getLeft(), 0);
                keepExploding = true;
                keepSplitting = true;
                splitCount = 0;
                while (true) {
                    while (keepExploding) {
                        keepExploding = false;
                        explode((Pointer<Object, Object>) this.head.getLeft(), 0, false);
                    }

                    keepSplitting = false;
                    split((Pointer<Object, Object>) this.head.getLeft());
                    keepExploding = keepSplitting;
                    if (!keepExploding) {
                        break;
                    }
                }
            }
            wasCalledOnce =true;
        }
        printAnswer(calculateSum((Pointer<Object, Object>)  this.head.getLeft()));
    }


    private void recreate(String currentLine, Stack<Pointer> mutablePairs) {
        Pointer<Object, Object> ptr = null;
        mutablePairs.clear();
        for (int i = 0; i < currentLine.length(); i ++) {
            char currentChar = currentLine.charAt(i);
            if (currentChar == '[') {
                if (head == null) {
                    head = Pointer.of(null, null);
                    ptr = head;
                    mutablePairs.push(ptr);
                }
                else {
                    if (ptr.getLeft() == null) {
                        ptr.setLeft(Pointer.of(null, null));
                        ptr = (Pointer) ptr.getLeft();
                        mutablePairs.push(ptr);
                    }
                    else if (ptr.getRight() == null) {
                        ptr.setRight(Pointer.of(null, null));
                        ptr = (Pointer) ptr.getRight();
                        mutablePairs.push(ptr);
                    }
                }
            }
            else if (Character.isDigit(currentChar)) {
                if (ptr.getLeft() == null) {
                    ptr.setLeft(new Pointer(currentChar - '0'));
                }
                else {
                    ptr.setRight(new Pointer(currentChar - '0'));
                    mutablePairs.pop();
                    if (mutablePairs.size() > 0) {
                        ptr = mutablePairs.peek();
                        while (ptr.getRight() != null) {
                            mutablePairs.pop();
                            if (mutablePairs.size() > 0) {
                                ptr = mutablePairs.peek();
                            }
                            else {
                                ptr = null;
                                break;
                            }
                        }
                    }
                    else  {
                        ptr = null;
                    }

                }
            }
        }
    }
    @Override
    public void solveSecondProblem() {


        Pointer<Object, Object> ptr = null;
        Stack<Pointer> mutablePairs = new Stack<>();
        for (int outer =0 ; outer < this.allLines.size(); outer ++ ) {


            for (int j = 0; j < this.allLines.size(); j ++) {
                if (outer == j) {
                    continue;
                }
                this.head = null;
                this.ptr = null;
                mutablePairs.clear();
                recreate(allLines.get(outer), mutablePairs);
                Pointer<Object, Object> nextPair = Pointer.of(this.head, null);
                this.head = nextPair;
                ptr = nextPair;
                mutablePairs.push(ptr);


                String currentLine = allLines.get(j);

                for (int i = 0; i < currentLine.length(); i ++) {
                    char currentChar = currentLine.charAt(i);
                    if (currentChar == '[') {
                        if (head == null) {
                            head = Pointer.of(null, null);
                            ptr = head;
                            mutablePairs.push(ptr);
                        }
                        else {
                            if (ptr.getLeft() == null) {
                                ptr.setLeft(Pointer.of(null, null));
                                ptr = (Pointer) ptr.getLeft();
                                mutablePairs.push(ptr);
                            }
                            else if (ptr.getRight() == null) {
                                ptr.setRight(Pointer.of(null, null));
                                ptr = (Pointer) ptr.getRight();
                                mutablePairs.push(ptr);
                            }
                        }
                    }
                    else if (Character.isDigit(currentChar)) {
                        if (ptr.getLeft() == null) {
                            ptr.setLeft(new Pointer(currentChar - '0'));
                        }
                        else {
                            ptr.setRight(new Pointer(currentChar - '0'));
                            mutablePairs.pop();
                            if (mutablePairs.size() > 0) {
                                ptr = mutablePairs.peek();
                                while (ptr.getRight() != null) {
                                    mutablePairs.pop();
                                    if (mutablePairs.size() > 0) {
                                        ptr = mutablePairs.peek();
                                    }
                                    else {
                                        ptr = null;
                                        break;
                                    }
                                }
                            }
                            else  {
                                ptr = null;
                            }

                        }
                    }
                }

                mutablePairs.clear();
                nextPair = Pointer.of(this.head, null);
                this.head = nextPair;
                ptr = nextPair;
                mutablePairs.push(ptr);
                linkedList = Pointer.of(null, null);
                this.ptr = linkedList;
                createLinkedList((Pointer<Object, Object>)this.head.getLeft(), 0);
                keepExploding = true;
                keepSplitting = true;
                splitCount = 0;
                while (true) {
                    while (keepExploding) {
                        keepExploding = false;
                        explode((Pointer<Object, Object>) this.head.getLeft(), 0, false);
                    }

                    keepSplitting = false;
                    split((Pointer<Object, Object>) this.head.getLeft());
                    keepExploding = keepSplitting;
                    if (!keepExploding) {
                        break;
                    }
                }
                largestMagnitude = Math.max(calculateSum((Pointer<Object, Object>)  this.head.getLeft()), largestMagnitude);
            }
        }

        printAnswer(largestMagnitude);
    }
}

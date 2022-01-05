package advent.code.days;

import advent.code.utils.Pair;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

public class SixteenthDay extends DaySolver {
    final String line;

    final Map<Character, String> toBinary = new HashMap<>() {{
        put('0', "0000");
        put('1', "0001");
        put('2', "0010");
        put('3', "0011");
        put('4', "0100");
        put('5', "0101");
        put('6', "0110");
        put('7', "0111");
        put('8', "1000");
        put('9', "1001");
        put('A', "1010");
        put('B', "1011");
        put('C', "1100");
        put('D', "1101");
        put('E', "1110");
        put('F', "1111");
    }};


    protected SixteenthDay(String fileName) throws FileNotFoundException {
        super(fileName);
        Scanner scanner = readFile.toScanner();
        line = scanner.nextLine();
    }

    private String getBinary(String string) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < string.length(); i ++) {
            sb.append(toBinary.get(string.charAt(i)));
        }
        return sb.toString();
    }

    private boolean isAllZeroes(String s) {
        for (int i = 0; i < s.length(); i ++) {
            if (s.charAt(i) != '0') {
                return false;
            }
        }
        return true;
    }

    private int parsePacket(String binaryString) {
        int sum = 0;
        int readPtr = 0;
        while (readPtr < binaryString.length()) {
            if (isAllZeroes(binaryString.substring(readPtr))) {
                break;
            }
            // version
            final int versionEnd = readPtr + 3;

            final String versionString = binaryString.substring(readPtr, readPtr + 3);
            final long version = binaryValue(versionString);
            //VVVTTTAAAAABBBBBCCCCC
            //^

            sum += version;

            //VVVTTTAAAAABBBBBCCCCC
            //   ^
            // type
            final int typeEnd = versionEnd + 3;
            final String typeString = binaryString.substring(versionEnd, typeEnd);
            final long typeInt = binaryValue(typeString);

            readPtr = typeEnd;
            // literal, will be size of 5
            if (typeInt == 4) {
                char oneBit;
                do {
                    oneBit = binaryString.charAt(readPtr);
                    readPtr += 5;
                } while (oneBit == '1');
            }
            else {
                final boolean isType15 = binaryString.charAt(readPtr++) == '0';
                if (isType15) {
                    readPtr += 15;
                }
                else {
                    sum += binaryValue(binaryString.substring(readPtr, (int) (readPtr + binaryValue(binaryString.substring(readPtr, readPtr += 11)))));
                }
            }
        }
        return sum;
    }

    private Pair<Integer, List<Long>> parsePacketPart2(String binaryString) {
        int readPtr = 0;

        List<Long> ansList = new ArrayList<>();

        if (isAllZeroes(binaryString.substring(readPtr))) {
            return Pair.of(binaryString.length(), ansList);
        }
        // version
        final String versionString = binaryString.substring(readPtr, readPtr += 3);
        final long version = binaryValue(versionString);
        //VVVTTTAAAAABBBBBCCCCC
        //^

        //VVVTTTAAAAABBBBBCCCCC
        //   ^
        // type
        final String typeString = binaryString.substring(readPtr, readPtr += 3);
        final long typeId = binaryValue(typeString);

        //VVVTTTAAAAABBBBBCCCCC
        //      ^

        if (typeId == 4) {
            StringBuilder subPacket = new StringBuilder();
            char oneBit;
            do {
                oneBit = binaryString.charAt(readPtr);
                subPacket.append(binaryString, readPtr + 1, readPtr += 5);
            } while (oneBit == '1');
            // we have to read the last group, it's the end of the packet.
            //110100101111111000101000
            //VVVTTTAAAAABBBBBCCCCC000
            //                     ^
            ansList.add(binaryValue(subPacket.toString()));
        }
        else {
            List<Long> subPacketsList = new ArrayList<>();

            /// 00111000000000000110111101000101001010010001001000000000
            /// VVVTTTILLLLLLLLLLLLLLLAAAAAAAAAAABBBBBBBBBBBBBBBB
            ///       ^ -> At a zero. It's a 15. It represents the number of bits in the packet.
            // if it's a 0, then we know the length of the inner sub-packet.
            long upperBound;
            boolean isLengthTypID15 = binaryString.charAt(readPtr++) == '0';
            if (isLengthTypID15) {
                /// 00111000000000000110111101000101001010010001001000000000
                /// VVVTTTILLLLLLLLLLLLLLLAAAAAAAAAAABBBBBBBBBBBBBBBB
                ///                       ^ -> Will start here now
                upperBound = binaryValue(binaryString.substring(readPtr, readPtr += 15 )); // we need to consume bits of length 27.
            }
            else {
                // number of sub-packets immediately contained
                upperBound = binaryValue(binaryString.substring(readPtr, readPtr += 11));
            }
            for (int increment = 0; increment < upperBound;){
                Pair<Integer, List<Long>> listPair = parsePacketPart2(binaryString.substring(readPtr));
                readPtr += listPair.getLeft();
                subPacketsList.addAll(listPair.getRight());
                increment += (isLengthTypID15 ? listPair.getLeft() : 1);
            }
            ansList.add(performOperation(subPacketsList, typeId));
        }
        return Pair.of(readPtr, ansList);
    }


    private long sumAll(List<Long> list) {
        long sum = 0;
        for (long value : list) {
            sum += value;
        }
        return sum;
    }

    private long product(List<Long> list) {
        long sum = 1;
        for (long value : list) {
            sum *= value;
        }
        return sum;
    }

    private long findMin(List<Long> list) {
        long min = Integer.MAX_VALUE;
        for (long value : list) {
            min = Math.min(min, value);
        }
        return min;
    }

    private long findMax(List<Long> list) {
        long max = Integer.MIN_VALUE;
        for (long value : list) {
            max = Math.max(max, value);
        }
        return max;
    }

    private long performOperation(List<Long> list, long typeId) {
        if (typeId == 1L) {
            return product(list);
        }
        if (typeId == 2) {
            return findMin(list);
        }
        if (typeId == 3) {
            return findMax(list);
        }
        if (typeId == 5) {
            return list.get(0) > list.get(1) ? 1 : 0;
        }
        if (typeId == 6) {
            return list.get(0) < list.get(1) ? 1 : 0;
        }
        if (typeId == 7) {
            return Objects.equals(list.get(0), list.get(1)) ? 1 : 0;
        }
        return sumAll(list);
    }

    private long binaryValue(String s) {
        return Long.parseLong(s, 2);
    }

    @Override
    public void solveFirstProblem() {
        final String binaryString = getBinary(line);
        printAnswer(parsePacket(binaryString));
    }

    @Override
    public void solveSecondProblem() {
        final String binaryString = getBinary(line);
        printAnswer(parsePacketPart2(binaryString).getRight().get(0));
    }
}

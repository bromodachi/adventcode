package advent.code.days;

import advent.code.utils.ConverterUtil;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class NineteenthDay extends DaySolver{

    Queue<ScannerClass> list =  new LinkedList<>();
    List<ScannerClass> allScanners =  new ArrayList<>();
    class ScannerClass {
        List<int []> arr;
        Point startingPoint;

        public ScannerClass() {}

        public ScannerClass(List<int[]> arr, Point startingPoint) {
            this.arr = arr;
            this.startingPoint = startingPoint;
        }

        public List<List<int[]>> allTranslations() {
            List<List<int[]>> all = new ArrayList<>();
            for (int[][]matrix : matrices) {
                List<int[]> innerLists = new ArrayList<>();
                for (int[] ints : this.arr) {
                    int[] foo = multiplyMatrices(ints, matrix)[0];
                    innerLists.add(foo);
                }
                all.add(innerLists);
            }
            return all;
        }
    }

    static class Point {
        final int x;
        final int y;
        final int z;
        public Point(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return x == point.x && y == point.y && z == point.z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z);
        }
    }


    private ScannerClass findScanner(ScannerClass zero, ScannerClass first) {
        for (List<int[]> translations : first.allTranslations()) {
            Map<Point, Integer> map = new HashMap<>();
            for (int zeroIndex = 0; zeroIndex < zero.arr.size(); zeroIndex ++) {
                for (int [] arr : translations) {
                    final int[] zeroArray = Arrays.copyOf(zero.arr.get(zeroIndex), zero.arr.get(0).length);
                    final int[] firstArray = Arrays.copyOf(arr, first.arr.get(0).length);
                    final int x = zeroArray[0] - firstArray[0];
                    final int y = zeroArray[1] - firstArray[1];
                    final int z = zeroArray[2] - firstArray[2];
                    map.merge(new Point(x, y, z), 1, Integer::sum);
                }
                List<Point> pointsGreaterThan12 = map.entrySet().stream().filter(o -> o.getValue() > 11).map(Map.Entry::getKey).collect(Collectors.toList());
                if (pointsGreaterThan12.size() == 1) {
                    final Point p = pointsGreaterThan12.get(0);
                    return new ScannerClass(translations.stream().map(o -> new int[]{ p.x + o[0], p.y + o[1], p.z + o[2]}).collect(Collectors.toList()), p);
                }
            }
        }
        return null;

    }

    protected NineteenthDay(String fileName) throws FileNotFoundException {
        super(fileName);

        Scanner scanner = readFile.toScanner();

        ScannerClass scannerClass = null;
        List<int[]> innerList = new ArrayList<>();
        while (scanner.hasNext()) {
            final String line = scanner.nextLine();
            if (line.contains("scanner")) {
                if (scannerClass != null) {
                    scannerClass.arr = new ArrayList<>(innerList);
                    list.add(scannerClass);
                    innerList.clear();
                }
                scannerClass = new ScannerClass();
                continue;
            }
            if (line.isBlank()) {
                continue;
            }
            innerList.add(ConverterUtil.readLineAsArray(line));
        }

        if (scannerClass != null) {
            scannerClass.arr = new ArrayList<>(innerList);
            list.add(scannerClass);
            innerList.clear();
        }
    }

    int[][] multiplyMatrices(int[] firstMatrix, int[][] secondMatrix) {
        int[][] result = new int[1][secondMatrix[0].length];

        for (int row = 0; row < result.length; row++) {
            for (int col = 0; col < result[row].length; col++) {
                result[row][col] = multiplyMatricesCell(firstMatrix, secondMatrix, col);
            }
        }
        return result;
    }

    int multiplyMatricesCell(int[] firstMatrix, int[][] secondMatrix, int col) {
        int cell = 0;
        for (int i = 0; i < secondMatrix.length; i++) {
            cell += firstMatrix[i] * secondMatrix[i][col];
        }
        return cell;
    }

    @Override
    public void solveFirstProblem() {
        final ScannerClass zeroScanner = list.poll();
        List<int []> allScanners = new ArrayList<>(zeroScanner.arr);
        while (!list.isEmpty()) {
            final ScannerClass otherScanner = list.poll();
            ScannerClass potential = findScanner(new ScannerClass(allScanners, new Point(0, 0, 0)), otherScanner);
            if (potential == null) {
                list.offer(otherScanner);
            }
            else {
                allScanners.addAll(potential.arr);
                this.allScanners.add(potential);
            }
        }
        Set<Point> points = new HashSet<>();
        for (int [] beacons : allScanners) {
            points.add(new Point(beacons[0], beacons[1], beacons[2]));
        }
        printAnswer(points.size());
    }

    @Override
    public void solveSecondProblem() {
        int maxDistance = Integer.MIN_VALUE;
        for (int i = 0; i < allScanners.size(); i ++) {
            for (int j = 0; j < allScanners.size(); j ++) {
                if (i == j) {
                    continue;
                }
                var x1 = allScanners.get(i);
                var x2 = allScanners.get(j);
                maxDistance = Math.max(maxDistance, Math.abs(x1.startingPoint.x - x2.startingPoint.x) + Math.abs(x1.startingPoint.y - x2.startingPoint.y) + Math.abs(x1.startingPoint.z - x2.startingPoint.z));
            }
        }
        printAnswer(maxDistance);
    }


    // probably a better way to do this....
    static final int [][][] matrices = new int[][][]{
            new int[][] {
                    {1,0,0},
                    {0,1,0},
                    {0,0,1}
            },
            new int[][] {
                    {-1,0,0},
                    {0,-1,0},
                    {0,0,1}
            },
            new int[][] {
                    {-1,0,0},
                    {0,1,0},
                    {0,0,-1}
            },
            new int[][] {
                    {1,0,0},
                    {0,-1,0},
                    {0,0,-1}
            },

            new int[][] {
                    {-1,0,0},
                    {0,0,1},
                    {0,1,0}
            },
            new int[][] {
                    {1,0,0},
                    {0,0,-1},
                    {0,1,0}
            },
            new int[][] {
                    {1,0,0},
                    {0,0,1},
                    {0,-1,0}
            },
            new int[][] {
                    {-1,0,0},
                    {0,0,-1},
                    {0,-1,0}
            },

            new int[][] {
                    {0,-1,0},
                    {1,0,0},
                    {0,0,1}
            },
            new int[][] {
                    {0,1,0},
                    {-1,0,0},
                    {0,0,1}
            },
            new int[][] {
                    {0,1,0},
                    {1,0,0},
                    {0,0,-1}
            },
            new int[][] {
                    {0,-1,0},
                    {-1,0,0},
                    {0,0,-1}
            },


            new int[][] {
                    {0,1,0},
                    {0,0,1},
                    {1,0,0}
            },
            new int[][] {
                    {0,-1,0},
                    {0,0,-1},
                    {1,0,0}
            },
            new int[][] {
                    {0,-1,0},
                    {0,0,1},
                    {-1,0,0}
            },
            new int[][] {
                    {0,1,0},
                    {0,0,-1},
                    {-1,0,0}
            },


            new int[][] {
                    {0,0,1},
                    {1,0,0},
                    {0,1,0}
            },
            new int[][] {
                    {0,0,-1},
                    {-1,0,0},
                    {0,1,0}
            },
            new int[][] {
                    {0,0,-1},
                    {1,0,0},
                    {0,-1,0}
            },
            new int[][] {
                    {0,0,1},
                    {-1,0,0},
                    {0,-1,0}
            },


            new int[][] {
                    {0,0,-1},
                    {0,1,0},
                    {1,0,0}
            },
            new int[][] {
                    {0,0,1},
                    {0,-1,0},
                    {1,0,0}
            },
            new int[][] {
                    {0,0,1},
                    {0,1,0},
                    {-1,0,0}
            },
            new int[][] {
                    {0,0,-1},
                    {0,-1,0},
                    {-1,0,0}
            }
    };
}

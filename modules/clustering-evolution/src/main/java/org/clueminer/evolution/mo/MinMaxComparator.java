package org.clueminer.evolution.mo;

import java.util.Comparator;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.comparator.ConstraintViolationComparator;
import org.uma.jmetal.util.comparator.impl.OverallConstraintViolationComparator;

/**
 * Comparator allowing minimizing or maximizing either of criteria or both of
 * them.
 *
 * @author Tomas Barton
 */
public class MinMaxComparator implements Comparator<Solution> {

    private boolean[] maximize;
    private double epsilon = 1e-9;
    private ConstraintViolationComparator constraintViolationComparator;

    public MinMaxComparator(boolean[] maximize) {
        this(new OverallConstraintViolationComparator(), 0.0);
        this.maximize = maximize;
    }

    /**
     * Constructor
     */
    public MinMaxComparator() {
        this(new OverallConstraintViolationComparator(), 0.0);
    }

    /**
     * Constructor
     *
     * @param epsilon
     */
    public MinMaxComparator(double epsilon) {
        this(new OverallConstraintViolationComparator(), epsilon);
    }

    /**
     * Constructor
     *
     * @param constraintComparator
     */
    public MinMaxComparator(ConstraintViolationComparator constraintComparator) {
        this(constraintComparator, 0.0);
    }

    /**
     * Constructor
     *
     * @param constraintComparator
     * @param epsilon
     */
    public MinMaxComparator(ConstraintViolationComparator constraintComparator, double epsilon) {
        constraintViolationComparator = constraintComparator;
        this.epsilon = epsilon;
    }

    /**
     * Compares two solutions.
     *
     * @param solution1 Object representing the first <code>Solution</code>.
     * @param solution2 Object representing the second <code>Solution</code>.
     * @return -1, or 0, or 1 if solution1 dominates solution2, both are
     *         non-dominated, or solution1 is dominated by solution2, respectively.
     */
    @Override
    public int compare(Solution solution1, Solution solution2) {
        if (solution1 == null) {
            throw new JMetalException("Solution1 is null");
        } else if (solution2 == null) {
            throw new JMetalException("Solution2 is null");
        }
        int result;
        result = constraintViolationComparator.compare(solution1, solution2);
        if (result == 0) {
            if (maximize == null) {
                result = dominanceTest(solution1, solution2);
            } else {
                result = dominanceTest(solution1, solution2, maximize);
            }
        }
        return result;
    }

    private int dominanceTest(Solution solution1, Solution solution2, boolean[] max) {
        boolean solution1Dominates = false;
        boolean solution2Dominates = false;

        int flag;
        double value1, value2;
        double diff;
        for (int i = 0; i < solution1.getNumberOfObjectives(); i++) {
            value1 = solution1.getObjective(i);
            value2 = solution2.getObjective(i);

            diff = value1 - value2;
            if (Math.abs(diff) <= epsilon) {
                //same with epsilon tolerance
                flag = 0;
            } else {
                //maximize objective
                if (max[i]) {
                    if (diff > 0.0) {
                        //solution 1 dominates
                        flag = -1;
                    } else {
                        flag = 1;
                    }

                } else {
                    //minimize objective
                    if (diff > 0.0) {
                        //solution 1 dominates
                        flag = 1;
                    } else {
                        flag = -1;
                    }
                }
            }

            if (flag == -1) {
                solution1Dominates = true;
            }

            if (flag == 1) {
                solution2Dominates = true;
            }
        }
        return dominance2int(solution1Dominates, solution2Dominates);
    }

    private int dominance2int(boolean solution1Dominates, boolean solution2Dominates) {
        int result;
        if (solution1Dominates == solution2Dominates) {
            // non-dominated solutions
            result = 0;
        } else if (solution1Dominates) {
            // solution1 dominates
            result = -1;
        } else {
            // solution2 dominates
            result = 1;
        }
        return result;
    }

    private int dominanceTest(Solution solution1, Solution solution2) {
        boolean solution1Dominates = false;
        boolean solution2Dominates = false;

        int flag;
        double value1, value2, diff;
        for (int i = 0; i < solution1.getNumberOfObjectives(); i++) {
            value1 = solution1.getObjective(i);
            value2 = solution2.getObjective(i);

            diff = value1 - value2;
            if (Math.abs(diff) <= epsilon) {
                //same with epsilon tolerance
                flag = 0;
            } else {
                //minimize objective
                if (diff > 0.0) {
                    //solution 1 dominates
                    flag = 1;
                } else {
                    flag = -1;
                }
            }

            if (flag == -1) {
                solution1Dominates = true;
            }

            if (flag == 1) {
                solution2Dominates = true;
            }
        }
        return dominance2int(solution1Dominates, solution2Dominates);
    }

    public boolean[] getMaximize() {
        return maximize;
    }

    public void setMaximize(boolean[] maximize) {
        this.maximize = maximize;
    }

}

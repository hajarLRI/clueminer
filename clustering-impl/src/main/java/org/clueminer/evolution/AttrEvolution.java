package org.clueminer.evolution;

import org.clueminer.clustering.api.evolution.Pair;
import org.clueminer.clustering.api.evolution.Individual;
import org.clueminer.clustering.api.evolution.EvolutionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import javax.swing.event.EventListenerList;
import org.clueminer.clustering.api.ClusterEvaluation;
import org.clueminer.clustering.api.ClusteringAlgorithm;
import org.clueminer.clustering.api.evolution.Evolution;
import org.clueminer.dataset.api.Dataset;
import org.clueminer.dataset.api.Instance;

/**
 *
 * @author Tomas Barton
 */
public class AttrEvolution implements Runnable, Evolution {

    private int populationSize = 100;
    private int generations;
    private Dataset<Instance> dataset;
    private boolean isFinished = true;
    private Random rand = new Random();
    /**
     * Probability of mutation
     */
    protected double mutationProbability = 0.3;
    /**
     * Probability of crossover
     */
    protected double crossoverProbability = 0.3;
    /**
     * for start and final average fitness
     */
    private Pair<Double, Double> avgFitness;
    /**
     * for start and final best fitness in whole population
     */
    private Pair<Double, Double> bestFitness;
    /**
     * for star and final time
     */
    private Pair<Long, Long> time;
    protected ClusterEvaluation evaluator;
    protected ClusterEvaluation external;
    protected ClusteringAlgorithm algorithm;

    public AttrEvolution(Dataset<Instance> dataset, int generations) {
        this.dataset = dataset;
        isFinished = false;
        this.generations = generations;
        avgFitness = new Pair<Double, Double>();
        bestFitness = new Pair<Double, Double>();
        time = new Pair<Long, Long>();
    }

    protected int attributesCount() {
        return dataset.attributeCount();
    }

    @Override
    public Dataset<Instance> getDataset() {
        return dataset;
    }

    @Override
    public void run() {
        time.a = System.currentTimeMillis();
        LinkedList<Individual> children = new LinkedList<Individual>();
        Population pop = new Population(this, populationSize);
        avgFitness.a = pop.getAvgFitness();
        Individual best = pop.getBestIndividual();
        bestFitness.a = best.getFitness();
        ArrayList<Individual> selected = new ArrayList<Individual>(populationSize);
        //System.out.println(pop);

        for (int g = 0; g < generations && !isFinished; g++) {

            // clear collection for new individuals
            children.clear();

            // apply crossover operator
            for (int i = 0; i < pop.getIndividuals().length; i++) {
                if (rand.nextDouble() < getCrossoverProbability()) {
                    // take copy of current individual
                    Individual thisOne = pop.getIndividual(i);
                    // take another individual
                    List<? extends Individual> second = pop.selectIndividuals(1);
                    // do crossover
                    List<Individual> ancestors = thisOne.cross(second.get(0));
                    // put childrens to the list of new individuals
                    children.addAll(ancestors);
                }
            }

            // apply mutate operator
            for (int i = 0; i < pop.getIndividuals().length; i++) {
                Individual thisOne = pop.getIndividual(i).deepCopy();
                thisOne.mutate();
                // put mutated individual to the list of new individuals
                children.add(thisOne);
            }
            double fitness;
            // count fitness of all changed individuals
            for (int i = 0; i < children.size(); i++) {
                children.get(i).countFitness();
                children.get(i).getFitness();
            }
            selected.clear();
            // merge new and old individuals
            for (int i = children.size(); i < pop.individualsLength(); i++) {
                Individual tmpi = pop.getIndividual(i).deepCopy();
                tmpi.countFitness();                
                selected.add(tmpi);
            }
            
            for (Individual ind : children) {
                fitness = ind.getFitness();
                if (!Double.isNaN(fitness)) {
                    selected.add(ind);
                }
            }

            // sort them by fitness (thanks to Individual implements interface Comparable)
            Individual[] newIndsArr = selected.toArray(new Individual[0]);
            //  for (int i = 0; i < newIndsArr.length; i++) {
            //      System.out.println(i + ": " + newIndsArr[i].getFitness());
            //  }
            Arrays.sort(newIndsArr, Collections.reverseOrder());
            
            int indsToCopy;
            if (newIndsArr.length > pop.individualsLength()) {
                indsToCopy = pop.individualsLength();
            } else {
                indsToCopy = newIndsArr.length;
            }
            if (indsToCopy > 0) {
                //System.out.println("copying " + indsToCopy);
                //TODO: old population should be sorted as well? take only part of the new population?
                System.arraycopy(newIndsArr, 0, pop.getIndividuals(), 0, indsToCopy);
            } else {
                throw new RuntimeException("no new individuals");
            }


            // print statistic
            // System.out.println("gen: " + g + "\t bestFit: " + pop.getBestIndividual().getFitness() + "\t avgFit: " + pop.getAvgFitness());            
            fireBestIndividual(g, pop.getBestIndividual(), pop.getAvgFitness());
        }

        time.b = System.currentTimeMillis();
        pop.sortByFitness();
        avgFitness.b = pop.getAvgFitness();
        best = pop.getBestIndividual();
        bestFitness.b = best.getFitness();

        // System.out.println("evolution took " + (end - start) + " ms");
        fireFinalResult(generations, best, time, bestFitness, avgFitness);
    }

    private double externalValidation(Individual best) {
        if (external != null) {
            return external.score(best.getClustering(), dataset);
        }
        return Double.NaN;
    }
    private transient EventListenerList evoListeners = new EventListenerList();

    private void fireBestIndividual(int generationNum, Individual best, double avgFitness) {
        EvolutionListener[] listeners;

        if (evoListeners != null) {
            listeners = evoListeners.getListeners(EvolutionListener.class);
            for (int i = 0; i < listeners.length; i++) {
                listeners[i].bestInGeneration(generationNum, best, avgFitness, externalValidation(best));
            }
        }
    }

    public void addEvolutionListener(EvolutionListener listener) {
        evoListeners.add(EvolutionListener.class, listener);
    }

    private void fireFinalResult(int g, Individual best, Pair<Long, Long> time,
            Pair<Double, Double> bestFitness, Pair<Double, Double> avgFitness) {
        EvolutionListener[] listeners;

        if (evoListeners != null) {
            listeners = evoListeners.getListeners(EvolutionListener.class);
            for (int i = 0; i < listeners.length; i++) {
                listeners[i].finalResult(this, g, best, time, bestFitness, avgFitness, externalValidation(best));
            }
        }
    }

    public double getMutationProbability() {
        return mutationProbability;
    }

    public void setMutationProbability(double mutationProbability) {
        this.mutationProbability = mutationProbability;
    }

    public double getCrossoverProbability() {
        return crossoverProbability;
    }

    public void setCrossoverProbability(double crossoverProbability) {
        this.crossoverProbability = crossoverProbability;
    }

    public ClusteringAlgorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(ClusteringAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    public ClusterEvaluation getEvaluator() {
        return evaluator;
    }

    public void setEvaluator(ClusterEvaluation evaluator) {
        this.evaluator = evaluator;
    }

    /**
     * External validation criterion, is used only for reporting, not during
     * evolution
     *
     * @return
     */
    public ClusterEvaluation getExternal() {
        return external;
    }

    public void setExternal(ClusterEvaluation external) {
        this.external = external;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }
}
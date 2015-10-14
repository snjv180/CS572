/**
 * 
 */
package com.comp572.proj3;

/**
 * @author Sanjeev
 * 
 */
public class PopulationStatistics {
    private Integer generationNumer;

    private String bestIndividual4Gen;

    private Integer bestIndividualFitness4Gen;

    private Double avgFitness4Gen;
    
    private Double avgSize4Gen;

    private Double timeToEvolve4Gen;

    public PopulationStatistics(){
	setGenerationNumer(0);
	setBestIndividual4Gen("");
	setBestIndividualFitness4Gen(0);
	setAvgFitness4Gen(0.0);
	setAvgSize4Gen(0.0);
    }
    public PopulationStatistics(Integer generationNumer,
	    String bestIndividual4Gen, Integer bestIndividualFitness4Gen,
	    Double avgFitness4Gen,Double avgSize4Gen, Double timeToEvolve4Gen) {
	setGenerationNumer(generationNumer);
	setBestIndividual4Gen(bestIndividual4Gen);
	setBestIndividualFitness4Gen(bestIndividualFitness4Gen);
	setAvgFitness4Gen(avgFitness4Gen);
	setAvgSize4Gen(avgSize4Gen);
	setTimeToEvolve4Gen(timeToEvolve4Gen);
    }

    /**
     * @return the generationNumer
     */
    public Integer getGenerationNumer() {
	return generationNumer;
    }

    /**
     * @param generationNumer
     *            the generationNumer to set
     */
    public void setGenerationNumer(Integer generationNumer) {
	this.generationNumer = generationNumer;
    }

    /**
     * @return the bestIndividual4Gen
     */
    public String getBestIndividual4Gen() {
	return bestIndividual4Gen;
    }

    /**
     * @param bestIndividual4Gen
     *            the bestIndividual4Gen to set
     */
    public void setBestIndividual4Gen(String bestIndividual4Gen) {
	this.bestIndividual4Gen = bestIndividual4Gen;
    }

    /**
     * @return the bestIndividualFitness4Gen
     */
    public Integer getBestIndividualFitness4Gen() {
	return bestIndividualFitness4Gen;
    }

    /**
     * @param bestIndividualFitness4Gen
     *            the bestIndividualFitness4Gen to set
     */
    public void setBestIndividualFitness4Gen(Integer bestIndividualFitness4Gen) {
	this.bestIndividualFitness4Gen = bestIndividualFitness4Gen;
    }

    /**
     * @return the avgFitness4Gen
     */
    public Double getAvgFitness4Gen() {
	return avgFitness4Gen;
    }

    /**
     * @param avgFitness4Gen
     *            the avgFitness4Gen to set
     */
    public void setAvgFitness4Gen(Double avgFitness4Gen) {
	this.avgFitness4Gen = avgFitness4Gen;
    }

    /**
     * @return the avgSize4Gen
     */
    public Double getAvgSize4Gen() {
        return avgSize4Gen;
    }
    /**
     * @param avgSize4Gen the avgSize4Gen to set
     */
    public void setAvgSize4Gen(Double avgSize4Gen) {
        this.avgSize4Gen = avgSize4Gen;
    }
    /**
     * @return the timeToEvolve4Gen
     */
    public Double getTimeToEvolve4Gen() {
	return timeToEvolve4Gen;
    }

    /**
     * @param timeToEvolve4Gen
     *            the timeToEvolve4Gen to set
     */
    public void setTimeToEvolve4Gen(Double timeToEvolve4Gen) {
	this.timeToEvolve4Gen = timeToEvolve4Gen;
    }

}

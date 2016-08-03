package org.yaen.starter.core.model.models.tool;

import org.yaen.starter.core.model.models.OneModel;

import lombok.Getter;

/**
 * performance monitor model, mainly used to calculate time
 * 
 * @author Yaen 2016年8月3日下午1:31:41
 */
public class PerfMonModel extends OneModel {

	/** the start time in millisecond */
	@Getter
	private long startTime = 0L;

	/** the stop time in millisecond */
	@Getter
	private long stopTime = 0L;

	/**
	 * constructor, inner call start
	 */
	public PerfMonModel() {
		this.start();
	}

	/**
	 * get used time in millisecond, just stop - start
	 * 
	 * @return
	 */
	public long getUsedTime() {
		return this.stopTime - this.startTime;
	}

	/**
	 * start measure, and reset other time, return the start time in millisecond
	 * 
	 * @return
	 */
	public long start() {
		this.startTime = System.currentTimeMillis();
		this.stopTime = 0L;
		return this.startTime;
	}

	/**
	 * stop measure, and return the used time(difference of start and stop)
	 * <p>
	 * this maybe called multiple time to get each used time when called
	 * 
	 * @return
	 */
	public long stop() {
		this.stopTime = System.currentTimeMillis();
		return this.getUsedTime();
	}

}

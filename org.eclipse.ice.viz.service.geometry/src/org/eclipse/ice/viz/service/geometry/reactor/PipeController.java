/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Robert Smith
 *******************************************************************************/
package org.eclipse.ice.viz.service.geometry.reactor;

import org.eclipse.ice.viz.service.modeling.AbstractController;
import org.eclipse.ice.viz.service.modeling.AbstractView;
import org.eclipse.ice.viz.service.modeling.IWireFramePart;
import org.eclipse.ice.viz.service.modeling.TubeController;

/**
 * A Pipe part for the Reactor Analyzer.
 * 
 * @author Robert Smith
 *
 */
public class PipeController extends TubeController implements IWireFramePart {

	/**
	 * The nullary constructor.
	 */
	public PipeController() {
		super();
	}

	/**
	 * The default constructor
	 * 
	 * @param model
	 * @param view
	 */
	public PipeController(PipeMesh model, AbstractView view) {
		super(model, view);
	}

	/**
	 * Get the farthest points in all three directions for the pipe's lower end
	 * 
	 * @return The pipe's lower boundary's minimum and maximum x, y, and z
	 *         coordinates
	 */
	public Extrema getLowerExtrema() {
		return ((PipeView) view).getLowerExtrema();
	}

	/**
	 * Convenience getter method for the number of rods.
	 * 
	 * @return The number of rods in a SubChannel pipe
	 */
	public int getNumRods() {
		return ((PipeMesh) model).getNumRods();
	}

	/**
	 * Convenience getter method for the pitch
	 * 
	 * @return The pipe's pitch
	 */
	public double getPitch() {
		return ((PipeMesh) model).getPitch();
	}

	/**
	 * Convenience getter method for the rod diameter
	 * 
	 * @return The pipe's rod diameter, under the assumption that all rods are
	 *         of uniform size.
	 */
	public double getRodDiameter() {
		return ((PipeMesh) model).getRodDiameter();
	}

	/**
	 * Get the farthest points in all three directions for the pipe's upper end
	 * 
	 * @return The pipe's upper boundary's minimum and maximum x, y, and z
	 *         coordinates
	 */
	public Extrema getUpperExtrema() {
		return ((PipeView) view).getUpperExtrema();
	}

	/**
	 * Convenience setter method for a SubChannel Pipe's number of rods. Does
	 * nothing for non-SubChannel pipes.
	 * 
	 * @param numRods
	 *            The number of rods in the SubChannel
	 */
	public void setNumRods(int numRods) {
		((PipeMesh) model).setNumRods(numRods);
	}

	/**
	 * Convenience setter method for the pipe's pitch
	 * 
	 * @param pitch
	 */
	public void setPitch(double pitch) {
		((PipeMesh) model).setPitch(pitch);
	}

	/**
	 * Convenience setter method for the pipe's rod diameter
	 * 
	 * @param rodDiameter
	 *            The pipe's rod diameter
	 */
	public void setRodDiameter(double rodDiameter) {
		((PipeMesh) model).setRodDiameter(rodDiameter);
	}

	/**
	 * Convience setter method for the pipe's length
	 * 
	 * @param length
	 *            The pipe's length
	 */
	public void setLength(Double length) {
		((PipeMesh) model).setLength(length);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.reactor.javafx.datatypes.WireFramePart#
	 * setWireFrameMode(boolean)
	 */
	@Override
	public void setWireFrameMode(boolean on) {
		((IWireFramePart) view).setWireFrameMode(on);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.modeling.AbstractController#clone()
	 */
	@Override
	public Object clone() {

		// Create a new shape from clones of the model and view
		PipeController clone = new PipeController();

		// Copy any other data into the clone
		clone.copy(this);

		return clone;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.modeling.AbstractController#copy(org.eclipse.
	 * ice.viz.service.modeling.AbstractController)
	 */
	@Override
	public void copy(AbstractController source) {

		// Create the model and give it a reference to this
		model = new PipeMesh();
		model.setController(this);

		// Copy the other object's data members
		model.copy(source.getModel());
		view = (AbstractView) source.getView().clone();

		// Register as a listener to the model and view
		model.register(this);
		view.register(this);
	}

}

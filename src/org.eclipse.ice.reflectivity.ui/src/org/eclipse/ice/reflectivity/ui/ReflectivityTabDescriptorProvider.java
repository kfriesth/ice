/**
 * 
 */
package org.eclipse.ice.reflectivity.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ice.datastructures.ICEObject.ListComponent;
import org.eclipse.ice.datastructures.componentVisitor.IComponentVisitor;
import org.eclipse.ice.datastructures.componentVisitor.IReactorComponent;
import org.eclipse.ice.datastructures.form.AdaptiveTreeComposite;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.MasterDetailsComponent;
import org.eclipse.ice.datastructures.form.MatrixComponent;
import org.eclipse.ice.datastructures.form.ResourceComponent;
import org.eclipse.ice.datastructures.form.TableComponent;
import org.eclipse.ice.datastructures.form.TimeDataComponent;
import org.eclipse.ice.datastructures.form.TreeComposite;
import org.eclipse.ice.datastructures.form.emf.EMFComponent;
import org.eclipse.ice.datastructures.form.geometry.GeometryComponent;
import org.eclipse.ice.datastructures.form.geometry.IShape;
import org.eclipse.ice.datastructures.form.mesh.MeshComponent;
import org.eclipse.jface.viewers.IFilter;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.AbstractSectionDescriptor;
import org.eclipse.ui.views.properties.tabbed.AbstractTabDescriptor;
import org.eclipse.ui.views.properties.tabbed.ISection;
import org.eclipse.ui.views.properties.tabbed.ITabDescriptor;
import org.eclipse.ui.views.properties.tabbed.ITabDescriptorProvider;

/**
 * Provides custom tabs to the reflectivity model's custom tabbed properties
 * view. The first tab describes the data component and input values for its
 * entries. </br>
 * </br>
 * 
 * TODO: Need to implement second tab for list component selections! </br>
 * 
 * TODO: Need to implement vistor pattern for Data and List components! This is
 * a much better way of handling new selections rather than computing everything
 * in the getTabDescriptors() method.
 * 
 * @author Kasper Gammeltoft
 *
 */
public class ReflectivityTabDescriptorProvider
		implements ITabDescriptorProvider, IComponentVisitor {

	/**
	 * The tab descriptors. Should only hold the one tab for now with the
	 * information on the data component entries
	 */
	ITabDescriptor[] descriptors;

	/**
	 * The data component that is currently being inspected
	 */
	DataComponent component;

	/**
	 * The constructor
	 */
	public ReflectivityTabDescriptorProvider() {
		component = null;
		descriptors = new ITabDescriptor[1];
	}

	private final IFilter filter = new IFilter() {
		@Override
		public boolean select(Object toTest) {
			return (toTest instanceof DataComponent
					|| toTest instanceof ListComponent);
		}
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.tabbed.ITabDescriptorProvider#
	 * getTabDescriptors(org.eclipse.ui.IWorkbenchPart,
	 * org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public ITabDescriptor[] getTabDescriptors(IWorkbenchPart part,
			ISelection selection) {

		// Make sure the selection is valid
		if (selection != null && selection instanceof IStructuredSelection) {

			// Get a reference to the data component
			Object obj = ((IStructuredSelection) selection).getFirstElement();
			if (obj instanceof DataComponent) {
				component = (DataComponent) obj;
			}

			// Create the tab first if it has not been done already
			if (descriptors[0] == null) {

				// New tab, generic and the same for all reflectivity models
				AbstractTabDescriptor inputTabDescriptor = new AbstractTabDescriptor() {

					@Override
					public String getCategory() {
						return "Reflectivity";
					}

					@Override
					public String getId() {
						return "Reflectivity.Input";
					}

					@Override
					public String getLabel() {
						return "Inputs";
					}

				};

				// Set the tab provider
				descriptors[0] = inputTabDescriptor;
			}

			ITabDescriptor tab = descriptors[0];

			// Create a SectionDescriptor for the data component's inputs
			AbstractSectionDescriptor generalSection = new AbstractSectionDescriptor() {

				@Override
				public String getId() {
					return "Input:";
				}

				@Override
				public ISection getSectionClass() {
					ReflectivityPropertySection section;
					section = new ReflectivityPropertySection();
					section.setDataComponent(component);
					return section;
				}

				@Override
				public String getTargetTab() {
					return tab.getId();
				}

				@Override
				public IFilter getFilter() {
					return filter;
				}

			};

			// Add the section descriptor to the tab
			List<AbstractSectionDescriptor> sectionDescriptors = new ArrayList<AbstractSectionDescriptor>();
			sectionDescriptors.add(generalSection);

			((AbstractTabDescriptor) tab)
					.setSectionDescriptors(sectionDescriptors);

		}

		return descriptors;
	}

	@Override
	public void visit(DataComponent component) {
		this.component = component;
	}

	@Override
	public void visit(ResourceComponent component) {
		return;

	}

	@Override
	public void visit(TableComponent component) {
		return;
	}

	@Override
	public void visit(MatrixComponent component) {
		return;
	}

	@Override
	public void visit(IShape component) {
		return;
	}

	@Override
	public void visit(GeometryComponent component) {
		return;
	}

	@Override
	public void visit(MasterDetailsComponent component) {
		return;
	}

	@Override
	public void visit(TreeComposite component) {
		return;
	}

	@Override
	public void visit(AdaptiveTreeComposite component) {
		return;
	}

	@Override
	public void visit(IReactorComponent component) {
		return;
	}

	@Override
	public void visit(TimeDataComponent component) {
		return;
	}

	@Override
	public void visit(MeshComponent component) {
		return;
	}

	@Override
	public void visit(EMFComponent component) {
		return;
	}

	@Override
	public void visit(ListComponent<?> component) {
		return;
	}

}

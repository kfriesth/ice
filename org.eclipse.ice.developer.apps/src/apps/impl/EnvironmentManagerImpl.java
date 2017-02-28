/**
 */
package apps.impl;

import apps.AppsFactory;
import apps.AppsPackage;
import apps.EnvironmentCreator;
import apps.EnvironmentManager;
import apps.EnvironmentState;
import apps.IEnvironment;
import apps.docker.DockerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.osgi.service.prefs.BackingStoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <!-- begin-user-doc --> An implementation of the model object
 * '<em><b>Manager</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link apps.impl.EnvironmentManagerImpl#getEnvironmentCreator
 * <em>Environment Creator</em>}</li>
 * </ul>
 *
 * @generated
 */
public class EnvironmentManagerImpl extends MinimalEObjectImpl.Container implements EnvironmentManager {
	/**
	 * The cached value of the '{@link #getEnvironmentCreator() <em>Environment
	 * Creator</em>}' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getEnvironmentCreator()
	 * @generated
	 * @ordered
	 */
	protected EnvironmentCreator environmentCreator;

	protected HashMap<String, IEnvironment> environments;

	protected final Logger logger;

	private static final String preferencesId = "org.eclipse.ice.developer.apps";

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 */
	protected EnvironmentManagerImpl() {
		super();
		logger = LoggerFactory.getLogger(getClass());

		// FIXME, make this extensible to others.
		environmentCreator = AppsFactory.eINSTANCE.createJsonEnvironmentCreator();

		// Initialize the mapping of environments
		environments = new HashMap<String, IEnvironment>();

		// FIXME MAKE THE PREFERENCE STORE EXTENSIBLE to not have to use Eclipse
		// Get the Application preferences
		IEclipsePreferences prefs = InstanceScope.INSTANCE.getNode(preferencesId);

		if (prefs != null) {
			try {
				for (String key : prefs.keys()) {
					String pref = prefs.get(key, "");
					if (!pref.isEmpty()) {
						IEnvironment env = loadFromXMI(pref);
						environments.put(env.getName(), env);
						System.out.println("HELLO: " + pref);
					}
				}
			} catch (BackingStoreException e) {
				logger.error(getClass().getName() + " Exception!", e);
			}
		}

	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AppsPackage.Literals.ENVIRONMENT_MANAGER;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EnvironmentCreator getEnvironmentCreator() {
		if (environmentCreator != null && environmentCreator.eIsProxy()) {
			InternalEObject oldEnvironmentCreator = (InternalEObject) environmentCreator;
			environmentCreator = (EnvironmentCreator) eResolveProxy(oldEnvironmentCreator);
			if (environmentCreator != oldEnvironmentCreator) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							AppsPackage.ENVIRONMENT_MANAGER__ENVIRONMENT_CREATOR, oldEnvironmentCreator,
							environmentCreator));
			}
		}
		return environmentCreator;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EnvironmentCreator basicGetEnvironmentCreator() {
		return environmentCreator;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setEnvironmentCreator(EnvironmentCreator newEnvironmentCreator) {
		EnvironmentCreator oldEnvironmentCreator = environmentCreator;
		environmentCreator = newEnvironmentCreator;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AppsPackage.ENVIRONMENT_MANAGER__ENVIRONMENT_CREATOR,
					oldEnvironmentCreator, environmentCreator));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 */
	public IEnvironment create(String dataString) {
		IEnvironment env = environmentCreator.create(dataString);
		environments.put(env.getName(), env);
		env.setState(EnvironmentState.STOPPED);
		return env;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 */
	public EList<String> list() {
		EList<String> list = new BasicEList<String>();
		for (String s : environments.keySet()) {
			list.add(s);
		}
		return list;

	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 */
	public IEnvironment get(String environmentName) {
		if (environments.containsKey(environmentName)) {
			return environments.get(environmentName);
		} else {
			throw new IllegalArgumentException("No environment named " + environmentName);
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 */
	public IEnvironment loadFromFile(String fileName) {
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		Map<String, Object> m = reg.getExtensionToFactoryMap();
		m.put("environment", new XMIResourceFactoryImpl());
		// Obtain a new resource set
		ResourceSet resSet = new ResourceSetImpl();
		// Get the resource
		Resource resource = resSet.getResource(URI.createURI(fileName), true);
		// Get the first model element and cast it to the right type, in my
		// example everything is hierarchical included in this first node
		IEnvironment environment = (IEnvironment) resource.getContents().get(0);
		return environment;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 */
	public String persistToString(String environmentName) {
		
		if (!environments.containsKey(environmentName)) {
			throw new IllegalArgumentException("Invalid environment name to persist.");
		}
		
		IEnvironment environment = environments.get(environmentName);
        Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
        Map<String, Object> m = reg.getExtensionToFactoryMap();
        m.put("env", new XMIResourceFactoryImpl());

        // Obtain a new resource set
        ResourceSet resSet = new ResourceSetImpl();

        // create a resource
        Resource resource = resSet.createResource(URI
                        .createURI("dummy.env"));
        
        // Get the first model element and cast it to the right type, in my
        // example everything is hierarchical included in this first node
        resource.getContents().add(environment);

        OutputStream output = new OutputStream()
        {
            private StringBuilder string = new StringBuilder();
            @Override
            public void write(int b) throws IOException {
                this.string.append((char) b );
            }

            public String toString(){
                return this.string.toString();
            }
        };
        // now save the content.
        try {
                resource.save(output, Collections.EMPTY_MAP);
        } catch (IOException e) {
                e.printStackTrace();
        }
        
        return output.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 */
	public void persistToFile(String environmentName, String fileName) {
		if (!environments.containsKey(environmentName)) {
			throw new IllegalArgumentException("Invalid environment name to persist.");
		}
		
		IEnvironment environment = environments.get(environmentName);
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		Map<String, Object> m = reg.getExtensionToFactoryMap();
		m.put("env", new XMIResourceFactoryImpl());

		// Obtain a new resource set
		ResourceSet resSet = new ResourceSetImpl();

		// create a resource
		Resource resource = resSet.createResource(URI.createURI("fileName"));

		// Get the first model element and cast it to the right type, in my
		// example everything is hierarchical included in this first node
		resource.getContents().add(environment);

		// now save the content.
		try {
			resource.save(Collections.EMPTY_MAP);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean connect(String environmentName) {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc --> This method creates the correct IEnvironment
	 * instance based on the user specified type String. Currently supported are
	 * the Docker and Local File System Environment types. <!-- end-user-doc -->
	 */
	public IEnvironment createEnvironment(String dataString) {
		IEnvironment env = environmentCreator.create(dataString);
		environments.put(env.getName(), env);
		return env;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 */
	public IEnvironment loadEnvironmentFromFile(String file) {
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		Map<String, Object> m = reg.getExtensionToFactoryMap();
		m.put("environment", new XMIResourceFactoryImpl());
		// Obtain a new resource set
		ResourceSet resSet = new ResourceSetImpl();
		// Get the resource
		Resource resource = resSet.getResource(URI.createURI(file), true);
		// Get the first model element and cast it to the right type, in my
		// example everything is hierarchical included in this first node
		IEnvironment environment = (IEnvironment) resource.getContents().get(0);
		return environment;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<String> listAvailableSpackPackages() {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 */
	public void persistEnvironments() {
		for (IEnvironment env : environments.values()) {
			String xmiStr = persistToString(env.getName());
			// Save this App as a Preference
			IEclipsePreferences prefs = InstanceScope.INSTANCE.getNode(preferencesId);
			try {
				prefs.put(env.getName(), xmiStr);
				prefs.flush();
			} catch (BackingStoreException e) {
				logger.error(getClass().getName() + " Exception!", e);
			}
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 */
	public IEnvironment createEmpty(String type) {
		if (type.equals("Docker")) {
			return DockerFactory.eINSTANCE.createDockerEnvironment();
		} else {
			throw new IllegalArgumentException("Illegal Environment type");
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 */
	public IEnvironment loadFromXMI(String xmiStr) {
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		Map<String, Object> m = reg.getExtensionToFactoryMap();
		m.put("environment", new XMIResourceFactoryImpl());

		ResourceSet resSet = new ResourceSetImpl();
		// Get the resource
		Resource resource = new XMIResourceImpl();
		resSet.getResources().add(resource);

		try {
			resource.load(new ByteArrayInputStream(xmiStr.getBytes()), m);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Get the first model element and cast it to the right type, in my
		// example everything is hierarchical included in this first node
		IEnvironment environment = (IEnvironment) resource.getContents().get(0);
		return environment;
	}


	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case AppsPackage.ENVIRONMENT_MANAGER__ENVIRONMENT_CREATOR:
			if (resolve)
				return getEnvironmentCreator();
			return basicGetEnvironmentCreator();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case AppsPackage.ENVIRONMENT_MANAGER__ENVIRONMENT_CREATOR:
			setEnvironmentCreator((EnvironmentCreator) newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
		case AppsPackage.ENVIRONMENT_MANAGER__ENVIRONMENT_CREATOR:
			setEnvironmentCreator((EnvironmentCreator) null);
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
		case AppsPackage.ENVIRONMENT_MANAGER__ENVIRONMENT_CREATOR:
			return environmentCreator != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
		case AppsPackage.ENVIRONMENT_MANAGER___CREATE__STRING:
			return create((String) arguments.get(0));
		case AppsPackage.ENVIRONMENT_MANAGER___LIST:
			return list();
		case AppsPackage.ENVIRONMENT_MANAGER___GET__STRING:
			return get((String) arguments.get(0));
		case AppsPackage.ENVIRONMENT_MANAGER___LOAD_FROM_FILE__STRING:
			return loadFromFile((String) arguments.get(0));
		case AppsPackage.ENVIRONMENT_MANAGER___PERSIST_TO_STRING__STRING:
			return persistToString((String) arguments.get(0));
		case AppsPackage.ENVIRONMENT_MANAGER___PERSIST_TO_FILE__STRING_STRING:
			persistToFile((String) arguments.get(0), (String) arguments.get(1));
			return null;
		case AppsPackage.ENVIRONMENT_MANAGER___CONNECT__STRING:
			return connect((String) arguments.get(0));
		case AppsPackage.ENVIRONMENT_MANAGER___LIST_AVAILABLE_SPACK_PACKAGES:
			return listAvailableSpackPackages();
		case AppsPackage.ENVIRONMENT_MANAGER___PERSIST_ENVIRONMENTS:
			persistEnvironments();
			return null;
		case AppsPackage.ENVIRONMENT_MANAGER___CREATE_EMPTY__STRING:
			return createEmpty((String) arguments.get(0));
		case AppsPackage.ENVIRONMENT_MANAGER___LOAD_FROM_XMI__STRING:
			return loadFromXMI((String) arguments.get(0));
		}
		return super.eInvoke(operationID, arguments);
	}

} // EnvironmentManagerImpl

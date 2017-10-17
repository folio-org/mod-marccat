package librisuite.business.common;


/**
 * This class is used by DAOMadesDescriptor to supply accessPointFactory
 * statically without necessarily MadesCatalog 
 * @author michelem
 *
 */
public class MadesMapBackedFactorySupport {

	private static AbstractMapBackedFactory accessPointFactory;

	static {
		accessPointFactory = new MapBackedFactory();
		PropertyBasedFactoryBuilder builder = new PropertyBasedFactoryBuilder();
		builder.load("/librisuite/business/cataloguing/mades/accessPointFactory.properties", accessPointFactory);
	}

	public static AbstractMapBackedFactory getAccessPointFactory() {
		return accessPointFactory;
	}
}

package galena.hats.services;

import org.apache.xbean.finder.ResourceFinder;

public class CommonServices {

    private static final ResourceFinder LOADER = new ResourceFinder("META-INF/services/");

    public static final INetwork NETWORK = CoreServices.load(INetwork.class);

}

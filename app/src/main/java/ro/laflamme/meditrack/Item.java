package ro.laflamme.meditrack;

/**
 * Created by loopiezlol on 19.04.2015.
 */
public class Item implements ItemInterface {
    @Override
    public String getTitle() {
        throw new UnsupportedOperationException("Called super's method");
    }

    @Override
    public String getSubtitle() {
        throw new UnsupportedOperationException("Called super's method");
    }
}

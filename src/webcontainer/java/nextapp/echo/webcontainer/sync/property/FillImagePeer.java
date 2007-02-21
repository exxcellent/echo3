package nextapp.echo.webcontainer.sync.property;

import org.w3c.dom.Element;

import nextapp.echo.app.FillImage;
import nextapp.echo.app.ImageReference;
import nextapp.echo.app.util.Context;
import nextapp.echo.app.xml.PropertyPeerFactory;
import nextapp.echo.app.xml.XmlContext;

public class FillImagePeer extends nextapp.echo.app.xml.property.FillImagePeer {

    public Element createFillImageElement(Context context, FillImage fillImage) {
        XmlContext xmlContext = (XmlContext) context.get(XmlContext.class);

        Element fiElement = xmlContext.getDocument().createElement("fi");
        
        ImageReference imageReference = fillImage.getImage();
        PropertyPeerFactory propertyPeerFactory = (PropertyPeerFactory) context.get(PropertyPeerFactory.class);
        AbstractImageReferencePeer imagePeer = 
                (AbstractImageReferencePeer) propertyPeerFactory.getPeerForProperty(imageReference.getClass());
        if (imagePeer == null) {
            throw new IllegalArgumentException("Image synchronization peer not found for container image");
        }
        
        fiElement.setAttribute("u", imagePeer.getImageUrl(context, imageReference));
        
        switch (fillImage.getRepeat()) {
        case FillImage.NO_REPEAT:
            fiElement.setAttribute("r", "0");
            break;
        case FillImage.REPEAT_HORIZONTAL:
            fiElement.setAttribute("r", "x");
            break;
        case FillImage.REPEAT_VERTICAL:
            fiElement.setAttribute("r", "y");
            break;
        default:
            fiElement.setAttribute("r", "xy");
            break;
        }

        return fiElement;
    }
    
    /**
     * @see nextapp.echo.webcontainer.PropertySynchronizePeer#toProperty(Context, Class, org.w3c.dom.Element)
     */
    public Object toProperty(Context context, Class objectClass, Element propertyElement) {
        //TODO. Implement.
        return null;
    }

    /**
     * @see nextapp.echo.webcontainer.PropertySynchronizePeer#toXml(Context, Class, org.w3c.dom.Element, java.lang.Object)
     */
    public void toXml(Context rc, Class objectClass, Element propertyElement, Object propertyValue) {
        FillImage fillImage = (FillImage) propertyValue;
        propertyElement.setAttribute("t", "FillImage");
        propertyElement.appendChild(createFillImageElement(rc, fillImage));
    }
}

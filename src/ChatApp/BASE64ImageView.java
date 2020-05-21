/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ChatApp;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.Base64;
import java.util.Dictionary;
import java.util.Hashtable;
import javax.imageio.ImageIO;
import javax.swing.text.Element;
import javax.swing.text.View;
import javax.swing.text.html.HTML;
import javax.swing.text.html.ImageView;

/**
 *
 * @author ASUS
 */
public class BASE64ImageView extends ImageView {

    private URL url;

    public BASE64ImageView(Element elmnt) {
        super(elmnt);
        populateImage();
    }

    private void populateImage() {
        Dictionary<URL, Image> cache = (Dictionary<URL, Image>) getDocument()
                .getProperty("imageCache");
        if (cache == null) {
            cache = new Hashtable<>();
            getDocument().putProperty("imageCache", cache);
        }

        URL src = getImageURL();
        cache.put(src, loadImage());

    }

    private Image loadImage() {
        String b64 = getBASE64Image();
        BufferedImage newImage = null;
        ByteArrayInputStream bais = null;
        try {
            bais = new ByteArrayInputStream(
                    Base64.getDecoder().decode(b64.getBytes()));
            newImage = ImageIO.read(bais);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return newImage;
    }

    @Override
    public URL getImageURL() {
        String src = (String) getElement().getAttributes()
                .getAttribute(HTML.Attribute.SRC);
        if (isBase64Encoded(src)) {

            this.url = BASE64ImageView.class.getProtectionDomain()
                    .getCodeSource().getLocation();

            return this.url;
        }
        return super.getImageURL();
    }

    private boolean isBase64Encoded(String src) {
        return src != null && src.contains("base64,");
    }

    private String getBASE64Image() {
        String src = (String) getElement().getAttributes()
                .getAttribute(HTML.Attribute.SRC);
        if (!isBase64Encoded(src)) {
            return null;
        }
        return src.substring(src.indexOf("base64,") + 7, src.length() - 1);
    }

}

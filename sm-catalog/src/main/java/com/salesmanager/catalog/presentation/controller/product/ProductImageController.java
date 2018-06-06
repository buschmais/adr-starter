package com.salesmanager.catalog.presentation.controller.product;

import com.salesmanager.catalog.business.service.product.image.ProductImageService;
import com.salesmanager.catalog.model.content.FileContentType;
import com.salesmanager.catalog.model.content.OutputContentFile;
import com.salesmanager.catalog.model.product.file.ProductImageSize;
import com.salesmanager.common.business.exception.ServiceException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
public class ProductImageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductImageController.class);


    @Inject
    private ProductImageService productImageService;

    /**
     * For product images
     * @Deprecated
     * @param storeCode
     * @param productCode
     * @param imageType
     * @param imageName
     * @param extension
     * @return
     * @throws IOException
     */
    @RequestMapping("/static/{storeCode}/{imageType}/{productCode}/{imageName}.{extension}")
    public @ResponseBody
    byte[] printImage(@PathVariable final String storeCode, @PathVariable final String productCode, @PathVariable final String imageType, @PathVariable final String imageName, @PathVariable final String extension) throws IOException {

        // product image
        // example small product image -> /static/DEFAULT/products/TB12345/product1.jpg

        // example large product image -> /static/DEFAULT/products/TB12345/product1.jpg


        /**
         * List of possible imageType
         *
         */


        ProductImageSize size = ProductImageSize.SMALL;

        if(imageType.equals(FileContentType.PRODUCTLG.name())) {
            size = ProductImageSize.LARGE;
        }



        OutputContentFile image = null;
        try {
            image = productImageService.getProductImage(storeCode, productCode, new StringBuilder().append(imageName).append(".").append(extension).toString(), size);
        } catch (ServiceException e) {
            LOGGER.error("Cannot retrieve image " + imageName, e);
        }
        if(image!=null) {
            return image.getFile().toByteArray();
        } else {
            //empty image placeholder
            return null;
        }

    }

    /**
     * Exclusive method for dealing with product images
     * @param storeCode
     * @param productCode
     * @param imageName
     * @param extension
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping("/static/products/{storeCode}/{productCode}/{imageSize}/{imageName}.{extension}")
    public @ResponseBody byte[] printImage(@PathVariable final String storeCode, @PathVariable final String productCode, @PathVariable final String imageSize, @PathVariable final String imageName, @PathVariable final String extension, HttpServletRequest request) throws IOException {

        // product image small
        // example small product image -> /static/products/DEFAULT/TB12345/SMALL/product1.jpg

        // example large product image -> /static/products/DEFAULT/TB12345/LARGE/product1.jpg


        /**
         * List of possible imageType
         *
         */


        ProductImageSize size = ProductImageSize.SMALL;

        if(FileContentType.PRODUCTLG.name().equals(imageSize)) {
            size = ProductImageSize.LARGE;
        }




        OutputContentFile image = null;
        try {
            image = productImageService.getProductImage(storeCode, productCode, new StringBuilder().append(imageName).append(".").append(extension).toString(), size);
        } catch (ServiceException e) {
            LOGGER.error("Cannot retrieve image " + imageName, e);
        }
        if(image!=null) {
            return image.getFile().toByteArray();
        } else {
            //empty image placeholder
            return null;
        }

    }

    /**
     * Exclusive method for dealing with product images
     * @param storeCode
     * @param productCode
     * @param imageName
     * @param extension
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping("/static/products/{storeCode}/{productCode}/{imageName}.{extension}")
    public @ResponseBody byte[] printImage(@PathVariable final String storeCode, @PathVariable final String productCode, @PathVariable final String imageName, @PathVariable final String extension, HttpServletRequest request) throws IOException {

        // product image
        // example small product image -> /static/products/DEFAULT/TB12345/product1.jpg?size=small

        // example large product image -> /static/products/DEFAULT/TB12345/product1.jpg
        // or
        //example large product image -> /static/products/DEFAULT/TB12345/product1.jpg?size=large


        /**
         * List of possible imageType
         *
         */


        ProductImageSize size = ProductImageSize.LARGE;


        if(StringUtils.isNotBlank(request.getParameter("size"))) {
            String requestSize = request.getParameter("size");
            if(requestSize.equals(ProductImageSize.SMALL.name())) {
                size = ProductImageSize.SMALL;
            }
        }



        OutputContentFile image = null;
        try {
            image = productImageService.getProductImage(storeCode, productCode, new StringBuilder().append(imageName).append(".").append(extension).toString(), size);
        } catch (ServiceException e) {
            LOGGER.error("Cannot retrieve image " + imageName, e);
        }
        if(image!=null) {
            return image.getFile().toByteArray();
        } else {
            //empty image placeholder
            return null;
        }

    }

}

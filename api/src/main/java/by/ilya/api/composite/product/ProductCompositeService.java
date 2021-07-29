package by.ilya.api.composite.product;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "ProductComposite", description = "REST API for composite product information.")
public interface ProductCompositeService {

   /**
     * Sample usage: curl $HOST:$PORT/product-composite/1
     *
     * @param productId
     * @return the composite product info, if found, else null
     */
    @Operation(
        description = "${api.product-composite.get-composite-product.description}",
        summary = "${api.product-composite.get-composite-product.notes}")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Bad Request, invalid format of the request. See response message for more information."),
        @ApiResponse(responseCode = "404", description = "Not found, the specified id does not exist."),
        @ApiResponse(responseCode = "422", description = "Unprocessable entity, input parameters caused the processing to fails. See response message for more information.")
    })
    @GetMapping(
        value    = "/product-composite/{productId}",
        produces = "application/json")
    ProductAggregate getProduct(@PathVariable int productId);

     /**
     * Sample usage: curl $HOST:$PORT/product-composite/1
     *
     * @param productId
     * @return the composite product info, if found, else null
     */
    @Operation(
        description = "${api.product-composite.create-composite-product.description}",
        summary = "${api.product-composite.create-composite-product.notes}"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Bad Request, invalid format of the request. See response message for more information."),
        @ApiResponse(responseCode = "404", description = "Not found, the specified id does not exist."),
        @ApiResponse(responseCode = "422", description = "Unprocessable entity, input parameters caused the processing to fail. See response message for more information.")
    })
    @PostMapping(
        value = "/product-composite",
        consumes = "application/json")
    void createCompositeProduct(@RequestBody ProductAggregate body);

    
    /**
     * Sample usage:
     *
     * curl -X DELETE $HOST:$PORT/product-composite/1
     *
     * @param productId
     */
    @Operation(
        description = "${api.product-composite.delete-composite-product.description}",
        summary = "${api.product-composite.delete-composite-product.notes}")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Bad Request, invalid format of the request. See response message for more information."),
        @ApiResponse(responseCode = "422", description = "Unprocessable entity, input parameters caused the processing to fail. See response message for more information.")
    })
    @DeleteMapping(value = "/product-composite/{productId}")
    void deleteCompositeProduct(@PathVariable int productId);
}

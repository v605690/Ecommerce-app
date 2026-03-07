package com.ecommerce.project.service;

import com.ecommerce.project.dto.CartDTO;
import com.ecommerce.project.dto.ProductDTO;
import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Cart;
import com.ecommerce.project.model.CartItem;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.repositories.CartItemRepository;
import com.ecommerce.project.repositories.CartRepository;
import com.ecommerce.project.repositories.ProductRepository;
import com.ecommerce.project.util.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AuthUtil authUtil;

    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity) {

        // Find an existing cart or create one
        Cart cart = createCart();

        // Retrieve Product Details
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        // Perform Validations
        CartItem cartItem = cartItemRepository.findCartItemByProductProductIdAndCartId(cart.getCartId(), productId);

        if (cartItem != null) {
            throw new APIException("Product " + product.getProductName() + "already exists in the cart");
        }
        if (product.getQuantity() == 0) {
            throw new APIException(product.getProductName() + " is not available");
        }
        if (product.getQuantity() < quantity) {
            throw new APIException("Please, make an order of the " + product.getProductName() + " less than or equal to the quantity " +
                    product.getQuantity() + ".");
        }

        // Create Cart Item
        CartItem newCartItem = new CartItem();

        newCartItem.setProduct(product);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(quantity);
        newCartItem.setDiscount(product.getDiscount());
        newCartItem.setProductPrice(product.getSpecialPrice());

        // Save Cart Item
        cartItemRepository.save(newCartItem);
        product.setQuantity(product.getQuantity());
        cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice() * quantity));

        cartRepository.save(cart);

        // Return update cart
        // convert cart to CartDTO
        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

        // get data
        // -> stream it
        // -> map it
        // -> collect to list
        // -> store in dto

        // get cart items
        // transform each CartItem to ProductDTO
        // add the quantity
        // return productDTO
        // convert the result into a list
        // put that list into the CartDTO
        // return cartDTO
        List<ProductDTO> productDTOS = cart.getCartItems().stream()
                .map(item -> {
                    ProductDTO productDTO = modelMapper.map(item.getProduct(), ProductDTO.class);
                    productDTO.setQuantity(item.getQuantity());
                    return productDTO;
                })
                .toList();

        cartDTO.setProducts(productDTOS);
        return cartDTO;
    }

    @Override
    public List<CartDTO> getAllCarts() {
        // get the standard list of carts first
        List<Cart> carts = cartRepository.findAll();

        // check if the cart is empty first
        if (carts.size() == 0) {
            throw new APIException("No cart exist");
        }
        // if the cart is not empty, transform the standard cart list to a CartDTO list
        List<CartDTO> cartDTOS = carts.stream()
                .map(cart -> {
                   CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
                    // CartDTO has ProductDTO where the products are received from CartItem.
                    // We're getting the cart-items, converting into streams, also we are using a map to get every product and transforming
                    // into ProductDTO list.
                   List<ProductDTO> productDTOS = cart.getCartItems().stream()
                           .map(p -> modelMapper.map(p.getProduct(), ProductDTO.class))
                           .toList();
                   // set the products and return cartDTO
                   cartDTO.setProducts(productDTOS);
                   return cartDTO;
                }).toList();
        return cartDTOS;
    }

    private Cart createCart() {
        Cart userCart = cartRepository.findCartByEmail(authUtil.loggedInEmail());
        // if cart is found return cart
        if (userCart != null) {
            return userCart;
        }
        // if the cart is not found, create a new cart
        Cart cart = new Cart();
        cart.setTotalPrice(0.00);
        cart.setUser(authUtil.loggedInUser());
        Cart newCart = cartRepository.save(cart);
        return newCart;
    }
}

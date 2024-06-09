import { createContext, useState } from "react";
// import { food_list } from "../assets/images";
import { userService } from "../services";
import axios from "axios";

export const StoreContext = createContext(null);

const StoreContextProvider = (props) => {

    const BASE_URL = "http://localhost:8080"
    const [cartItems, setCartItems] = useState({});
    const [profileInfo, setProfileInfo] = useState({})
    const [products, setProducts] = useState([]);
    const [carts, setCarts] = useState([])

    const fetchProductsByCategory = async (category) => {
        const response = await axios.get(`${BASE_URL}/api/v1/products?category=${category}`)
        if (response.data.statusCode === 200) {
            setProducts(prev => response.data.dataList || [])
            // console.log("Check products >>> ", products);
        } else if (response.data.statusCode = 404) {
            setProducts(prev => response.data.dataList || [])
            console.log(response.data.message)
        } else {
            console.log(response.data.message)
        }
    }

    const fetchAllCartByUser = async () => {
        if (profileInfo.id) {
            const response = await axios.get(`${BASE_URL}/api/v1/user/${profileInfo.id}/carts`)
            if (response.status === 200) {
                // console.log(response.data.dataList);
                setCarts(response.data.dataList);
                // return response.data.dataList;
            }
        }
    }

    const fetchProfileData = async () => {
        try {

            const token = localStorage.getItem('token'); // Retrieve the token from localStorage
            if (token) {
                const response = await userService.getUserProfile(token);
                setProfileInfo(response.data);

            }

        } catch (err) {
            console.error('Error fetching profile information:', err);
        }
    }

    const addToCart = async (itemId) => {
        // Optimistic UI Update: cập nhật giỏ hàng ngay lập tức
        // setCartItems((prev) => {
        //     const newQuantity = prev[itemId] ? prev[itemId] + 1 : 1;
        //     return { ...prev, [itemId]: newQuantity };
        // });


        try {
            // Lấy thông tin sản phẩm từ server
            const productResponse = await axios.get(`${BASE_URL}/api/v1/products/${itemId}`);
            if (productResponse.data.statusCode === 404) {
                throw new Error("Product not found");
            }

            if (profileInfo != null) {
                // Gửi yêu cầu để thêm sản phẩm vào giỏ hàng của người dùng
                const addToCartResponse = await axios.post(`${BASE_URL}/api/v1/user/${profileInfo.id}/addCart`, productResponse.data.data);
                if (addToCartResponse.data.statusCode !== 200) {
                    throw new Error(addToCartResponse.data.message);
                }
            }

        } catch (error) {
            console.error("Failed to add item to cart:", error.message);

            // Rollback trạng thái UI nếu có lỗi
            setCartItems((prev) => {
                const newQuantity = prev[itemId] ? prev[itemId] - 1 : 0;
                if (newQuantity <= 0) {
                    const { [itemId]: _, ...rest } = prev;
                    return rest;
                } else {
                    return { ...prev, [itemId]: newQuantity };
                }
            });
        }
    };

    const removeFromCart = async (id) => {
        // Lưu trạng thái hiện tại của giỏ hàng để có thể khôi phục nếu có lỗi
        const previousCarts = [...carts];

        // Optimistic UI Update: Cập nhật UI ngay lập tức
        setCarts(prevCart => prevCart.filter(item => item.id !== id));

        try {
            const response = await axios.delete(`${BASE_URL}/api/v1/user/${profileInfo.id}/carts/${id}`);

            if (response.data.statusCode !== 200) {
                throw new Error(response.data.message);
            }
        } catch (error) {
            console.error("Error removing item from cart:", error.message);
            // Rollback: Khôi phục lại trạng thái ban đầu nếu có lỗi
            setCarts(previousCarts);
        }
    };

    const contextValue = {
        cartItems,
        setCartItems,
        addToCart,
        profileInfo,
        setProfileInfo,
        products,
        fetchProductsByCategory,
        fetchAllCartByUser,
        fetchProfileData,
        carts,
        setCarts,
        removeFromCart
    }


    return (
        <StoreContext.Provider value={contextValue}>
            {props.children}
        </StoreContext.Provider>
    )
}

export default StoreContextProvider;
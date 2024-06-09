import { createContext, useEffect, useState } from "react";
// import { food_list } from "../assets/images";
import { userService } from "../services";
import axios from "axios";


export const StoreContext = createContext(null);

const StoreContextProvider = (props) => {

    const BASE_URL = "http://localhost:8080"
    const [cartItems, setCartItems] = useState({});
    const [profileInfo, setProfileInfo] = useState({})
    const [products, setProducts] = useState([]);

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

    // const fetchProfileData = async () => {
    //     try {

    //         const token = localStorage.getItem('token'); // Retrieve the token from localStorage
    //         if (!token) {
    //             window.location.href = "/login"
    //         }
    //         const response = await userService.getUserProfile(token);

    //         setProfileInfo(response.data);

    //     } catch (err) {
    //         console.error('Error fetching profile information:', err);
    //     }
    // }

    const addToCart = (itemId) => {
        if (!cartItems[itemId]) {
            setCartItems((prev) => ({ ...prev, [itemId]: 1 }))
        } else {
            setCartItems((prev) => ({ ...prev, [itemId]: prev[itemId] + 1 }))
        }
    }

    const removeFromCart = (itemId) => {
        setCartItems((prev) => ({ ...prev, [itemId]: prev[itemId] - 1 }))
    }
    const getTotalCartAmount = () => {
        let totalAmount = 0
        for (const item in cartItems) {
            if (cartItems[item] > 0) {
                let itemInfo = products.find((product) => product.id === item)
                totalAmount += itemInfo.price * cartItems[item];
            }
        }
        return totalAmount;
    }


    const contextValue = {
        cartItems,
        setCartItems,
        addToCart,
        removeFromCart,
        getTotalCartAmount,
        profileInfo,
        setProfileInfo,
        products,
        fetchProductsByCategory
    }


    return (
        <StoreContext.Provider value={contextValue}>
            {props.children}
        </StoreContext.Provider>
    )
}

export default StoreContextProvider;
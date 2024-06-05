import { createContext, useEffect, useState } from "react";
import { food_list } from "../assets/images";
import { userService } from "../services";
export const StoreContext = createContext(null);

const StoreContextProvider = (props) => {


    const [cartItems, setCartItems] = useState({});
    const [profileInfo, setProfileInfo] = useState({})

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
                let itemInfo = food_list.find((product) => product._id === item)
                totalAmount += itemInfo.price * cartItems[item];
            }
        }
        return totalAmount;
    }


    const contextValue = {
        food_list,
        cartItems,
        setCartItems,
        addToCart,
        removeFromCart,
        getTotalCartAmount,
        profileInfo,
        setProfileInfo
    }


    return (
        <StoreContext.Provider value={contextValue}>
            {props.children}
        </StoreContext.Provider>
    )
}

export default StoreContextProvider;
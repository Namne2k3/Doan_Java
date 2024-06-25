import React, { useContext, useEffect, useState } from 'react'
import "./techdisplay.css"
import { StoreContext } from '../../context/StoreContext'
import TechItem from '../TechItem/TechItem';
import NotFound from '../NotFound/NotFound';
import { ToastContainer, toast } from 'react-toastify';

const TechDisplay = ({ category, title = "Top món đồ công nghệ", search = null }) => {

    const { fetchProductBySearching, fetchProductsByCategory, products } = useContext(StoreContext);

    const addCartItem = async (id, name) => {
        toast.promise(
            addToCart(id),
            {
                pending: 'Thêm vào giỏ hàng',
                success: `Đã thêm ${name} vào giỏ hàng 👌`,
                error: 'Có lỗi khi thêm sản phẩm vào giỏ hàng 🤯'
            },
            { containerId: 'C' }
        )
    }

    const { cartItems, addToCart } = useContext(StoreContext);


    useEffect(() => {

        search &&
            fetchProductBySearching(search)

        category != null &&
            fetchProductsByCategory(category)

    }, [category, search])

    return (
        <>
            <div className='tech-display' id='tech-display'>
                <h2>{title}</h2>
                {
                    products.length !== 0
                        ?
                        <div className="tech-display-list">
                            {
                                products.map((item, index) => {

                                    return (
                                        <TechItem addCartItem={addCartItem} cartItems={cartItems} item={item} key={index} />
                                    )
                                })
                            }
                        </div>
                        :
                        <NotFound />
                }
            </div>
            <ToastContainer hideProgressBar draggable stacked autoClose={1500} containerId="C" />
        </>
    )
}

export default TechDisplay
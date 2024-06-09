import React, { useContext, useEffect } from 'react'
import "./techdisplay.css"
import { StoreContext } from '../../context/StoreContext'
import TechItem from '../TechItem/TechItem';
import NotFound from '../NotFound/NotFound';
import { ToastContainer, toast } from 'react-toastify';

const TechDisplay = ({ category }) => {

    const { fetchProductsByCategory, products } = useContext(StoreContext);

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

        fetchProductsByCategory(category)

    }, [category])

    return (
        <>
            <div className='tech-display' id='tech-display'>
                <h2>Top món đồ công nghệ</h2>
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
            <ToastContainer hideProgressBar draggable autoClose={1500} containerId="C" />
        </>
    )
}

export default TechDisplay
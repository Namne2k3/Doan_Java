import React, { useContext, useEffect, useState } from 'react'
import "./techdisplay.css"
import { StoreContext } from '../../context/StoreContext'
import TechItem from '../TechItem/TechItem';
import NotFound from '../NotFound/NotFound';
import { ToastContainer, toast } from 'react-toastify';
import axios from 'axios';

const TechDisplay = ({ category, search = null }) => {

    const { fetchProductBySearching, products } = useContext(StoreContext);
    const BASE_URL = `http://localhost:8080`
    const [categories, setCategories] = useState([])


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

        const fetchCategory = async () => {
            try {
                const response = await axios.get(`${BASE_URL}/api/v1/categories`);
                if (response.data.statusCode === 200) {
                    setCategories(response.data.dataList);
                } else {
                    throw new Error(response.data.message)
                }

            } catch (e) {
                console.error(e.message);
            }
        }

        search &&
            fetchProductBySearching(search)

        fetchCategory()

    }, [search])

    return (
        <>
            <div className='tech-display' id='tech-display'>
                <h2>Sản phẩm nổi bật</h2>
                {
                    products?.length !== 0
                        ?
                        <div className="tech-display-list">
                            {
                                products?.map((item, index) => {
                                    return (
                                        <TechItem addCartItem={addCartItem} cartItems={cartItems} item={item} key={index} />
                                    )
                                })
                            }
                        </div>
                        :
                        <NotFound />
                }
                {
                    categories?.map((cate, index) => {
                        return (
                            <div id={cate.name} key={index} className='mt-5'>
                                <h2>{cate.name}</h2>
                                {
                                    products?.length &&
                                    <div className="tech-display-list">
                                        {
                                            products?.map((item, index) => {
                                                if (item.category.id === cate.id)
                                                    return (
                                                        <TechItem addCartItem={addCartItem} cartItems={cartItems} item={item} key={index} />
                                                    )
                                            })
                                        }
                                    </div>
                                }
                            </div>
                        )
                    })
                }
            </div>
            <ToastContainer hideProgressBar draggable stacked autoClose={1500} containerId="C" />
        </>
    )
}

export default TechDisplay